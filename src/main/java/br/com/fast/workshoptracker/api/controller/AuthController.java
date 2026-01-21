package br.com.fast.workshoptracker.api.controller;

import br.com.fast.workshoptracker.api.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.api.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.api.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.api.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "Registrar usuário (nome/e-mail/senha)", security = {})
	@PostMapping("/register")
	public ResponseEntity<UsuarioResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
		UsuarioResponse response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@Operation(summary = "Login (e-mail/senha) -> token JWT", security = {})
	@PostMapping("/login")
	public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody AuthLoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}
}
