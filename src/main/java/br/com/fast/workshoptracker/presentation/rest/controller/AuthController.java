package br.com.fast.workshoptracker.presentation.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.presentation.openapi.api.AuthApi;
import br.com.fast.workshoptracker.application.port.input.AutenticarUsuarioUseCase;
import br.com.fast.workshoptracker.application.port.input.RegistrarUsuarioUseCase;
import br.com.fast.workshoptracker.presentation.rest.mapper.AuthMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
	private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
	private final AuthMapper authMapper;

	@Override
	public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
		var dto = registrarUsuarioUseCase.execute(authMapper.toCommand(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(authMapper.toResponse(dto));
	}

	@Override
	public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody AuthLoginRequest request) {
		var dto = autenticarUsuarioUseCase.execute(authMapper.toCommand(request));
		return ResponseEntity.ok(authMapper.toResponse(dto));
	}
}
