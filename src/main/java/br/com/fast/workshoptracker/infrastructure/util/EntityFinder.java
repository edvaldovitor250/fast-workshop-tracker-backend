package br.com.fast.workshoptracker.infrastructure.util;

import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Utilitário para busca e validação de entidades.
 */
public final class EntityFinder {

	private EntityFinder() {
		throw new UnsupportedOperationException("Utility class");
	}

	
	public static <T, ID> void validateAllFound(
			List<ID> requestedIds,
			List<T> foundEntities,
			Function<T, ID> idExtractor,
			String entityName,
			String fieldName
	) {
		if (requestedIds == null || requestedIds.isEmpty()) {
			return;
		}

		if (foundEntities.size() != requestedIds.size()) {
			Set<ID> foundIds = new HashSet<>(foundEntities.size());
			for (T entity : foundEntities) {
				foundIds.add(idExtractor.apply(entity));
			}
			List<ID> missing = CollectionValidator.findMissingIds(requestedIds, foundIds);
			throw Exceptions.notFound(
					entityName + " não encontrados: ids=" + missing,
					ExceptionUtils.context(fieldName, missing)
			);
		}
	}
}
