package de.fechtelhoff.pseudonymisator.api;

import java.io.InputStream;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface KeyManagement {

	KeyStore createKeyStore(final String password) throws ApplicationException;
	void writeKeyStore(final KeyStore keyStore, final String password, final Path path) throws ApplicationException;
	KeyStore readKeyStore(final String password, final InputStream inputStream) throws ApplicationException;
	KeyStore readKeyStore(final String password, final Path path) throws ApplicationException;

	PrivateKey getPrivateKey(final KeyStore keyStore, final String password) throws ApplicationException;

	X509Certificate createCertificate(final KeyStore keyStore) throws ApplicationException;
	void writeCertificate(final X509Certificate certificate, final Path path) throws ApplicationException;
	X509Certificate readCertificate(final InputStream inputStream) throws ApplicationException;
	X509Certificate readCertificate(final Path path) throws ApplicationException;
}
