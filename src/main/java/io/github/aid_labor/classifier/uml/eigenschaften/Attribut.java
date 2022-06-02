/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.json.JsonBooleanProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.EditierbarerBeobachter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;


public class Attribut extends EditierbarBasis implements EditierbarerBeobachter {
//	private static final Logger log = Logger.getLogger(Attribut.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final JsonObjectProperty<Modifizierer> sichtbarkeit;
	private final JsonStringProperty name;
	@JsonIgnore private final JsonObjectProperty<Datentyp> datentyp;
	private final JsonStringProperty initialwert;
	private final JsonBooleanProperty hatGetter;
	private final JsonBooleanProperty hatSetter;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@JsonCreator
	public Attribut(
			@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit,
			@JsonProperty("datentyp") Datentyp datentyp) {
		this.sichtbarkeit = new JsonObjectProperty<>(sichtbarkeit);
		this.datentyp = new JsonObjectProperty<>(datentyp);
		this.name = new JsonStringProperty("");
		this.initialwert = new JsonStringProperty(null);
		this.hatGetter = new JsonBooleanProperty(false);
		this.hatSetter = new JsonBooleanProperty(false);
		
		this.ueberwachePropertyAenderung(this.sichtbarkeit);
		this.ueberwachePropertyAenderung(this.datentyp);
		this.ueberwachePropertyAenderung(this.name);
		this.ueberwachePropertyAenderung(this.initialwert);
		this.ueberwachePropertyAenderung(this.hatGetter);
		this.ueberwachePropertyAenderung(this.hatSetter);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public Modifizierer getSichtbarkeit() {
		return sichtbarkeit.get();
	}
	
	public void setSichtbarkeit(Modifizierer sichtbarkeit) {
		this.sichtbarkeit.set(sichtbarkeit);
	}
	
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	@JsonProperty("datentyp")
	public Datentyp getDatentyp() {
		if(datentyp.get() == null) {
			return new Datentyp(null);
		}
		return datentyp.get();
	}
	
	@JsonProperty("datentyp")
	public void setDatentyp(Datentyp datentyp) {
		this.datentyp.set(datentyp);
	}
	
	public String getInitialwert() {
		return initialwert.get();
	}
	
	public void setInitialwert(String initialwert) {
		this.initialwert.set(initialwert);
	}
	
	public boolean hatGetter() {
		return hatGetter.get();
	}
	
	public void nutzeGetter(boolean hatGetter) {
		this.hatGetter.set(hatGetter);
	}
	
	public boolean hatSetter() {
		return hatSetter.get();
	}
	
	public void nutzeSetter(boolean hatSetter) {
		this.hatSetter.set(hatSetter);
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ObjectProperty<Modifizierer> getSichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public ObjectProperty<Datentyp> getDatentypProperty() {
		return datentyp;
	}
	
	public StringProperty getInitialwertProperty() {
		return initialwert;
	}
	
	public BooleanProperty getHatGetterProperty() {
		return hatGetter;
	}
	
	public BooleanProperty getHatSetterProperty() {
		return hatSetter;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public String toString() {
		return "Attribut <%s %s %s%s {%s %s}>".formatted(getSichtbarkeit(),
				getDatentyp().getTypName(), getName(),
				getInitialwert() != null ? "=" + getInitialwert() : "",
				hatGetter() ? " Getter" : "",
				hatSetter() ? "Setter " : "");
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getDatentyp(), hatGetter(), hatSetter(), getInitialwert(),
				getName(), getSichtbarkeit());
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
		Attribut other = (Attribut) obj;
		return Objects.equals(getDatentyp(), other.getDatentyp()) 
				&& hatGetter() == other.hatGetter()
				&& hatSetter() == other.hatSetter()
				&& Objects.equals(getInitialwert(), other.getInitialwert())
				&& Objects.equals(getName(), other.getName())
				&& Objects.equals(getSichtbarkeit(), other.getSichtbarkeit());
	}
	
	public Attribut erzeugeTiefeKopie() {
		var kopie = new Attribut(getSichtbarkeit(), getDatentyp().erzeugeTiefeKopie());
		kopie.setInitialwert(getInitialwert());
		kopie.setName(getName());
		kopie.nutzeGetter(hatGetter());
		kopie.nutzeSetter(hatSetter());
		
		return kopie;
	}

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}