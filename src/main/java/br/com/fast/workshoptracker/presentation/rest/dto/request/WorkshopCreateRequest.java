package br.com.fast.workshoptracker.presentation.rest.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.fast.workshoptracker.presentation.rest.validation.ata.DataRealizacao;
import br.com.fast.workshoptracker.presentation.rest.validation.workshop.WorkshopDescricao;
import br.com.fast.workshoptracker.presentation.rest.validation.workshop.WorkshopNome;
import io.swagger.v3.oas.annotations.media.Schema;

public record WorkshopCreateRequest(
		@WorkshopNome
		@Schema(example = "Workshop Spring")
		String nome,

		@DataRealizacao
		@JsonFormat(pattern = "dd/MM/yyyy")
		@Schema(example = "20/01/2026")
		LocalDate dataRealizacao,

		@WorkshopDescricao
		@Schema(example = "Conteúdo do workshop...")
		String descricao
) {
}
