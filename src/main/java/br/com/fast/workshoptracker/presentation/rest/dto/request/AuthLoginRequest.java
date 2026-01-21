package br.com.fast.workshoptracker.presentation.rest.dto.request;

import br.com.fast.workshoptracker.presentation.rest.validation.usuario.EmailAddress;
import br.com.fast.workshoptracker.presentation.rest.validation.usuario.Senha;
import io.swagger.v3.oas.annotations.media.Schema;

public record AuthLoginRequest(
		@EmailAddress
		@Schema(example = "ana@fast.com")
		String email,

		@Senha
		@Schema(example = "Senha@123")
		String senha
) {
}

