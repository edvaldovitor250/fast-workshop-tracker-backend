package br.com.fast.workshoptracker.api.dto.response;

import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorId;
import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorNome;
import io.swagger.v3.oas.annotations.media.Schema;

public record ColaboradorResponse(
		@ColaboradorId
		@Schema(example = "1")
		Long id,

		@ColaboradorNome
		@Schema(example = "Ana Silva")
		String nome
) {
}

