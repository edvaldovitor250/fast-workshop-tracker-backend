package br.com.fast.workshoptracker.testconfig;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorParticipacoesDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AtaResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.presentation.rest.mapper.AtaMapper;

@Component
@Primary
@Profile("test")
public class AtaMapperTestImpl implements AtaMapper {

	@Override
	public CriarAtaCommand toCommand(AtaCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new CriarAtaCommand(request.workshopId(), request.colaboradoresIds());
	}

	@Override
	public AtaResponse toResponse(AtaDTO dto) {
		if (dto == null) {
			return null;
		}
		return new AtaResponse(
				dto.id(),
				toWorkshopResponse(dto.workshop()),
				toColaboradoresResponse(dto.colaboradores())
		);
	}

	@Override
	public ColaboradorParticipacoesResponse toResponse(ColaboradorParticipacoesDTO dto) {
		if (dto == null) {
			return null;
		}
		List<WorkshopResponse> workshops = dto.workshops() == null
				? List.of()
				: dto.workshops().stream().map(this::toWorkshopResponse).toList();
		return new ColaboradorParticipacoesResponse(dto.colaboradorId(), dto.nome(), workshops);
	}

	private WorkshopResponse toWorkshopResponse(WorkshopDTO dto) {
		if (dto == null) {
			return null;
		}
		return new WorkshopResponse(dto.id(), dto.nome(), dto.dataRealizacao(), dto.descricao());
	}

	private List<ColaboradorResponse> toColaboradoresResponse(List<ColaboradorDTO> dtos) {
		if (dtos == null || dtos.isEmpty()) {
			return List.of();
		}
		return dtos.stream()
				.filter(c -> c != null)
				.map(c -> new ColaboradorResponse(c.id(), c.nome()))
				.toList();
	}
}
