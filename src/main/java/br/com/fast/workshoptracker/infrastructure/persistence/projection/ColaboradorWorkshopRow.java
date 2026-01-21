package br.com.fast.workshoptracker.infrastructure.persistence.projection;

import java.time.LocalDate;

public interface ColaboradorWorkshopRow {
	Long getColaboradorId();
	String getColaboradorNome();

	Long getWorkshopId();
	String getWorkshopNome();
	LocalDate getWorkshopDataRealizacao();
	String getWorkshopDescricao();
}

