package br.com.fast.workshoptracker.infrastructure.persistence.adapter;

import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ColaboradorRepositoryAdapter implements ColaboradorRepositoryPort {

	private final ColaboradorRepository colaboradorRepository;

	@Override
	public List<Colaborador> findAllById(Iterable<Long> ids) {
		return colaboradorRepository.findAllById(ids);
	}

	@Override
	public Optional<Colaborador> findById(Long id) {
		return colaboradorRepository.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		return colaboradorRepository.existsById(id);
	}

	@Override
	public Colaborador save(Colaborador colaborador) {
		return colaboradorRepository.save(colaborador);
	}
}
