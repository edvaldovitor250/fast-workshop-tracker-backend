package br.com.fast.workshoptracker.dto.response;

import java.util.List;

public record AtaResponse(
		Long id,
		WorkshopResponse workshop,
		List<ColaboradorResponse> colaboradores
) {
}

