package br.com.fast.workshoptracker.infrastructure.observability;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Health check customizado para verificar conectividade com o banco de dados.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseHealthIndicator implements HealthIndicator {

	private final DataSource dataSource;

	@Override
	public Health health() {
		try (Connection connection = dataSource.getConnection();
		     Statement statement = connection.createStatement();
		     ResultSet resultSet = statement.executeQuery("SELECT 1")) {

			if (resultSet.next() && resultSet.getInt(1) == 1) {
				return Health.up()
						.withDetail("database", "MySQL")
						.withDetail("status", "Connected")
						.withDetail("validationQuery", "SELECT 1")
						.build();
			}

			return Health.down()
					.withDetail("database", "MySQL")
					.withDetail("reason", "Validation query failed")
					.build();

		} catch (Exception ex) {
			log.error("Database health check failed", ex);
			return Health.down()
					.withDetail("database", "MySQL")
					.withDetail("error", ex.getClass().getName())
					.withDetail("message", ex.getMessage())
					.build();
		}
	}
}
