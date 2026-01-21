package br.com.fast.workshoptracker.infrastructure.persistence.adapter;

import br.com.fast.workshoptracker.application.port.output.WorkshopRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkshopRepositoryAdapter implements WorkshopRepositoryPort {

	private final WorkshopRepository workshopRepository;

	@Override
	public Optional<Workshop> findById(Long id) {
		return workshopRepository.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return workshopRepository.existsById(id);
	}

	@Override
	public Workshop save(Workshop workshop) {
		return workshopRepository.save(workshop);
	}
}
