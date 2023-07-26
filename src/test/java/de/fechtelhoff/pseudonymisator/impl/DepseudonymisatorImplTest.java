package de.fechtelhoff.pseudonymisator.impl;

import java.security.KeyStore;
import java.security.PrivateKey;
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
import de.fechtelhoff.pseudonymisator.api.Depseudonymisator;
import de.fechtelhoff.pseudonymisator.api.KeyManagement;
import de.fechtelhoff.pseudonymisator.api.Pseudonymisator;
import de.fechtelhoff.pseudonymisator.api.types.AbstractPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.AbstractType;
import de.fechtelhoff.pseudonymisator.api.types.Bic;
import de.fechtelhoff.pseudonymisator.api.types.BicPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.Iban;
import de.fechtelhoff.pseudonymisator.api.types.IbanPseudonym;
import net.datafaker.Faker;

class DepseudonymisatorImplTest {

	private static final int REPETITIONS = 10;
	private static Pseudonymisator PSEUDONYMISATOR;
	private static Depseudonymisator DEPSEUDONYMISATOR;

	@BeforeAll
	static void beforeAll() throws ApplicationException {
		final String password = "test";
		final KeyManagement keyManagement = new KeyManagementImpl();
		final KeyStore keyStore = keyManagement.createKeyStore(password);
		final PrivateKey privateKey = keyManagement.getPrivateKey(keyStore, password);

		final X509Certificate certificate = keyManagement.createCertificate(keyStore);
		final PublicKey publicKey = certificate.getPublicKey();

		PSEUDONYMISATOR = new PseudonymisatorImpl(publicKey);
		DEPSEUDONYMISATOR = new DepseudonymisatorImpl(privateKey);
	}

	@RepeatedTest(REPETITIONS)
	void bicDepseudonymisationTest() {
		final Bic bic = new Bic(new Faker().finance().bic());
		final BicPseudonym bicPseudonym = Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseBic(bic));
		final Bic bicDepseudo = Assertions.assertDoesNotThrow(() -> DEPSEUDONYMISATOR.depseudonymiseBic(bicPseudonym));
		Assertions.assertEquals(bic, bicDepseudo);
		System.out.println(bic + " -> " + bicDepseudo + " (" + bicPseudonym + ")");
	}

	@RepeatedTest(REPETITIONS)
	void ibanDepseudonymisationTest() {
		final Iban iban = new Iban(new Faker().finance().iban("DE"));
		final IbanPseudonym ibanPseudonym = Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseIban(iban));
		final Iban ibanDepseudo = Assertions.assertDoesNotThrow(() -> DEPSEUDONYMISATOR.depseudonymiseIban(ibanPseudonym));
		Assertions.assertEquals(iban, ibanDepseudo);
		System.out.println(iban + " -> " + ibanDepseudo + " (" + ibanPseudonym + ")");
	}

	@Test
	void bicCollectionDepseudonymisationTest() {
		final Faker faker = new Faker();
		final List<BicPseudonym> inputList = Stream
			.generate(() -> faker.finance().bic())
			.map(Bic::new)
			.map(bic -> Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseBic(bic)))
			.limit(REPETITIONS)
			.toList();

		final Map<BicPseudonym, Bic> outputMap = Assertions.assertDoesNotThrow(() -> DEPSEUDONYMISATOR.depseudonymiseBic(inputList));
		printMap(outputMap);
	}

	@Test
	void ibanCollectionDepseudonymisationTest() {
		final Faker faker = new Faker();
		final List<IbanPseudonym> inputList = Stream
			.generate(() -> faker.finance().iban("DE"))
			.map(Iban::new)
			.map(iban -> Assertions.assertDoesNotThrow(() -> PSEUDONYMISATOR.pseudonymiseIban(iban)))
			.limit(REPETITIONS)
			.toList();

		final Map<IbanPseudonym, Iban> outputMap = Assertions.assertDoesNotThrow(() -> DEPSEUDONYMISATOR.depseudonymiseIban(inputList));
		printMap(outputMap);
	}

	private void printMap(Map<? extends AbstractPseudonym, ? extends AbstractType> map) {
		map.entrySet().forEach(System.out::println);
	}
}
