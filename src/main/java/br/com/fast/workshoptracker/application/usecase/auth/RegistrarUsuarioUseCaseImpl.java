package br.com.fast.workshoptracker.application.usecase.auth;

import br.com.fast.workshoptracker.application.dto.command.RegistrarUsuarioCommand;
import br.com.fast.workshoptracker.application.dto.query.UsuarioDTO;
import br.com.fast.workshoptracker.application.port.input.RegistrarUsuarioUseCase;
import br.com.fast.workshoptracker.application.port.output.PasswordEncoderPort;
import br.com.fast.workshoptracker.application.port.output.UsuarioRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import br.com.fast.workshoptracker.domain.enums.UserRole;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import br.com.fast.workshoptracker.infrastructure.util.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

	private final PasswordEncoderPort passwordEncoder;
	private final UsuarioRepositoryPort usuarioRepository;

	@Override
	@Transactional
	public UsuarioDTO execute(RegistrarUsuarioCommand command) {
		String email = StringNormalizer.normalizeEmail(command.email());
		if (usuarioRepository.existsByEmailIgnoreCase(email)) {
			throw Exceptions.conflict(
					"E-mail já cadastrado",
					ExceptionUtils.context("email", email)
			);
		}

		String senhaHash = passwordEncoder.encode(command.senha());
		Usuario usuario = new Usuario(command.nome(), email, senhaHash);
		usuario.getRoles().addAll(Set.of(UserRole.CREATOR, UserRole.READER));
		usuario = usuarioRepository.save(usuario);

		List<String> roles = usuario.getRoles().stream().map(Enum::name).sorted().toList();
		return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(), roles);
	}
}

