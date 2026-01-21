package br.com.fast.workshoptracker.domain.exception.codes;

import br.com.fast.workshoptracker.domain.exception.core.ErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.ErrorCodeMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	AUTH_001_INVALID_CREDENTIALS(
			new ErrorCodeMeta("Credenciais invalidas", ErrorCategory.SECURITY, ErrorSeverity.MEDIUM, false)
	),
	AUTH_002_UNAUTHORIZED(
			new ErrorCodeMeta("Nao autenticado", ErrorCategory.SECURITY, ErrorSeverity.MEDIUM, false)
	),
	AUTH_003_FORBIDDEN(
			new ErrorCodeMeta("Acesso negado", ErrorCategory.SECURITY, ErrorSeverity.MEDIUM, false)
	);

	private final ErrorCodeMeta meta;
}

