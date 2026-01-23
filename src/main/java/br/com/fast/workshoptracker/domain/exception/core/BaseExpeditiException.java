package br.com.fast.workshoptracker.domain.exception.core;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import br.com.fast.workshoptracker.domain.exception.codes.ErrorCategory;
import br.com.fast.workshoptracker.domain.exception.codes.ErrorSeverity;

@Getter
public abstract class BaseExpeditiException extends RuntimeException {

	private final UUID exceptionId;
	private final OffsetDateTime timestamp;
	private final ErrorCode errorCode;
	private final Map<String, Object> context;
	private final String sourceClass;
	private final String sourceMethod;
	private final int sourceLine;

	protected BaseExpeditiException(ErrorCode errorCode, String message) {
		this(errorCode, message, null, Map.of());
	}

	protected BaseExpeditiException(ErrorCode errorCode, String message, Throwable cause) {
		this(errorCode, message, cause, Map.of());
	}

	protected BaseExpeditiException(ErrorCode errorCode, String message, Throwable cause, Map<String, Object> context) {
		super(message, cause);
		if (errorCode == null) {
			throw new IllegalArgumentException("errorCode must not be null");
		}
		this.exceptionId = UUID.randomUUID();
		this.timestamp = OffsetDateTime.now();
		this.errorCode = errorCode;
		this.context = context == null ? Map.of() : Map.copyOf(context);

		SourceLocation source = SourceLocation.capture();
		this.sourceClass = source.sourceClass();
		this.sourceMethod = source.sourceMethod();
		this.sourceLine = source.sourceLine();
	}

	public ErrorCategory getCategory() {
		return errorCode.getCategory();
	}

	public ErrorSeverity getSeverity() {
		return errorCode.getSeverity();
	}

	public boolean isRetryable() {
		return errorCode.isRetryable();
	}

	private record SourceLocation(String sourceClass, String sourceMethod, int sourceLine) {
		private static SourceLocation capture() {
			return StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
					.walk(frames -> frames
							.filter(frame -> {
								String className = frame.getClassName();
								return className != null
										&& !className.startsWith("br.com.fast.workshoptracker.domain.exception.");
							})
							.findFirst()
							.map(frame -> new SourceLocation(frame.getClassName(), frame.getMethodName(), frame.getLineNumber()))
							.orElse(new SourceLocation(null, null, -1))
					);
		}
	}
}
