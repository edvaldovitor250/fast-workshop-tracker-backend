package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.api.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkshopMapper {

	public Workshop toEntity(WorkshopCreateRequest request) {
		if (request == null) {
			return null;
		}
		Workshop workshop = new Workshop(request.nome(), request.dataRealizacao());
		workshop.setDescricao(request.descricao());
		return workshop;
	}

	public WorkshopResponse toResponse(Workshop entity) {
		if (entity == null) {
			return null;
		}
		return new WorkshopResponse(entity.getId(), entity.getNome(), entity.getDataRealizacao(), entity.getDescricao());
	}
}

