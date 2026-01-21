package br.com.fast.workshoptracker.api.dto.response;

import br.com.fast.workshoptracker.api.validation.ata.AtaColaboradores;
import br.com.fast.workshoptracker.api.validation.ata.AtaId;
import br.com.fast.workshoptracker.api.validation.ata.AtaWorkshop;
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

