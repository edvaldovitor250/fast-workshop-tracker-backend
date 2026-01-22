package br.com.fast.workshoptracker.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testes de integração para endpoints de segurança.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void devePermitirAcessoAoSwaggerSemAutenticacao() throws Exception {
		mockMvc.perform(get("/swagger-ui.html"))
				.andExpect(status().isOk());
	}

	@Test
	void devePermitirAcessoAoHealthCheckSemAutenticacao() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}

	@Test
	void deveNegarAcessoAEndpointsProtegidosSemAutenticacao() throws Exception {
		mockMvc.perform(get("/api/v1/workshops"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(roles = "READER")
	void devePermitirAcessoComRoleREADER() throws Exception {
		mockMvc.perform(get("/api/v1/atas/participacoes"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "CREATOR")
	void devePermitirCriacaoComRoleCREATOR() throws Exception {
		// Este teste seria expandido com um POST real
		// Por enquanto valida apenas que o role permite acesso
		mockMvc.perform(get("/api/v1/atas/participacoes"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "USER")
	void deveNegarAcessoComRoleInvalido() throws Exception {
		mockMvc.perform(get("/api/v1/atas/participacoes"))
				.andExpect(status().isForbidden());
	}

	@Test
	void deveRetornarMetricasPrometheusNoActuator() throws Exception {
		mockMvc.perform(get("/actuator/prometheus"))
				.andExpect(status().isOk());
	}

	@Test
	void deveRetornarHealthDetalhado() throws Exception {
		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").exists())
				.andExpect(jsonPath("$.components").exists());
	}
}
