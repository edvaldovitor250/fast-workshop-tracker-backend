package br.com.fast.workshoptracker.presentation.rest.controller;

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

import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AtaResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.presentation.openapi.api.AtaApi;
import br.com.fast.workshoptracker.application.dto.command.AdicionarColaboradorAtaCommand;
import br.com.fast.workshoptracker.application.dto.command.RemoverColaboradorAtaCommand;
import br.com.fast.workshoptracker.application.dto.query.ListarParticipacoesQuery;
import br.com.fast.workshoptracker.application.port.input.AdicionarColaboradorAtaUseCase;
import br.com.fast.workshoptracker.application.port.input.CriarAtaUseCase;
import br.com.fast.workshoptracker.application.port.input.ListarParticipacoesUseCase;
import br.com.fast.workshoptracker.application.port.input.RemoverColaboradorAtaUseCase;
import br.com.fast.workshoptracker.presentation.rest.mapper.AtaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AtaController implements AtaApi {

	private final CriarAtaUseCase criarAtaUseCase;
	private final AdicionarColaboradorAtaUseCase adicionarColaboradorAtaUseCase;
	private final RemoverColaboradorAtaUseCase removerColaboradorAtaUseCase;
	private final ListarParticipacoesUseCase listarParticipacoesUseCase;
	private final AtaMapper ataMapper;

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<AtaResponse> create(@Valid @RequestBody AtaCreateRequest request) {
		var dto = criarAtaUseCase.execute(ataMapper.toCommand(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(ataMapper.toResponse(dto));
	}

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<AtaResponse> addColaborador(
			@PathVariable Long workshopId,
			@PathVariable Long ataId,
			@Valid @RequestBody AtaAddColaboradorRequest request
	) {
		var cmd = new AdicionarColaboradorAtaCommand(workshopId, ataId, request.colaboradorId());
		var dto = adicionarColaboradorAtaUseCase.execute(cmd);
		return ResponseEntity.ok(ataMapper.toResponse(dto));
	}

	@Override
	@PreAuthorize("hasAnyRole('CREATOR','ADMIN')")
	public ResponseEntity<Void> removeColaborador(@PathVariable Long ataId, @PathVariable Long colaboradorId) {
		removerColaboradorAtaUseCase.execute(new RemoverColaboradorAtaCommand(ataId, colaboradorId));
		return ResponseEntity.noContent().build();
	}

	@Override
	@PreAuthorize("hasAnyRole('READER','CREATOR','ADMIN')")
	public ResponseEntity<List<ColaboradorParticipacoesResponse>> listarParticipacoes(
			@RequestParam(required = false) String workshopNome,
			@RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate data
	) {
		var dtos = listarParticipacoesUseCase.execute(new ListarParticipacoesQuery(workshopNome, data));
		var response = dtos.stream().map(ataMapper::toResponse).toList();
		return ResponseEntity.ok(response);
	}
}
