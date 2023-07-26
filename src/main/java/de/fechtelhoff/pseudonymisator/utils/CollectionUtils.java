package de.fechtelhoff.pseudonymisator.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {

	private CollectionUtils() {
		// Intentionally blank
	}

	public static <T> Set<T> getKeys(final Map<T, ?> map) {
		return map.keySet();
	}

	public static <T> Set<T> getValues(final Map<?, T> map) {
		return new HashSet<>(map.values());
	}
}
