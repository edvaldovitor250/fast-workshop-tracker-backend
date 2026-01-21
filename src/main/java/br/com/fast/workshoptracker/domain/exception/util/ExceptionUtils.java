package br.com.fast.workshoptracker.domain.exception.util;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ExceptionUtils {

	private ExceptionUtils() {}

	public static Map<String, Object> context(Object... keyValues) {
		if (keyValues == null || keyValues.length == 0) {
			return Map.of();
		}
		if (keyValues.length % 2 != 0) {
			throw new IllegalArgumentException("context requires an even number of arguments (key/value pairs)");
		}

		Map<String, Object> map = new LinkedHashMap<>();
		for (int i = 0; i < keyValues.length; i += 2) {
			Object key = keyValues[i];
			Object value = keyValues[i + 1];
			if (key == null || value == null) {
				continue;
			}
			map.put(String.valueOf(key), value);
		}
		return Map.copyOf(map);
	}

	public static Map<String, Object> mergeContext(Map<String, Object> base, Map<String, Object> extra) {
		if ((base == null || base.isEmpty()) && (extra == null || extra.isEmpty())) {
			return Map.of();
		}
		Map<String, Object> merged = new LinkedHashMap<>();
		if (base != null) {
			merged.putAll(base);
		}
		if (extra != null) {
			for (Map.Entry<String, Object> entry : extra.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					merged.put(entry.getKey(), entry.getValue());
				}
			}
		}
		return Map.copyOf(merged);
	}
}
