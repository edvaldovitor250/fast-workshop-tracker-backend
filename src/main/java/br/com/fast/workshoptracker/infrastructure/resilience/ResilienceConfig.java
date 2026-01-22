package br.com.fast.workshoptracker.infrastructure.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuração de padrões de resiliência usando Resilience4j.
 */
@Configuration
public class ResilienceConfig {

	/**
	 * Circuit Breaker para operações de banco de dados.
	 */
	@Bean
	 CircuitBreaker databaseCircuitBreaker(CircuitBreakerRegistry registry) {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
				.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
				.slidingWindowSize(10)
				.minimumNumberOfCalls(5)
				.failureRateThreshold(50.0f)
				.waitDurationInOpenState(Duration.ofSeconds(30))
				.permittedNumberOfCallsInHalfOpenState(3)
				.automaticTransitionFromOpenToHalfOpenEnabled(true)
				.recordExceptions(Exception.class)
				.build();

		return registry.circuitBreaker("database", config);
	}

	/**
	 * Retry para operações transientes.
	 */
	@Bean
	 Retry databaseRetry(RetryRegistry registry) {
		RetryConfig config = RetryConfig.custom()
				.maxAttempts(3)
				.waitDuration(Duration.ofMillis(500))
				.retryExceptions(
						org.springframework.dao.QueryTimeoutException.class,
						org.springframework.dao.TransientDataAccessResourceException.class
				)
				.build();

		return registry.retry("database", config);
	}

	/**
	 * Rate Limiter para APIs públicas.
	 */
	@Bean
	 RateLimiter apiRateLimiter(RateLimiterRegistry registry) {
		RateLimiterConfig config = RateLimiterConfig.custom()
				.limitForPeriod(100) 
				.limitRefreshPeriod(Duration.ofMinutes(1)) 
				.timeoutDuration(Duration.ofSeconds(5))
				.build();

		return registry.rateLimiter("api", config);
	}

	/**
	 * Circuit Breaker para operações externas (caso necessário no futuro).
	 */
	@Bean
	 CircuitBreaker externalServiceCircuitBreaker(CircuitBreakerRegistry registry) {
		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
				.slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
				.slidingWindowSize(60) 
				.minimumNumberOfCalls(10)
				.failureRateThreshold(60.0f)
				.waitDurationInOpenState(Duration.ofMinutes(1))
				.permittedNumberOfCallsInHalfOpenState(5)
				.automaticTransitionFromOpenToHalfOpenEnabled(true)
				.build();

		return registry.circuitBreaker("externalService", config);
	}

	/**
	 * Retry para operações externas com backoff exponencial.
	 */
	@Bean
	 Retry externalServiceRetry(RetryRegistry registry) {
		RetryConfig config = RetryConfig.custom()
				.maxAttempts(3)
				.intervalFunction(IntervalFunction.ofExponentialBackoff(
						Duration.ofSeconds(1), 
						2.0 
				))
				.build();

		return registry.retry("externalService", config);
	}
}
