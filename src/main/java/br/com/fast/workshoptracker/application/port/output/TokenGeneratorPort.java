package br.com.fast.workshoptracker.application.port.output;

import br.com.fast.workshoptracker.application.dto.query.AuthTokenDTO;

import java.util.List;

public interface TokenGeneratorPort {
	AuthTokenDTO generate(String subject, List<String> roles);
}

