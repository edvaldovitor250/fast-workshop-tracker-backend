package br.com.fast.workshoptracker.domain.exception.security;

import br.com.fast.workshoptracker.domain.exception.codes.AuthErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.BaseDomainException;

import java.util.Map;

public class AuthException extends BaseDomainException {

	public AuthException(AuthErrorCode errorCode, String message, Throwable cause, Map<String, Object> context) {
		super(errorCode, message, cause, context);
	}
}

