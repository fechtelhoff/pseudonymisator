package de.fechtelhoff.pseudonymisator.api.types;

public class BicPseudonym extends AbstractPseudonym {

	private static final String LABEL = "BIC_PSEUDONYM";
	private static final String FORMAT = "bic.{344}";

	public BicPseudonym(final String value) {
		super(LABEL, value);
	}

	public static boolean isValid(final String value) {
		return validate(LABEL, value, FORMAT).isEmpty();
	}
}
