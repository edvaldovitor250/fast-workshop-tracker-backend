package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.CriarColaboradorCommand;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ColaboradorMapper {

	CriarColaboradorCommand toCommand(ColaboradorCreateRequest request);

	ColaboradorResponse toResponse(ColaboradorDTO dto);
}
