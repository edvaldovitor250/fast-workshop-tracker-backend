package br.com.fast.workshoptracker.api.dto.response;

import br.com.fast.workshoptracker.api.validation.ata.ParticipacaoWorkshops;
import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorId;
import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorNome;
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

