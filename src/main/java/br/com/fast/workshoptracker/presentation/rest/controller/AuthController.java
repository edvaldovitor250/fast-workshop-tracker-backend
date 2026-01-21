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
import br.com.fast.workshoptracker.presentation.rest.mapper.AuthRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final RegistrarUsuarioUseCase registrarUsuarioUseCase;
	private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
	private final AuthRestMapper authRestMapper;

	@Override
	public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
		var dto = registrarUsuarioUseCase.execute(authRestMapper.toCommand(request));
		return ResponseEntity.status(HttpStatus.CREATED).body(authRestMapper.toResponse(dto));
	}

	@Override
	public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody AuthLoginRequest request) {
		var dto = autenticarUsuarioUseCase.execute(authRestMapper.toCommand(request));
		return ResponseEntity.ok(authRestMapper.toResponse(dto));
	}
}

