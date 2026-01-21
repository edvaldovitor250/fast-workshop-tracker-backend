package br.com.fast.workshoptracker.domain.exception.core;

import java.util.Map;

public abstract class BaseDomainException extends BaseExpeditiException {

	protected BaseDomainException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	protected BaseDomainException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

	protected BaseDomainException(ErrorCode errorCode, String message, Throwable cause, Map<String, Object> context) {
		super(errorCode, message, cause, context);
	}
}

