package br.com.fast.workshoptracker.application.usecase.auth;

import br.com.fast.workshoptracker.application.dto.command.AutenticarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.query.AuthTokenDTO;
import br.com.fast.workshoptracker.application.port.input.AutenticarUsuarioUseCase;
import br.com.fast.workshoptracker.application.port.output.PasswordEncoderPort;
import br.com.fast.workshoptracker.application.port.output.TokenGeneratorPort;
import br.com.fast.workshoptracker.application.port.output.UsuarioRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.infrastructure.util.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutenticarUsuarioUseCaseImpl implements AutenticarUsuarioUseCase {

	private final PasswordEncoderPort passwordEncoder;
	private final TokenGeneratorPort tokenGenerator;
	private final UsuarioRepositoryPort usuarioRepository;

	@Override
	@Transactional(readOnly = true)
	public AuthTokenDTO execute(AutenticarUsuarioCommand command) {
		String email = StringNormalizer.normalizeEmail(command.email());
		Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> Exceptions.invalidCredentials(email));

		if (!passwordEncoder.matches(command.senha(), usuario.getSenhaHash())) {
			throw Exceptions.invalidCredentials(email);
		}

		List<String> roles = usuario.getRoles().stream().map(Enum::name).sorted().toList();
		return tokenGenerator.generate(usuario.getEmail(), roles);
	}
}

