package br.com.fast.workshoptracker.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

public record ErrorResponse(
		@Schema(example = "2026-01-20T21:55:35.099-03:00")
		OffsetDateTime timestamp,

		@Schema(example = "400")
		int status,

		@Schema(example = "VALIDATION_ERROR")
		String errorCode,

		@Schema(example = "Erro de validação")
		String message,

		@Schema(example = "/api/workshops")
		String path
) {
}

