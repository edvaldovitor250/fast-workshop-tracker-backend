<body>
  <header>
    <h1 align="center">📘 Workshop Tracker – API REST (Java 21 + Spring Boot 3.2 + MySQL)</h1>

    <p align="center">
      <img src="https://img.shields.io/badge/spring--boot-3.2.12-6DB33F" alt="Spring Boot 3.2.12"/>
      <img src="https://img.shields.io/badge/java-21-orange" alt="Java 21"/>
      <img src="https://img.shields.io/badge/db-MySQL%208.0-blue" alt="MySQL 8"/>
      <img src="https://img.shields.io/badge/migrations-Flyway-red" alt="Flyway"/>
      <img src="https://img.shields.io/badge/security-JWT%20Bearer-black" alt="JWT Bearer"/>
      <img src="https://img.shields.io/badge/docs-Swagger%20UI-85EA2D" alt="Swagger UI"/>
      <img src="https://img.shields.io/badge/metrics-Prometheus-orange" alt="Prometheus"/>
      <img src="https://img.shields.io/badge/tracing-Zipkin-blueviolet" alt="Zipkin"/>
      <img src="https://img.shields.io/badge/mapping-MapStruct-lightgrey" alt="MapStruct"/>
      <img src="https://img.shields.io/badge/build-Maven-CC0000" alt="Maven"/>
    </p>

    <p align="center">
      Desafio <strong>FAST – Soluções Tecnológicas</strong> (Etapa Backend): API REST para cadastrar workshops,
      colaboradores e processar atas de presença, com listagem de participações e filtros.
      <br/>
      Este README concentra <strong>toda</strong> a documentação do projeto (arquitetura, modelagem, contratos, execução e padrões).
    </p>
  </header>

  <main>
    <h2>🧭 Índice</h2>
    <ol>
      <li><a href="#visao-geral">Visão Geral</a></li>
      <li><a href="#requisitos">Requisitos do Desafio</a></li>
      <li><a href="#arquitetura-e-pacotes">Arquitetura &amp; Pacotes</a></li>
      <li><a href="#modelo-de-dados">Modelo de Dados (ER)</a></li>
      <li><a href="#tech-stack">Tech Stack &amp; Dependências</a></li>
      <li><a href="#como-rodar">Como Rodar (Docker &amp; Local)</a></li>
      <li><a href="#seguranca">Segurança (JWT / Roles)</a></li>
      <li><a href="#endpoints">Endpoints (Swagger / Exemplos)</a></li>
      <li><a href="#formatos">Formatos (Datas, Ordenação e Filtros)</a></li>
      <li><a href="#tratamento-erros">Tratamento de Erros</a></li>
      <li><a href="#observabilidade">Observabilidade (Actuator / Prometheus / Zipkin)</a></li>
      <li><a href="#testes">Testes</a></li>
      <li><a href="#boas-praticas">Boas Práticas &amp; Padrões</a></li>
      <li><a href="#licenca-autor">Licença &amp; Autor</a></li>
    </ol>

    <section id="visao-geral">
      <h2>ℹ️ Visão Geral</h2>
      <p>
        O <strong>Workshop Tracker</strong> é uma API REST para rastrear a participação de colaboradores em workshops por meio de
        <strong>atas de presença</strong>. O domínio foi modelado para manter consistência e evitar duplicidades, garantindo
        consultas eficientes e contratos bem documentados via Swagger.
      </p>

      <h3>📌 Definições (domínio)</h3>
      <ul>
        <li><strong>Colaborador</strong>: <code>id</code>, <code>nome</code></li>
        <li><strong>Workshop</strong>: <code>id</code>, <code>nome</code>, <code>dataRealizacao</code>, <code>descricao</code></li>
        <li><strong>Ata</strong>: <code>id</code>, <code>workshop</code>, <code>colaboradores</code> (presenças)</li>
      </ul>

      <h3>✅ Regras do domínio implementadas</h3>
      <ul>
        <li><strong>1 ata por workshop</strong>: constraint única <code>uk_ata_workshop</code> em <code>ata.workshop_id</code>.</li>
        <li>Uma <strong>Ata</strong> pertence a um <strong>Workshop</strong> e contém vários <strong>Colaboradores</strong>.</li>
        <li>Sem duplicidade de colaborador na mesma ata: chave composta <code>(ata_id, colaborador_id)</code> em <code>ata_colaborador</code>.</li>
        <li>Remover colaborador que <strong>não está</strong> na ata retorna <strong>404</strong>.</li>
      </ul>
    </section>

    <section id="requisitos">
      <h2>📋 Requisitos do Desafio (FAST)</h2>
      <p>
        <strong>Desafio 1a – Etapa Backend</strong>: construir uma API REST em Java para listar detalhes de workshops e presença de colaboradores.
      </p>

      <h3>Processamento de Atas</h3>
      <ul>
        <li><code>POST /api/workshops</code> — cadastrar Workshop</li>
        <li><code>POST /api/colaboradores</code> — cadastrar Colaborador</li>
        <li><code>POST /api/atas</code> — criar ata de presença para um Workshop</li>
        <li><code>PUT /api/workshops/&lt;workshopId&gt;/atas/&lt;ataId&gt;</code> — adicionar colaborador em uma Ata</li>
        <li><code>DELETE /api/atas/&lt;ataId&gt;/colaboradores/&lt;colaboradorId&gt;</code> — remover colaborador de uma Ata</li>
      </ul>

      <h3>Identificação de Colaboradores Presentes</h3>
      <ul>
        <li><code>GET /api/atas</code> — lista colaboradores em ordem alfabética e workshops que participaram</li>
        <li><code>GET /api/atas?workshopNome=&lt;nome&gt;</code> — filtra por nome do workshop</li>
        <li><code>GET /api/atas?data=&lt;data&gt;</code> — filtra por data de realização do workshop</li>
      </ul>

      <h3>Bônus (opcional) — implementado</h3>
      <ul>
        <li><strong>Persistência</strong>: MySQL + Flyway (migrações versionadas)</li>
        <li><strong>Autenticação/Autorização</strong>: JWT Bearer + roles</li>
        <li><strong>Documentação</strong>: Swagger UI (springdoc-openapi)</li>
        <li><strong>Observabilidade</strong>: Actuator + Prometheus + Tracing Zipkin</li>
      </ul>
    </section>

    <section id="arquitetura-e-pacotes">
      <h2>🏗️ Arquitetura &amp; Pacotes</h2>
      <p>
        A solução segue <strong>arquitetura em camadas</strong> com inspiração em <strong>Ports &amp; Adapters</strong> (hexagonal):
        a camada de <em>aplicação</em> define portas (interfaces), e a infraestrutura fornece adaptações concretas
        (persistência, segurança, etc.).
      </p>

      <pre><code>📦src/main/java/br/com/fast/workshoptracker
 ┣ 📂presentation
 ┃ ┣ 📂rest
 ┃ ┃ ┣ 📂controller   (AtaController, WorkshopController, ColaboradorController, AuthController)
 ┃ ┃ ┣ 📂dto          (request/response)
 ┃ ┃ ┣ 📂mapper       (mappers REST ↔ application, MapStruct)
 ┃ ┃ ┣ 📂validation   (Bean Validation com anotações customizadas)
 ┃ ┃ ┗ 📂error        (ApiExceptionHandler + ErrorResponse)
 ┃ ┗ 📂openapi
 ┃   ┣ 📂api          (interfaces @RequestMapping + anotações OpenAPI)
 ┃   ┗ 📂docs         (strings HTML com descrições e exemplos)
 ┣ 📂application
 ┃ ┣ 📂dto            (command/query)
 ┃ ┣ 📂port
 ┃ ┃ ┣ 📂input        (use cases)
 ┃ ┃ ┗ 📂output       (ports para repositórios e serviços)
 ┃ ┣ 📂usecase        (implementações dos casos de uso)
 ┃ ┗ 📂mapper         (mappers application ↔ domain, MapStruct)
 ┣ 📂domain
 ┃ ┣ 📂entity         (Ata, Workshop, Colaborador, Usuario)
 ┃ ┣ 📂enums          (UserRole)
 ┃ ┗ 📂exception      (errors padronizados por categoria/gravidade)
 ┗ 📂infrastructure
   ┣ 📂config         (WebMvcConfig, OpenApiConfig, JwtProperties)
   ┣ 📂persistence    (Spring Data JPA, adapters, projections)
   ┣ 📂security       (Spring Security + Resource Server JWT)
   ┣ 📂observability  (Actuator health custom + métricas custom)
   ┗ 📂util           (normalização, validações, formatters)
