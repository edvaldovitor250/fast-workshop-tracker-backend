package br.com.fast.workshoptracker.api.controller;

import br.com.fast.workshoptracker.api.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.api.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.AtaResponse;
import br.com.fast.workshoptracker.api.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.application.service.AtaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Atas")
@RestController
@RequiredArgsConstructor
public class AtaController {

	private final AtaService ataService;

	@Operation(summary = "Criar ata de presença")
	@PostMapping("/api/atas")
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<AtaResponse> create(@Valid @RequestBody AtaCreateRequest request) {
		AtaResponse response = ataService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Adicionar colaborador em uma ata (workshop dono da ata)")
	@PutMapping("/api/workshops/{workshopId}/atas/{ataId}")
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<AtaResponse> addColaborador(
			@PathVariable Long workshopId,
			@PathVariable Long ataId,
			@Valid @RequestBody AtaAddColaboradorRequest request
	) {
		AtaResponse response = ataService.addColaborador(workshopId, ataId, request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Remover colaborador de uma ata")
	@DeleteMapping("/api/atas/{ataId}/colaboradores/{colaboradorId}")
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<Void> removeColaborador(@PathVariable Long ataId, @PathVariable Long colaboradorId) {
		ataService.removeColaborador(ataId, colaboradorId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Listar colaboradores e workshops que participaram")
	@GetMapping("/api/atas")
	@PreAuthorize("hasAnyRole('READER','CREATOR','ADMIN')")
	public ResponseEntity<List<ColaboradorParticipacoesResponse>> listarParticipacoes(
			@RequestParam(required = false) String workshopNome,
			@Parameter(description = "Formato yyyy-MM-dd")
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
	) {
		return ResponseEntity.ok(ataService.listarParticipacoes(workshopNome, data));
	}
}
