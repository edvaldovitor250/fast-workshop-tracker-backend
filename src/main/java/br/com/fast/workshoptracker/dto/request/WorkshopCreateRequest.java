package br.com.fast.workshoptracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record WorkshopCreateRequest(
		@Schema(example = "Workshop Spring")
		@NotBlank
		@Size(min = 2, max = 150)
		String nome,

		@Schema(example = "2026-01-20")
		@NotNull
		LocalDate dataRealizacao,

		@Schema(example = "Conteúdo do workshop...")
		@Size(max = 500)
		String descricao
) {
}

