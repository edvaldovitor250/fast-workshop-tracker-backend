package br.com.fast.workshoptracker.testconfig;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.application.mapper.AtaApplicationMapper;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;

@Component
@Primary
@Profile("test")
public class AtaApplicationMapperTestImpl implements AtaApplicationMapper {

	@Override
	public AtaDTO toDto(Ata entity) {
		if (entity == null) {
			return null;
		}
		return new AtaDTO(
				entity.getId(),
				toDto(entity.getWorkshop()),
				toColaboradorDtoList(entity.getColaboradores())
		);
	}

	@Override
	public WorkshopDTO toDto(Workshop entity) {
		if (entity == null) {
			return null;
		}
		return new WorkshopDTO(
				entity.getId(),
				entity.getNome(),
				entity.getDataRealizacao(),
				entity.getDescricao()
		);
	}

	@Override
	public ColaboradorDTO toDto(Colaborador entity) {
		if (entity == null) {
			return null;
		}
		return new ColaboradorDTO(entity.getId(), entity.getNome());
	}
}
