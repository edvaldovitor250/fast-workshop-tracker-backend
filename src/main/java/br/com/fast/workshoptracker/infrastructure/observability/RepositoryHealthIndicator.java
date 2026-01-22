package br.com.fast.workshoptracker.infrastructure.observability;

import br.com.fast.workshoptracker.application.port.output.AtaRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.ColaboradorRepositoryPort;
import br.com.fast.workshoptracker.application.port.output.WorkshopRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Health check customizado para verificar integridade dos repositórios.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RepositoryHealthIndicator implements HealthIndicator {

	private final WorkshopRepositoryPort workshopRepository;
	private final ColaboradorRepositoryPort colaboradorRepository;
	private final AtaRepositoryPort ataRepository;

	@Override
	public Health health() {
		try {
			long workshopsCount = workshopRepository.count();
			long colaboradoresCount = colaboradorRepository.count();
			long atasCount = ataRepository.count();

			return Health.up()
					.withDetail("workshops", workshopsCount)
					.withDetail("colaboradores", colaboradoresCount)
					.withDetail("atas", atasCount)
					.withDetail("status", "All repositories accessible")
					.build();

		} catch (Exception ex) {
			log.error("Repository health check failed", ex);
			return Health.down()
					.withDetail("error", ex.getClass().getName())
					.withDetail("message", ex.getMessage())
					.build();
		}
	}
}
