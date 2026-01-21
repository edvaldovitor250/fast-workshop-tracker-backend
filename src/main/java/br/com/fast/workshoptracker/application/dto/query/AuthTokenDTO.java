package br.com.fast.workshoptracker.application.dto.query;

import java.time.Instant;
import java.util.List;

public record AuthTokenDTO(
		String tokenType,
		String accessToken,
		Instant expiresAt,
		List<String> roles
) {
}

