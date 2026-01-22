package br.com.fast.workshoptracker.testconfig;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.fast.workshoptracker.application.dto.command.CriarColaboradorCommand;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.presentation.rest.mapper.ColaboradorMapper;

@Component
@Primary
@Profile("test")
public class ColaboradorMapperTestImpl implements ColaboradorMapper {

	@Override
	public CriarColaboradorCommand toCommand(ColaboradorCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new CriarColaboradorCommand(request.nome());
	}

	@Override
	public ColaboradorResponse toResponse(ColaboradorDTO dto) {
		if (dto == null) {
			return null;
		}
		return new ColaboradorResponse(dto.id(), dto.nome());
	}
}
