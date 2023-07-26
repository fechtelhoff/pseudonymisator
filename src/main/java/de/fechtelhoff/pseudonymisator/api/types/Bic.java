package de.fechtelhoff.pseudonymisator.api.types;

/**
 * BIC = Business Identifier Code.
 * Auch als SWIFT-Code (Society for Worldwide Interbank Financial Telecommunication) bezeichnet/bekannt.
 * <p>
 * Kann 8- oder 11-stellig sein.
 * 1-4 - Bankcode, vom Geldinstitut frei wählbar
 * 5-6 - Ländercode (Country Code) nach ISO 3166-1
 * 7-8 - Codierung des Ortes
 * 9-11 - Kennzeichnung (Branch-Code) der Filiale oder Abteilung (optional)
 */
public class Bic extends AbstractType {

	private static final String BIC_LABEL = "BIC";
	private static final String FORMAT = "[A-Z0-9]{8}([A-Z0-9]{3})?";

	public Bic(final String value) {
		super(BIC_LABEL, value, FORMAT);
	}

	public static boolean isValid(final String value) {
		return validate(BIC_LABEL, value, FORMAT).isEmpty();
	}
}
