package br.com.fast.workshoptracker.application.usecase.colaborador;

import br.com.fast.workshoptracker.application.dto.command.CriarColaboradorCommand;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.port.input.CriarColaboradorUseCase;
import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CriarColaboradorUseCaseImpl implements CriarColaboradorUseCase {

	private final ColaboradorRepositoryPort colaboradorRepository;

	@Override
	@Transactional
	public ColaboradorDTO execute(CriarColaboradorCommand command) {
		Colaborador colaborador = new Colaborador(command.nome());
		colaborador = colaboradorRepository.save(colaborador);
		return new ColaboradorDTO(colaborador.getId(), colaborador.getNome());
	}
}

