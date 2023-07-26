package de.fechtelhoff.pseudonymisator.api.types;

/**
 * IBAN = International Bank Account Number (Internationale Bankkontonummer)
 * <p>
 * Deutsches Format (22 Stellen):
 * 1-2 - DE (Ländercode, Country Code)
 * 3-4 - Prüfsumme (Checksum)
 * 5-12 - Bankleitzahl (BLZ)
 * 13-22 - Kontonummer
 */
public class Iban extends AbstractType {

	private static final String IBAN_LABEL = "IBAN";
	private static final String FORMAT = "[A-Z]{2}\\d{20}";

	public Iban(final String value) {
		super(IBAN_LABEL, value, FORMAT);
	}

	public String getCountryCode() {
		return value.substring(0, 2);
	}

	public String getChecksum() {
		return value.substring(2, 4);
	}

	public String getBankleitzahl() {
		return value.substring(4, 12);
	}

	public String getKontonummer() {
		return value.substring(12, 22);
	}

	public String toReadableString() {
		return value.replaceAll(".{4}", "$0 ");
	}

	public static boolean isValid(final String value) {
		return validate(IBAN_LABEL, value, FORMAT).isEmpty();
	}
}
