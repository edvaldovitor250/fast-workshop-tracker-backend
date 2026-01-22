package br.com.fast.workshoptracker.application.port.output;

import br.com.fast.workshoptracker.domain.entity.Workshop;

import java.util.Optional;

public interface WorkshopRepositoryPort {
	Optional<Workshop> findById(Long id);

	boolean existsById(Long id);

	Workshop save(Workshop workshop);

	long count();
}
