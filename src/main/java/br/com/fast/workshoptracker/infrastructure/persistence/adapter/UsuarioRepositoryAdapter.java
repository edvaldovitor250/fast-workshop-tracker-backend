package br.com.fast.workshoptracker.infrastructure.persistence.adapter;

import br.com.fast.workshoptracker.application.port.output.UsuarioRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

	private final UsuarioRepository usuarioRepository;

	@Override
	public Optional<Usuario> findByEmailIgnoreCase(String email) {
		return usuarioRepository.findByEmailIgnoreCase(email);
	}

	@Override
	public boolean existsByEmailIgnoreCase(String email) {
		return usuarioRepository.existsByEmailIgnoreCase(email);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}
}

