package br.com.fast.workshoptracker.application.port.output;

public interface PasswordEncoderPort {
	String encode(String rawPassword);

	boolean matches(String rawPassword, String encodedPassword);
}

