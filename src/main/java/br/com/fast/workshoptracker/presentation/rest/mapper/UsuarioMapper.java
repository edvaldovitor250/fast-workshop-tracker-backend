package br.com.fast.workshoptracker.presentation.rest.mapper;

import br.com.fast.workshoptracker.presentation.rest.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

	@Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
	UsuarioResponse toResponse(Usuario entity);

	@Named("rolesToStrings")
	default List<String> rolesToStrings(Set<? extends Enum<?>> roles) {
		if (roles == null || roles.isEmpty()) {
			return List.of();
		}
		return roles.stream()
				.map(Enum::name)
				.sorted()
				.toList();
	}
}

