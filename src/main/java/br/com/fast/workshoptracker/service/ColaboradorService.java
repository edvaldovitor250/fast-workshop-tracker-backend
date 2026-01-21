package br.com.fast.workshoptracker.service;

import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.mapper.ColaboradorMapper;
import br.com.fast.workshoptracker.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

	private final ColaboradorRepository colaboradorRepository;
	private final ColaboradorMapper colaboradorMapper;

	@Transactional
	public ColaboradorResponse create(ColaboradorCreateRequest request) {
		Colaborador colaborador = colaboradorMapper.toEntity(request);
		colaborador = colaboradorRepository.save(colaborador);
		return colaboradorMapper.toResponse(colaborador);
	}
}

