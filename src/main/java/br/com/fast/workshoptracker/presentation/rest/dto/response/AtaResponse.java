package br.com.fast.workshoptracker.presentation.rest.dto.response;

import br.com.fast.workshoptracker.presentation.rest.validation.ata.AtaColaboradores;
import br.com.fast.workshoptracker.presentation.rest.validation.ata.AtaId;
import br.com.fast.workshoptracker.presentation.rest.validation.ata.AtaWorkshop;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record AtaResponse(
		@AtaId
		@Schema(example = "1")
		Long id,
		@AtaWorkshop WorkshopResponse workshop,
		@AtaColaboradores List<ColaboradorResponse> colaboradores
) {
}

