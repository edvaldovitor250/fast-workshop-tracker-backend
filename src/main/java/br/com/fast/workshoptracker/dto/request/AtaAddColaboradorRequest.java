package br.com.fast.workshoptracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AtaAddColaboradorRequest(
		@NotNull
		@Schema(example = "10")
		Long colaboradorId
) {
}

