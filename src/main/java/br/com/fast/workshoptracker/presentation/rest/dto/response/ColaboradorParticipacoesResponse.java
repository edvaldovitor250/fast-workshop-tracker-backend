package br.com.fast.workshoptracker.presentation.rest.dto.response;

import br.com.fast.workshoptracker.presentation.rest.validation.ata.ParticipacaoWorkshops;
import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradorId;
import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradorNome;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ColaboradorParticipacoesResponse(
		@ColaboradorId
		@Schema(example = "1")
		Long colaboradorId,

		@ColaboradorNome
		@Schema(example = "Ana Silva")
		String nome,

		@ParticipacaoWorkshops List<WorkshopResponse> workshops
) {
}

