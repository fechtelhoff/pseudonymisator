package de.fechtelhoff.pseudonymisator.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.util.encoders.Base64;
import de.fechtelhoff.pseudonymisator.api.ApplicationException;
import de.fechtelhoff.pseudonymisator.api.ApplicationException.ErrorCode;
import de.fechtelhoff.pseudonymisator.api.Depseudonymisator;
import de.fechtelhoff.pseudonymisator.api.types.Bic;
import de.fechtelhoff.pseudonymisator.api.types.BicPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.Iban;
import de.fechtelhoff.pseudonymisator.api.types.IbanPseudonym;

public class DepseudonymisatorImpl extends Application implements Depseudonymisator {

	private final PrivateKey privateKey;

	public DepseudonymisatorImpl(final PrivateKey privateKey) {
		super();
		this.privateKey = privateKey;
	}

	@Override
	public Bic depseudonymiseBic(final BicPseudonym bicPseudonym) throws ApplicationException {
		try {
			final String pseudonym = bicPseudonym.toString().substring(Application.BIC_PSEUDONYM_SUFFIX.length());
			return new Bic(depseudonymise(pseudonym));
		} catch (final InvalidCipherTextException | IOException exception) {
			throw new ApplicationException(ErrorCode.BIC_DEPSEUDONYMISATION_ERROR, exception);
		}
	}

	@Override
	public Map<BicPseudonym, Bic> depseudonymiseBic(final Collection<BicPseudonym> bicPseudonyms) throws ApplicationException {
		final Map<BicPseudonym, Bic> resultMap = new HashMap<>();
		for (final BicPseudonym bicPseudonym : bicPseudonyms) {
			final Bic bic = depseudonymiseBic(bicPseudonym);
			resultMap.put(bicPseudonym, bic);
		}
		return resultMap;
	}

	@Override
	public Iban depseudonymiseIban(final IbanPseudonym ibanPseudonym) throws ApplicationException {
		try {
			final String pseudonym = ibanPseudonym.toString().substring(Application.IBAN_PSEUDONYM_SUFFIX.length());
			return new Iban(depseudonymise(pseudonym));
		} catch (final InvalidCipherTextException | IOException exception) {
			throw new ApplicationException(ErrorCode.IBAN_DEPSEUDONYMISATION_ERROR, exception);
		}
	}

	@Override
	public Map<IbanPseudonym, Iban> depseudonymiseIban(final Collection<IbanPseudonym> ibanPseudonyms) throws ApplicationException {
		final Map<IbanPseudonym, Iban> resultMap = new HashMap<>();
		for (final IbanPseudonym ibanPseudonym : ibanPseudonyms) {
			final Iban iban = depseudonymiseIban(ibanPseudonym);
			resultMap.put(ibanPseudonym, iban);
		}
		return resultMap;
	}

	private String depseudonymise(final String pseudonym) throws InvalidCipherTextException, IOException {
		final byte[] toDecryptBytes = pseudonym.trim().getBytes(StandardCharsets.UTF_8);
		final AsymmetricBlockCipher asymmetricBlockCipher = new RSAEngine();
		asymmetricBlockCipher.init(false, PrivateKeyFactory.createKey(privateKey.getEncoded()));
		final byte[] bufferHex = Base64.decode(toDecryptBytes);
		final byte[] buffer = asymmetricBlockCipher.processBlock(bufferHex, 0, bufferHex.length);
		return new String(buffer, StandardCharsets.UTF_8);
	}
}
