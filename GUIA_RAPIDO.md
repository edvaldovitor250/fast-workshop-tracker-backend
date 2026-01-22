# 🚀 Guia Rápido - Workshop Tracker Nível Sênior

## Início Rápido (5 minutos)

### 1. Subir Stack Completo
```bash
# MySQL, Prometheus, Grafana e Zipkin
docker-compose -f docker-compose-monitoring.yaml up -d

# Aguardar containers ficarem healthy
docker-compose -f docker-compose-monitoring.yaml ps
```

### 2. Executar Aplicação
```bash
./mvnw spring-boot:run
```

### 3. Verificar Health
```bash
curl http://localhost:8080/actuator/health | jq
```

## 📊 Acessar Ferramentas

| Ferramenta | URL | Credenciais |
|------------|-----|-------------|
| Aplicação | http://localhost:8080 | - |
| Swagger | http://localhost:8080/swagger-ui.html | - |
| Actuator | http://localhost:8080/actuator | - |
| Prometheus | http://localhost:9090 | - |
| Grafana | http://localhost:3000 | admin/admin |
| Zipkin | http://localhost:9411 | - |

## 🧪 Executar Testes

```bash
# Todos
./mvnw test

# Apenas integração (MySQL real via TestContainers)
./mvnw test -Dtest="*IntegrationTest"

# Apenas performance
./mvnw test -Dtest="PerformanceTest"
```

## 📈 Verificar Métricas

### Métricas de Negócio
```bash
# Total de atas criadas
curl -s http://localhost:8080/actuator/metrics/atas.criadas.total | jq

# Taxa de cache hits
curl -s http://localhost:8080/actuator/metrics/cache.hits.total | jq

# Duração de operações
curl -s http://localhost:8080/actuator/metrics/business.operation.duration | jq
```

### Prometheus Queries
```promql
# Taxa de criação de atas por minuto
rate(atas_criadas_total[5m])

# Tempo de resposta p95
histogram_quantile(0.95, rate(business_operation_duration_seconds_bucket[5m]))

# Cache hit rate
rate(cache_hits_total[5m]) / (rate(cache_hits_total[5m]) + rate(cache_misses_total[5m]))
```

## 🛡️ Testar Resiliência

### Circuit Breaker
```bash
# Verificar estado
curl -s http://localhost:8080/actuator/health | jq '.components.circuitBreakers'

# Métricas do circuit breaker
curl -s http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.state | jq
```

### Rate Limiter
```bash
# Fazer múltiplas requisições rápidas para testar
for i in {1..150}; do
  curl -s http://localhost:8080/actuator/health > /dev/null
  echo "Request $i"
done
```

## 💾 Testar Cache

### Criar dados para cache
```bash
# Registrar usuário
TOKEN=$(curl -s -H "Content-Type: application/json" \
  -d '{"nome":"Test","email":"test@fast.com","senha":"Senha@123"}' \
  http://localhost:8080/api/auth/register | jq -r .accessToken)

# Criar workshop
curl -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Workshop Cache","dataRealizacao":"21/01/2026"}' \
  http://localhost:8080/api/workshops

# Verificar cache
curl http://localhost:8080/actuator/caches | jq
```

### Ver estatísticas de cache nos logs
- Cache stats são logados automaticamente a cada 5 minutos
- Verifique o arquivo `logs/workshop-tracker.log`

## 📊 Configurar Grafana

### 1. Acessar Grafana
```
http://localhost:3000
Login: admin / admin
```

### 2. Adicionar Prometheus como DataSource
1. Configuration → Data Sources → Add data source
2. Escolher "Prometheus"
3. URL: `http://prometheus:9090`
4. Save & Test

### 3. Importar Dashboard
1. Dashboards → Import
2. Upload do arquivo `grafana-dashboard.json`
3. Ou usar dashboard ID 4701 (Spring Boot Statistics)

## 🔍 Rastreamento Distribuído (Zipkin)

### Acessar Zipkin
```
http://localhost:9411
```

### Fazer requisições para gerar traces
```bash
# Criar ata (gera trace completo)
curl -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"workshopId":1,"colaboradoresIds":[]}' \
  http://localhost:8080/api/atas

# Ver traces no Zipkin
# Filtre por: serviceName=workshop-tracker
```

## 📝 Verificar Logs Estruturados

### Ver logs em tempo real
```bash
tail -f logs/workshop-tracker.log
```

### Buscar por requestId
```bash
grep "requestId=12345" logs/workshop-tracker.log
```

### Ver apenas erros
```bash
grep "ERROR" logs/workshop-tracker.log
```

## 🎯 Cenários de Teste Rápido

### Cenário 1: Criar Ata Completa
```bash
# 1. Registrar
TOKEN=$(curl -s -H "Content-Type: application/json" \
  -d '{"nome":"Ana","email":"ana@test.com","senha":"Senha@123"}' \
  http://localhost:8080/api/auth/register | jq -r .accessToken)

# 2. Criar workshop
WORKSHOP=$(curl -s -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Java Avançado","dataRealizacao":"21/01/2026"}' \
  http://localhost:8080/api/workshops | jq -r .id)

# 3. Criar colaborador
COLAB=$(curl -s -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Maria Silva"}' \
  http://localhost:8080/api/colaboradores | jq -r .id)

# 4. Criar ata
curl -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{\"workshopId\":$WORKSHOP,\"colaboradoresIds\":[$COLAB]}" \
  http://localhost:8080/api/atas | jq

# 5. Verificar métricas
curl -s http://localhost:8080/actuator/metrics/atas.criadas.total | jq
```

### Cenário 2: Testar Performance
```bash
# Executar teste de performance
./mvnw test -Dtest="PerformanceTest#deveCriarAtaEmTempoAceitavel"

# Verificar resultado nos logs
grep "Tempo de criação" target/surefire-reports/*.txt
```

## 🚨 Troubleshooting

### Aplicação não inicia
```bash
# Verificar se portas estão livres
netstat -an | grep 8080
netstat -an | grep 3306

# Ver logs detalhados
./mvnw spring-boot:run -X
```

### Testes falham
```bash
# Limpar e recompilar
./mvnw clean install -DskipTests

# Executar testes individualmente
./mvnw test -Dtest="AtaIntegrationTest"
```

### Métricas não aparecem
```bash
# Verificar se actuator está respondendo
curl http://localhost:8080/actuator

# Verificar configuração do Prometheus
curl http://localhost:9090/targets
```

## 📚 Recursos Adicionais

- [MELHORIAS_SENIOR.md](MELHORIAS_SENIOR.md) - Documentação completa
- [CHECKLIST_MELHORIAS.md](CHECKLIST_MELHORIAS.md) - Checklist de implementação
- [README.md](README.md) - Documentação original do projeto

## 🎉 Parabéns!

Você agora tem uma aplicação **nível Sênior** com:
✅ Observabilidade completa
✅ Resiliência implementada
✅ Performance otimizada
✅ Testes abrangentes
✅ Logs estruturados
✅ Monitoramento em tempo real

**Happy Coding!** 🚀
