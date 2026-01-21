package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.CriarColaboradorCommand;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import org.springframework.stereotype.Component;

@Component
public class ColaboradorRestMapper {

	public CriarColaboradorCommand toCommand(ColaboradorCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new CriarColaboradorCommand(request.nome());
	}

	public ColaboradorResponse toResponse(ColaboradorDTO dto) {
		if (dto == null) {
			return null;
		}
		return new ColaboradorResponse(dto.id(), dto.nome());
	}
}

