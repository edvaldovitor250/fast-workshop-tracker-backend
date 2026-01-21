package br.com.fast.workshoptracker.application.dto.command;

public record AutenticarUsuarioCommand(
		String email,
		String senha
) {
}

