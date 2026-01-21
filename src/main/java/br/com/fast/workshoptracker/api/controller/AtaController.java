package br.com.fast.workshoptracker.api.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fast.workshoptracker.api.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.api.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.AtaResponse;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.api.openapi.api.AtaApi;
import br.com.fast.workshoptracker.application.service.AtaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AtaController implements AtaApi {

	private final AtaService ataService;

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<AtaResponse> create(@Valid @RequestBody AtaCreateRequest request) {
		AtaResponse response = ataService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<AtaResponse> addColaborador(
			@PathVariable Long workshopId,
			@PathVariable Long ataId,
			@Valid @RequestBody AtaAddColaboradorRequest request
	) {
		AtaResponse response = ataService.addColaborador(workshopId, ataId, request);
		return ResponseEntity.ok(response);
	}

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<Void> removeColaborador(@PathVariable Long ataId, @PathVariable Long colaboradorId) {
		ataService.removeColaborador(ataId, colaboradorId);
		return ResponseEntity.noContent().build();
	}

	@Override
	@PreAuthorize("hasAnyRole('READER','CREATOR','ADMIN')")
	public ResponseEntity<List<ColaboradorParticipacoesResponse>> listarParticipacoes(
			@RequestParam(required = false) String workshopNome,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
	) {
		return ResponseEntity.ok(ataService.listarParticipacoes(workshopNome, data));
	}
}

