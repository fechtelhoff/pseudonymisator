package de.fechtelhoff.pseudonymisator.api;

import java.util.Collection;
import java.util.Map;
import de.fechtelhoff.pseudonymisator.api.types.Bic;
import de.fechtelhoff.pseudonymisator.api.types.BicPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.Iban;
import de.fechtelhoff.pseudonymisator.api.types.IbanPseudonym;

public interface Pseudonymisator {

	BicPseudonym pseudonymiseBic(final Bic bic) throws ApplicationException;

	Map<Bic, BicPseudonym> pseudonymiseBic(final Collection<Bic> bics) throws ApplicationException;

	IbanPseudonym pseudonymiseIban(final Iban iban) throws ApplicationException;

	Map<Iban, IbanPseudonym> pseudonymiseIban(final Collection<Iban> ibans) throws ApplicationException;
}
