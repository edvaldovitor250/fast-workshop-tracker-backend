package br.com.fast.workshoptracker.application.usecase.ata;

import br.com.fast.workshoptracker.application.dto.command.RemoverColaboradorAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.mapper.AtaApplicationMapper;
import br.com.fast.workshoptracker.application.port.input.RemoverColaboradorAtaUseCase;
import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RemoverColaboradorAtaUseCaseImpl implements RemoverColaboradorAtaUseCase {

	private final AtaRepositoryPort ataRepository;
	private final ColaboradorRepositoryPort colaboradorRepository;
	private final AtaApplicationMapper ataMapper;

	@Override
	@Transactional
	public AtaDTO execute(RemoverColaboradorAtaCommand command) {
		Ata ata = ataRepository.findById(command.ataId())
				.orElseThrow(() -> Exceptions.notFound(
						"Ata não encontrada: id=" + command.ataId(),
						ExceptionUtils.context("ataId", command.ataId())
				));

		if (!colaboradorRepository.existsById(command.colaboradorId())) {
			throw Exceptions.notFound(
					"Colaborador não encontrado: id=" + command.colaboradorId(),
					ExceptionUtils.context("colaboradorId", command.colaboradorId())
			);
		}

		boolean removed = ata.getColaboradores().removeIf(c -> Objects.equals(c.getId(), command.colaboradorId()));
		if (!removed) {
			throw Exceptions.notFound(
					"Colaborador não está presente na ata: ataId=" + command.ataId() + ", colaboradorId=" + command.colaboradorId(),
					ExceptionUtils.context("ataId", command.ataId(), "colaboradorId", command.colaboradorId())
			);
		}

		return ataMapper.toDto(ata);
	}
}
