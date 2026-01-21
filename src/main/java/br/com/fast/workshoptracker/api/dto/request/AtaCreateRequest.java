package br.com.fast.workshoptracker.api.dto.request;

import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradorId;
import br.com.fast.workshoptracker.api.validation.colaborador.ColaboradoresIds;
import br.com.fast.workshoptracker.api.validation.workshop.WorkshopId;
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

