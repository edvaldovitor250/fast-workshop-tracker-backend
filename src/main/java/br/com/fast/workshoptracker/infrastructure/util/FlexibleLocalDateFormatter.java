package br.com.fast.workshoptracker.infrastructure.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class FlexibleLocalDateFormatter implements Formatter<LocalDate> {

	private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE;
	private static final DateTimeFormatter LEGACY_BR = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	@Override
	public LocalDate parse(String text, Locale locale) throws ParseException {
		if (text == null) {
			return null;
		}
		String value = text.trim();
		if (value.isEmpty()) {
			return null;
		}

		try {
			return LocalDate.parse(value, LEGACY_BR);
		} catch (DateTimeParseException ignored) {
			// fall through
		}

		try {
			return LocalDate.parse(value, ISO);
		} catch (DateTimeParseException ex) {
			throw new ParseException("Data invalida: '" + text + "'. Use dd/MM/yyyy (ou yyyy-MM-dd por compatibilidade).", 0);
		}
	}

	@Override
	public String print(LocalDate object, Locale locale) {
		return object == null ? "" : LEGACY_BR.format(object);
	}
}
