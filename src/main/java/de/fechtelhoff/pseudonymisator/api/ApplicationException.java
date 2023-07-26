package de.fechtelhoff.pseudonymisator.api;

public class ApplicationException extends Exception {

	private final ErrorCode errorCode;

	public ApplicationException(final ErrorCode errorCode, final Throwable cause) {
		super(errorCode.getError(), cause);
		this.errorCode = errorCode;
	}

	@Override
	public String getMessage() {
		return errorCode.getError();
	}

	public enum ErrorCode {

		// KeyManagement
		WRITE_KEYSTORE_ERROR("Kann den KeyStore nicht schreiben."),
		READ_KEYSTORE_FROM_STREAM_ERROR("Kann den KeyStore nicht aus dem InputStream erzeugen."),
		READ_KEYSTORE_ERROR("Kann den KeyStore nicht lesen."),
		GET_PRIVATEKEY_ERROR("Kann den privaten Schlüssel nicht auslesen."),
		CREATE_CERTIFICATE_ERROR("Kann das Zertifikat nicht erzeugen."),
		CREATE_CERTIFICATE_FROM_KEYSTORE_ERROR("Kann das Zertifikat nicht im KeyStore finden."),
		WRITE_CERTIFICATE_ERROR("Kann das Zertifikat nicht schreiben."),
		READ_CERTIFICATE_FROM_STREAM_ERROR("Kann das Zertifikat nicht aus dem InputStream erzeugen."),
		READ_CERTIFICATE_ERROR("Kann das Zertifikat nicht lesen."),
		CREATE_KEYPAIR_ERROR("Kann das Schlüsselpaar nicht erzeugen."),

		// Pseudonymisation
		BIC_PSEUDONYMISATION_ERROR("Kann BIC nicht pseudonymisieren."),
		IBAN_PSEUDONYMISATION_ERROR("Kann IBAN nicht pseudonymisieren."),

		// Depseudonymisation
		BIC_DEPSEUDONYMISATION_ERROR("Kann BIC nicht depseudonymisieren."),
		IBAN_DEPSEUDONYMISATION_ERROR("Kann IBAN nicht depseudonymisieren."),

		/* */;

		public final String error;

		ErrorCode(final String error) {
			this.error = error;
		}

		public String getError() {
			return error;
		}
	}
}
