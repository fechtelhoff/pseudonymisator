package de.fechtelhoff.pseudonymisator.api.types;

import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import net.datafaker.Faker;

class IbanTest {

	private static Stream<Arguments> getTestData() {
		return Stream.of(
			Arguments.arguments(true, "DE12345678901234567890"),
			Arguments.arguments(false, "DEE2345678901234567890"),
			Arguments.arguments(false, "DE1234567890123456789"),
			Arguments.arguments(false, "12345678901234567890DE"),
			Arguments.arguments(false, ""), // Empty String
			Arguments.arguments(false, null)
		);
	}

	@ParameterizedTest(name = "expectedResult={0}, ibanString={1}")
	@MethodSource("getTestData")
	void isValidTest(final boolean expectedResult, final String ibanString) {
		Assertions.assertEquals(expectedResult, Iban.isValid(ibanString));
	}

	@ParameterizedTest(name = "expectedResult={0}, ibanString={1}")
	@MethodSource("getTestData")
	void constructorTest(final boolean expectedResult, final String ibanString) {
		if (expectedResult) {
			Assertions.assertDoesNotThrow(() -> new Iban(ibanString));
		} else {
			Assertions.assertThrows(IllegalArgumentException.class, () -> new Iban(ibanString));
		}
	}

	@Test
	void smokeTest() {
		final Iban iban = Assertions.assertDoesNotThrow(() -> new Iban(new Faker().finance().iban("DE")));
		System.out.println("IBAN:            " + iban);
		System.out.println("IBAN (readable): " + iban.toReadableString());
		System.out.println("CountryCode:     " + iban.getCountryCode());
		System.out.println("Checksum:        " + iban.getChecksum());
		System.out.println("Bankleitzahl:    " + iban.getBankleitzahl());
		System.out.println("Kontonummer:     " + iban.getKontonummer());
	}

	@Test
	@SuppressWarnings({"SimplifiableAssertion", "java:S5785"}) // java:S5785 -> JUnit assertTrue/assertFalse should be simplified to the corresponding dedicated assertion
	void equalsTest() {
		final Faker faker = new Faker();
		final Iban firstIban = new Iban(faker.finance().iban("DE"));
		final Iban secondIban = new Iban(faker.finance().iban("DE"));
		final String that = "Other Object";

		Assertions.assertTrue(firstIban.equals(firstIban));
		Assertions.assertFalse(firstIban.equals(secondIban));
		Assertions.assertFalse(firstIban.equals(that));
	}

	@Test
	void hashCodeTest() {
		final Iban iban = new Iban(new Faker().finance().iban("DE"));

		Assertions.assertDoesNotThrow(iban::hashCode);
	}

	@Test
	void compareToTest() {
		final Faker faker = new Faker();
		final Iban firstIban = new Iban(faker.finance().iban("DE"));
		final Iban secondIban = new Iban(faker.finance().iban("DE"));

		Assertions.assertDoesNotThrow(() -> firstIban.compareTo(secondIban));
	}
}
