package br.com.fast.workshoptracker.presentation.openapi.api;

import br.com.fast.workshoptracker.presentation.rest.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ErrorResponse;
import br.com.fast.workshoptracker.presentation.openapi.docs.ColaboradorDocs;
import br.com.fast.workshoptracker.presentation.openapi.docs.CommonErrorDocs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = ColaboradorDocs.TAG)
@RequestMapping("/api/colaboradores")
public interface ColaboradorApi {

	@Operation(summary = ColaboradorDocs.CREATE_SUMMARY, description = ColaboradorDocs.CREATE_DESCRIPTION)
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Criado",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ColaboradorResponse.class),
							examples = @ExampleObject(name = "created", value = ColaboradorDocs.CREATE_201_RESPONSE)
					)
			),
			@ApiResponse(
					responseCode = "400",
					description = "Validacao/JSON invalido",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = {
									@ExampleObject(name = "validation", value = CommonErrorDocs.ERROR_400_VALIDATION),
									@ExampleObject(name = "badRequest", value = CommonErrorDocs.ERROR_400_BAD_REQUEST)
							}
					)
			)
	})
	@PostMapping
	ResponseEntity<ColaboradorResponse> create(
			@Valid
			@org.springframework.web.bind.annotation.RequestBody
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ColaboradorCreateRequest.class),
							examples = @ExampleObject(name = "request", value = ColaboradorDocs.CREATE_REQUEST_EXAMPLE)
					)
			)
			ColaboradorCreateRequest request
	);
}
