package br.com.fast.workshoptracker.dto.response;

import java.util.List;

public record ColaboradorParticipacoesResponse(
		Long colaboradorId,
		String nome,
		List<WorkshopResponse> workshops
) {
}

