package br.com.fast.workshoptracker.application.usecase.workshop;

import br.com.fast.workshoptracker.application.dto.command.CriarWorkshopCommand;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.application.port.input.CriarWorkshopUseCase;
import br.com.fast.workshoptracker.application.port.output.WorkshopRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CriarWorkshopUseCaseImpl implements CriarWorkshopUseCase {

	private final WorkshopRepositoryPort workshopRepository;

	@Override
	@Transactional
	public WorkshopDTO execute(CriarWorkshopCommand command) {
		Workshop workshop = new Workshop(command.nome(), command.dataRealizacao());
		workshop.setDescricao(command.descricao());

		workshop = workshopRepository.save(workshop);
		return new WorkshopDTO(workshop.getId(), workshop.getNome(), workshop.getDataRealizacao(), workshop.getDescricao());
	}
}

