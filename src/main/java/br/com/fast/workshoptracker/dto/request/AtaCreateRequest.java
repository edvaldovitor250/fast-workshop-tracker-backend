package br.com.fast.workshoptracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AtaCreateRequest(
		@NotNull
		@Schema(example = "1")
		Long workshopId,

		@Schema(example = "[1,2,3]")
		List<Long> colaboradoresIds
) {
}

