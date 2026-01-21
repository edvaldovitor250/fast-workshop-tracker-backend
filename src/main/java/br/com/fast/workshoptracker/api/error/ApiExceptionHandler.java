package br.com.fast.workshoptracker.api.error;

import br.com.fast.workshoptracker.api.dto.response.ErrorResponse;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.codes.AuthErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.BusinessErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.ErrorCategory;
import br.com.fast.workshoptracker.domain.exception.codes.TechnicalErrorCode;
import br.com.fast.workshoptracker.domain.exception.codes.ValidationErrorCode;
import br.com.fast.workshoptracker.domain.exception.core.BaseExpeditiException;
import br.com.fast.workshoptracker.domain.exception.domain.BusinessException;
import br.com.fast.workshoptracker.domain.exception.technical.TechnicalException;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import br.com.fast.workshoptracker.domain.exception.validation.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ApiExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler(BaseExpeditiException.class)
	public ResponseEntity<ErrorResponse> handleBase(BaseExpeditiException ex, HttpServletRequest request) {
		HttpStatus status = toHttpStatus(ex);
		logException(ex, status, request);
		return ResponseEntity.status(status).body(toResponse(ex, status, request.getRequestURI()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(ApiExceptionHandler::toFieldError)
				.toList();

		String message = "Erro de validação";
		if (!errors.isEmpty()) {
			Object field = errors.getFirst().get("field");
			Object errorMessage = errors.getFirst().get("message");
			if (field != null && errorMessage != null) {
				message = "Erro de validação: " + field + " - " + errorMessage;
			}
		}

		ValidationException ve = Exceptions.validation(
				ValidationErrorCode.VAL_001_VALIDATION_ERROR,
				message,
				ex,
				ExceptionUtils.context("errors", errors)
		);
		return handleBase(ve, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
		Map<String, Object> violation = ex.getConstraintViolations().stream()
				.findFirst()
				.map(v -> ExceptionUtils.context(
						"path", Objects.toString(v.getPropertyPath(), null),
						"message", v.getMessage(),
						"invalidValue", v.getInvalidValue()
				))
				.orElse(Map.of());

		String message = "Erro de validação";
		if (!violation.isEmpty()) {
			message = "Erro de validação: " + violation.get("path") + " - " + violation.get("message");
		}

		ValidationException ve = Exceptions.validation(
				ValidationErrorCode.VAL_001_VALIDATION_ERROR,
				message,
				ex,
				violation.isEmpty() ? Map.of() : ExceptionUtils.context("violation", violation)
		);

		return handleBase(ve, request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		ValidationException ve = Exceptions.validation(
				ValidationErrorCode.VAL_004_TYPE_MISMATCH,
				"Parâmetro inválido: " + ex.getName(),
				ex,
				ExceptionUtils.context(
						"param", ex.getName(),
						"value", ex.getValue(),
						"expectedType", ex.getRequiredType() == null ? null : ex.getRequiredType().getSimpleName()
				)
		);
		return handleBase(ve, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
		ValidationException ve = Exceptions.validation(
				ValidationErrorCode.VAL_003_INVALID_JSON,
				"JSON inválido ou formato de data inválido",
				ex
		);
		return handleBase(ve, request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
		BusinessException be = Exceptions.dataIntegrity("Conflito de dados", ex);
		return handleBase(be, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
		TechnicalException te = Exceptions.technical(TechnicalErrorCode.TECH_001_INTERNAL_ERROR, "Erro interno", ex);
		return handleBase(te, request);
	}

	private static Map<String, Object> toFieldError(FieldError fieldError) {
		Map<String, Object> map = new LinkedHashMap<>();
		if (fieldError.getField() != null) {
			map.put("field", fieldError.getField());
		}
		if (fieldError.getDefaultMessage() != null) {
			map.put("message", fieldError.getDefaultMessage());
		}
		if (fieldError.getRejectedValue() != null) {
			map.put("rejectedValue", fieldError.getRejectedValue());
		}
		return Map.copyOf(map);
	}

	private static HttpStatus toHttpStatus(BaseExpeditiException ex) {
		return switch (ex.getCategory()) {
			case VALIDATION -> HttpStatus.BAD_REQUEST;
			case SECURITY -> {
				if (ex.getErrorCode() == AuthErrorCode.AUTH_003_FORBIDDEN) {
					yield HttpStatus.FORBIDDEN;
				}
				yield HttpStatus.UNAUTHORIZED;
			}
			case BUSINESS -> {
				if (ex.getErrorCode() == BusinessErrorCode.BUS_001_NOT_FOUND) {
					yield HttpStatus.NOT_FOUND;
				}
				if (ex.getErrorCode() == BusinessErrorCode.BUS_002_CONFLICT || ex.getErrorCode() == BusinessErrorCode.BUS_003_DATA_INTEGRITY) {
					yield HttpStatus.CONFLICT;
				}
				yield HttpStatus.UNPROCESSABLE_ENTITY;
			}
			case INFRASTRUCTURE -> HttpStatus.SERVICE_UNAVAILABLE;
			case TECHNICAL -> HttpStatus.INTERNAL_SERVER_ERROR;
		};
	}

	private static void logException(BaseExpeditiException ex, HttpStatus status, HttpServletRequest request) {
		String exceptionId = ex.getExceptionId().toString();
		String errorCode = ex.getErrorCode().getCode();
		String path = request.getRequestURI();

		if (status.is5xxServerError()) {
			log.error("exceptionId={} errorCode={} status={} path={}", exceptionId, errorCode, status.value(), path, ex);
			return;
		}

		if (ex.getCategory() == ErrorCategory.VALIDATION) {
			log.debug("exceptionId={} errorCode={} status={} path={} message={}", exceptionId, errorCode, status.value(), path, ex.getMessage());
			return;
		}

		log.warn("exceptionId={} errorCode={} status={} path={} message={}", exceptionId, errorCode, status.value(), path, ex.getMessage());
	}

	private static ErrorResponse toResponse(BaseExpeditiException ex, HttpStatus status, String path) {
		return new ErrorResponse(
				ex.getExceptionId().toString(),
				ex.getTimestamp(),
				status.value(),
				ex.getErrorCode().getCode(),
				ex.getMessage(),
				ex.getCategory().name(),
				ex.getSeverity().name(),
				ex.isRetryable(),
				path,
				ex.getContext()
		);
	}
}

