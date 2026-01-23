package br.com.fast.workshoptracker.infrastructure.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Configuração otimizada do pool de conexões para alta performance.
 */
@Configuration
public class DatabaseConfig {

	private final String jdbcUrl;
	private final String username;
	private final String password;

	public DatabaseConfig(
			@Value("${spring.datasource.url}") String jdbcUrl,
			@Value("${spring.datasource.username}") String username,
			@Value("${spring.datasource.password}") String password
	) {
		this.jdbcUrl = jdbcUrl;
		this.username = username;
		this.password = password;
	}

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(username);
		config.setPassword(password);

		// Configurações de performance
		config.setMaximumPoolSize(20); // Pool máximo de 20 conexões
		config.setMinimumIdle(5); // Mínimo de 5 conexões idle
		config.setConnectionTimeout(30000); // 30 segundos timeout
		config.setIdleTimeout(600000); // 10 minutos idle timeout
		config.setMaxLifetime(1800000); // 30 minutos max lifetime
		config.setLeakDetectionThreshold(60000); // Detecta leaks após 1 minuto

		// Propriedades MySQL otimizadas
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		config.addDataSourceProperty("useLocalSessionState", "true");
		config.addDataSourceProperty("rewriteBatchedStatements", "true");
		config.addDataSourceProperty("cacheResultSetMetadata", "true");
		config.addDataSourceProperty("cacheServerConfiguration", "true");
		config.addDataSourceProperty("elideSetAutoCommits", "true");
		config.addDataSourceProperty("maintainTimeStats", "false");

		return new HikariDataSource(config);
	}
}
