package br.com.fast.workshoptracker.mapper;

import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.dto.response.ColaboradorResponse;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Component
public class ColaboradorMapper {

	public Colaborador toEntity(ColaboradorCreateRequest request) {
		if (request == null) {
			return null;
		}
		return new Colaborador(request.nome());
	}

	public ColaboradorResponse toResponse(Colaborador entity) {
		if (entity == null) {
			return null;
		}
		return new ColaboradorResponse(entity.getId(), entity.getNome());
	}

	public List<ColaboradorResponse> toResponseList(Set<Colaborador> entities) {
		if (entities == null || entities.isEmpty()) {
			return List.of();
		}
		return entities.stream()
				.sorted(Comparator.comparing(Colaborador::getNome, Comparator.nullsLast(String::compareToIgnoreCase)))
				.map(this::toResponse)
				.toList();
	}
}

