package br.com.fast.workshoptracker.api.controller;

import br.com.fast.workshoptracker.api.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.api.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.api.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.api.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.api.openapi.AuthApi;
import br.com.fast.workshoptracker.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

	private final AuthService authService;

	@Override
	public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
		UsuarioResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Override
	public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody AuthLoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}
}

