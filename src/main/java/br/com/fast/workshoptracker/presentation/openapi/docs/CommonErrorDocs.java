package br.com.fast.workshoptracker.presentation.openapi.docs;

public final class CommonErrorDocs {
	private CommonErrorDocs() {
	}

	public static final String ERROR_400_VALIDATION = """
			{
			  "timestamp": "20/01/2026 21:55:35-03:00",
			  "status": 400,
			  "errorCode": "VALIDATION_ERROR",
			  "message": "Erro de validacao: nome - nome deve ter entre 2 e 120 caracteres",
			  "path": "/api/colaboradores"
			}
			""";

	public static final String ERROR_400_BAD_REQUEST = """
			{
			  "timestamp": "20/01/2026 21:55:35-03:00",
			  "status": 400,
			  "errorCode": "BAD_REQUEST",
			  "message": "JSON invalido ou formato de data invalido",
			  "path": "/api/workshops"
			}
			""";

	public static final String ERROR_401_UNAUTHORIZED = """
			{
			  "timestamp": "20/01/2026 21:55:35-03:00",
			  "status": 401,
			  "errorCode": "UNAUTHORIZED",
			  "message": "Credenciais invalidas",
			  "path": "/api/auth/login"
			}
			""";

	public static final String ERROR_404_NOT_FOUND = """
			{
			  "timestamp": "20/01/2026 21:55:35-03:00",
			  "status": 404,
			  "errorCode": "NOT_FOUND",
			  "message": "Recurso nao encontrado",
			  "path": "/api/atas"
			}
			""";

	public static final String ERROR_409_CONFLICT = """
			{
			  "timestamp": "20/01/2026 21:55:35-03:00",
			  "status": 409,
			  "errorCode": "CONFLICT",
			  "message": "Conflito de dados",
			  "path": "/api/atas"
			}
			""";
}

