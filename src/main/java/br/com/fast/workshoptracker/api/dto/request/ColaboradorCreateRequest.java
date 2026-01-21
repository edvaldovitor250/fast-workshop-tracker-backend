package br.com.fast.workshoptracker.api.dto.request;

import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorNome;
import io.swagger.v3.oas.annotations.media.Schema;

public record ColaboradorCreateRequest(
		@ColaboradorNome
		@Schema(example = "Ana Silva")
		String nome
) {
}

