package de.fechtelhoff.pseudonymisator.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import de.fechtelhoff.pseudonymisator.api.ApplicationException;
import de.fechtelhoff.pseudonymisator.api.ApplicationException.ErrorCode;
import de.fechtelhoff.pseudonymisator.api.KeyManagement;

public class KeyManagementImpl extends Application implements KeyManagement {

	private static final String KEYSTORE_TYPE = "pkcs12";
	public static final String KEYSTORE_ENTRY_NAME = "Pseudonymisierung";

	private static final String ALGORITHM = "RSA";
	private static final int RSA_KEY_LENGTH = 2048;
	private static final String CERTIFICATE_SIGNING_ALGORITHM = "SHA256WithRSAEncryption";

	public KeyManagementImpl() {
		super();
	}

	@Override
	public KeyStore createKeyStore(final String password) throws ApplicationException {
		final LocalDate validFrom = LocalDate.now();
		final LocalDate validTo = validFrom.plusYears(5);

		try {
			final KeyPair keyPair = generateRsaKeyPair();
			final X509Certificate certificate = generateCertificate(keyPair, validFrom, validTo);

			final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
			keyStore.load(null, password.toCharArray());
			keyStore.setKeyEntry(KEYSTORE_ENTRY_NAME, keyPair.getPrivate(), password.toCharArray(), generateCertificateArray(certificate));
			return keyStore;
		} catch (final CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException exception) {
			throw new ApplicationException(ErrorCode.WRITE_KEYSTORE_ERROR, exception);
		}
	}

	@Override
	public void writeKeyStore(final KeyStore keyStore, final String password, final Path path) throws ApplicationException {
		try (final FileOutputStream fileOutputStream = new FileOutputStream(path.toFile())) {
			keyStore.store(fileOutputStream, password.toCharArray());
		} catch (final IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException exception) {
			throw new ApplicationException(ErrorCode.WRITE_KEYSTORE_ERROR, exception);
		}
	}

	@Override
	public KeyStore readKeyStore(final String password, final InputStream inputStream) throws ApplicationException {
		try {
			final KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
			keyStore.load(inputStream, password.toCharArray());
			return keyStore;
		} catch (final IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException exception) {
			throw new ApplicationException(ErrorCode.READ_KEYSTORE_FROM_STREAM_ERROR, exception);
		}
	}

	@Override
	public KeyStore readKeyStore(final String password, final Path path) throws ApplicationException {
		try (final InputStream inputStream = Files.newInputStream(path)) {
			return readKeyStore(password, inputStream);
		} catch (final IOException exception) {
			throw new ApplicationException(ErrorCode.READ_KEYSTORE_ERROR, exception);
		}
	}

	@Override
	public PrivateKey getPrivateKey(final KeyStore keyStore, final String password) throws ApplicationException {
		try {
			return (PrivateKey) keyStore.getKey(KEYSTORE_ENTRY_NAME, password.toCharArray());
		} catch (final KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException exception) {
			throw new ApplicationException(ErrorCode.GET_PRIVATEKEY_ERROR, exception);
		}
	}

	@Override
	public X509Certificate createCertificate(final KeyStore keyStore) throws ApplicationException {
		try {
			return (X509Certificate) keyStore.getCertificate(KEYSTORE_ENTRY_NAME);
		} catch (final KeyStoreException exception) {
			throw new ApplicationException(ErrorCode.CREATE_CERTIFICATE_FROM_KEYSTORE_ERROR, exception);
		}
	}

	@Override
	public void writeCertificate(final X509Certificate certificate, final Path path) throws ApplicationException {
		try (
			final FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
			final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
			final JcaPEMWriter pemWriter = new JcaPEMWriter(outputStreamWriter)
		) {
			pemWriter.writeObject(certificate);
			pemWriter.flush();
		} catch (final IOException exception) {
			throw new ApplicationException(ErrorCode.WRITE_CERTIFICATE_ERROR, exception);
		}
	}

	@Override
	public X509Certificate readCertificate(final InputStream inputStream) throws ApplicationException {
		try (final PEMParser pemParser = new PEMParser(new InputStreamReader(inputStream))) {
			return new JcaX509CertificateConverter()
				.setProvider(Application.BOUNCY_CASTLE_SECURITY_PROVIDER)
				.getCertificate((X509CertificateHolder) pemParser.readObject());
		} catch (final IOException | CertificateException exception) {
			throw new ApplicationException(ErrorCode.READ_CERTIFICATE_FROM_STREAM_ERROR, exception);
		}
	}

	@Override
	public X509Certificate readCertificate(final Path path) throws ApplicationException {
		try (final InputStream inputStream = Files.newInputStream(path)) {
			return readCertificate(inputStream);
		} catch (final IOException exception) {
			throw new ApplicationException(ErrorCode.READ_CERTIFICATE_ERROR, exception);
		}
	}

	private KeyPair generateRsaKeyPair() throws ApplicationException {
		try {
			final KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
			generator.initialize(RSA_KEY_LENGTH, new SecureRandom());
			return generator.generateKeyPair();
		} catch (final NoSuchAlgorithmException exception) {
			throw new ApplicationException(ErrorCode.CREATE_KEYPAIR_ERROR, exception);
		}
	}

	private X509Certificate[] generateCertificateArray(final X509Certificate... certificates) {
		return certificates;
	}

	private X509Certificate generateCertificate(final KeyPair keyPair, final LocalDate validFrom, final LocalDate validTo) throws ApplicationException {
		final X500Name issuerName = generateX500Name();
		final BigInteger serialNumber = BigInteger.valueOf(new SecureRandom().nextLong());

		final X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
			issuerName, serialNumber, asDate(validFrom), asDate(validTo), issuerName, keyPair.getPublic()
		);

		try {
			final ContentSigner contentSigner = new JcaContentSignerBuilder(CERTIFICATE_SIGNING_ALGORITHM)
				.setProvider(Application.BOUNCY_CASTLE_SECURITY_PROVIDER)
				.build(keyPair.getPrivate());

			final X509Certificate certificate = new JcaX509CertificateConverter()
				.setProvider(Application.BOUNCY_CASTLE_SECURITY_PROVIDER)
				.getCertificate(certificateBuilder.build(contentSigner));

			certificate.checkValidity(asDate(validFrom));
			certificate.verify(keyPair.getPublic());

			return certificate;
		} catch (final NoSuchProviderException | InvalidKeyException | OperatorCreationException | SignatureException | NoSuchAlgorithmException |
		               CertificateException exception) {
			throw new ApplicationException(ErrorCode.CREATE_CERTIFICATE_ERROR, exception);
		}
	}

	private X500Name generateX500Name() {
		final X500NameBuilder builder = new X500NameBuilder(X500Name.getDefaultStyle());
		builder.addRDN(BCStyle.C, "DE"); // country code - StringType(SIZE(2))
		builder.addRDN(BCStyle.ST, "Brandenburg"); // state, or province name - StringType(SIZE(1..64))
		builder.addRDN(BCStyle.OU, "fechtelhoff.de"); // organizational unit name - StringType(SIZE(1..64))
		return builder.build();
	}

	private Date asDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
}
