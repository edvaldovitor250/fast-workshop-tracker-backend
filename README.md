<header>
  <h1 align="center">📊 Workshop Tracker – Frontend (Angular 21 + Bootstrap 5.3)</h1>
  <p align="center">
    <img src="https://img.shields.io/badge/angular-21.1-red" alt="Angular 21.1"/>
    <img src="https://img.shields.io/badge/bootstrap-5.3.8-7952B3" alt="Bootstrap 5.3.8"/>
    <img src="https://img.shields.io/badge/ng--bootstrap-20.0.0-563D7C" alt="ng-bootstrap 20.0.0"/>
    <img src="https://img.shields.io/badge/chart.js-4.5.1-FF6384" alt="Chart.js 4.5.1"/>
    <img src="https://img.shields.io/badge/rxjs-7.8-B7178C" alt="RxJS 7.8"/>
    <img src="https://img.shields.io/badge/typescript-5.9-3178C6" alt="TypeScript 5.9"/>
    <img src="https://img.shields.io/badge/standalone-components-blue" alt="Standalone Components"/>
    <img src="https://img.shields.io/badge/tests-vitest-6E9F18" alt="Vitest"/>
    <img src="https://img.shields.io/badge/icons-bootstrap--icons-111827" alt="Bootstrap Icons"/>
    <img src="https://img.shields.io/badge/ui-kit-shared%2Fui-0EA5E9" alt="UI Kit"/>
    <img src="https://img.shields.io/badge/build-npm-CC0000" alt="npm build"/>
  </p>

  <p align="center">
    Desafio <strong>FAST – Soluções Tecnológicas</strong> (Frontend): UI para acompanhar workshops, colaboradores e atas
    (presenças), com filtros, tabelas e analytics.
    <br />
    Este README concentra <strong>toda</strong> a documentação do projeto (arquitetura, UI Kit/contratos, execução e padrões).
  </p>
</header>

<main>
  <h2>🧭 Índice</h2>
  <ol>
    <li><a href="#visao-geral">Visão Geral</a></li>
    <li><a href="#rotas">Rotas</a></li>
    <li><a href="#arquitetura">Arquitetura &amp; Pastas</a></li>
    <li><a href="#tech-stack">Tech Stack &amp; Dependências</a></li>
    <li><a href="#ui-kit">UI Kit (Contrato)</a></li>
    <li><a href="#estilos">Estilos (Tokens, Layout e Componentes)</a></li>
    <li><a href="#formatos">Formatos (Datas e Filtros)</a></li>
    <li><a href="#padroes-codigo">Padrões de Código</a></li>
    <li><a href="#dados">Dados &amp; Integração</a></li>
    <li><a href="#como-rodar">Como Rodar</a></li>
    <li><a href="#testes">Testes</a></li>
    <li><a href="#contratos">Contratos (anti-despadronização)</a></li>
    <li><a href="#autor">Autor</a></li>
  </ol>

  <section id="visao-geral">
    <h2>ℹ️ Visão Geral</h2>
    <p>
      O <strong>Workshop Tracker (Frontend)</strong> é uma aplicação Angular (standalone) com Bootstrap 5.3
      que padroniza toda a UI via <strong>shared/ui</strong>: cabeçalho de página, barra de filtros, tabela base e estados
      (loading/empty/error). O objetivo é garantir consistência de layout, espaçamento, botões e componentes reutilizáveis.
    </p>
    <h3>📌 Domínio (modelos TS puros)</h3>
    <ul>
      <li><strong>Colaborador</strong>: <code>id</code>, <code>nome</code> (<code>src/app/shared/domain/colaborador.model.ts</code>)</li>
      <li><strong>Workshop</strong>: <code>id</code>, <code>nome</code>, <code>dataRealizacao</code>, <code>descricao</code> (<code>src/app/shared/domain/workshop.model.ts</code>)</li>
      <li><strong>Ata</strong>: <code>id</code>, <code>workshop</code>, <code>colaboradores</code> (<code>src/app/shared/domain/ata.model.ts</code>)</li>
    </ul>
  </section>

  <section id="rotas">
    <h2>🧩 Rotas</h2>
    <ul>
      <li><code>/dashboard</code> — visão geral + cards + filtro + tabela (atas)</li>
      <li><code>/analytics</code> — gráficos (ng2-charts/Chart.js) + ranking em tabela</li>
      <li><code>/atas</code> — listagem paginada (filtros + tabela padrão)</li>
      <li><code>/workshops/:id</code> — detalhe do workshop + participantes</li>
    </ul>
    <details>
      <summary><b>Onde ficam as rotas</b></summary>
      <p><code>src/app/app.routes.ts</code> usa <code>loadComponent</code> (lazy) para cada page.</p>
    </details>
  </section>

  <section id="arquitetura">
    <h2>🏗️ Arquitetura &amp; Pastas</h2>
    <p>
      A arquitetura é <strong>core / shared / features</strong>, com fronteiras explícitas para escalar o código e evitar
      dependências cruzadas entre features.
    </p>
    <h3>📦 Estrutura (alto nível)</h3>
    <pre><code>src/app
 ┣ core/                # cross-cutting (layout shell, http, auth, data-access app-wide)
 ┣ shared/              # contrato obrigatório (domain TS-only, styles, ui, pipes)
 ┗ features/            # módulos por domínio (analytics, dashboard, atas, workshops...)
