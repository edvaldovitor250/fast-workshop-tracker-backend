package br.com.fast.workshoptracker.api.error;

import br.com.fast.workshoptracker.api.dto.response.ErrorResponse;
import br.com.fast.workshoptracker.domain.exception.BadRequestException;
import br.com.fast.workshoptracker.domain.exception.ConflictException;
import br.com.fast.workshoptracker.domain.exception.NotFoundException;
import br.com.fast.workshoptracker.domain.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex, HttpServletRequest request) {
		return error(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
		return error(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), request);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
		return error(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), request);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
		return error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(ApiExceptionHandler::toFieldError)
				.toList();

		String message = "Erro de validação";
		if (!errors.isEmpty()) {
			Map<String, Object> first = errors.getFirst();
			message = "Erro de validação: " + first.get("field") + " - " + first.get("message");
		}

		return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
		String message = ex.getConstraintViolations().stream()
				.findFirst()
				.map(v -> "Erro de validação: " + v.getPropertyPath() + " - " + v.getMessage())
				.orElse("Erro de validação");
		return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		String message = "Parâmetro inválido: " + ex.getName();
		return error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", message, request);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
		return error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "JSON inválido ou formato de data inválido", request);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
		return error(HttpStatus.CONFLICT, "CONFLICT", "Conflito de dados", request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Erro interno", request);
	}

	private static Map<String, Object> toFieldError(FieldError fieldError) {
		return Map.of(
				"field", fieldError.getField(),
				"message", fieldError.getDefaultMessage()
		);
	}

	private static ResponseEntity<ErrorResponse> error(HttpStatus status, String errorCode, String message, HttpServletRequest request) {
		ErrorResponse body = new ErrorResponse(
				OffsetDateTime.now(),
				status.value(),
				errorCode,
				message,
				request.getRequestURI()
		);
		return ResponseEntity.status(status).body(body);
	}
}

