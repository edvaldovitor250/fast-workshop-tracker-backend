package br.com.fast.workshoptracker.infrastructure.persistence.adapter;

import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorWorkshopRowDTO;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.infrastructure.persistence.projection.ColaboradorWorkshopRow;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.AtaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AtaRepositoryAdapter implements AtaRepositoryPort {

	private final AtaRepository ataRepository;

	@Override
	public boolean existsByWorkshopId(Long workshopId) {
		return ataRepository.existsByWorkshopId(workshopId);
	}

	@Override
	public Optional<Ata> findById(Long id) {
		return ataRepository.findById(id);
	}

	@Override
	public Optional<Ata> findByIdAndWorkshopId(Long id, Long workshopId) {
		return ataRepository.findByIdAndWorkshopId(id, workshopId);
	}

	@Override
	public List<ColaboradorWorkshopRowDTO> findParticipacoes(String workshopNome, LocalDate dataRealizacao) {
		List<ColaboradorWorkshopRow> rows = ataRepository.findParticipacoes(workshopNome, dataRealizacao, Pageable.unpaged()).getContent();
		return rows.stream().map(r -> new ColaboradorWorkshopRowDTO(
				r.getColaboradorId(),
				r.getColaboradorNome(),
				r.getWorkshopId(),
				r.getWorkshopNome(),
				r.getWorkshopDataRealizacao(),
				r.getWorkshopDescricao()
		)).toList();
	}

	@Override
	public Ata save(Ata ata) {
		return ataRepository.save(ata);
	}

	@Override
	public long count() {
		return ataRepository.count();
	}
}
