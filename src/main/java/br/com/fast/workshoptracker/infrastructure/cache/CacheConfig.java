package br.com.fast.workshoptracker.infrastructure.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuração de cache usando Caffeine para otimização de performance.
 */
@Configuration
@EnableCaching
public class CacheConfig {

	public static final String WORKSHOPS_CACHE = "workshops";
	public static final String COLABORADORES_CACHE = "colaboradores";
	public static final String ATAS_CACHE = "atas";
	public static final String PARTICIPACOES_CACHE = "participacoes";

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager(
				WORKSHOPS_CACHE,
				COLABORADORES_CACHE,
				ATAS_CACHE,
				PARTICIPACOES_CACHE
		);

		cacheManager.setCaffeine(caffeineCacheBuilder());
		return cacheManager;
	}

	private Caffeine<Object, Object> caffeineCacheBuilder() {
		return Caffeine.newBuilder()
				.maximumSize(1000) // Máximo de 1000 entradas por cache
				.expireAfterWrite(10, TimeUnit.MINUTES) // Expira após 10 minutos
				.expireAfterAccess(5, TimeUnit.MINUTES) // Expira se não acessado por 5 minutos
				.recordStats(); // Habilita estatísticas para monitoramento
	}

	/**
	 * Cache específico para workshops com TTL maior (dados mais estáveis).
	 */
	@Bean
	public Caffeine<Object, Object> workshopsCaffeine() {
		return Caffeine.newBuilder()
				.maximumSize(500)
				.expireAfterWrite(30, TimeUnit.MINUTES)
				.recordStats();
	}

	/**
	 * Cache específico para colaboradores com TTL maior.
	 */
	@Bean
	public Caffeine<Object, Object> colaboradoresCaffeine() {
		return Caffeine.newBuilder()
				.maximumSize(500)
				.expireAfterWrite(30, TimeUnit.MINUTES)
				.recordStats();
	}

	/**
	 * Cache específico para atas com TTL médio.
	 */
	@Bean
	public Caffeine<Object, Object> atasCaffeine() {
		return Caffeine.newBuilder()
				.maximumSize(300)
				.expireAfterWrite(15, TimeUnit.MINUTES)
				.expireAfterAccess(10, TimeUnit.MINUTES)
				.recordStats();
	}

	/**
	 * Cache específico para participações com TTL menor (dados mais voláteis).
	 */
	@Bean
	public Caffeine<Object, Object> participacoesCaffeine() {
		return Caffeine.newBuilder()
				.maximumSize(200)
				.expireAfterWrite(5, TimeUnit.MINUTES)
				.expireAfterAccess(3, TimeUnit.MINUTES)
				.recordStats();
	}
}
