package br.com.fast.workshoptracker.api.mapper;

import br.com.fast.workshoptracker.api.dto.response.AtaResponse;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.domain.entity.Ata;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-01-21T12:49:56-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260101-2150, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class AtaMapperImpl implements AtaMapper {

    @Autowired
    private WorkshopMapper workshopMapper;
    @Autowired
    private ColaboradorMapper colaboradorMapper;

    @Override
    public AtaResponse toResponse(Ata entity) {
        if ( entity == null ) {
            return null;
        }

        List<ColaboradorResponse> colaboradores = null;
        Long id = null;
        WorkshopResponse workshop = null;

        colaboradores = colaboradorMapper.toResponseList( entity.getColaboradores() );
        id = entity.getId();
        workshop = workshopMapper.toResponse( entity.getWorkshop() );

        AtaResponse ataResponse = new AtaResponse( id, workshop, colaboradores );

        return ataResponse;
    }
}
