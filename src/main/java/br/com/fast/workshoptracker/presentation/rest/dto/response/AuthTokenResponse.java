package br.com.fast.workshoptracker.presentation.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import br.com.fast.workshoptracker.presentation.rest.validation.auth.jwt.JwtAccessToken;
import br.com.fast.workshoptracker.presentation.rest.validation.auth.jwt.JwtExpiresAt;
import br.com.fast.workshoptracker.presentation.rest.validation.auth.jwt.JwtRoles;
import br.com.fast.workshoptracker.presentation.rest.validation.auth.jwt.JwtTokenType;
import br.com.fast.workshoptracker.presentation.rest.validation.usuario.RoleName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record AuthTokenResponse(
		@JwtTokenType
		@Schema(example = "Bearer")
		String tokenType,

		@JwtAccessToken
		@Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3b3Jrc2hvcC10cmFja2VyIiwic3ViIjoiYW5hQGZhc3QuY29tIiwiZXhwIjoxNzM3NDAwMDAwfQ.XXXX")
		String accessToken,

		@JwtExpiresAt
		@JsonFormat(pattern = "dd/MM/yyyy HH:mm:ssXXX", timezone = "America/Sao_Paulo")
		@Schema(example = "20/01/2026 18:30:00-03:00")
		Instant expiresAt,

		@JwtRoles
		@Schema(example = "[\"CREATOR\",\"READER\"]")
		List<@RoleName String> roles
) {
}

