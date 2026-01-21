package br.com.fast.workshoptracker.api.dto.request;

import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorId;
import io.swagger.v3.oas.annotations.media.Schema;

public record AtaAddColaboradorRequest(
		@ColaboradorId
		@Schema(example = "10")
		Long colaboradorId
) {
}

