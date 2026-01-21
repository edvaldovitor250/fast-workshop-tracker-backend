package br.com.fast.workshoptracker.presentation.rest.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErrorResponse(
		@Schema(example = "9d6e08ae-9d72-4e59-8f0a-0c49f3d5b5f2")
		String exceptionId,

		@Schema(example = "2026-01-20T21:55:35.099-03:00")
		OffsetDateTime timestamp,

		@Schema(example = "400")
		int status,

		@Schema(example = "VAL_001_VALIDATION_ERROR")
		String errorCode,

		@Schema(example = "Erro de validacao")
		String message,

		@Schema(example = "VALIDATION")
		String category,

		@Schema(example = "LOW")
		String severity,

		@Schema(example = "false")
		boolean retryable,

		@Schema(example = "/api/workshops")
		String path,

		Map<String, Object> context
) {
}

