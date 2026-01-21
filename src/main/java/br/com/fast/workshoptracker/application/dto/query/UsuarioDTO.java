package br.com.fast.workshoptracker.application.dto.query;

import java.util.List;

public record UsuarioDTO(
		Long id,
		String nome,
		String email,
		List<String> roles
) {
}

