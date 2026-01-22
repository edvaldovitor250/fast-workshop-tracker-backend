package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorParticipacoesDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AtaResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorParticipacoesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {WorkshopMapper.class, ColaboradorMapper.class})
public interface AtaMapper {

	CriarAtaCommand toCommand(AtaCreateRequest request);

	@Mapping(target = "colaboradores", source = "colaboradores")
	AtaResponse toResponse(AtaDTO dto);

	ColaboradorParticipacoesResponse toResponse(ColaboradorParticipacoesDTO dto);
}
