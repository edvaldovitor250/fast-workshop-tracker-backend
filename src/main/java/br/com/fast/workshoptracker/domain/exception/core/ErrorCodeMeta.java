package br.com.fast.workshoptracker.domain.exception.core;

import br.com.fast.workshoptracker.domain.exception.codes.ErrorCategory;
import br.com.fast.workshoptracker.domain.exception.codes.ErrorSeverity;

public record ErrorCodeMeta(
		String description,
		ErrorCategory category,
		ErrorSeverity severity,
		boolean retryable
) {
}

