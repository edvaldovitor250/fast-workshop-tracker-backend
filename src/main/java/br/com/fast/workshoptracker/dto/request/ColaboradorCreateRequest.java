package br.com.fast.workshoptracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ColaboradorCreateRequest(
		@Schema(example = "Ana Silva")
		@NotBlank
		@Size(min = 2, max = 120)
		String nome
) {
}

