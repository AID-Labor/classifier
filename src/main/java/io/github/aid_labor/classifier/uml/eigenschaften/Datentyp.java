/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.EditierbarerBeobachter;

//@formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
//@formatter:on
public class Datentyp extends EditierbarBasis implements EditierbarerBeobachter {
	
	
	private final String typName;
	
	@JsonCreator
	public Datentyp(@JsonProperty("typName") String typName) {
		this.typName = typName;
	}
	
	public String getTypName() {
		return typName;
	}
	
	public Datentyp erzeugeTiefeKopie() {
		return new Datentyp(typName);
	}
	
	@Override
	public String toString() {
		return this.getTypName();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(typName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Datentyp other = (Datentyp) obj;
		return Objects.equals(typName, other.typName);
	}
	
	
}
