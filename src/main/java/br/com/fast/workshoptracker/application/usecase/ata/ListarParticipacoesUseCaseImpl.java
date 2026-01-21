package br.com.fast.workshoptracker.application.usecase.ata;

import br.com.fast.workshoptracker.application.dto.query.ColaboradorParticipacoesDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorWorkshopRowDTO;
import br.com.fast.workshoptracker.application.dto.query.ListarParticipacoesQuery;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.application.port.input.ListarParticipacoesUseCase;
import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.infrastructure.util.StringNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ListarParticipacoesUseCaseImpl implements ListarParticipacoesUseCase {

	private final AtaRepositoryPort ataRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ColaboradorParticipacoesDTO> execute(ListarParticipacoesQuery query) {
		String nome = StringNormalizer.normalizeBlankToNull(query.workshopNome());

		List<ColaboradorWorkshopRowDTO> rows = ataRepository.findParticipacoes(nome, query.dataRealizacao());

		Map<Long, ColaboradorParticipacoesAccumulator> map = new LinkedHashMap<>();
		for (ColaboradorWorkshopRowDTO row : rows) {
			ColaboradorParticipacoesAccumulator acc = map.computeIfAbsent(
					row.colaboradorId(),
					id -> new ColaboradorParticipacoesAccumulator(row.colaboradorId(), row.colaboradorNome())
			);
			acc.addWorkshop(new WorkshopDTO(
					row.workshopId(),
					row.workshopNome(),
					row.workshopDataRealizacao(),
					row.workshopDescricao()
			));
		}

		return map.values().stream().map(ColaboradorParticipacoesAccumulator::toDto).toList();
	}

	private static final class ColaboradorParticipacoesAccumulator {
		private final Long colaboradorId;
		private final String nome;
		private final List<WorkshopDTO> workshops = new ArrayList<>();
		private final Set<Long> workshopIds = new HashSet<>();

		private ColaboradorParticipacoesAccumulator(Long colaboradorId, String nome) {
			this.colaboradorId = colaboradorId;
			this.nome = nome;
		}

		private void addWorkshop(WorkshopDTO workshop) {
			if (workshop == null || workshop.id() == null) {
				return;
			}
			if (workshopIds.add(workshop.id())) {
				workshops.add(workshop);
			}
		}

		private ColaboradorParticipacoesDTO toDto() {
			return new ColaboradorParticipacoesDTO(colaboradorId, nome, workshops);
		}
	}
}

