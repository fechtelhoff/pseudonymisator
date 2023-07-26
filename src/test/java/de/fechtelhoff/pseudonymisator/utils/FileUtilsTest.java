package de.fechtelhoff.pseudonymisator.utils;

import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class FileUtilsTest {

	private static Stream<Arguments> getTestData() {
		return Stream.of(
			Arguments.arguments(false, "file"),
			Arguments.arguments(true, "file.abc"),
			Arguments.arguments(false, ""), // Empty String
			Arguments.arguments(false, null)
		);
	}

	@ParameterizedTest(name = "expectedResult={0}, fileName={1}")
	@MethodSource("getTestData")
	void getFileExtensionTest(final boolean expectedResult, final String fileName) {
		final Path path = Objects.nonNull(fileName) ? Path.of(fileName) : null;
		final String fileExtension = FileUtils.getFileExtension(path);

		if (expectedResult) {
			Assertions.assertNotNull(fileExtension);
		} else {
			Assertions.assertNull(fileExtension);
		}
	}

	@ParameterizedTest(name = "expectedResult={0}, fileName={1}")
	@MethodSource("getTestData")
	void hasFileExtensionTest(final boolean expectedResult, final String fileName) {
		final Path path = Objects.nonNull(fileName) ? Path.of(fileName) : null;
		Assertions.assertEquals(expectedResult, FileUtils.hasFileExtension(path));
	}

	@ParameterizedTest
	@CsvSource({
		"true, file.abc, abc",
		"true, file.p12, p12",
		"false, file.xyz, abc",
		"false, file, abc",
		"false, '', abc",
		"false, , abc",
	})
	void isFileExtensionTest(final boolean expectedResult, final String fileName, final String fileExtension) {
		final Path path = Objects.nonNull(fileName) ? Path.of(fileName) : null;
		Assertions.assertEquals(expectedResult, FileUtils.isFileExtension(path, fileExtension));
	}
}
