package de.fechtelhoff.pseudonymisator.api.types;

public class IbanPseudonym extends AbstractPseudonym {

	private static final String LABEL = "IBAN_PSEUDONYM";
	private static final String FORMAT = "iban.{344}";

	public IbanPseudonym(final String value) {
		super(LABEL, value);
	}

	public static boolean isValid(final String value) {
		return validate(LABEL, value, FORMAT).isEmpty();
	}
}
