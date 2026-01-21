package br.com.fast.workshoptracker.domain.exception.core;

import br.com.fast.workshoptracker.domain.exception.codes.ErrorCategory;
import br.com.fast.workshoptracker.domain.exception.codes.ErrorSeverity;

public interface ErrorCode {
	default String getCode() {
		return ((Enum<?>) this).name();
	}

	ErrorCodeMeta getMeta();

	default ErrorCodeMeta meta() {
		return getMeta();
	}

	default String getDescription() {
		return getMeta().description();
	}

	default ErrorCategory getCategory() {
		return getMeta().category();
	}

	default ErrorSeverity getSeverity() {
		return getMeta().severity();
	}

	default boolean isRetryable() {
		return getMeta().retryable();
	}
}
