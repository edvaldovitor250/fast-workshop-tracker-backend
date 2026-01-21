package br.com.fast.workshoptracker.presentation.rest.dto.request;

import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradorId;
import io.swagger.v3.oas.annotations.media.Schema;

public record AtaAddColaboradorRequest(
		@ColaboradorId
		@Schema(example = "10")
		Long colaboradorId
) {
}

