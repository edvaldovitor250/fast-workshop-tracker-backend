package br.com.fast.workshoptracker.mapper;

import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.dto.response.WorkshopResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkshopMapper {

	public Workshop toEntity(WorkshopCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new Workshop(request.nome(), request.dataRealizacao(), request.descricao());
	}

	public WorkshopResponse toResponse(Workshop entity) {
		if (entity == null) {
			return null;
		}
		return new WorkshopResponse(entity.getId(), entity.getNome(), entity.getDataRealizacao(), entity.getDescricao());
	}
}

