package br.com.fast.workshoptracker.application.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;

import br.com.fast.workshoptracker.application.dto.query.AtaDTO;
import br.com.fast.workshoptracker.application.dto.query.ColaboradorDTO;
import br.com.fast.workshoptracker.application.dto.query.WorkshopDTO;
import br.com.fast.workshoptracker.domain.entity.Ata;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;

@Mapper(componentModel = "spring")
public interface AtaApplicationMapper {

	AtaDTO toDto(Ata entity);

	WorkshopDTO toDto(Workshop entity);

	ColaboradorDTO toDto(Colaborador entity);

	default List<ColaboradorDTO> toColaboradorDtoList(Set<Colaborador> colaboradores) {
		if (colaboradores == null || colaboradores.isEmpty()) {
			return List.of();
		}
		return colaboradores.stream()
				.filter(c -> c != null)
				.map(this::toDto)
				.toList();
	}
}
