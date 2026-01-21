package br.com.fast.workshoptracker.application.service;

import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.api.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.api.mapper.WorkshopMapper;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.WorkshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkshopService {

	private final WorkshopRepository workshopRepository;
	private final WorkshopMapper workshopMapper;

	@Transactional
	public WorkshopResponse create(WorkshopCreateRequest request) {
		Workshop workshop = workshopMapper.toEntity(request);
		workshop = workshopRepository.save(workshop);
		return workshopMapper.toResponse(workshop);
	}
}

