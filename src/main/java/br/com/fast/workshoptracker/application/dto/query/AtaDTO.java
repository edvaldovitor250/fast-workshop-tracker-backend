package br.com.fast.workshoptracker.application.dto.query;

import java.util.List;

public record AtaDTO(
		Long id,
		WorkshopDTO workshop,
		List<ColaboradorDTO> colaboradores
) {
}

