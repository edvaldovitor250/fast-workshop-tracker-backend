package br.com.fast.workshoptracker.application.dto.query;

import java.time.LocalDate;

public record WorkshopDTO(
		Long id,
		String nome,
		LocalDate dataRealizacao,
		String descricao
) {
}

