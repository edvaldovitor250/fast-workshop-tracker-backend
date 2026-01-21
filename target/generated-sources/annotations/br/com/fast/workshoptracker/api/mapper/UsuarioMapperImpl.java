package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.api.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.domain.entity.Usuario;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T12:49:56-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario entity) {
        if ( entity == null ) {
            return null;
        }

        List<String> roles = null;
        Long id = null;
        String nome = null;
        String email = null;

        roles = rolesToStrings( entity.getRoles() );
        id = entity.getId();
        nome = entity.getNome();
        email = entity.getEmail();

        UsuarioResponse usuarioResponse = new UsuarioResponse( id, nome, email, roles );

        return usuarioResponse;
    }
}
