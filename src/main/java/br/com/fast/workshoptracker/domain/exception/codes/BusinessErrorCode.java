package br.com.fast.workshoptracker.domain.exception.codes;

import br.com.fast.workshoptracker.domain.exception.core.ErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.ErrorCodeMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorCode implements ErrorCode {
	BUS_001_NOT_FOUND(
			new ErrorCodeMeta("Recurso nao encontrado", ErrorCategory.BUSINESS, ErrorSeverity.MEDIUM, false)
	),
	BUS_002_CONFLICT(
			new ErrorCodeMeta("Conflito de negocio", ErrorCategory.BUSINESS, ErrorSeverity.HIGH, false)
	),
	BUS_003_DATA_INTEGRITY(
			new ErrorCodeMeta("Conflito de dados", ErrorCategory.BUSINESS, ErrorSeverity.HIGH, false)
	);

	private final ErrorCodeMeta meta;
}

