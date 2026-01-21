package br.com.fast.workshoptracker.api.controller;

import br.com.fast.workshoptracker.api.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.api.openapi.api.WorkshopApi;
import br.com.fast.workshoptracker.application.service.WorkshopService;
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

	private final WorkshopService workshopService;

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<WorkshopResponse> create(@Valid @RequestBody WorkshopCreateRequest request) {
		WorkshopResponse response = workshopService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}

