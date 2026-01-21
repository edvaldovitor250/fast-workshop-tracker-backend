package br.com.fast.workshoptracker.domain.exception.codes;

import br.com.fast.workshoptracker.domain.exception.core.ErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.ErrorCodeMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalErrorCode implements ErrorCode {
	TECH_001_INTERNAL_ERROR(
			new ErrorCodeMeta("Erro interno", ErrorCategory.TECHNICAL, ErrorSeverity.CRITICAL, false)
	);

	private final ErrorCodeMeta meta;
}

