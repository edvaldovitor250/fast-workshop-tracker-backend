package br.com.fast.workshoptracker.testconfig;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.fast.workshoptracker.application.dto.command.CriarWorkshopCommand;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.presentation.rest.mapper.WorkshopMapper;

@Component
@Primary
@Profile("test")
public class WorkshopMapperTestImpl implements WorkshopMapper {

	@Override
	public CriarWorkshopCommand toCommand(WorkshopCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new CriarWorkshopCommand(request.nome(), request.dataRealizacao(), request.descricao());
	}

	@Override
	public WorkshopResponse toResponse(WorkshopDTO dto) {
		if (dto == null) {
			return null;
		}
		return new WorkshopResponse(dto.id(), dto.nome(), dto.dataRealizacao(), dto.descricao());
	}
}
