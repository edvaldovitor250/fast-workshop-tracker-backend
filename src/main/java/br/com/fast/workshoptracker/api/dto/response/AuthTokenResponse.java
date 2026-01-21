package br.com.fast.workshoptracker.api.dto.response;

import br.com.fast.workshoptracker.api.validation.auth.jwt.JwtAccessToken;
import br.com.fast.workshoptracker.api.validation.auth.jwt.JwtExpiresAt;
import br.com.fast.workshoptracker.api.validation.auth.jwt.JwtRoles;
import br.com.fast.workshoptracker.api.validation.auth.jwt.JwtTokenType;
import br.com.fast.workshoptracker.api.validation.usuario.RoleName;
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
		@Schema(example = "2026-01-20T21:30:00Z")
		Instant expiresAt,

		@JwtRoles
		@Schema(example = "[\"CREATOR\",\"READER\"]")
		List<@RoleName String> roles
) {
}

