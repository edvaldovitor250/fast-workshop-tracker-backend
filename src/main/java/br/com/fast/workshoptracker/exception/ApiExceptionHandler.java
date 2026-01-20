package br.com.fast.workshoptracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
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
	public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest request) {
		return problem(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
	}

	@ExceptionHandler(ConflictException.class)
	public ProblemDetail handleConflict(ConflictException ex, HttpServletRequest request) {
		return problem(HttpStatus.CONFLICT, ex.getMessage(), request, null);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ProblemDetail handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
		return problem(HttpStatus.UNAUTHORIZED, ex.getMessage(), request, null);
	}

	@ExceptionHandler(BadRequestException.class)
	public ProblemDetail handleBadRequest(BadRequestException ex, HttpServletRequest request) {
		return problem(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(ApiExceptionHandler::toFieldError)
				.toList();
		return problem(HttpStatus.BAD_REQUEST, "Erro de validação", request, Map.of("errors", errors));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
		return problem(HttpStatus.BAD_REQUEST, "Erro de validação", request, Map.of("errors", ex.getConstraintViolations().stream()
				.map(v -> Map.of(
						"path", v.getPropertyPath().toString(),
						"message", v.getMessage()
				))
				.toList()));
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
		String detail = "Parâmetro inválido: " + ex.getName();
		return problem(HttpStatus.BAD_REQUEST, detail, request, null);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
		return problem(HttpStatus.BAD_REQUEST, "JSON inválido ou formato de data inválido", request, null);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ProblemDetail handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
		return problem(HttpStatus.CONFLICT, "Conflito de dados", request, null);
	}

	@ExceptionHandler(ErrorResponseException.class)
	public ProblemDetail handleErrorResponseException(ErrorResponseException ex, HttpServletRequest request) {
		ProblemDetail pd = ex.getBody();
		if (pd.getInstance() == null) {
			pd.setInstance(java.net.URI.create(request.getRequestURI()));
		}
		pd.setProperty("timestamp", OffsetDateTime.now());
		return pd;
	}

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
		return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno", request, null);
	}

	private static Map<String, Object> toFieldError(FieldError fieldError) {
		return Map.of(
				"field", fieldError.getField(),
				"message", fieldError.getDefaultMessage()
		);
	}

	private static ProblemDetail problem(HttpStatus status, String detail, HttpServletRequest request, Map<String, Object> props) {
		ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
		pd.setTitle(status.getReasonPhrase());
		pd.setInstance(java.net.URI.create(request.getRequestURI()));
		pd.setProperty("timestamp", OffsetDateTime.now());
		if (props != null) {
			props.forEach(pd::setProperty);
		}
		return pd;
	}
}
