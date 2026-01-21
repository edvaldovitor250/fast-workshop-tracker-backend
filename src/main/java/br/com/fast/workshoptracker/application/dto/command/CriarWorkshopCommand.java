package br.com.fast.workshoptracker.application.dto.command;

import java.time.LocalDate;

public record CriarWorkshopCommand(
		String nome,
		LocalDate dataRealizacao,
		String descricao
) {
}

