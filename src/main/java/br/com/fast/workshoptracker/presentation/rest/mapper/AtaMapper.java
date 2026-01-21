package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.presentation.rest.dto.response.AtaResponse;
import br.com.fast.workshoptracker.domain.entity.Ata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WorkshopMapper.class, ColaboradorMapper.class})
public interface AtaMapper {

	@Mapping(target = "colaboradores", source = "colaboradores")
	AtaResponse toResponse(Ata entity);
}