</code></pre>

      <h3>Fluxos principais</h3>
      <ul>
        <li><strong>POST /api/atas</strong> valida unicidade e existência de entidades e cria a ata (1 por workshop).</li>
        <li><strong>GET /api/atas</strong> usa uma query otimizada que retorna linhas (colaborador x workshop) e agrega em memória por colaborador.</li>
      </ul>
    </section>

    <section id="modelo-de-dados">
      <h2>🗄️ Modelo de Dados (ER)</h2>
      <p>
        A persistência é relacional (MySQL). O schema é criado/validado via <strong>Flyway</strong> com migrações versionadas.
      </p>

      <h3>Entidades e relacionamentos</h3>
      <ul>
        <li><strong>workshop</strong> (1) — (1) <strong>ata</strong> (por constraint única)</li>
        <li><strong>ata</strong> (N) — (N) <strong>colaborador</strong> via tabela de junção <strong>ata_colaborador</strong></li>
        <li><strong>usuario</strong> (1) — (N) <strong>usuario_role</strong> (roles)</li>
      </ul>

      <details>
        <summary><b>Diagrama ER (Mermaid)</b></summary>
        <pre><code class="language-mermaid">erDiagram
  WORKSHOP ||--|| ATA : "1 workshop → 1 ata"
  ATA ||--o{ ATA_COLABORADOR : "presencas"
  COLABORADOR ||--o{ ATA_COLABORADOR : "presencas"

  USUARIO ||--o{ USUARIO_ROLE : "roles"

  WORKSHOP {
    BIGINT id PK
    VARCHAR nome
    DATE data_realizacao
    VARCHAR descricao
  }

  ATA {
    BIGINT id PK
    BIGINT workshop_id UK,FK
  }

  COLABORADOR {
    BIGINT id PK
    VARCHAR nome
  }

  ATA_COLABORADOR {
    BIGINT ata_id PK,FK
    BIGINT colaborador_id PK,FK
  }

  USUARIO {
    BIGINT id PK
    VARCHAR nome
    VARCHAR email UK
    VARCHAR senha_hash
  }

  USUARIO_ROLE {
    BIGINT usuario_id PK,FK
    VARCHAR role PK
  }
</code></pre>
      </details>

      <details>
        <summary><b>SQL (Flyway)</b></summary>
        <p>Migração inicial do domínio (workshops/atas/colaboradores):</p>
        <pre><code class="language-sql">CREATE TABLE colaborador (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL
);

CREATE TABLE workshop (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    data_realizacao DATE NOT NULL,
    descricao VARCHAR(500)
);

CREATE TABLE ata (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workshop_id BIGINT NOT NULL,
    CONSTRAINT fk_ata_workshop FOREIGN KEY (workshop_id) REFERENCES workshop (id),
    CONSTRAINT uk_ata_workshop UNIQUE (workshop_id)
);

CREATE TABLE ata_colaborador (
    ata_id BIGINT NOT NULL,
    colaborador_id BIGINT NOT NULL,
    PRIMARY KEY (ata_id, colaborador_id),
    CONSTRAINT fk_ata_colaborador_ata FOREIGN KEY (ata_id) REFERENCES ata (id) ON DELETE CASCADE,
    CONSTRAINT fk_ata_colaborador_colaborador FOREIGN KEY (colaborador_id) REFERENCES colaborador (id)
);
</code></pre>

        <p>Migração de autenticação/usuários:</p>
        <pre><code class="language-sql">CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(180) NOT NULL,
    senha_hash VARCHAR(255) NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);

CREATE TABLE usuario_role (
    usuario_id BIGINT NOT NULL,
    role VARCHAR(30) NOT NULL,
    PRIMARY KEY (usuario_id, role),
    CONSTRAINT fk_usuario_role_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (id) ON DELETE CASCADE
);
</code></pre>
      </details>
    </section>

    <section id="tech-stack">
      <h2>🛠️ Tech Stack &amp; Dependências</h2>
      <ul>
        <li><strong>Java 21</strong> + <strong>Spring Boot 3.2.12</strong></li>
        <li><strong>Spring Web</strong>, <strong>Spring Data JPA</strong> (Hibernate), <strong>Bean Validation</strong></li>
        <li><strong>MySQL 8</strong> + <strong>Flyway</strong> (migrações e <code>ddl-auto=validate</code>)</li>
        <li><strong>Spring Security</strong> + <strong>OAuth2 Resource Server</strong> (JWT HS256)</li>
        <li><strong>OpenAPI/Swagger UI</strong> (springdoc)</li>
        <li><strong>MapStruct</strong> + Lombok (mapeamento e redução de boilerplate)</li>
        <li><strong>Actuator</strong> + Micrometer (Prometheus) + Tracing (Brave/Zipkin)</li>
        <li><strong>Testes</strong>: JUnit 5 + Mockito + Testcontainers + Awaitility</li>
      </ul>

      <details>
        <summary><b>Observação sobre cache/resiliência</b></summary>
        <p>
          Apesar de existirem arquivos auxiliares no repositório mencionando cache/resiliência, o build atual não inclui
          dependências de Caffeine ou Resilience4j. Esta documentação reflete o código realmente ativo.
        </p>
      </details>
    </section>

    <section id="como-rodar">
      <h2>🚀 Como Rodar (Docker &amp; Local)</h2>

      <h3>Pré-requisitos</h3>
      <ul>
        <li>Java 21</li>
        <li>(Opcional) Docker + Docker Compose</li>
      </ul>

      <h3>Opção A) MySQL via Docker (recomendado)</h3>
      <pre><code class="language-bash"># subir apenas o banco
docker compose -f compose.yaml up -d

# executar a API
./mvnw spring-boot:run
</code></pre>

      <h3>Opção B) Stack de monitoramento (MySQL + Prometheus + Grafana + Zipkin)</h3>
      <pre><code class="language-bash">docker compose -f docker-compose-monitoring.yaml up -d

