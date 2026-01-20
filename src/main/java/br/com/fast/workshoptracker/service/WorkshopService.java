package br.com.fast.workshoptracker.service;

import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.mapper.WorkshopMapper;
import br.com.fast.workshoptracker.repository.WorkshopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkshopService {

	private final WorkshopRepository workshopRepository;
	private final WorkshopMapper workshopMapper;

	public WorkshopService(WorkshopRepository workshopRepository, WorkshopMapper workshopMapper) {
		this.workshopRepository = workshopRepository;
		this.workshopMapper = workshopMapper;
	}

	@Transactional
	public WorkshopResponse create(WorkshopCreateRequest request) {
		Workshop workshop = workshopMapper.toEntity(request);
		workshop = workshopRepository.save(workshop);
		return workshopMapper.toResponse(workshop);
	}
}

