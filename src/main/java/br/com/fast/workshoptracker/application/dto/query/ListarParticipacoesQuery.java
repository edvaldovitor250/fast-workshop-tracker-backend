package br.com.fast.workshoptracker.application.dto.query;

import java.time.LocalDate;

public record ListarParticipacoesQuery(
		String workshopNome,
		LocalDate dataRealizacao
) {
}

