package br.com.fast.workshoptracker.infrastructure.util;

import br.com.fast.workshoptracker.domain.exception.Exceptions;
import br.com.fast.workshoptracker.domain.exception.util.ExceptionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utilitário para validação de coleções.
 */
public final class CollectionValidator {

	private CollectionValidator() {
		throw new UnsupportedOperationException("Utility class");
	}

	public static void validateUniqueIds(List<Long> ids, String fieldName) {
		if (ids == null || ids.isEmpty()) {
			return;
		}
		Set<Long> seen = new HashSet<>(ids.size());
		for (Long id : ids) {
			if (id == null) {
				throw Exceptions.badRequest(
						fieldName + " não pode conter null",
						ExceptionUtils.context(fieldName, ids)
				);
			}
			if (!seen.add(id)) {
				throw Exceptions.badRequest(
						fieldName + " contém ids duplicados",
						ExceptionUtils.context(fieldName, ids)
				);
			}
		}
	}

	
	public static <T> List<T> findMissingIds(List<T> requestedIds, Set<T> foundIds) {
		if (requestedIds == null || requestedIds.isEmpty()) {
			return List.of();
		}
		return requestedIds.stream()
				.filter(id -> !foundIds.contains(id))
				.toList();
	}
}
