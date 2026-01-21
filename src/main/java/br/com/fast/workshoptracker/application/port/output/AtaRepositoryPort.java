package br.com.fast.workshoptracker.application.port.output;

import br.com.fast.workshoptracker.application.dto.query.ColaboradorWorkshopRowDTO;
import br.com.fast.workshoptracker.domain.entity.Ata;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AtaRepositoryPort {
	boolean existsByWorkshopId(Long workshopId);

	Optional<Ata> findById(Long id);

	Optional<Ata> findByIdAndWorkshopId(Long id, Long workshopId);

	List<ColaboradorWorkshopRowDTO> findParticipacoes(String workshopNome, LocalDate dataRealizacao);

	Ata save(Ata ata);
}
