package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.RemoverColaboradorAtaCommand;

public interface RemoverColaboradorAtaUseCase {
	void execute(RemoverColaboradorAtaCommand command);
}

