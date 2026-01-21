package br.com.fast.workshoptracker.application.dto.query;

import java.time.LocalDate;

public record ColaboradorWorkshopRowDTO(
		Long colaboradorId,
		String colaboradorNome,
		Long workshopId,
		String workshopNome,
		LocalDate workshopDataRealizacao,
		String workshopDescricao
) {
}

