package de.fechtelhoff.pseudonymisator.api.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractType implements Comparable<AbstractType>, Serializable {

	protected final String label;
	protected final String value;

	protected AbstractType(final String label, final String value, final String format) {
		final List<String> errors = validate(label, value, format);
		if (errors.isEmpty()) {
			this.label = label;
			this.value = value;
		} else {
			throw new IllegalArgumentException(getCombinedErrorString(errors));
		}
	}

	protected AbstractType() {
		this.label = null;
		this.value = null;
	}

	protected AbstractType(final String value) {
		this.label = null;
		this.value = value;
	}

	protected AbstractType(final String label, final String value) {
		this.label = label;
		this.value = value;
	}

	protected static List<String> validate(final String label, final String value, final String format) {
		final List<String> errors = new ArrayList<>();
		if (value == null) {
			errors.add(String.format("%s darf nicht null sein", label));
		} else if (StringUtils.isBlank(format)) {
			errors.add("Format darf nicht null sein");
		} else {
			final Pattern pattern = Pattern.compile(format);
			if (!pattern.matcher(value).matches()) {
				errors.add(String.format("%s entspricht nicht dem gewählten Format %s: %s", label, pattern, value));
			}
		}
		return errors;
	}

	public String getCombinedErrorString(final List<String> errors) {
		final String combinedError = String.join(", ", errors);
		return String.format("%s '%s' ist invalide: %s", label, value, combinedError);
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		return Objects.equals(this.toString(), that.toString());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	@Override
	public int compareTo(final AbstractType that) {
		return this.toString().compareTo(that.toString());
	}
}
