package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;

public interface CriarAtaUseCase {
	AtaDTO execute(CriarAtaCommand command);
}

