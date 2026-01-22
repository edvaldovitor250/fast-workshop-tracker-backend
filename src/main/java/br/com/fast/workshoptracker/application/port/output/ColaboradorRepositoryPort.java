package br.com.fast.workshoptracker.application.port.output;

import br.com.fast.workshoptracker.domain.entity.Colaborador;

import java.util.List;
import java.util.Optional;

public interface ColaboradorRepositoryPort {
	List<Colaborador> findAllById(Iterable<Long> ids);

	Optional<Colaborador> findById(Long id);

	boolean existsById(Long id);

	Colaborador save(Colaborador colaborador);

	long count();
}
