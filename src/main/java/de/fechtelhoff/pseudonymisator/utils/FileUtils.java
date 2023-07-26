package de.fechtelhoff.pseudonymisator.utils;

import java.nio.file.Path;
import java.util.Objects;

public final class FileUtils {

	private FileUtils() {
		// Intentionally blank
	}

	public static String getFileExtension(final Path path) {
		if (Objects.isNull(path)) {
			return null;
		}
		final String fileName = path.getFileName().toString();
		final int extensionIndex = fileName.lastIndexOf('.');
		if (extensionIndex > 0) {
			return fileName.substring(extensionIndex + 1);
		} else {
			return null;
		}
	}

	public static boolean hasFileExtension(final Path path) {
		return Objects.nonNull(getFileExtension(path));
	}

	public static boolean isFileExtension(final Path path, final String fileExtension) {
		if (Objects.isNull(fileExtension)) {
			return false;
		}
		return fileExtension.equalsIgnoreCase(getFileExtension(path));
	}
}
