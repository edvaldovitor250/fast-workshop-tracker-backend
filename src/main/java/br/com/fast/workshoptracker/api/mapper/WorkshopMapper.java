package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.api.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkshopMapper {

	@Mapping(target = "id", ignore = true)
	Workshop toEntity(WorkshopCreateRequest request);

	WorkshopResponse toResponse(Workshop entity);
}

