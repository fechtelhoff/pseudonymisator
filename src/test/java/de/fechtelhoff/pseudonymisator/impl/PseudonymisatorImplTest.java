package de.fechtelhoff.pseudonymisator.impl;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import de.fechtelhoff.pseudonymisator.api.ApplicationException;
import de.fechtelhoff.pseudonymisator.api.KeyManagement;
import de.fechtelhoff.pseudonymisator.api.Pseudonymisator;
import de.fechtelhoff.pseudonymisator.api.types.AbstractPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.AbstractType;
import de.fechtelhoff.pseudonymisator.api.types.Bic;
import de.fechtelhoff.pseudonymisator.api.types.BicPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.Iban;
import de.fechtelhoff.pseudonymisator.api.types.IbanPseudonym;
import net.datafaker.Faker;

class PseudonymisatorImplTest {

	private static final int REPETITIONS = 10;
	private static Pseudonymisator PSEUDONYMISATOR;

	@BeforeAll
	static void beforeAll() throws ApplicationException {
		final String password = "test";
		final KeyManagement keyManagement = new KeyManagementImpl();
		final KeyStore keyStore = keyManagement.createKeyStore(password);
		final X509Certificate certificate = keyManagement.createCertificate(keyStore);
		final PublicKey publicKey = certificate.getPublicKey();

		PSEUDONYMISATOR = new PseudonymisatorImpl(publicKey);
	}

	@RepeatedTest(REPETITIONS)
	void bicPseudonymisationTest() {
		final Bic bic = new Bic(new Faker().finance().bic());
		final BicPseudonym bicPseudonym = Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseBic(bic));
		System.out.println(bic + " -> " + bicPseudonym + " (" + bicPseudonym.toString().length() + ")");
	}

	@RepeatedTest(REPETITIONS)
	void ibanPseudonymisationTest() {
		final Iban iban = new Iban(new Faker().finance().iban("DE"));
		final IbanPseudonym ibanPseudonym = Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseIban(iban));
		System.out.println(iban + " -> " + ibanPseudonym + " (" + ibanPseudonym.toString().length() + ")");
	}

	@Test
	void bicCollectionPseudonymisationTest() {
		final Faker faker = new Faker();
		final List<Bic> inputList = Stream
			.generate(() -> faker.finance().bic())
			.map(Bic::new)
			.limit(REPETITIONS)
			.toList();
		final Map<Bic, BicPseudonym> outputMap = Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseBic(inputList));
		printMap(outputMap);
	}

	@Test
	void ibanCollectionPseudonymisationTest() {
		final Faker faker = new Faker();
		final List<Iban> inputList = Stream
			.generate(() -> faker.finance().iban("DE"))
			.map(Iban::new)
			.limit(REPETITIONS)
			.toList();
		final Map<Iban, IbanPseudonym> outputMap = Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseIban(inputList));
		printMap(outputMap);
	}

	private void printMap(Map<? extends AbstractType, ? extends AbstractPseudonym> map) {
		map.entrySet().forEach(System.out::println);
	}
}
