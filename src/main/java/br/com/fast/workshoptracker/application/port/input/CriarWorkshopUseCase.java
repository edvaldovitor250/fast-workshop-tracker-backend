package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.CriarWorkshopCommand;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;

public interface CriarWorkshopUseCase {
	WorkshopDTO execute(CriarWorkshopCommand command);
}

