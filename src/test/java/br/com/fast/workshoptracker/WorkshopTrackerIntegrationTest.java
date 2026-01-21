package br.com.fast.workshoptracker;

import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaAddColaboradorRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AtaCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthLoginRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.AuthRegisterRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.ColaboradorCreateRequest;
import br.com.fast.workshoptracker.presentation.rest.dto.request.WorkshopCreateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WorkshopTrackerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void fluxoPrincipal_endpointsFuncionam() throws Exception {
		register();
		String token = login();
		long workshopId = createWorkshop(token);
		long colaborador1 = createColaborador(token, "Ana Silva");
		long colaborador2 = createColaborador(token, "Bruno Souza");

		long ataId = createAta(token, workshopId, List.of(colaborador1));

		mockMvc.perform(put("/api/workshops/{workshopId}/atas/{ataId}", workshopId, ataId)
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new AtaAddColaboradorRequest(colaborador2))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(ataId))
				.andExpect(jsonPath("$.colaboradores.length()").value(2));

		mockMvc.perform(delete("/api/atas/{ataId}/colaboradores/{colaboradorId}", ataId, colaborador1)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/atas")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nome").value("Bruno Souza"))
				.andExpect(jsonPath("$[0].workshops.length()").value(1));

		mockMvc.perform(get("/api/atas")
						.queryParam("workshopNome", "spring")
						.queryParam("data", "2026-01-20")
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].nome").value("Bruno Souza"))
				.andExpect(jsonPath("$[0].workshops[0].nome").value("Workshop Spring"));
	}

	private long createWorkshop(String token) throws Exception {
		var req = new WorkshopCreateRequest("Workshop Spring", LocalDate.of(2026, 1, 20), "...");
		String json = mockMvc.perform(post("/api/workshops")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		JsonNode node = objectMapper.readTree(json);
		return node.get("id").asLong();
	}

	private long createColaborador(String token, String nome) throws Exception {
		var req = new ColaboradorCreateRequest(nome);
		String json = mockMvc.perform(post("/api/colaboradores")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		JsonNode node = objectMapper.readTree(json);
		return node.get("id").asLong();
	}

	private long createAta(String token, long workshopId, List<Long> colaboradoresIds) throws Exception {
		var req = new AtaCreateRequest(workshopId, colaboradoresIds);
		String json = mockMvc.perform(post("/api/atas")
						.header("Authorization", "Bearer " + token)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();
		JsonNode node = objectMapper.readTree(json);
		return node.get("id").asLong();
	}

	private String token() throws Exception {
		return login();
	}

	private void register() throws Exception {
		var req = new AuthRegisterRequest("Test User", "test@local", "test123");
		mockMvc.perform(post("/api/auth/register")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isCreated());
	}

	private String login() throws Exception {
		var req = new AuthLoginRequest("test@local", "test123");
		String json = mockMvc.perform(post("/api/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req)))
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();
		return objectMapper.readTree(json).get("accessToken").asText();
	}
}
