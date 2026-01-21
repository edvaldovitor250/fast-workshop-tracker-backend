package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.command.RegistrarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.query.UsuarioDTO;

public interface RegistrarUsuarioUseCase {
	UsuarioDTO execute(RegistrarUsuarioCommand command);
}

