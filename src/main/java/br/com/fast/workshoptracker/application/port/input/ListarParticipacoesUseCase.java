package br.com.fast.workshoptracker.application.port.input;

import br.com.fast.workshoptracker.application.dto.query.ColaboradorParticipacoesDTO;
import br.com.fast.workshoptracker.application.dto.query.ListarParticipacoesQuery;

import java.util.List;

public interface ListarParticipacoesUseCase {
	List<ColaboradorParticipacoesDTO> execute(ListarParticipacoesQuery query);
}

