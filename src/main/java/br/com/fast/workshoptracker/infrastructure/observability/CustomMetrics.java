package br.com.fast.workshoptracker.infrastructure.observability;

import br.com.fast.workshoptracker.application.port.output.MetricsPort;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Métricas customizadas para observabilidade da aplicação.
 */
@Component
@RequiredArgsConstructor
public class CustomMetrics implements MetricsPort {

	private final MeterRegistry meterRegistry;

	/**
	 * Incrementa contador de atas criadas.
	 */
	@Override
	public void incrementAtasCriadas() {
		Counter.builder("atas.criadas.total")
				.description("Total de atas criadas no sistema")
				.tag("tipo", "criacao")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Incrementa contador de colaboradores adicionados.
	 */
	public void incrementColaboradoresAdicionados() {
		Counter.builder("atas.colaboradores.adicionados.total")
				.description("Total de colaboradores adicionados em atas")
				.tag("tipo", "adicao")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Incrementa contador de colaboradores removidos.
	 */
	public void incrementColaboradoresRemovidos() {
		Counter.builder("atas.colaboradores.removidos.total")
				.description("Total de colaboradores removidos de atas")
				.tag("tipo", "remocao")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Incrementa contador de workshops criados.
	 */
	public void incrementWorkshopsCriados() {
		Counter.builder("workshops.criados.total")
				.description("Total de workshops criados")
				.tag("tipo", "criacao")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Incrementa contador de autenticações bem-sucedidas.
	 */
	public void incrementAuthSuccessful() {
		Counter.builder("auth.login.total")
				.description("Total de logins bem-sucedidos")
				.tag("status", "success")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Incrementa contador de autenticações falhadas.
	 */
	public void incrementAuthFailed() {
		Counter.builder("auth.login.total")
				.description("Total de tentativas de login falhadas")
				.tag("status", "failed")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Registra tempo de execução de query de participações.
	 */
	public <T> T timeParticipacoesQuery(Supplier<T> supplier) {
		Timer timer = Timer.builder("queries.participacoes.duration")
				.description("Tempo de execução da query de participações")
				.tag("query", "participacoes")
				.register(meterRegistry);

		return timer.record(supplier);
	}

	/**
	 * Registra contador de cache hits.
	 */
	public void recordCacheHit(String cacheName) {
		Counter.builder("cache.hits.total")
				.description("Total de cache hits")
				.tag("cache", cacheName)
				.tag("result", "hit")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Registra contador de cache misses.
	 */
	public void recordCacheMiss(String cacheName) {
		Counter.builder("cache.misses.total")
				.description("Total de cache misses")
				.tag("cache", cacheName)
				.tag("result", "miss")
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Registra duração de operações de negócio.
	 */
	@Override
	public <T> T timeBusinessOperation(String operationName, Supplier<T> supplier) {
		Timer timer = Timer.builder("business.operation.duration")
				.description("Tempo de execução de operações de negócio")
				.tag("operation", operationName)
				.register(meterRegistry);

		return timer.record(supplier);
	}

	/**
	 * Registra contador de erros por tipo.
	 */
	public void incrementErrorCounter(String errorType, String errorCode) {
		Counter.builder("errors.total")
				.description("Total de erros por tipo")
				.tag("type", errorType)
				.tag("code", errorCode)
				.register(meterRegistry)
				.increment();
	}

	/**
	 * Registra tamanho médio de listas retornadas.
	 */
	public void recordListSize(String listName, int size) {
		meterRegistry.summary("lists.size", "list", listName)
				.record(size);
	}
}
