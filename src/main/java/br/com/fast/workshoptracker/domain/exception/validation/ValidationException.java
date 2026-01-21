package br.com.fast.workshoptracker.domain.exception.validation;

import java.util.Map;

import br.com.fast.workshoptracker.domain.exception.codes.ValidationErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.BaseDomainException;

public final class ValidationException extends BaseDomainException {

	public ValidationException(ValidationErrorCode errorCode, String message, Throwable cause, Map<String, Object> context) {
		super(errorCode, message, cause, context);
	}
}

