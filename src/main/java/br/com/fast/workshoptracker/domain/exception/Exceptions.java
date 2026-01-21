package br.com.fast.workshoptracker.domain.exception;

import br.com.fast.workshoptracker.domain.exception.codes.AuthErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.BusinessErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.TechnicalErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.ValidationErrorCode;
import br.com.fast.workshoptracker.domain.exception.domain.BusinessException;
import br.com.fast.workshoptracker.domain.exception.security.AuthException;
import br.com.fast.workshoptracker.domain.exception.technical.TechnicalException;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import br.com.fast.workshoptracker.domain.exception.validation.ValidationException;

import java.util.Map;

public final class Exceptions {

	private Exceptions() {
	}

	public static BusinessException business(BusinessErrorCode code, String message) {
		return business(code, message, null, null);
	}

	public static BusinessException business(BusinessErrorCode code, String message, Throwable cause) {
		return business(code, message, cause, null);
	}

	public static BusinessException business(BusinessErrorCode code, String message, Throwable cause, Map<String, Object> context) {
		return new BusinessException(code, message, cause, context);
	}

	public static ValidationException validation(ValidationErrorCode code, String message) {
		return validation(code, message, null, null);
	}

	public static ValidationException validation(ValidationErrorCode code, String message, Throwable cause) {
		return validation(code, message, cause, null);
	}

	public static ValidationException validation(ValidationErrorCode code, String message, Throwable cause, Map<String, Object> context) {
		return new ValidationException(code, message, cause, context);
	}

	public static TechnicalException technical(TechnicalErrorCode code, String message) {
		return technical(code, message, null, null);
	}

	public static TechnicalException technical(TechnicalErrorCode code, String message, Throwable cause) {
		return technical(code, message, cause, null);
	}

	public static TechnicalException technical(TechnicalErrorCode code, String message, Throwable cause, Map<String, Object> context) {
		return new TechnicalException(code, message, cause, context);
	}

	public static AuthException auth(AuthErrorCode code, String message) {
		return auth(code, message, null, null);
	}

	public static AuthException auth(AuthErrorCode code, String message, Throwable cause) {
		return auth(code, message, cause, null);
	}

	public static AuthException auth(AuthErrorCode code, String message, Throwable cause, Map<String, Object> context) {
		return new AuthException(code, message, cause, context);
	}

	public static BusinessException notFound(String message, Map<String, Object> context) {
		return business(BusinessErrorCode.BUS_001_NOT_FOUND, message, null, context);
	}

	public static BusinessException conflict(String message, Map<String, Object> context) {
		return business(BusinessErrorCode.BUS_002_CONFLICT, message, null, context);
	}

	public static BusinessException dataIntegrity(String message, Throwable cause) {
		return business(BusinessErrorCode.BUS_003_DATA_INTEGRITY, message, cause, null);
	}

	public static ValidationException badRequest(String message, Map<String, Object> context) {
		return validation(ValidationErrorCode.VAL_002_BAD_REQUEST, message, null, context);
	}

	public static AuthException invalidCredentials(String email) {
		return auth(
				AuthErrorCode.AUTH_001_INVALID_CREDENTIALS,
				"Credenciais invalidas",
				null,
				ExceptionUtils.context("email", email)
		);
	}
}

