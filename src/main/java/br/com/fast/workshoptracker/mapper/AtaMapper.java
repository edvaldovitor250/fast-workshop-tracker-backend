package br.com.fast.workshoptracker.mapper;

import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.dto.response.AtaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AtaMapper {

	private final WorkshopMapper workshopMapper;
	private final ColaboradorMapper colaboradorMapper;

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