# depois rode a aplicação na sua máquina (host)
./mvnw spring-boot:run
</code></pre>
      <p>
        Observação: o Prometheus está configurado para coletar métricas em <code>host.docker.internal:8080</code>.
      </p>

      <h3>Opção C) Sem MySQL (H2 em memória)</h3>
      <p>
        O profile <code>local</code> usa H2 em memória (modo MySQL) + Flyway.
      </p>
      <pre><code class="language-bash"># PowerShell
./mvnw spring-boot:run "-Dspring-boot.run.profiles=local"

# Bash/Zsh
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
</code></pre>

      <h3>Variáveis de ambiente (principais)</h3>
      <ul>
        <li><code>SERVER_PORT</code> (default: 8080)</li>
        <li><code>DB_HOST</code>, <code>DB_PORT</code>, <code>DB_NAME</code>, <code>DB_USER</code>, <code>DB_PASSWORD</code></li>
        <li><code>ZIPKIN_URL</code> (default: <code>http://localhost:9411/api/v2/spans</code>)</li>
        <li><code>API_JWT_SECRET</code>, <code>API_JWT_ISSUER</code>, <code>API_JWT_TTL</code></li>
      </ul>

      <details>
        <summary><b>Se a porta 8080 estiver em uso</b></summary>
        <pre><code class="language-powershell">$env:SERVER_PORT=8081; ./mvnw spring-boot:run</code></pre>
      </details>
    </section>

    <section id="seguranca">
      <h2>🔐 Segurança (JWT / Roles)</h2>
      <p>
        Todos os endpoints em <code>/api/**</code> exigem autenticação JWT, <strong>exceto</strong>:
        <code>/api/auth/register</code>, <code>/api/auth/login</code> e rotas do Swagger (<code>/swagger-ui</code>, <code>/v3/api-docs</code>).
      </p>

      <h3>Roles</h3>
      <ul>
        <li><code>ADMIN</code> — acesso total</li>
        <li><code>CREATOR</code> — escrita (criar/alterar)</li>
        <li><code>READER</code> — leitura (consultas)</li>
      </ul>
      <p>
        Por padrão, um usuário registrado recebe as roles: <code>CREATOR</code> e <code>READER</code>.
      </p>

      <h3>Como obter token</h3>
      <ol>
        <li>Registrar: <code>POST /api/auth/register</code></li>
        <li>Login: <code>POST /api/auth/login</code></li>
      </ol>
      <p>Enviar o token em:</p>
      <pre><code class="language-http">Authorization: Bearer &lt;token&gt;</code></pre>
      <p>
        No Swagger UI, clique em <strong>Authorize</strong> e cole apenas o token (sem o prefixo <code>Bearer</code>).
      </p>
    </section>

    <section id="endpoints">
      <h2>📦 Endpoints (Swagger / Exemplos)</h2>
      <p>
        Swagger UI: <code>http://localhost:8080/swagger-ui.html</code>
      </p>

      <h3>Resumo</h3>
      <table>
        <thead>
          <tr>
            <th align="left">Método</th>
            <th align="left">Rota</th>
            <th align="left">Auth</th>
            <th align="left">Role</th>
            <th align="left">Descrição</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td><code>POST</code></td>
            <td><code>/api/auth/register</code></td>
            <td>Não</td>
            <td>-</td>
            <td>Registrar usuário</td>
          </tr>
          <tr>
            <td><code>POST</code></td>
            <td><code>/api/auth/login</code></td>
            <td>Não</td>
            <td>-</td>
            <td>Autenticar e obter JWT</td>
          </tr>
          <tr>
            <td><code>POST</code></td>
            <td><code>/api/workshops</code></td>
            <td>Sim</td>
            <td><code>CREATOR</code>/<code>ADMIN</code></td>
            <td>Cadastrar workshop</td>
          </tr>
          <tr>
            <td><code>POST</code></td>
            <td><code>/api/colaboradores</code></td>
            <td>Sim</td>
            <td><code>CREATOR</code>/<code>ADMIN</code></td>
            <td>Cadastrar colaborador</td>
          </tr>
          <tr>
            <td><code>POST</code></td>
            <td><code>/api/atas</code></td>
            <td>Sim</td>
            <td><code>CREATOR</code>/<code>ADMIN</code></td>
            <td>Criar ata (1 por workshop)</td>
          </tr>
          <tr>
            <td><code>PUT</code></td>
            <td><code>/api/workshops/{workshopId}/atas/{ataId}</code></td>
            <td>Sim</td>
            <td><code>CREATOR</code>/<code>ADMIN</code></td>
            <td>Adicionar colaborador na ata</td>
          </tr>
          <tr>
            <td><code>DELETE</code></td>
            <td><code>/api/atas/{ataId}/colaboradores/{colaboradorId}</code></td>
            <td>Sim</td>
            <td><code>CREATOR</code>/<code>ADMIN</code></td>
            <td>Remover colaborador da ata</td>
          </tr>
          <tr>
            <td><code>GET</code></td>
            <td><code>/api/atas</code></td>
            <td>Sim</td>
            <td><code>READER</code>/<code>CREATOR</code>/<code>ADMIN</code></td>
            <td>Listar participações (com filtros)</td>
          </tr>
        </tbody>
      </table>

      <h3>Exemplos (curl)</h3>
      <details open>
        <summary><b>1) Registrar + login</b></summary>
        <pre><code class="language-bash">curl -s -H "Content-Type: application/json" \
  -d '{ "nome": "Ana Silva", "email": "ana@fast.com", "senha": "Senha@123" }' \
  http://localhost:8080/api/auth/register

TOKEN=$(curl -s -H "Content-Type: application/json" \
  -d '{ "email": "ana@fast.com", "senha": "Senha@123" }' \
  http://localhost:8080/api/auth/login | jq -r .accessToken)
</code></pre>
      </details>

      <details>
        <summary><b>2) Criar workshop + colaborador + ata</b></summary>
        <pre><code class="language-bash">curl -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{ "nome": "Workshop Spring", "dataRealizacao": "20/01/2026", "descricao": "Conteúdo do workshop..." }' \
  http://localhost:8080/api/workshops

curl -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{ "nome": "Ana Silva" }' \
  http://localhost:8080/api/colaboradores

curl -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{ "workshopId": 1, "colaboradoresIds": [1] }' \
  http://localhost:8080/api/atas
</code></pre>
      </details>

      <details>
        <summary><b>3) Adicionar / remover colaborador na ata</b></summary>
        <pre><code class="language-bash">curl -H "Authorization: Bearer $TOKEN" -X PUT -H "Content-Type: application/json" \
  -d '{ "colaboradorId": 2 }' \
  http://localhost:8080/api/workshops/1/atas/1

curl -H "Authorization: Bearer $TOKEN" -X DELETE \
  http://localhost:8080/api/atas/1/colaboradores/2
</code></pre>
      </details>

      <details>
        <summary><b>4) Listar participações (com filtros)</b></summary>
        <pre><code class="language-bash"># lista geral
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/atas

# filtro por nome do workshop (contém, case-insensitive)
curl -H "Authorization: Bearer $TOKEN" "http://localhost:8080/api/atas?workshopNome=spring"

# filtro por data de realização (dd/MM/yyyy; também aceita yyyy-MM-dd)
curl -H "Authorization: Bearer $TOKEN" "http://localhost:8080/api/atas?data=20/01/2026"
</code></pre>
      </details>
    </section>

    <section id="formatos">
      <h2>📐 Formatos (Datas, Ordenação e Filtros)</h2>

      <h3>Datas</h3>
      <ul>
        <li><strong>JSON</strong> (<code>dataRealizacao</code>): <code>dd/MM/yyyy</code> (ex.: <code>20/01/2026</code>)</li>
        <li><strong>Query param</strong> (<code>data</code> em <code>GET /api/atas</code>): <code>dd/MM/yyyy</code> (compatível com <code>yyyy-MM-dd</code>)</li>
        <li><strong>Timestamp em erros</strong>: <code>dd/MM/yyyy HH:mm:ssXXX</code> (ex.: <code>20/01/2026 21:55:35-03:00</code>)</li>
      </ul>

      <h3>Filtros e ordenação em <code>GET /api/atas</code></h3>
      <ul>
        <li>Filtros opcionais: <code>workshopNome</code> (contém, case-insensitive) e <code>data</code> (igualdade exata de data).</li>
        <li>Combinação: quando ambos são enviados, aplica <strong>AND</strong>.</li>
        <li>Ordenação do resultado: <strong>colaborador.nome ASC</strong>, depois <strong>workshop.dataRealizacao ASC</strong>, depois <strong>workshop.nome ASC</strong>.</li>
      </ul>
    </section>

    <section id="tratamento-erros">
      <h2>🚧 Tratamento de Erros</h2>
      <p>
        A API padroniza erros em <code>ErrorResponse</code> e mapeia exceções de forma consistente
        (<code>400</code> validação/JSON, <code>401/403</code> segurança, <code>404</code> não encontrado, <code>409</code> conflito, <code>500</code> erro interno).
      </p>

      <h3>Formato do erro</h3>
      <pre><code class="language-json">{
  "exceptionId": "9d6e08ae-9d72-4e59-8f0a-0c49f3d5b5f2",
  "timestamp": "20/01/2026 21:55:35-03:00",
  "status": 400,
  "errorCode": "VAL_001_VALIDATION_ERROR",
  "message": "Erro de validação: nome - nome deve ter entre 2 e 120 caracteres",
  "category": "VALIDATION",
  "severity": "LOW",
  "retryable": false,
  "path": "/api/colaboradores",
  "context": {
    "errors": [
      { "field": "nome", "message": "nome deve ter entre 2 e 120 caracteres" }
    ]
  }
}</code></pre>
    </section>

    <section id="observabilidade">
      <h2>📈 Observabilidade (Actuator / Prometheus / Zipkin)</h2>

      <h3>URLs úteis</h3>
      <table>
        <thead>
          <tr>
            <th align="left">Ferramenta</th>
            <th align="left">URL</th>
            <th align="left">Obs.</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>Swagger UI</td>
            <td><code>http://localhost:8080/swagger-ui.html</code></td>
            <td>Documentação + testes dos endpoints</td>
          </tr>
          <tr>
            <td>Health</td>
            <td><code>http://localhost:8080/actuator/health</code></td>
            <td>Inclui detalhes e health checks custom</td>
          </tr>
          <tr>
            <td>Prometheus</td>
            <td><code>http://localhost:8080/actuator/prometheus</code></td>
            <td>Endpoint scrape</td>
          </tr>
          <tr>
            <td>Grafana</td>
            <td><code>http://localhost:3000</code></td>
            <td>admin/admin (quando usar docker-compose-monitoring)</td>
          </tr>
          <tr>
            <td>Zipkin</td>
            <td><code>http://localhost:9411</code></td>
            <td>Tracing distribuído</td>
          </tr>
        </tbody>
      </table>

      <h3>Métricas customizadas (Micrometer)</h3>
      <ul>
        <li><code>atas.criadas.total</code> — total de atas criadas</li>
        <li><code>auth.login.total{status="success|failed"}</code> — tentativas de login</li>
        <li><code>business.operation.duration{operation="..."}</code> — duração de operações</li>
      </ul>

      <details>
        <summary><b>PromQL – exemplos úteis</b></summary>
        <pre><code class="language-promql"># Taxa de criação de atas por minuto
rate(atas_criadas_total[5m]) * 60

# P95 de latência HTTP
histogram_quantile(0.95, sum(rate(http_server_requests_seconds_bucket[5m])) by (uri, le))
</code></pre>
      </details>
    </section>

    <section id="testes">
      <h2>🧪 Testes</h2>
      <p>
        A suíte combina testes unitários, integração (Testcontainers) e cenários específicos (performance/segurança).
      </p>
      <p>
        <strong>Testes de arquitetura (ArchUnit)</strong>: valida regras de codificação e a arquitetura em camadas
        (domain/application/presentation/infrastructure). Veja: <code>CodingRulesArchTest</code> e
        <code>HexagonalArchitectureArchTest</code>.
      </p>

      <pre><code class="language-bash"># Todos os testes
./mvnw clean test

# Apenas unitários
./mvnw test -Dtest="*UseCaseImplTest"

# Apenas integração (Testcontainers)
./mvnw test -Dtest="*IntegrationTest"
</code></pre>
    </section>

    <section id="boas-praticas">
      <h2>🏅 Boas Práticas &amp; Padrões</h2>
      <ul>
        <li><strong>Clean-ish Architecture</strong>: separação entre presentation / application / domain / infrastructure.</li>
        <li><strong>Ports &amp; Adapters</strong>: repositórios e serviços expostos como <code>port/output</code> e implementados em adapters.</li>
        <li><strong>Validação robusta</strong>: Bean Validation + anotações customizadas por campo e DTO.</li>
        <li><strong>MapStruct</strong>: mapeamento declarativo (DTOs/Commands/Domain) com <code>componentModel="spring"</code>.</li>
        <li><strong>Erros padronizados</strong>: categorias e códigos consistentes.</li>
        <li><strong>Observabilidade</strong>: Actuator + métricas e tracing para depuração e SLA.</li>
        <li><strong>Banco versionado</strong>: Flyway + <code>ddl-auto=validate</code> para evitar drift de schema.</li>
        <li><strong>Segurança por método</strong>: <code>@PreAuthorize</code> por endpoint (role-based).</li>
      </ul>
    </section>

    <section id="licenca-autor">
      <h2 align="center">💻 Autor</h2>
      <div align="center">
        <p>Edvaldo Vitor</p>
        <p><a href="https://github.com/edvaldovitor250" target="_blank" rel="noopener">github.com/edvaldovitor250</a></p>
      </div>

      <h2 align="center">📄 Licença</h2>
      <p align="center">
        Este projeto está licenciado sob a MIT License. Veja o arquivo <a href="LICENSE">LICENSE</a> para mais detalhes.
      </p>
    </section>
  </main>
</body>

