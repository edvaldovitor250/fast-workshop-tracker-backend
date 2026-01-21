package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.AdicionarColaboradorAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;

public interface AdicionarColaboradorAtaUseCase {
	AtaDTO execute(AdicionarColaboradorAtaCommand command);
}

