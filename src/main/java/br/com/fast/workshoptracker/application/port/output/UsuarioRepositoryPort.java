package br.com.fast.workshoptracker.application.port.output;

import br.com.fast.workshoptracker.domain.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepositoryPort {
	Optional<Usuario> findByEmailIgnoreCase(String email);

	boolean existsByEmailIgnoreCase(String email);

	Usuario save(Usuario usuario);
}

