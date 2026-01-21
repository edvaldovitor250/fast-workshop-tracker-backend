package br.com.fast.workshoptracker.application.dto.query;

import java.util.List;

public record ColaboradorParticipacoesDTO(
		Long colaboradorId,
		String nome,
		List<WorkshopDTO> workshops
) {
}

