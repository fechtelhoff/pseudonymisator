package de.fechtelhoff.pseudonymisator.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import de.fechtelhoff.pseudonymisator.api.KeyManagement;

class KeyManagementImplTest {

	private static final Path TEST_DIRECTORY = Path.of("target/test");

	@BeforeAll
	static void beforeAll() throws IOException {
		if (Files.notExists(TEST_DIRECTORY)) {
			Files.createDirectories(TEST_DIRECTORY);
		}
	}

	@Test
	void keyManagementTest() throws IOException {
		final String password = "test";
		final Path keyStorePath = TEST_DIRECTORY.resolve("myKeyStore.p12");
		final Path certificatePath = TEST_DIRECTORY.resolve("myCertificate.cer");

		final KeyManagement keyManagement = new KeyManagementImpl();

		final KeyStore keyStore = Assertions.assertDoesNotThrow(() -> keyManagement.createKeyStore(password));
		Assertions.assertDoesNotThrow(() -> keyManagement.writeKeyStore(keyStore, password, keyStorePath));

		Assertions.assertDoesNotThrow(() -> keyManagement.readKeyStore(password, keyStorePath));
		try (final InputStream keyStoreInputStream = Files.newInputStream(keyStorePath)) {
			Assertions.assertDoesNotThrow(() -> keyManagement.readKeyStore(password, keyStoreInputStream));
		}

		Assertions.assertDoesNotThrow(() -> keyManagement.getPrivateKey(keyStore, password));

		final X509Certificate certificate = Assertions.assertDoesNotThrow(() -> keyManagement.createCertificate(keyStore));
		Assertions.assertDoesNotThrow(() -> keyManagement.writeCertificate(certificate, certificatePath));

		Assertions.assertDoesNotThrow(() -> keyManagement.readCertificate(certificatePath));
		try (final InputStream certificateInputStream = Files.newInputStream(certificatePath)) {
			Assertions.assertDoesNotThrow(() -> keyManagement.readCertificate(certificateInputStream));
		}
	}
}
