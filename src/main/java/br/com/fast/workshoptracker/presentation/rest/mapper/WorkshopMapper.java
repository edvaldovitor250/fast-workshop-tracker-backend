package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.presentation.rest.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

	@Mapping(target = "id", ignore = true)
	Workshop toEntity(WorkshopCreateRequest request);

	WorkshopResponse toResponse(Workshop entity);
}

