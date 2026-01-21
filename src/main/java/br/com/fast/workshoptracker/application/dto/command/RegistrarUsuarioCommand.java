package br.com.fast.workshoptracker.application.dto.command;

public record RegistrarUsuarioCommand(
		String nome,
		String email,
		String senha
) {
}

