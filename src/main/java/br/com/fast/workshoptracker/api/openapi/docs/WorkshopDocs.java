package br.com.fast.workshoptracker.api.openapi.docs;

public final class WorkshopDocs {
	private WorkshopDocs() {
	}

	public static final String TAG = "Workshops";

	public static final String CREATE_SUMMARY = "Cadastrar workshop";
	public static final String CREATE_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Criacao de Workshop</h3>
			    <p>Cadastra um workshop.</p>
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
			          <td><strong>nome</strong></td>
			          <td>`@WorkshopNome`</td>
			          <td>Nome do workshop.</td>
			          <td>Workshop Spring</td>
			        </tr>
			        <tr>
			          <td><strong>dataRealizacao</strong></td>
			          <td>`@DataRealizacao`</td>
			          <td>Data de realizacao (yyyy-MM-dd).</td>
			          <td>2026-01-20</td>
			        </tr>
			        <tr>
			          <td><strong>descricao</strong></td>
			          <td>`@WorkshopDescricao`</td>
			          <td>Descricao (opcional).</td>
			          <td>Conteudo do workshop...</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String CREATE_REQUEST_EXAMPLE = """
			{
			  "nome": "Workshop Spring",
			  "dataRealizacao": "2026-01-20",
			  "descricao": "Conteudo do workshop..."
			}
			""";

	public static final String CREATE_201_RESPONSE = """
			{
			  "id": 1,
			  "nome": "Workshop Spring",
			  "dataRealizacao": "2026-01-20",
			  "descricao": "Conteudo do workshop..."
			}
			""";
}
