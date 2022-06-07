/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
	private final Datentyp datentyp;
	@JsonIgnore
	private final JsonStringProperty initialwert;
	private final JsonBooleanProperty hatGetter;
	private final JsonBooleanProperty hatSetter;
	private final JsonBooleanProperty istStatisch;
	@JsonBackReference("getterAttribut")
	private Methode getter;
	@JsonBackReference("setterAttribut")
	private Methode setter;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@JsonCreator
	public Attribut(
			@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit,
			@JsonProperty("name") String name,
			@JsonProperty("datentyp") Datentyp datentyp,
			@JsonProperty("initialwert") String initialwert,
			@JsonProperty("hatGetter") boolean hatGetter,
			@JsonProperty("hatSetter") boolean hatSetter,
			@JsonProperty("istStatisch") boolean istStatisch,
			@JsonProperty("getterAttribut") Methode getter,
			@JsonProperty("setterAttribut") Methode setter) {
		this.sichtbarkeit = new JsonObjectProperty<>(this, "sichtbarkeit", sichtbarkeit);
		this.datentyp = datentyp;
		this.name = new JsonStringProperty(this, "attributName", name);
		this.initialwert = new JsonStringProperty(this, "initialwert", initialwert);
		this.hatGetter = new JsonBooleanProperty(this, "hatGetter", hatGetter);
		this.hatSetter = new JsonBooleanProperty(this, "hatSetter", hatSetter);
		this.istStatisch = new JsonBooleanProperty(this, "istStatisch", istStatisch);
		this.getter = getter;
		this.setter = setter;
		
		this.ueberwachePropertyAenderung(this.sichtbarkeit);
		this.ueberwachePropertyAenderung(this.datentyp.getTypNameProperty());
		this.ueberwachePropertyAenderung(this.name);
		this.ueberwachePropertyAenderung(this.initialwert);
		this.ueberwachePropertyAenderung(this.hatGetter);
		this.ueberwachePropertyAenderung(this.hatSetter);
		this.ueberwachePropertyAenderung(this.istStatisch);
	}
	
	public Attribut(Modifizierer sichtbarkeit, Datentyp datentyp, boolean istStatisch) {
		this(sichtbarkeit, "", datentyp, null, false, false, istStatisch, null, null);
	}
	
	public Attribut(Modifizierer sichtbarkeit, Datentyp datentyp) {
		this(sichtbarkeit, datentyp, false);
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
	
	public Datentyp getDatentyp() {
		return datentyp;
	}
	
	@JsonProperty("initialwert")
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
	
	public boolean istStatisch() {
		return istStatisch.get();
	}
	
	public void setzeStatisch(boolean istStatisch) {
		this.istStatisch.set(istStatisch);
	}
	
	public Methode getGetter() {
		return getter;
	}
	
	public void setGetter(Methode getter) {
		this.getter = getter;
	}
	
	public Methode getSetter() {
		return setter;
	}
	
	public void setSetter(Methode setter) {
		this.setter = setter;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ObjectProperty<Modifizierer> getSichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public StringProperty getNameProperty() {
		return name;
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
	
	public BooleanProperty getIstStatischProperty() {
		return istStatisch;
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
				getName(), getSichtbarkeit(), istStatisch());
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
		boolean datentypGleich = Objects.equals(getDatentyp(), other.getDatentyp());
		boolean getterGleich = hatGetter() == other.hatGetter();
		boolean setterGleich = hatSetter() == other.hatSetter();
		boolean initialwertGleich = Objects.equals(getInitialwert(), other.getInitialwert());
		boolean nameGleich = Objects.equals(getName(), other.getName());
		boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(),
				other.getSichtbarkeit());
		boolean statischGleich = this.istStatisch() == other.istStatisch();
		boolean istGleich = datentypGleich && getterGleich && setterGleich && initialwertGleich
				&& nameGleich && sichtbarkeitGleich && statischGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- datentypGleich: %s
				   |-- getterGleich: %s
				   |-- setterGleich: %s
				   |-- initialwertGleich: %s
				   |-- nameGleich: %s
				   |-- sichtbarkeitGleich: %s
				   ╰-- statischGleich: %s""".formatted(istGleich, datentypGleich, getterGleich,
				setterGleich, initialwertGleich, nameGleich, sichtbarkeitGleich,
				statischGleich));
		
		return istGleich;
	}
	
	public Attribut erzeugeTiefeKopie() {
		var kopie = new Attribut(getSichtbarkeit(), getDatentyp().erzeugeTiefeKopie(),
				istStatisch());
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