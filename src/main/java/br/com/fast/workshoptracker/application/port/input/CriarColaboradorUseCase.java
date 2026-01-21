package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.CriarColaboradorCommand;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;

public interface CriarColaboradorUseCase {
	ColaboradorDTO execute(CriarColaboradorCommand command);
}

