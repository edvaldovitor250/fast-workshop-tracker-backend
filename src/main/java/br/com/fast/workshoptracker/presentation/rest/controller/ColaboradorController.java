package br.com.fast.workshoptracker.presentation.rest.controller;

import br.com.fast.workshoptracker.presentation.rest.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.presentation.openapi.api.ColaboradorApi;
import br.com.fast.workshoptracker.application.port.input.CriarColaboradorUseCase;
import br.com.fast.workshoptracker.presentation.rest.mapper.ColaboradorRestMapper;
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

	private final CriarColaboradorUseCase criarColaboradorUseCase;
	private final ColaboradorRestMapper colaboradorRestMapper;

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<ColaboradorResponse> create(@Valid @RequestBody ColaboradorCreateRequest request) {
		var dto = criarColaboradorUseCase.execute(colaboradorRestMapper.toCommand(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(colaboradorRestMapper.toResponse(dto));
	}
}

