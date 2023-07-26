package de.fechtelhoff.pseudonymisator.impl;

import java.security.Security;
import java.util.Objects;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Application {

	public static final String BOUNCY_CASTLE_SECURITY_PROVIDER = "BC";
	public static final String BIC_PSEUDONYM_SUFFIX = "bic";
	public static final String IBAN_PSEUDONYM_SUFFIX = "iban";

	static {
		if (Objects.isNull(Security.getProvider(BOUNCY_CASTLE_SECURITY_PROVIDER))) {
			Security.addProvider(new BouncyCastleProvider());
		}
	}
}
