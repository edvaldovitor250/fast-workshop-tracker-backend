package br.com.fast.workshoptracker.domain.exception.technical;

import java.util.Map;

import br.com.fast.workshoptracker.domain.exception.codes.TechnicalErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.BaseDomainException;

public final class TechnicalException extends BaseDomainException {

	public TechnicalException(TechnicalErrorCode errorCode, String message, Throwable cause, Map<String, Object> context) {
		super(errorCode, message, cause, context);
	}
}

