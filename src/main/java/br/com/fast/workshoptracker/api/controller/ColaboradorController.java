package br.com.fast.workshoptracker.api.controller;

import br.com.fast.workshoptracker.api.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.api.openapi.api.ColaboradorApi;
import br.com.fast.workshoptracker.application.service.ColaboradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ColaboradorController implements ColaboradorApi {

	private final ColaboradorService colaboradorService;

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<ColaboradorResponse> create(@Valid @RequestBody ColaboradorCreateRequest request) {
		ColaboradorResponse response = colaboradorService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}

