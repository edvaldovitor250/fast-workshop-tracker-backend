package br.com.fast.workshoptracker.domain.exception.codes;

import br.com.fast.workshoptracker.domain.exception.core.ErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.ErrorCodeMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidationErrorCode implements ErrorCode {
	VAL_001_VALIDATION_ERROR(
			new ErrorCodeMeta("Erro de validacao", ErrorCategory.VALIDATION, ErrorSeverity.LOW, false)
	),
	VAL_002_BAD_REQUEST(
			new ErrorCodeMeta("Requisicao invalida", ErrorCategory.VALIDATION, ErrorSeverity.LOW, false)
	),
	VAL_003_INVALID_JSON(
			new ErrorCodeMeta("JSON invalido", ErrorCategory.VALIDATION, ErrorSeverity.LOW, false)
	),
	VAL_004_TYPE_MISMATCH(
			new ErrorCodeMeta("Parametro invalido", ErrorCategory.VALIDATION, ErrorSeverity.LOW, false)
	);

	private final ErrorCodeMeta meta;
}

