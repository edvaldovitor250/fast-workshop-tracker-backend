package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorParticipacoesDTO;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AtaResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AtaRestMapper {

	public CriarAtaCommand toCommand(AtaCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new CriarAtaCommand(request.workshopId(), request.colaboradoresIds());
	}

	public AtaResponse toResponse(AtaDTO dto) {
		if (dto == null) {
			return null;
		}
		return new AtaResponse(dto.id(), toResponse(dto.workshop()), toColaboradorResponseList(dto.colaboradores()));
	}

	private WorkshopResponse toResponse(WorkshopDTO dto) {
		if (dto == null) {
			return null;
		}
		return new WorkshopResponse(dto.id(), dto.nome(), dto.dataRealizacao(), dto.descricao());
	}

	private List<ColaboradorResponse> toColaboradorResponseList(List<ColaboradorDTO> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			return List.of();
		}
		List<ColaboradorResponse> list = new ArrayList<>(dtos.size());
		for (ColaboradorDTO dto : dtos) {
			if (dto == null) {
				continue;
			}
			list.add(new ColaboradorResponse(dto.id(), dto.nome()));
		}
		return List.copyOf(list);
	}

	public ColaboradorParticipacoesResponse toResponse(ColaboradorParticipacoesDTO dto) {
		if (dto == null) {
			return null;
		}
		return new ColaboradorParticipacoesResponse(dto.colaboradorId(), dto.nome(), toWorkshopResponseList(dto.workshops()));
	}

	private List<WorkshopResponse> toWorkshopResponseList(List<WorkshopDTO> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			return List.of();
		}
		List<WorkshopResponse> list = new ArrayList<>(dtos.size());
		for (WorkshopDTO dto : dtos) {
			if (dto == null) {
				continue;
			}
			list.add(toResponse(dto));
		}
		return List.copyOf(list);
	}
}
