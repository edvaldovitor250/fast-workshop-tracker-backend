package br.com.fast.workshoptracker.dto.response;

import java.util.List;

public record UsuarioResponse(
		Long id,
		String nome,
		String email,
		List<String> roles
) {
}

