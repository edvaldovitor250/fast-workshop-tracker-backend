package br.com.fast.workshoptracker.application.dto.command;

public record RemoverColaboradorAtaCommand(
		Long ataId,
		Long colaboradorId
) {
}

