package de.fechtelhoff.pseudonymisator.api;

import java.util.Collection;
import java.util.Map;
import de.fechtelhoff.pseudonymisator.api.types.Bic;
import de.fechtelhoff.pseudonymisator.api.types.BicPseudonym;
import de.fechtelhoff.pseudonymisator.api.types.Iban;
import de.fechtelhoff.pseudonymisator.api.types.IbanPseudonym;

public interface Depseudonymisator {

	Bic depseudonymiseBic(final BicPseudonym bicPseudonym) throws ApplicationException;

	Map<BicPseudonym, Bic> depseudonymiseBic(final Collection<BicPseudonym> bicPseudonyms) throws ApplicationException;

	Iban depseudonymiseIban(final IbanPseudonym ibanPseudonym) throws ApplicationException;

	Map<IbanPseudonym, Iban> depseudonymiseIban(final Collection<IbanPseudonym> ibanPseudonyms) throws ApplicationException;
}
