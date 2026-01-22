package br.com.fast.workshoptracker.performance;

import br.com.fast.workshoptracker.application.dto.command.CriarAtaCommand;
import br.com.fast.workshoptracker.application.port.input.CriarAtaUseCase;
import br.com.fast.workshoptracker.domain.entity.Colaborador;
import br.com.fast.workshoptracker.domain.entity.Workshop;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.AtaRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.ColaboradorRepository;
import br.com.fast.workshoptracker.infrastructure.persistence.repository.WorkshopRepository;
import br.com.fast.workshoptracker.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

/**
 * Testes de performance para validar comportamento sob carga.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PerformanceTest extends AbstractIntegrationTest {

	@Autowired
	private CriarAtaUseCase criarAtaUseCase;

	@Autowired
	private WorkshopRepository workshopRepository;

	@Autowired
	private ColaboradorRepository colaboradorRepository;

	@Autowired
	private AtaRepository ataRepository;

	@BeforeEach
	void setUp() {
		ataRepository.deleteAll();
		colaboradorRepository.deleteAll();
		workshopRepository.deleteAll();
	}

	@Test
	void deveCriarAtaEmTempoAceitavel() {
		// Given
		Workshop workshop = workshopRepository.save(new Workshop("Performance Test", LocalDate.now()));
		List<Long> colaboradoresIds = criarColaboradoresEmLote(10);

		// When
		long startTime = System.currentTimeMillis();
		var result = criarAtaUseCase.execute(new CriarAtaCommand(workshop.getId(), colaboradoresIds));
		long endTime = System.currentTimeMillis();

		// Then
		long duration = endTime - startTime;
		assertThat(result).isNotNull();
		assertThat(duration).isLessThan(1000); // Deve executar em menos de 1 segundo
		System.out.println("Tempo de criação de ata com 10 colaboradores: " + duration + "ms");
	}

	@Test
	void deveCriarMultiplasAtasEmSequencia() {
		// Given
		List<Workshop> workshops = criarWorkshopsEmLote(20);
		List<Long> colaboradoresIds = criarColaboradoresEmLote(5);

		// When
		long startTime = System.currentTimeMillis();
		workshops.forEach(workshop -> {
			criarAtaUseCase.execute(new CriarAtaCommand(workshop.getId(), colaboradoresIds));
		});
		long endTime = System.currentTimeMillis();

		// Then
		long duration = endTime - startTime;
		long avgDuration = duration / workshops.size();
		assertThat(ataRepository.count()).isEqualTo(workshops.size());
		assertThat(avgDuration).isLessThan(500); // Média de 500ms por ata
		System.out.println("Tempo médio de criação de ata: " + avgDuration + "ms");
		System.out.println("Tempo total para " + workshops.size() + " atas: " + duration + "ms");
	}

	@Test
	void deveManterPerformanceComMuitosColaboradores() {
		// Given
		Workshop workshop = workshopRepository.save(new Workshop("Large Workshop", LocalDate.now()));
		List<Long> colaboradoresIds = criarColaboradoresEmLote(100);

		// When
		long startTime = System.currentTimeMillis();
		var result = criarAtaUseCase.execute(new CriarAtaCommand(workshop.getId(), colaboradoresIds));
		long endTime = System.currentTimeMillis();

		// Then
		long duration = endTime - startTime;
		assertThat(result.colaboradores()).hasSize(100);
		assertThat(duration).isLessThan(3000); // Deve executar em menos de 3 segundos
		System.out.println("Tempo de criação de ata com 100 colaboradores: " + duration + "ms");
	}

	@Test
	void deveResponderRapidamenteABuscasMassivas() {
		// Given: Criar massa de dados
		List<Workshop> workshops = criarWorkshopsEmLote(50);
		List<Long> colaboradoresIds = criarColaboradoresEmLote(10);

		workshops.forEach(workshop -> {
			criarAtaUseCase.execute(new CriarAtaCommand(workshop.getId(), colaboradoresIds));
		});

		// When: Buscar todas as atas
		long startTime = System.currentTimeMillis();
		long count = ataRepository.count();
		long endTime = System.currentTimeMillis();

		// Then
		long duration = endTime - startTime;
		assertThat(count).isEqualTo(50);
		assertThat(duration).isLessThan(500); // Busca deve ser rápida
		System.out.println("Tempo de contagem de 50 atas: " + duration + "ms");
	}

	@Test
	void deveExecutarTransacoesComTimeout() {
		// Given
		Workshop workshop = workshopRepository.save(new Workshop("Timeout Test", LocalDate.now()));
		List<Long> colaboradoresIds = criarColaboradoresEmLote(5);

		// When/Then: Valida que não há timeout na operação
		await()
				.atMost(2, TimeUnit.SECONDS)
				.untilAsserted(() -> {
					var result = criarAtaUseCase.execute(new CriarAtaCommand(workshop.getId(), colaboradoresIds));
					assertThat(result).isNotNull();
				});
	}

	private List<Long> criarColaboradoresEmLote(int quantidade) {
		List<Long> ids = new ArrayList<>();
		for (int i = 0; i < quantidade; i++) {
			Colaborador colaborador = colaboradorRepository.save(
					new Colaborador("Colaborador " + i)
			);
			ids.add(colaborador.getId());
		}
		return ids;
	}

	private List<Workshop> criarWorkshopsEmLote(int quantidade) {
		List<Workshop> workshops = new ArrayList<>();
		for (int i = 0; i < quantidade; i++) {
			Workshop workshop = workshopRepository.save(
					new Workshop("Workshop " + i, LocalDate.now().plusDays(i))
			);
			workshops.add(workshop);
		}
		return workshops;
	}
}
