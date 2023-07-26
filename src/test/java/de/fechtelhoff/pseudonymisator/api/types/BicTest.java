package de.fechtelhoff.pseudonymisator.api.types;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import net.datafaker.Faker;

class BicTest {

	private static Stream<Arguments> getTestData() {
		return Stream.of(
			Arguments.arguments(true, "MARKDEFF"),
			Arguments.arguments(true, "MARKDEFFXXX"),
			Arguments.arguments(false, "ABC"),
			Arguments.arguments(false, "ABCDEF"),
			Arguments.arguments(false, "ABCDEFHIJ"),
			Arguments.arguments(false, "ABCDEFHIJKLM"),
			Arguments.arguments(false, ""), // Empty String
			Arguments.arguments(false, null)
		);
	}

	@ParameterizedTest(name = "expectedResult={0}, bicString={1}")
	@MethodSource("getTestData")
	void isValidTest(final boolean expectedResult, final String bicString) {
		Assertions.assertEquals(expectedResult, Bic.isValid(bicString));
	}

	@ParameterizedTest(name = "expectedResult={0}, bicString={1}")
	@MethodSource("getTestData")
	void constructorTest(final boolean expectedResult, final String bicString) {
		if (expectedResult) {
			Assertions.assertDoesNotThrow(() -> new Bic(bicString));
		} else {
			Assertions.assertThrows(IllegalArgumentException.class, () -> new Bic(bicString));
		}
	}

	@Test
	void equalsTest() {
		final Faker faker = new Faker();
		final Bic firstBic = new Bic(faker.finance().bic());
		final Bic secondBic = new Bic(faker.finance().bic());

		Assertions.assertDoesNotThrow(() -> firstBic.equals(secondBic));
	}

	@Test
	void hashCodeTest() {
		final Bic bic = new Bic(new Faker().finance().bic());

		Assertions.assertDoesNotThrow(bic::hashCode);
	}

	@Test
	void compareToTest() {
		final Faker faker = new Faker();
		final Bic firstBic = new Bic(faker.finance().bic());
		final Bic secondBic = new Bic(faker.finance().bic());

		Assertions.assertDoesNotThrow(() -> firstBic.compareTo(secondBic));
	}
}
