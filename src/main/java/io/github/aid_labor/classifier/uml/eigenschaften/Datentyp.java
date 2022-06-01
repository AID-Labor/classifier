/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.eigenschaften;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import io.github.aid_labor.classifier.uml.eigenschaften.Datentyp.BasisDatentyp;

//@formatter:off
@JsonTypeInfo(
		use = Id.NAME,
		include = As.PROPERTY,
		property = "klasse"
)
@JsonSubTypes({
	@JsonSubTypes.Type(value = BasisDatentyp.class)
})
// @formatter:on
public interface Datentyp {
	
	public static enum BasisDatentyp implements Datentyp {
		
		CHAR("char"), INT("int"), DOUBLE("double"), STRING("String");
		
		private String typName;
		
		private BasisDatentyp(String typName) {
			this.typName = typName;
		}
		
		@Override
		public String getTypName() {
			return typName;
		}
		
		@Override
		public String toString() {
			return getTypName();
		}
		
		@Override
		public Datentyp erzeugeTiefeKopie() {
			return this;
		}
	}
	
	public String getTypName();

	public Datentyp erzeugeTiefeKopie();
	
	
}
