package br.com.fast.workshoptracker.infrastructure.util;

import java.util.Locale;

/**
 * Utilitário para normalização de strings.
 */
public final class StringNormalizer {

	private StringNormalizer() {
		throw new UnsupportedOperationException("Utility class");
	}


	public static String normalizeEmail(String email) {
		if (email == null) {
			return null;
		}
		return email.trim().toLowerCase(Locale.ROOT);
	}

	
	public static String normalizeBlankToNull(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.isEmpty() ? null : trimmed;
	}

	
	public static String normalize(String value) {
		return value == null ? null : value.trim();
	}
}
