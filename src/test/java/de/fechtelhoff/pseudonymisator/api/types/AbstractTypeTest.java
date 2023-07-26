package de.fechtelhoff.pseudonymisator.api.types;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AbstractTypeTest {

	@Test
	void constructorTest() {
		// @Formatter:off
		Assertions.assertDoesNotThrow(() -> new AbstractType() {});
		Assertions.assertDoesNotThrow(() -> new AbstractType("value") {});
		Assertions.assertDoesNotThrow(() -> new AbstractType("label", "value") {});
		// @Formatter:on
	}

	@Test
	void validateTest() {

		class TestAbstractType extends AbstractType {

			public static List<String> validate(final String value) {
				return validate("label", value, null);
			}
		}

		final var result = TestAbstractType.validate("value");
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(1, result.size());
		Assertions.assertEquals("Format darf nicht null sein", result.get(0));
	}
}
