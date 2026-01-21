package br.com.fast.workshoptracker.application.dto.command;

public record AdicionarColaboradorAtaCommand(
		Long workshopId,
		Long ataId,
		Long colaboradorId
) {
}

