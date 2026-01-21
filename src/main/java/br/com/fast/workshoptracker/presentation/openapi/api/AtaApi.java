package br.com.fast.workshoptracker.presentation.openapi.api;

import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.response.AtaResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ColaboradorParticipacoesResponse;
import br.com.fast.workshoptracker.presentation.rest.dto.response.ErrorResponse;
import br.com.fast.workshoptracker.presentation.openapi.docs.AtaDocs;
import br.com.fast.workshoptracker.presentation.openapi.docs.CommonErrorDocs;
import br.com.fast.workshoptracker.presentation.rest.validation.ata.AtaId;
import br.com.fast.workshoptracker.presentation.rest.validation.colaborador.ColaboradorId;
import br.com.fast.workshoptracker.presentation.rest.validation.workshop.WorkshopId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Tag(name = AtaDocs.TAG)
@RequestMapping("/api")
public interface AtaApi {

	@Operation(summary = AtaDocs.CREATE_SUMMARY, description = AtaDocs.CREATE_DESCRIPTION)
	@ApiResponses({
			@ApiResponse(
					responseCode = "201",
					description = "Criado",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AtaResponse.class),
							examples = @ExampleObject(name = "created", value = AtaDocs.CREATE_201_RESPONSE)
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
			),
			@ApiResponse(
					responseCode = "404",
					description = "Workshop/Colaboradores nao encontrados",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "notFound", value = CommonErrorDocs.ERROR_404_NOT_FOUND)
					)
			),
			@ApiResponse(
					responseCode = "409",
					description = "Ata ja existe para o workshop",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "conflict", value = CommonErrorDocs.ERROR_409_CONFLICT)
					)
			)
	})
	@PostMapping("/atas")
	ResponseEntity<AtaResponse> create(
			@Valid
			@org.springframework.web.bind.annotation.RequestBody
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AtaCreateRequest.class),
							examples = @ExampleObject(name = "request", value = AtaDocs.CREATE_REQUEST_EXAMPLE)
					)
			)
			AtaCreateRequest request
	);

	@Operation(summary = AtaDocs.ADD_COLABORADOR_SUMMARY, description = AtaDocs.ADD_COLABORADOR_DESCRIPTION)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "OK",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AtaResponse.class),
							examples = @ExampleObject(name = "ok", value = AtaDocs.ADD_COLABORADOR_200_RESPONSE)
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
			),
			@ApiResponse(
					responseCode = "404",
					description = "Workshop/Ata/Colaborador nao encontrados",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "notFound", value = CommonErrorDocs.ERROR_404_NOT_FOUND)
					)
			),
			@ApiResponse(
					responseCode = "409",
					description = "Colaborador ja presente na ata",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "conflict", value = CommonErrorDocs.ERROR_409_CONFLICT)
					)
			)
	})
	@PutMapping("/workshops/{workshopId}/atas/{ataId}")
	ResponseEntity<AtaResponse> addColaborador(
			@Parameter(description = "ID do workshop dono da ata", example = "1")
			@PathVariable @WorkshopId Long workshopId,

			@Parameter(description = "ID da ata vinculada ao workshop", example = "1")
			@PathVariable @AtaId Long ataId,

			@Valid
			@org.springframework.web.bind.annotation.RequestBody
			@io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = AtaAddColaboradorRequest.class),
							examples = @ExampleObject(name = "request", value = AtaDocs.ADD_COLABORADOR_REQUEST_EXAMPLE)
					)
			)
			AtaAddColaboradorRequest request
	);

	@Operation(summary = AtaDocs.REMOVE_COLABORADOR_SUMMARY, description = AtaDocs.REMOVE_COLABORADOR_DESCRIPTION)
	@ApiResponses({
			@ApiResponse(responseCode = "204", description = "Removido"),
			@ApiResponse(
					responseCode = "404",
					description = "Ata/Colaborador nao encontrados ou colaborador nao esta na ata",
					content = @Content(
							mediaType = "application/json",
							schema = @Schema(implementation = ErrorResponse.class),
							examples = @ExampleObject(name = "notFound", value = CommonErrorDocs.ERROR_404_NOT_FOUND)
					)
			)
	})
	@DeleteMapping("/atas/{ataId}/colaboradores/{colaboradorId}")
	ResponseEntity<Void> removeColaborador(
			@Parameter(description = "ID da ata", example = "1")
			@PathVariable @AtaId Long ataId,
			@Parameter(description = "ID do colaborador", example = "10")
			@PathVariable @ColaboradorId Long colaboradorId
	);

	@Operation(summary = AtaDocs.LIST_SUMMARY, description = AtaDocs.LIST_DESCRIPTION)
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "OK",
					content = @Content(
							mediaType = "application/json",
							array = @ArraySchema(schema = @Schema(implementation = ColaboradorParticipacoesResponse.class)),
							examples = @ExampleObject(name = "ok", value = AtaDocs.LIST_200_RESPONSE)
					)
			)
	})
	@GetMapping("/atas")
	ResponseEntity<List<ColaboradorParticipacoesResponse>> listarParticipacoes(
			@Parameter(description = "Filtro por nome do workshop (contem, case-insensitive)", example = "spring")
			@RequestParam(required = false) String workshopNome,

			@Parameter(description = "Formato yyyy-MM-dd", example = "2026-01-20")
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
	);
}
