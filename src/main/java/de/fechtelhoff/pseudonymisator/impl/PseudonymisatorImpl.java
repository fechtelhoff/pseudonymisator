package de.fechtelhoff.pseudonymisator.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.encoders.Base64;
import de.fechtelhoff.pseudonymisator.api.ApplicationException;
import de.fechtelhoff.pseudonymisator.api.Pseudonymisator;
import de.fechtelhoff.pseudonymisator.api.types.Bic;
import de.fechtelhoff.pseudonymisator.api.types.BicPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.Iban;
import de.fechtelhoff.pseudonymisator.api.types.IbanPseudonym;

public class PseudonymisatorImpl extends Application implements Pseudonymisator {

	private final PublicKey publicKey;

	public PseudonymisatorImpl(final PublicKey publicKey) {
		super();
		this.publicKey = publicKey;
	}

	@Override
	public BicPseudonym pseudonymiseBic(final Bic bic) throws ApplicationException {
		try {
			final String pseudonym = pseudonymise(Application.BIC_PSEUDONYM_SUFFIX, bic.toString());
			return new BicPseudonym(pseudonym);
		} catch (final InvalidCipherTextException | IOException exception) {
			throw new ApplicationException(ApplicationException.ErrorCode.BIC_PSEUDONYMISATION_ERROR, exception);
		}
	}

	@Override
	public Map<Bic, BicPseudonym> pseudonymiseBic(final Collection<Bic> bics) throws ApplicationException {
		final Map<Bic, BicPseudonym> resultMap = new HashMap<>();
		for (final Bic bic : bics) {
			final BicPseudonym bicPseudonym = pseudonymiseBic(bic);
			resultMap.put(bic, bicPseudonym);
		}
		return resultMap;
	}

	@Override
	public IbanPseudonym pseudonymiseIban(final Iban iban) throws ApplicationException {
		try {
			final String pseudonym = pseudonymise(Application.IBAN_PSEUDONYM_SUFFIX, iban.toString());
			return new IbanPseudonym(pseudonym);
		} catch (final InvalidCipherTextException | IOException exception) {
			throw new ApplicationException(ApplicationException.ErrorCode.IBAN_PSEUDONYMISATION_ERROR, exception);
		}
	}

	@Override
	public Map<Iban, IbanPseudonym> pseudonymiseIban(final Collection<Iban> ibans) throws ApplicationException {
		final Map<Iban, IbanPseudonym> resultMap = new HashMap<>();
		for (final Iban iban : ibans) {
			final IbanPseudonym ibanPseudonym = pseudonymiseIban(iban);
			resultMap.put(iban, ibanPseudonym);
		}
		return resultMap;
	}

	private String pseudonymise(final String suffix, final String value) throws InvalidCipherTextException, IOException {
		return suffix + pseudonymise(value);
	}

	private String pseudonymise(final String value) throws InvalidCipherTextException, IOException {
		final byte[] toEncryptBytes = value.trim().getBytes(StandardCharsets.UTF_8);
		final AsymmetricBlockCipher asymmetricBlockCipher = new RSAEngine();
		asymmetricBlockCipher.init(true, PublicKeyFactory.createKey(publicKey.getEncoded()));
		final byte[] bufferHex = asymmetricBlockCipher.processBlock(toEncryptBytes, 0, toEncryptBytes.length);
		final byte[] buffer = Base64.encode(bufferHex);
		return new String(buffer, StandardCharsets.UTF_8);
	}
}
