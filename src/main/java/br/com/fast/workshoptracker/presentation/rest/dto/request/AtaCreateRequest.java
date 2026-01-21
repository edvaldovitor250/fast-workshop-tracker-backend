package br.com.fast.workshoptracker.presentation.rest.dto.request;

import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradorId;
import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradoresIds;
import br.com.fast.workshoptracker.presentation.rest.validation.workshop.WorkshopId;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AtaCreateRequest(
		@WorkshopId
		@Schema(example = "1")
		Long workshopId,

		@ColaboradoresIds
		@Schema(example = "[1,2,3]")
		List<@ColaboradorId Long> colaboradoresIds
) {
}