</code></pre>
    <h3>✅ Regras de dependência (não negociáveis)</h3>
    <ul>
      <li><code>core</code> pode depender de <code>shared</code></li>
      <li><code>features</code> podem depender de <code>core</code> e <code>shared</code></li>
      <li><code>shared</code> <strong>não</strong> pode depender de <code>features</code></li>
      <li><code>features/*/domain</code> é TypeScript puro (sem Angular)</li>
    </ul>
    <details>
      <summary><b>Diagrama (dependências)</b></summary>
      <pre><code class="language-mermaid">flowchart LR
  shared[shared] --> core[core]
  shared --> features[features/*]
  core --> features
  features -. não importa .-> features
</code></pre>
    </details>
    <details>
      <summary><b>Estrutura detalhada (resumo real do projeto)</b></summary>
      <pre><code>src/app
  app.routes.ts           # rotas lazy (loadComponent)
  app.config.ts           # providers (standalone)

  core/
    layout/               # shell + header + sidebar + footer
    http/interceptors/    # interceptors (auth, etc.)
    data-access/          # services app-wide (hoje: mocks)
    utils/                # helpers (ex.: formatação de datas)

  shared/
    domain/               # modelos TS puros (cross-feature)
    styles/               # tokens/layout/componentes globais
    ui/                   # UI Kit (contrato obrigatório)
    pipes/                # pipes compartilhados

  features/
    dashboard/            # pages/ui específicos
    analytics/            # charts + ranking
    atas/                 # lista paginada
    workshops/            # detalhe do workshop
</code></pre>
    </details>
  </section>

  <section id="tech-stack">
    <h2>🛠️ Tech Stack &amp; Dependências</h2>
    <ul>
      <li><strong>Angular 21</strong> (standalone + lazy routes via <code>loadComponent</code>)</li>
      <li><strong>Bootstrap 5.3</strong> + <strong>Bootstrap Icons</strong></li>
      <li><strong>ng-bootstrap</strong> (ex.: dropdown/pagination)</li>
      <li><strong>Chart.js</strong> + <strong>ng2-charts</strong> (analytics)</li>
      <li><strong>RxJS 7.8</strong> (viewmodels reativos com <code>vm$</code>)</li>
      <li><strong>Vitest</strong> (via <code>ng test</code>)</li>
    </ul>
    <details>
      <summary><b>Onde ficam as versões</b></summary>
      <p><code>package.json</code> concentra as dependências e versões.</p>
    </details>
  </section>

  <section id="ui-kit">
    <h2>🧰 UI Kit (Contrato)</h2>
    <p>
      O UI Kit fica em <code>src/app/shared/ui</code> e é o <strong>único caminho</strong> para padronizar botões, filtros,
      tabelas, estados e header de página.
    </p>
    <h3>📄 Wrapper padrão (.page)</h3>
    <ul>
      <li>Todas as telas herdam alinhamento/recuo via wrapper <code>.page</code> no shell (<code>src/app/core/layout/shell/shell.component.html</code>).</li>
      <li>O CSS do wrapper é global e vive em <code>src/app/shared/styles/_layout.scss</code>.</li>
    </ul>
    <h3>🧱 Componentes obrigatórios</h3>
    <ul>
      <li><code>app-page-header</code> — título/ícone/badge + slot de ações</li>
      <li><code>app-filters-bar</code> — barra de filtros padrão (colaborador/workshop/data)</li>
      <li><code>app-data-table</code> — “card” padrão de tabela + header + scroll</li>
      <li><code>app-ata-table</code> — tabela padrão de atas (um único componente para o app)</li>
      <li><code>app-loading-state</code>, <code>app-empty-state</code>, <code>app-error-state</code> — estados padrão</li>
    </ul>
    <details>
      <summary><b>Exemplos rápidos (uso recomendado)</b></summary>
      <pre><code class="language-html">&lt;app-page-header icon="bi-speedometer2" title="Dashboard" subtitle="..." /&gt;

&lt;app-filters-bar
  [resultsCount]="vm.total"
  [colaboradorControl]="colaboradorControl"
  [workshopControl]="workshopControl"
  [dataControl]="dataControl"
  (apply)="onApplyFiltersClick()"
  (clear)="clearFilters()"
/&gt;

&lt;app-data-table icon="bi-table" title="Registros"&gt;
  &lt;table class="app-data-table__table"&gt;...&lt;/table&gt;
&lt;/app-data-table&gt;</code></pre>
    </details>
    <details>
      <summary><b>Tabela padrão de Atas (unificada)</b></summary>
      <ul>
        <li>Componente: <code>src/app/shared/ui/ata-table/ata-table.component.ts</code></li>
        <li>Entrada: <code>rows: AtaTableRow[]</code></li>
        <li>Saída: <code>(openDetails)</code> com <code>workshopId</code></li>
      </ul>
    </details>
  </section>

  <section id="estilos">
    <h2>🎨 Estilos (Tokens, Layout e Componentes)</h2>
    <p>
      Padrões globais (tokens/layout/componentes) são centralizados em <code>src/app/shared/styles</code> e importados em
      <code>src/styles.scss</code>. É proibido manter “estilos globais disfarçados” em pastas de pages.
    </p>
    <ul>
      <li><strong>Tokens</strong>: <code>src/app/shared/styles/_tokens.scss</code> (CSS vars, cores, spacing, radius)</li>
      <li><strong>Layout</strong>: <code>src/app/shared/styles/_layout.scss</code> (ex.: <code>.page</code>)</li>
      <li><strong>Componentes</strong>: <code>src/app/shared/styles/_components.scss</code> (ex.: <code>.stats-grid</code>, <code>.btn-icon</code>)</li>
      <li><strong>Globais</strong>: <code>src/styles.scss</code> (overrides Bootstrap, focus, animações <code>.fade-in</code>)</li>
    </ul>
    <details>
      <summary><b>Ícones e botões</b></summary>
      <ul>
        <li>Ícones: Bootstrap Icons (<code>bootstrap-icons</code>)</li>
        <li>Botões: variantes Bootstrap (<code>.btn-primary</code>, <code>.btn-outline-secondary</code>, etc.) + <code>.btn-icon</code> (icon-only)</li>
      </ul>
    </details>
  </section>

  <section id="formatos">
    <h2>📐 Formatos (Datas e Filtros)</h2>
    <h3>Datas</h3>
    <ul>
      <li>Persistência atual (mock): <code>workshop.dataRealizacao</code> em formato ISO <code>yyyy-MM-dd</code> (ex.: <code>2024-02-10</code>).</li>
      <li>Filtro de data: o input HTML <code>type="date"</code> retorna <code>yyyy-MM-dd</code>, compatível com o formato do mock.</li>
      <li>Exibição: formatação via <code>Intl.DateTimeFormat</code> (pt-BR) em <code>src/app/core/utils/date-format.ts</code>.</li>
    </ul>
    <h3>Filtros</h3>
    <ul>
      <li><code>colaborador</code>: contém (case-insensitive)</li>
      <li><code>workshop</code>: contém em <code>nome</code> + <code>descricao</code> (case-insensitive)</li>
      <li><code>data</code>: igualdade exata (string ISO)</li>
    </ul>
  </section>

  <section id="padroes-codigo">
    <h2>🧠 Padrões de Código</h2>
    <ul>
      <li><strong>Standalone</strong>: pages e UI components são <code>standalone: true</code></li>
      <li><strong>OnPush</strong>: <code>ChangeDetectionStrategy.OnPush</code> por padrão</li>
      <li><strong>State/ViewModel</strong>: pages expõem <code>vm$</code> e consomem com <code>async</code> pipe</li>
      <li><strong>Sem subscribe manual</strong>: composição via RxJS (<code>combineLatest</code>, <code>map</code>, <code>debounceTime</code>)</li>
      <li><strong>Utilitários</strong>: formatação de data em <code>src/app/core/utils/date-format.ts</code></li>
    </ul>
  </section>

  <section id="dados">
    <h2>🗃️ Dados &amp; Integração</h2>
    <p>
      Hoje os dados são <strong>mockados</strong> (para UI/fluxos) e expostos via services “app-wide” em
      <code>src/app/core/data-access</code>.
    </p>
    <ul>
      <li>Mock principal: <code>src/app/core/data-access/mocks/atas.mock.ts</code></li>
      <li>Services: <code>src/app/core/data-access/ata.service.ts</code>, <code>workshop.service.ts</code>, <code>colaborador.service.ts</code></li>
    </ul>
    <details>
      <summary><b>Como os mocks funcionam hoje (padrões usados)</b></summary>
      <ul>
        <li><strong>Fonte de verdade</strong>: <code>ATAS_MOCK</code> (lista de atas) em <code>src/app/core/data-access/mocks/atas.mock.ts</code>.</li>
        <li><strong>Cache in-memory</strong>: services usam <code>shareReplay</code> para evitar recomputar/reemitir em cada subscribe.</li>
        <li><strong>Derivação</strong>: <code>WorkshopService</code> e <code>ColaboradorService</code> derivam seus dados a partir de <code>AtaService</code> (sem duplicar fonte de dados).</li>
        <li><strong>Observables</strong>: API pública dos services é reativa (<code>getAll(): Observable&lt;readonly T[]&gt;</code>).</li>
      </ul>
    </details>
    <details>
      <summary><b>Como plugar com uma API real (guia curto)</b></summary>
      <ol>
        <li>Trocar os services do <code>core/data-access</code> para usar <code>HttpClient</code> (mantendo a mesma API pública: <code>getAll()</code>).</li>
        <li>Criar um <code>environment</code> com <code>API_URL</code> e centralizar rotas.</li>
        <li>Manter models TS em <code>shared/domain</code> e mapear DTOs (não acoplar UI ao contrato HTTP diretamente).</li>
      </ol>
    </details>
  </section>

  <section id="como-rodar">
    <h2>🚀 Como Rodar</h2>
    <h3>Pré-requisitos</h3>
    <ul>
      <li>Node.js (recomendado LTS) + npm</li>
    </ul>
    <h3>Instalar</h3>
    <pre><code class="language-bash">npm ci</code></pre>
    <h3>Rodar em dev</h3>
    <pre><code class="language-bash">npm start</code></pre>
    <p>Abra: <code>http://localhost:4200</code></p>
    <h3>Build</h3>
    <pre><code class="language-bash">npm run build</code></pre>
    <details>
      <summary><b>Outros scripts úteis</b></summary>
      <pre><code class="language-bash"># build em modo watch
npm run watch</code></pre>
    </details>
  </section>

  <section id="testes">
    <h2>🧪 Testes</h2>
    <p>O projeto usa o runner padrão do Angular 21 (Vitest via <code>ng test</code>).</p>
    <pre><code class="language-bash">npm test</code></pre>
  </section>

  <section id="contratos">
    <h2>📏 Contratos (anti-despadronização)</h2>
    <ul>
      <li><code>shared</code> não importa <code>features</code> (nunca)</li>
      <li><code>features</code> não importam outras <code>features</code> (use <code>core</code>/<code>shared</code>)</li>
      <li><code>shared/domain</code> e <code>features/*/domain</code> são TS puro (sem Angular)</li>
      <li>Toda page deve usar <code>app-page-header</code> no topo</li>
      <li>Filtros: usar <code>app-filters-bar</code> (sem filtros por feature)</li>
      <li>Tabelas: usar <code>app-data-table</code> + <code>table.app-data-table__table</code></li>
      <li>Atas: usar apenas <code>app-ata-table</code> (sem duplicar tabelas por feature)</li>
      <li>Estados: usar components de state em <code>shared/ui</code></li>
      <li>Layout: padding/recuo esquerdo é responsabilidade do <code>.page</code> (proibido em pages)</li>
      <li>Padrões globais de SCSS ficam em <code>shared/styles</code> (não em <code>pages/**/styles</code>)</li>
    </ul>
  </section>
 <section id="licenca-autor">
      <h2 align="center">💻 Autor</h2>
      <div align="center">
  <img
    src="https://github.com/user-attachments/assets/af9619af-b4fc-4b18-b1a1-17a7e563741e"
    alt="Edvaldo Vitor"
    width="260"
    height="260"
  />

  <p><strong>Edvaldo Vitor</strong></p>
  <p>
    <a href="https://github.com/edvaldovitor250" target="_blank" rel="noopener">
      github.com/edvaldovitor250
    </a>
  </p>
</div>
      <h2 align="center">📄 Licença</h2>
      <p align="center">
        Este projeto está licenciado sob a MIT License. Veja o arquivo <a href="LICENSE">LICENSE</a> para mais detalhes.
      </p>
    </section>
</main>
