package br.com.fast.workshoptracker.presentation.rest.controller;

import br.com.fast.workshoptracker.presentation.rest.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.presentation.openapi.api.WorkshopApi;
import br.com.fast.workshoptracker.application.port.input.CriarWorkshopUseCase;
import br.com.fast.workshoptracker.presentation.rest.mapper.WorkshopRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkshopController implements WorkshopApi {

	private final CriarWorkshopUseCase criarWorkshopUseCase;
	private final WorkshopRestMapper workshopRestMapper;

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<WorkshopResponse> create(@Valid @RequestBody WorkshopCreateRequest request) {
		var dto = criarWorkshopUseCase.execute(workshopRestMapper.toCommand(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(workshopRestMapper.toResponse(dto));
	}
}

