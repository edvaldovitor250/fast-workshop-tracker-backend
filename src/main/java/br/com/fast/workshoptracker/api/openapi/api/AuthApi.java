package br.com.fast.workshoptracker.api.openapi.api;

import br.com.fast.workshoptracker.api.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.api.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.api.dto.response.AuthTokenResponse;
import br.com.fast.workshoptracker.api.dto.response.ErrorResponse;
import br.com.fast.workshoptracker.api.dto.response.UsuarioResponse;
import br.com.fast.workshoptracker.api.openapi.docs.AuthDocs;
import br.com.fast.workshoptracker.api.openapi.docs.CommonErrorDocs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = AuthDocs.TAG)
@RequestMapping("/api/auth")
public interface AuthApi {

	@Operation(summary = AuthDocs.REGISTER_SUMMARY, description = AuthDocs.REGISTER_DESCRIPTION, security = {})
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Criado",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = UsuarioResponse.class),
							examples = @ExampleObject(name = "created", value = AuthDocs.REGISTER_201_RESPONSE)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Validacao",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "validation", value = CommonErrorDocs.ERROR_400_VALIDATION)
					)
			),
			@ApiResponse(
					responseCode = "409",
					description = "Conflito (e-mail ja cadastrado)",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "conflict", value = CommonErrorDocs.ERROR_409_CONFLICT)
					)
			)
	})
	@PostMapping("/register")
	ResponseEntity<UsuarioResponse> register(
			@Valid
			@org.springframework.web.bind.annotation.RequestBody
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AuthRegisterRequest.class),
							examples = @ExampleObject(name = "request", value = AuthDocs.REGISTER_REQUEST_EXAMPLE)
					)
			)
			AuthRegisterRequest request
	);

	@Operation(summary = AuthDocs.LOGIN_SUMMARY, description = AuthDocs.LOGIN_DESCRIPTION, security = {})
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "OK",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AuthTokenResponse.class),
							examples = @ExampleObject(name = "ok", value = AuthDocs.LOGIN_200_RESPONSE)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Validacao",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "validation", value = CommonErrorDocs.ERROR_400_VALIDATION)
					)
			),
			@ApiResponse(
					responseCode = "401",
					description = "Credenciais invalidas",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "unauthorized", value = CommonErrorDocs.ERROR_401_UNAUTHORIZED)
					)
			)
	})
	@PostMapping("/login")
	ResponseEntity<AuthTokenResponse> login(
			@Valid
			@org.springframework.web.bind.annotation.RequestBody
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AuthLoginRequest.class),
							examples = @ExampleObject(name = "request", value = AuthDocs.LOGIN_REQUEST_EXAMPLE)
					)
			)
			AuthLoginRequest request
	);
}
