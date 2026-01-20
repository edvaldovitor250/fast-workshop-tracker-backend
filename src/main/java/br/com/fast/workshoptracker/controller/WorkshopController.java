package br.com.fast.workshoptracker.controller;

import br.com.fast.workshoptracker.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.service.WorkshopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Workshops")
@RestController
@RequestMapping("/api/workshops")
public class WorkshopController {

	private final WorkshopService workshopService;

	public WorkshopController(WorkshopService workshopService) {
		this.workshopService = workshopService;
	}

	@Operation(summary = "Cadastrar workshop")
	@PostMapping
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<WorkshopResponse> create(@Valid @RequestBody WorkshopCreateRequest request) {
		WorkshopResponse response = workshopService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
