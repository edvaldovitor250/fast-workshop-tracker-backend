package br.com.fast.workshoptracker.presentation.rest.dto.request;

import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradorNome;
import io.swagger.v3.oas.annotations.media.Schema;

public record ColaboradorCreateRequest(
		@ColaboradorNome
		@Schema(example = "Ana Silva")
		String nome
) {
}

