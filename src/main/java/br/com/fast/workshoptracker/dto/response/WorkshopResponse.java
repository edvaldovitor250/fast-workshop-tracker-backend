package br.com.fast.workshoptracker.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record WorkshopResponse(
		Long id,
		String nome,
		@Schema(example = "2026-01-20")
		LocalDate dataRealizacao,
		String descricao
) {
}

