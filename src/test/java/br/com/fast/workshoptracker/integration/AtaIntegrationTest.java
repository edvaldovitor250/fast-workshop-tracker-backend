package br.com.fast.workshoptracker.integration;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.port.input.CriarAtaUseCase;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.domain.exception.domain.BusinessException;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.AtaRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.WorkshopRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Teste de integração completo para o fluxo de criação de atas.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AtaIntegrationTest extends AbstractIntegrationTest {

	@Autowired
	private CriarAtaUseCase criarAtaUseCase;

	@Autowired
	private WorkshopRepository workshopRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private AtaRepository ataRepository;

	private Workshop workshop;
	private Colaborador colaborador1;
	private Colaborador colaborador2;

	@BeforeEach
	void setUp() {
		ataRepository.deleteAll();
		colaboradorRepository.deleteAll();
		workshopRepository.deleteAll();

		workshop = workshopRepository.save(new Workshop("Java Avançado", LocalDate.of(2026, 1, 21)));
		colaborador1 = colaboradorRepository.save(new Colaborador("Maria Silva"));
		colaborador2 = colaboradorRepository.save(new Colaborador("João Santos"));
	}

	@Test
	void deveCriarAtaComSucessoComColaboradores() {
		// Given
		var command = new CriarAtaCommand(workshop.getId(), List.of(colaborador1.getId(), colaborador2.getId()));

		// When
		var result = criarAtaUseCase.execute(command);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.id()).isNotNull();
		assertThat(result.workshop().nome()).isEqualTo("Java Avançado");
		assertThat(result.colaboradores()).hasSize(2);
		assertThat(result.colaboradores()).extracting("nome")
				.containsExactlyInAnyOrder("Maria Silva", "João Santos");

		// Verifica persistência
		var ataDb = ataRepository.findById(result.id());
		assertThat(ataDb).isPresent();
		assertThat(ataDb.get().getColaboradores()).hasSize(2);
	}

	@Test
	void deveCriarAtaSemColaboradores() {
		// Given
		var command = new CriarAtaCommand(workshop.getId(), List.of());

		// When
		var result = criarAtaUseCase.execute(command);

		// Then
		assertThat(result).isNotNull();
		assertThat(result.colaboradores()).isEmpty();
	}

	@Test
	void naoDeveCriarAtaDuplicadaParaMesmoWorkshop() {
		// Given
		var command = new CriarAtaCommand(workshop.getId(), List.of());
		criarAtaUseCase.execute(command);

		// When/Then
		assertThatThrownBy(() -> criarAtaUseCase.execute(command))
				.isInstanceOf(BusinessException.class)
				.hasMessageContaining("Já existe ata para o workshop");
	}

	@Test
	void naoDeveCriarAtaComWorkshopInexistente() {
		// Given
		var command = new CriarAtaCommand(999L, List.of());

		// When/Then
		assertThatThrownBy(() -> criarAtaUseCase.execute(command))
				.isInstanceOf(BusinessException.class)
				.hasMessageContaining("Workshop não encontrado");
	}

	@Test
	void naoDeveCriarAtaComColaboradorInexistente() {
		// Given
		var command = new CriarAtaCommand(workshop.getId(), List.of(999L));

		// When/Then
		assertThatThrownBy(() -> criarAtaUseCase.execute(command))
				.isInstanceOf(BusinessException.class)
				.hasMessageContaining("Colaboradores não encontrados");
	}

	@Test
	void deveValidarDadosDoWorkshopNaAta() {
		// Given
		var command = new CriarAtaCommand(workshop.getId(), List.of(colaborador1.getId()));

		// When
		var result = criarAtaUseCase.execute(command);

		// Then
		assertThat(result.workshop().id()).isEqualTo(workshop.getId());
		assertThat(result.workshop().nome()).isEqualTo("Java Avançado");
		assertThat(result.workshop().dataRealizacao()).isEqualTo(LocalDate.of(2026, 1, 21));
	}
}
