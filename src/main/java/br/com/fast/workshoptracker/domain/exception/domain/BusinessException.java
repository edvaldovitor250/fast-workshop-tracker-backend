package br.com.fast.workshoptracker.domain.exception.domain;

import br.com.fast.workshoptracker.domain.exception.codes.BusinessErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.BaseDomainException;

import java.util.Map;

public final class BusinessException extends BaseDomainException {

	public BusinessException(BusinessErrorCode errorCode, String message, Throwable cause, Map<String, Object> context) {
		super(errorCode, message, cause, context);
	}
}

