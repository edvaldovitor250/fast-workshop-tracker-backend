package br.com.fast.workshoptracker.api.openapi;

import br.com.fast.workshoptracker.api.dto.request.WorkshopCreateRequest;
import br.com.fast.workshoptracker.api.dto.response.ErrorResponse;
import br.com.fast.workshoptracker.api.dto.response.WorkshopResponse;
import br.com.fast.workshoptracker.api.openapi.docs.CommonErrorDocs;
import br.com.fast.workshoptracker.api.openapi.docs.WorkshopDocs;
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

@Tag(name = WorkshopDocs.TAG)
@RequestMapping("/api/workshops")
public interface WorkshopApi {

	@Operation(summary = WorkshopDocs.CREATE_SUMMARY, description = WorkshopDocs.CREATE_DESCRIPTION)
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Criado",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = WorkshopResponse.class),
							examples = @ExampleObject(name = "created", value = WorkshopDocs.CREATE_201_RESPONSE)
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
	ResponseEntity<WorkshopResponse> create(
			@Valid
			@org.springframework.web.bind.annotation.RequestBody
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = WorkshopCreateRequest.class),
							examples = @ExampleObject(name = "request", value = WorkshopDocs.CREATE_REQUEST_EXAMPLE)
					)
			)
			WorkshopCreateRequest request
	);
}
