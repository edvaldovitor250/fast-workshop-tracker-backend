package br.com.fast.workshoptracker.infrastructure.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Wrapper para aplicar patterns de resiliência em operações.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ResilienceWrapper {

	private final CircuitBreaker databaseCircuitBreaker;
	private final Retry databaseRetry;

	/**
	 * Executa operação com Circuit Breaker e Retry para banco de dados.
	 */
	public <T> T executeWithDatabaseResilience(Supplier<T> operation) {
		// Primeiro aplica Retry, depois Circuit Breaker
		Supplier<T> decoratedSupplier = Retry.decorateSupplier(databaseRetry, operation);
		decoratedSupplier = CircuitBreaker.decorateSupplier(databaseCircuitBreaker, decoratedSupplier);

		try {
			return decoratedSupplier.get();
		} catch (Exception ex) {
			log.error("Database operation failed after resilience patterns applied", ex);
			throw ex;
		}
	}

	/**
	 * Executa operação apenas com Circuit Breaker.
	 */
	public <T> T executeWithCircuitBreaker(Supplier<T> operation) {
		Supplier<T> decoratedSupplier = CircuitBreaker.decorateSupplier(databaseCircuitBreaker, operation);
		return decoratedSupplier.get();
	}

	/**
	 * Executa operação apenas com Retry.
	 */
	public <T> T executeWithRetry(Supplier<T> operation) {
		Supplier<T> decoratedSupplier = Retry.decorateSupplier(databaseRetry, operation);
		return decoratedSupplier.get();
	}

	/**
	 * Retorna estado atual do Circuit Breaker.
	 */
	public String getCircuitBreakerState() {
		return databaseCircuitBreaker.getState().name();
	}

	/**
	 * Verifica se Circuit Breaker permite chamadas.
	 */
	public boolean isCircuitBreakerClosed() {
		return databaseCircuitBreaker.getState() == CircuitBreaker.State.CLOSED;
	}
}
