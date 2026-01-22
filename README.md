# Workshop Tracker (Java 21 + Spring Boot 3 + MySQL)

API REST para rastrear a participação de colaboradores em workshops, via **Ata de Presença**.

## Regras do domínio

- **1 ata por workshop**: imposto por constraint única `uk_ata_workshop` (`ata.workshop_id`).
- Uma **Ata** pertence a um **Workshop**.
- Uma **Ata** possui vários **Colaboradores** (presenças).
- Um **Colaborador** pode participar de várias **Atas**.
- **Sem duplicidade** de colaborador na mesma ata: imposto por chave primária composta em `ata_colaborador (ata_id, colaborador_id)`.
- Remover colaborador que **não está** na ata retorna **404**.

## Stack

- Java 21
- Spring Boot 3.x (Maven)
- MySQL + Spring Data JPA (Hibernate)
- Flyway (migrações obrigatórias)
- Bean Validation
- Swagger UI (springdoc-openapi)
- Segurança: **Bearer Token (JWT)** em `/api/**` (Swagger liberado)
- **Observabilidade**: Micrometer + Prometheus + Actuator
- **Resiliência**: Resilience4j (Circuit Breaker, Retry, Rate Limiter)
- **Cache**: Caffeine
- **Testes**: JUnit 5 + Mockito + TestContainers + Awaitility
- **Tracing**: Micrometer Tracing + Zipkin

## Como subir o MySQL (Docker)

1. Suba o banco:
   - `docker compose -f compose.yaml up -d`
2. Aguarde o `mysql` ficar `healthy`.

Config padrão do `compose.yaml`:
- DB: `workshop_tracker`
- User: `workshop`
- Password: `workshop`
- Porta: `3306`

## Como executar a aplicação

- `./mvnw spring-boot:run`

Se a porta `8080` já estiver em uso, rode com:
- PowerShell: `$env:SERVER_PORT=8081; ./mvnw spring-boot:run`

## Executar sem MySQL (H2 em memória)

Se você não quiser subir o MySQL via Docker, existe o profile `local` que usa H2 em memória (modo MySQL) + Flyway:

- PowerShell: `./mvnw spring-boot:run "-Dspring-boot.run.profiles=local"`
- Bash/Zsh: `./mvnw spring-boot:run -Dspring-boot.run.profiles=local`

Variáveis opcionais (se quiser sobrescrever):
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`
- `API_JWT_SECRET`, `API_JWT_ISSUER`, `API_JWT_TTL`

As migrações Flyway rodam automaticamente no startup (`spring.flyway.enabled=true`).

Nota (Windows/WSL): o default usa `DB_HOST=127.0.0.1` para evitar conflitos comuns do `localhost` (IPv6/WSL port relay) na porta 3306.

## Swagger UI

- `http://localhost:8080/swagger-ui.html`

## Observabilidade e Monitoramento

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Métricas Prometheus
```bash
curl http://localhost:8080/actuator/prometheus
```

### Métricas Customizadas
- `atas.criadas.total` - Total de atas criadas
- `atas.colaboradores.adicionados.total` - Colaboradores adicionados
- `auth.login.total{status="success"}` - Logins bem-sucedidos
- `business.operation.duration{operation="criar-ata"}` - Duração de operações
- `cache.hits.total{cache="atas"}` - Cache hits

### Endpoints Actuator Disponíveis
- `/actuator/health` - Status da aplicação
- `/actuator/metrics` - Todas as métricas
- `/actuator/prometheus` - Formato Prometheus
- `/actuator/caches` - Informações de cache
- `/actuator/loggers` - Níveis de log

## Autenticação (JWT Bearer Token)

Todos os endpoints em `/api/**` exigem autenticação via header:
- `Authorization: Bearer <seu_token>`

### Onde “colocar o token”?

- No Swagger UI: clique em `Authorize` e cole **apenas o token** (sem `Bearer `). O Swagger adiciona o prefixo automaticamente.
- No curl/Postman: envie no header `Authorization: Bearer <token>`.

### Como obter o token

1) Registre um usuário: `POST /api/auth/register` (`nome`, `email`, `senha`)
2) Faça login: `POST /api/auth/login` (`email`, `senha`)

Por padrão, um usuário registrado recebe as roles: `CREATOR` e `READER`.

## Endpoints implementados (obrigatórios)

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/workshops`
- `POST /api/colaboradores`
- `POST /api/atas`
- `PUT /api/workshops/{workshopId}/atas/{ataId}`
- `DELETE /api/atas/{ataId}/colaboradores/{colaboradorId}`
- `GET /api/atas` (com filtros opcionais `workshopNome` e/ou `data=dd/MM/yyyy`, aplicando AND; aceita também `yyyy-MM-dd` por compatibilidade)

### Autorização (roles)

- Escrita (criação/alteração/remoção): `CREATOR` ou `ADMIN`
- Leitura (`GET /api/atas`): `READER` ou `CREATOR` ou `ADMIN`

## Como rodar testes

```bash
# Todos os testes
./mvnw clean test

# Apenas testes unitários
./mvnw test -Dtest="*UseCaseImplTest"

# Apenas testes de integração (com TestContainers)
./mvnw test -Dtest="*IntegrationTest"

# Apenas testes de performance
./mvnw test -Dtest="PerformanceTest"

# Apenas testes de segurança
./mvnw test -Dtest="SecurityIntegrationTest"
```

Os testes usam:
- **Testes Unitários**: H2 em modo MySQL
- **Testes de Integração**: TestContainers com MySQL real
- **Cobertura**: Unitários, Integração, Performance, Segurança

## Exemplos (curl)

0) Registrar + obter token

```bash
curl -s -H "Content-Type: application/json" \
  -d '{ "nome": "Ana Silva", "email": "ana@fast.com", "senha": "Senha@123" }' \
  http://localhost:8080/api/auth/register

# Se você tiver jq:
TOKEN=$(curl -s -H "Content-Type: application/json" \
  -d '{ "email": "ana@fast.com", "senha": "Senha@123" }' \
  http://localhost:8080/api/auth/login | jq -r .accessToken)
```

1) Criar workshop + criar colaborador + criar ata

```bash
curl -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{ "nome": "Workshop Spring", "dataRealizacao": "20/01/2026", "descricao": "..." }' \
  http://localhost:8080/api/workshops

curl -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{ "nome": "Ana Silva" }' \
  http://localhost:8080/api/colaboradores

curl -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{ "workshopId": 1, "colaboradoresIds": [1] }' \
  http://localhost:8080/api/atas
```

2) Adicionar colaborador na ata + remover colaborador da ata

```bash
curl -H "Authorization: Bearer $TOKEN" -X PUT -H "Content-Type: application/json" \
  -d '{ "colaboradorId": 2 }' \
  http://localhost:8080/api/workshops/1/atas/1

curl -H "Authorization: Bearer $TOKEN" -X DELETE \
  http://localhost:8080/api/atas/1/colaboradores/1
```

3) Listagem geral + listagem filtrada (nome + data)

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/atas

curl -H "Authorization: Bearer $TOKEN" \
  "http://localhost:8080/api/atas?workshopNome=spring&data=20/01/2026"
```
