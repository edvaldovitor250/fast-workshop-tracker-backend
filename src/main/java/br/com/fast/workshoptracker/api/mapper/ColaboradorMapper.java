package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.api.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {

	@Mapping(target = "id", ignore = true)
	Colaborador toEntity(ColaboradorCreateRequest request);

	ColaboradorResponse toResponse(Colaborador entity);

	@Named("toResponseListSorted")
	default List<ColaboradorResponse> toResponseListSorted(Set<Colaborador> entities) {
		if (entities == null || entities.isEmpty()) {
			return List.of();
		}
		return entities.stream()
				.sorted(Comparator.comparing(Colaborador::getNome, 
						Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
				.map(this::toResponse)
				.toList();
	}

	default List<ColaboradorResponse> toResponseList(Set<Colaborador> entities) {
		if (entities == null) {
			return List.of();
		}
		return entities.stream()
				.map(this::toResponse)
				.toList();
	}
}

