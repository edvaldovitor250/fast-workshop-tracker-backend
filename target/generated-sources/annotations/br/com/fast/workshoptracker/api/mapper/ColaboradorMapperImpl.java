package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.api.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T12:49:56-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ColaboradorMapperImpl implements ColaboradorMapper {

    @Override
    public Colaborador toEntity(ColaboradorCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        String nome = null;

        nome = request.nome();

        Colaborador colaborador = new Colaborador( nome );

        return colaborador;
    }

    @Override
    public ColaboradorResponse toResponse(Colaborador entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;

        id = entity.getId();
        nome = entity.getNome();

        ColaboradorResponse colaboradorResponse = new ColaboradorResponse( id, nome );

        return colaboradorResponse;
    }
}
