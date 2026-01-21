package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.AutenticarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.query.AuthTokenDTO;

public interface AutenticarUsuarioUseCase {
	AuthTokenDTO execute(AutenticarUsuarioCommand command);
}

