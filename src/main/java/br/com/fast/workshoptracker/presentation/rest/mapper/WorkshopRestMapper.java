package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.CriarWorkshopCommand;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import org.springframework.stereotype.Component;

@Component
public class WorkshopRestMapper {

	public CriarWorkshopCommand toCommand(WorkshopCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new CriarWorkshopCommand(request.nome(), request.dataRealizacao(), request.descricao());
	}

	public WorkshopResponse toResponse(WorkshopDTO dto) {
		if (dto == null) {
			return null;
		}
		return new WorkshopResponse(dto.id(), dto.nome(), dto.dataRealizacao(), dto.descricao());
	}
}

