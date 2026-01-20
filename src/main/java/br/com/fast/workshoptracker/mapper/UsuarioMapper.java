package br.com.fast.workshoptracker.mapper;

import br.com.fast.workshoptracker.domain.entity.Usuario;
import br.com.fast.workshoptracker.dto.response.UsuarioResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioMapper {

	public UsuarioResponse toResponse(Usuario entity) {
		if (entity == null) {
			return null;
		}
		List<String> roles = entity.getRoles().stream().map(Enum::name).sorted().toList();
		return new UsuarioResponse(entity.getId(), entity.getNome(), entity.getEmail(), roles);
	}
}

