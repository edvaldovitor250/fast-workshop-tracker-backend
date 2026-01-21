package br.com.fast.workshoptracker.api.dto.request;

import br.com.fast.workshoptracker.api.validation.ata.DataRealizacao;
import br.com.fast.workshoptracker.api.validation.workshop.WorkshopDescricao;
import br.com.fast.workshoptracker.api.validation.workshop.WorkshopNome;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record WorkshopCreateRequest(
		@WorkshopNome
		@Schema(example = "Workshop Spring")
		String nome,

		@DataRealizacao
		@Schema(example = "2026-01-20")
		LocalDate dataRealizacao,

		@WorkshopDescricao
		@Schema(example = "Conteúdo do workshop...")
		String descricao
) {
}
