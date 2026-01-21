package br.com.fast.workshoptracker.api.openapi.docs;

public final class ColaboradorDocs {
	private ColaboradorDocs() {
	}

	public static final String TAG = "Colaboradores";

	public static final String CREATE_SUMMARY = "Cadastrar colaborador";
	public static final String CREATE_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Criacao de Colaborador</h3>
			    <p>Cadastra um colaborador.</p>
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
			          <td>`@ColaboradorNome`</td>
			          <td>Nome do colaborador.</td>
			          <td>Ana Silva</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String CREATE_REQUEST_EXAMPLE = """
			{
			  "nome": "Ana Silva"
			}
			""";

	public static final String CREATE_201_RESPONSE = """
			{
			  "id": 1,
			  "nome": "Ana Silva"
			}
			""";
}
