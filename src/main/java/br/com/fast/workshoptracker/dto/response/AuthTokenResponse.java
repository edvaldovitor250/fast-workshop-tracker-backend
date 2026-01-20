package br.com.fast.workshoptracker.dto.response;

import java.time.Instant;
import java.util.List;

public record AuthTokenResponse(
		String tokenType,
		String accessToken,
		Instant expiresAt,
		List<String> roles
) {
}

