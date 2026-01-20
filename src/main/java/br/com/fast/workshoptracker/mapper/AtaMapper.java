package br.com.fast.workshoptracker.mapper;

import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.dto.response.AtaResponse;
import org.springframework.stereotype.Component;

@Component
public class AtaMapper {

	private final WorkshopMapper workshopMapper;
	private final ColaboradorMapper colaboradorMapper;

	public AtaMapper(WorkshopMapper workshopMapper, ColaboradorMapper colaboradorMapper) {
		this.workshopMapper = workshopMapper;
		this.colaboradorMapper = colaboradorMapper;
	}

	public AtaResponse toResponse(Ata entity) {
		if (entity == null) {
			return null;
		}
		return new AtaResponse(
				entity.getId(),
				workshopMapper.toResponse(entity.getWorkshop()),
				colaboradorMapper.toResponseList(entity.getColaboradores())
		);
	}
}

