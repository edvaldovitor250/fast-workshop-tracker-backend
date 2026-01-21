package br.com.fast.workshoptracker.presentation.openapi.docs;

public final class AuthDocs {
	private AuthDocs() {
	}

	public static final String TAG = "Auth";

	public static final String REGISTER_SUMMARY = "Registrar usuario (nome/e-mail/senha)";
	public static final String REGISTER_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Registro de Usuario</h3>
			    <p>Cria um usuario para autenticacao via JWT.</p>
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
			          <td>`@UsuarioNome`</td>
			          <td>Nome do usuario.</td>
			          <td>Ana Silva</td>
			        </tr>
			        <tr>
			          <td><strong>email</strong></td>
			          <td>`@EmailAddress`</td>
			          <td>E-mail unico (nao pode repetir).</td>
			          <td>ana@fast.com</td>
			        </tr>
			        <tr>
			          <td><strong>senha</strong></td>
			          <td>`@Senha`</td>
			          <td>Senha (min. 6).</td>
			          <td>Senha@123</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String REGISTER_REQUEST_EXAMPLE = """
			{
			  "nome": "Ana Silva",
			  "email": "ana@fast.com",
			  "senha": "Senha@123"
			}
			""";

	public static final String REGISTER_201_RESPONSE = """
			{
			  "id": 1,
			  "nome": "Ana Silva",
			  "email": "ana@fast.com",
			  "roles": ["CREATOR", "READER"]
			}
			""";

	public static final String LOGIN_SUMMARY = "Login (e-mail/senha) -> token JWT";
	public static final String LOGIN_DESCRIPTION = """
			<html>
			  <body>
			    <h3>Login</h3>
			    <p>Autentica e retorna um token JWT.</p>
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
			          <td><strong>email</strong></td>
			          <td>`@EmailAddress`</td>
			          <td>E-mail do usuario.</td>
			          <td>ana@fast.com</td>
			        </tr>
			        <tr>
			          <td><strong>senha</strong></td>
			          <td>`@Senha`</td>
			          <td>Senha do usuario.</td>
			          <td>Senha@123</td>
			        </tr>
			      </tbody>
			    </table>
			  </body>
			</html>
			""";

	public static final String LOGIN_REQUEST_EXAMPLE = """
			{
			  "email": "ana@fast.com",
			  "senha": "Senha@123"
			}
			""";

	public static final String LOGIN_200_RESPONSE = """
			{
			  "tokenType": "Bearer",
			  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3b3Jrc2hvcC10cmFja2VyIiwic3ViIjoiYW5hQGZhc3QuY29tIiwiZXhwIjoxNzM3NDAwMDAwfQ.XXXX",
			  "expiresAt": "2026-01-20T21:30:00Z",
			  "roles": ["CREATOR", "READER"]
			}
			""";
}
