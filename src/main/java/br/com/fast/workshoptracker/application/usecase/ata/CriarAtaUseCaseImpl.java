package br.com.fast.workshoptracker.application.usecase.ata;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.mapper.AtaApplicationMapper;
import br.com.fast.workshoptracker.application.port.input.CriarAtaUseCase;
import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.WorkshopRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import br.com.fast.workshoptracker.infrastructure.util.CollectionValidator;
import br.com.fast.workshoptracker.infrastructure.util.EntityFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriarAtaUseCaseImpl implements CriarAtaUseCase {

	private final AtaRepositoryPort ataRepository;
	private final WorkshopRepositoryPort workshopRepository;
	private final ColaboradorRepositoryPort colaboradorRepository;
	private final AtaApplicationMapper ataMapper;

	@Override
	@Transactional
	public AtaDTO execute(CriarAtaCommand command) {
		CollectionValidator.validateUniqueIds(command.colaboradoresIds(), "colaboradoresIds");

		Workshop workshop = workshopRepository.findById(command.workshopId())
				.orElseThrow(() -> Exceptions.notFound(
						"Workshop não encontrado: id=" + command.workshopId(),
						ExceptionUtils.context("workshopId", command.workshopId())
				));

		if (ataRepository.existsByWorkshopId(workshop.getId())) {
			throw Exceptions.conflict(
					"Já existe ata para o workshop: id=" + workshop.getId(),
					ExceptionUtils.context("workshopId", workshop.getId())
			);
		}

		Ata ata = new Ata(workshop);

		List<Long> colaboradoresIds = command.colaboradoresIds();
		if (colaboradoresIds != null && !colaboradoresIds.isEmpty()) {
			List<Colaborador> colaboradores = colaboradorRepository.findAllById(colaboradoresIds);
			EntityFinder.validateAllFound(
					colaboradoresIds,
					colaboradores,
					Colaborador::getId,
					"Colaboradores",
					"colaboradoresIds"
			);
			ata.getColaboradores().addAll(colaboradores);
		}

		ata = ataRepository.save(ata);
		return ataMapper.toDto(ata);
	}
}

