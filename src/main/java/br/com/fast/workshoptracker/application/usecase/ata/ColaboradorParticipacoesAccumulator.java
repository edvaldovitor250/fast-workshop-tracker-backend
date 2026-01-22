package br.com.fast.workshoptracker.application.usecase.ata;

import br.com.fast.workshoptracker.application.dto.query.ColaboradorParticipacoesDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class ColaboradorParticipacoesAccumulator {
	private final Long colaboradorId;
	private final String nome;
	private final List<WorkshopDTO> workshops = new ArrayList<>();
	private final Set<Long> workshopIds = new HashSet<>();

	ColaboradorParticipacoesAccumulator(Long colaboradorId, String nome) {
		this.colaboradorId = colaboradorId;
		this.nome = nome;
	}

	void addWorkshop(WorkshopDTO workshop) {
		if (workshop == null || workshop.id() == null) {
			return;
		}
		if (workshopIds.add(workshop.id())) {
			workshops.add(workshop);
		}
	}

	ColaboradorParticipacoesDTO toDto() {
		return new ColaboradorParticipacoesDTO(colaboradorId, nome, workshops);
	}
}

