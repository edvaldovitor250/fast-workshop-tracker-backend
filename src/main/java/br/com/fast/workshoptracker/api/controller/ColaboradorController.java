package br.com.fast.workshoptracker.api.controller;

import br.com.fast.workshoptracker.api.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.application.service.ColaboradorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Colaboradores")
@RestController
@RequestMapping("/api/colaboradores")
@RequiredArgsConstructor
public class ColaboradorController {

	private final ColaboradorService colaboradorService;

	@Operation(summary = "Cadastrar colaborador")
	@PostMapping
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<ColaboradorResponse> create(@Valid @RequestBody ColaboradorCreateRequest request) {
		ColaboradorResponse response = colaboradorService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
