package br.com.fast.workshoptracker.infrastructure.security;

public final class SecurityConstants {

	public static final String[] PUBLIC_ENDPOINTS = {
			"/swagger-ui.html",
			"/swagger-ui/**",
			"/v3/api-docs/**",
			"/api/auth/register",
			"/api/auth/login"
	};

	private SecurityConstants() {
		throw new UnsupportedOperationException("Utility class");
	}
}
