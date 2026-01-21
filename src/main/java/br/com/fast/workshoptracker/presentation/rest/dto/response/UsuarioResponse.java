package br.com.fast.workshoptracker.presentation.rest.dto.response;

import br.com.fast.workshoptracker.presentation.rest.validation.auth.jwt.JwtRoles;
import br.com.fast.workshoptracker.presentation.rest.validation.usuario.EmailAddress;
import br.com.fast.workshoptracker.presentation.rest.validation.usuario.RoleName;
import br.com.fast.workshoptracker.presentation.rest.validation.usuario.UsuarioId;
import br.com.fast.workshoptracker.presentation.rest.validation.usuario.UsuarioNome;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UsuarioResponse(
		@UsuarioId
		@Schema(example = "1")
		Long id,

		@UsuarioNome
		@Schema(example = "Ana Silva")
		String nome,

		@EmailAddress
		@Schema(example = "ana@fast.com")
		String email,

		@JwtRoles
		@Schema(example = "[\"CREATOR\",\"READER\"]")
		List<@RoleName String> roles
) {
}

