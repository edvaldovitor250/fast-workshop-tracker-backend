package br.com.fast.workshoptracker.application.dto.command;

import java.util.List;

public record CriarAtaCommand(
		Long workshopId,
		List<Long> colaboradoresIds
) {
}

