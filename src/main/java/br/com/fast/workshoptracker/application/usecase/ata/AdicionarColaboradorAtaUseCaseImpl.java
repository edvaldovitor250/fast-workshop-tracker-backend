package br.com.fast.workshoptracker.application.usecase.ata;

import br.com.fast.workshoptracker.application.dto.command.AdicionarColaboradorAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.mapper.AtaApplicationMapper;
import br.com.fast.workshoptracker.application.port.input.AdicionarColaboradorAtaUseCase;
import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.WorkshopRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdicionarColaboradorAtaUseCaseImpl implements AdicionarColaboradorAtaUseCase {

	private final AtaRepositoryPort ataRepository;
	private final WorkshopRepositoryPort workshopRepository;
	private final ColaboradorRepositoryPort colaboradorRepository;
	private final AtaApplicationMapper ataMapper;

	@Override
	@Transactional
	public AtaDTO execute(AdicionarColaboradorAtaCommand command) {
		Ata ata = ataRepository.findByIdAndWorkshopId(command.ataId(), command.workshopId()).orElse(null);
		if (ata == null) {
			if (!workshopRepository.existsById(command.workshopId())) {
				throw Exceptions.notFound(
						"Workshop não encontrado: id=" + command.workshopId(),
						ExceptionUtils.context("workshopId", command.workshopId())
				);
			}
			throw Exceptions.notFound(
					"Ata não encontrada para o workshop: ataId=" + command.ataId() + ", workshopId=" + command.workshopId(),
					ExceptionUtils.context("ataId", command.ataId(), "workshopId", command.workshopId())
			);
		}

		Colaborador colaborador = colaboradorRepository.findById(command.colaboradorId())
				.orElseThrow(() -> Exceptions.notFound(
						"Colaborador não encontrado: id=" + command.colaboradorId(),
						ExceptionUtils.context("colaboradorId", command.colaboradorId())
				));

		boolean added = ata.getColaboradores().add(colaborador);
		if (!added) {
			throw Exceptions.conflict(
					"Colaborador já está presente na ata: ataId=" + command.ataId() + ", colaboradorId=" + colaborador.getId(),
					ExceptionUtils.context("ataId", command.ataId(), "colaboradorId", colaborador.getId())
			);
		}

		return ataMapper.toDto(ata);
	}
}

