package br.com.fast.workshoptracker.presentation.openapi.docs;

public final class AtaDocs {
	private AtaDocs() {
	}

	public static final String TAG = "Atas";

	public static final String CREATE_SUMMARY = "Criar ata de presenca";
	public static final String CREATE_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Criacao de Ata</h3>
			    <p>Cria a ata de presenca de um workshop.</p>
			    <p><b>Regras:</b> se ja existir ata para o workshop, retorna 409.</p>
			    <table cellpadding="5" cellspacing="0">
			      <thead>
			        <tr>
			          <th width="20%">Campo</th>
			          <th width="20%">Validado por</th>
			          <th width="30%">Descricao</th>
			          <th width="30%">Exemplo</th>
			        </tr>
			      </thead>
			      <tbody>
			        <tr>
			          <td><strong>workshopId</strong></td>
			          <td>`@WorkshopId`</td>
			          <td>ID do workshop.</td>
			          <td>1</td>
			        </tr>
			        <tr>
			          <td><strong>colaboradoresIds</strong></td>
			          <td>`@ColaboradoresIds`, `@ColaboradorId`</td>
			          <td>IDs presentes (pode ser vazio/ausente).</td>
			          <td>[1,2,3]</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String CREATE_REQUEST_EXAMPLE = """
			{
			  "workshopId": 1,
			  "colaboradoresIds": [1, 2, 3]
			}
			""";

	public static final String CREATE_201_RESPONSE = """
			{
			  "id": 1,
			  "workshop": {
			    "id": 1,
			    "nome": "Workshop Spring",
			    "dataRealizacao": "20/01/2026",
			    "descricao": "Conteudo do workshop..."
			  },
			  "colaboradores": [
			    { "id": 1, "nome": "Ana Silva" },
			    { "id": 2, "nome": "Bruno Souza" }
			  ]
			}
			""";

	public static final String ADD_COLABORADOR_SUMMARY = "Adicionar colaborador em uma ata";
	public static final String ADD_COLABORADOR_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Adicionar Colaborador</h3>
			    <p>Adiciona um colaborador na ata vinculada ao workshop.</p>
			    <p><b>Path:</b> use <code>workshopId</code> e <code>ataId</code> como IDs positivos.</p>
			    <table cellpadding="5" cellspacing="0">
			      <thead>
			        <tr>
			          <th width="20%">Campo</th>
			          <th width="20%">Validado por</th>
			          <th width="30%">Descricao</th>
			          <th width="30%">Exemplo</th>
			        </tr>
			      </thead>
			      <tbody>
			        <tr>
			          <td><strong>colaboradorId</strong></td>
			          <td>`@ColaboradorId`</td>
			          <td>ID do colaborador a adicionar.</td>
			          <td>10</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String ADD_COLABORADOR_REQUEST_EXAMPLE = """
			{
			  "colaboradorId": 10
			}
			""";

	public static final String ADD_COLABORADOR_200_RESPONSE = """
			{
			  "id": 1,
			  "workshop": {
			    "id": 1,
			    "nome": "Workshop Spring",
			    "dataRealizacao": "20/01/2026",
			    "descricao": "Conteudo do workshop..."
			  },
			  "colaboradores": [
			    { "id": 1, "nome": "Ana Silva" },
			    { "id": 10, "nome": "Carlos Lima" }
			  ]
			}
			""";

	public static final String REMOVE_COLABORADOR_SUMMARY = "Remover colaborador de uma ata";
	public static final String REMOVE_COLABORADOR_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Remover Colaborador</h3>
			    <p>Remove um colaborador de uma ata.</p>
			    <p><b>Path:</b> use <code>ataId</code> e <code>colaboradorId</code> como IDs positivos.</p>
			  </body>
			</html>
			""";

	public static final String LIST_SUMMARY = "Listar colaboradores e workshops que participaram";
	public static final String LIST_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Participacoes</h3>
			    <p>Lista colaboradores (ordem alfabetica) e workshops que participaram.</p>
			    <p><b>Filtros:</b> <code>workshopNome</code> e <code>data</code> sao opcionais e podem ser combinados (AND).</p>
			    <table cellpadding="5" cellspacing="0">
			      <thead>
			        <tr>
			          <th width="20%">Campo</th>
			          <th width="20%">Validado por</th>
			          <th width="30%">Descricao</th>
			          <th width="30%">Exemplo</th>
			        </tr>
			      </thead>
			      <tbody>
			        <tr>
			          <td><strong>workshopNome</strong></td>
			          <td>-</td>
			          <td>Filtra por nome contendo (case-insensitive).</td>
			          <td>spring</td>
			        </tr>
			        <tr>
			          <td><strong>data</strong></td>
			          <td>`@DateTimeFormat(pattern = "dd/MM/yyyy")`</td>
			          <td>Filtra por data (dd/MM/yyyy).</td>
			          <td>20/01/2026</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String LIST_200_RESPONSE = """
			[
			  {
			    "colaboradorId": 1,
			    "nome": "Ana Silva",
			    "workshops": [
			      {
			        "id": 1,
			        "nome": "Workshop Spring",
			        "dataRealizacao": "20/01/2026",
			        "descricao": "Conteudo do workshop..."
			      }
			    ]
			  }
			]
			""";
}
