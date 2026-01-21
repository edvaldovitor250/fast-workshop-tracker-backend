package br.com.fast.workshoptracker.application.mapper;

import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AtaApplicationMapper {

	public AtaDTO toDto(Ata entity) {
		if (entity == null) {
			return null;
		}
		return new AtaDTO(
				entity.getId(),
				toDto(entity.getWorkshop()),
				toColaboradorDtoList(entity.getColaboradores() == null ? List.of() : List.copyOf(entity.getColaboradores()))
		);
	}

	private WorkshopDTO toDto(Workshop entity) {
		if (entity == null) {
			return null;
		}
		return new WorkshopDTO(entity.getId(), entity.getNome(), entity.getDataRealizacao(), entity.getDescricao());
	}

	private List<ColaboradorDTO> toColaboradorDtoList(List<Colaborador> colaboradores) {
		if (colaboradores == null || colaboradores.isEmpty()) {
			return List.of();
		}
		List<ColaboradorDTO> list = new ArrayList<>(colaboradores.size());
		for (Colaborador c : colaboradores) {
			if (c == null) {
				continue;
			}
			list.add(new ColaboradorDTO(c.getId(), c.getNome()));
		}
		return List.copyOf(list);
	}
}

