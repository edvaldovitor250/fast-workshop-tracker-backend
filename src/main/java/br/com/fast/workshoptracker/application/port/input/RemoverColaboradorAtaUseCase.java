package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.RemoverColaboradorAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;

public interface RemoverColaboradorAtaUseCase {
	AtaDTO execute(RemoverColaboradorAtaCommand command);
}
