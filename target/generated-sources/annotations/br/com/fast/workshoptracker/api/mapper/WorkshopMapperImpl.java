package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.api.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T12:49:56-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class WorkshopMapperImpl implements WorkshopMapper {

    @Override
    public Workshop toEntity(WorkshopCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        String nome = null;
        LocalDate dataRealizacao = null;

        nome = request.nome();
        dataRealizacao = request.dataRealizacao();

        Workshop workshop = new Workshop( nome, dataRealizacao );

        workshop.setDescricao( request.descricao() );

        return workshop;
    }

    @Override
    public WorkshopResponse toResponse(Workshop entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        LocalDate dataRealizacao = null;
        String descricao = null;

        id = entity.getId();
        nome = entity.getNome();
        dataRealizacao = entity.getDataRealizacao();
        descricao = entity.getDescricao();

        WorkshopResponse workshopResponse = new WorkshopResponse( id, nome, dataRealizacao, descricao );

        return workshopResponse;
    }
}
