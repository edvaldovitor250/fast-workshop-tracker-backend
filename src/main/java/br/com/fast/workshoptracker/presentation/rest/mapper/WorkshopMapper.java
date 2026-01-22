package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.CriarWorkshopCommand;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

	CriarWorkshopCommand toCommand(WorkshopCreateRequest request);

	WorkshopResponse toResponse(WorkshopDTO dto);
}
