package br.com.fast.workshoptracker.infrastructure.cache;

import br.com.fast.workshoptracker.infrastructure.observability.CustomMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.stats.CacheStats;

/**
 * Gerenciador de métricas e estatísticas de cache.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CacheMetricsManager {

	private final CacheManager cacheManager;
	private final CustomMetrics customMetrics;

	/**
	 * Registra hit no cache e atualiza métricas.
	 */
	public void recordHit(String cacheName) {
		customMetrics.recordCacheHit(cacheName);
		log.debug("Cache hit: {}", cacheName);
	}

	/**
	 * Registra miss no cache e atualiza métricas.
	 */
	public void recordMiss(String cacheName) {
		customMetrics.recordCacheMiss(cacheName);
		log.debug("Cache miss: {}", cacheName);
	}

	/**
	 * Limpa cache específico.
	 */
	public void evictCache(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			cache.clear();
			log.info("Cache limpo: {}", cacheName);
		}
	}

	/**
	 * Limpa todos os caches.
	 */
	public void evictAllCaches() {
		cacheManager.getCacheNames().forEach(cacheName -> {
			Cache cache = cacheManager.getCache(cacheName);
			if (cache != null) {
				cache.clear();
			}
		});
		log.info("Todos os caches foram limpos");
	}

	/**
	 * Log de estatísticas de cache a cada 5 minutos.
	 */
	@Scheduled(fixedRate = 300000) // 5 minutos
	public void logCacheStatistics() {
		cacheManager.getCacheNames().forEach(cacheName -> {
			Cache cache = cacheManager.getCache(cacheName);
			if (cache != null) {
				Object nativeCache = cache.getNativeCache();
				if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> caffeineCache) {
					CacheStats stats = caffeineCache.stats();
					log.info("Cache Stats [{}] - Hits: {}, Misses: {}, HitRate: {:.2f}%, Size: {}",
							cacheName,
							stats.hitCount(),
							stats.missCount(),
							stats.hitRate() * 100,
							caffeineCache.estimatedSize());
				}
			}
		});
	}

	/**
	 * Retorna estatísticas de um cache específico.
	 */
	public String getCacheStats(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			Object nativeCache = cache.getNativeCache();
			if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> caffeineCache) {
				CacheStats stats = caffeineCache.stats();
				return String.format("Cache: %s, Hits: %d, Misses: %d, HitRate: %.2f%%, Size: %d",
						cacheName,
						stats.hitCount(),
						stats.missCount(),
						stats.hitRate() * 100,
						caffeineCache.estimatedSize());
			}
		}
		return "Cache not found: " + cacheName;
	}
}
