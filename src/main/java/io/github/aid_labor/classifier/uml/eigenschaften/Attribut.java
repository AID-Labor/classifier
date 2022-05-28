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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Attribut {
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
	
	private Modifizierer sichtbarkeit;
	private String name;
	private Datentyp datentyp;
	private String initialwert;
	private boolean hatGetter;
	private boolean hatSetter;
	
	// @formatter:off
	@JsonIgnore private ObjectProperty<Modifizierer> sichtbarkeitProperty;
	@JsonIgnore private StringProperty nameProperty;
	@JsonIgnore private ObjectProperty<Datentyp> datentypProperty;
	@JsonIgnore private StringProperty initialwertProperty;
	@JsonIgnore private BooleanProperty hatGetterProperty;
	@JsonIgnore private BooleanProperty hatSetterProperty;
	// @formatter:on
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@JsonCreator
	public Attribut(
			@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit, 
			@JsonProperty("datentyp") Datentyp datentyp) {
		this.sichtbarkeit = sichtbarkeit;
		this.datentyp = datentyp;
		this.name = "";
		this.hatGetter = false;
		this.hatSetter = false;
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public Modifizierer getSichtbarkeit() {
		return sichtbarkeit;
	}
	
	public void setSichtbarkeit(Modifizierer sichtbarkeit) {
		if (sichtbarkeitProperty != null) {
			sichtbarkeitProperty.set(sichtbarkeit);
		}
		this.sichtbarkeit = sichtbarkeit;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		if (nameProperty != null) {
			nameProperty.set(name);
		}
		this.name = name;
	}
	
	public Datentyp getDatentyp() {
		return datentyp;
	}
	
	public void setDatentyp(Datentyp datentyp) {
		if (datentypProperty != null) {
			datentypProperty.set(datentyp);
		}
		this.datentyp = datentyp;
	}
	
	public String getInitialwert() {
		return initialwert;
	}
	
	public void setInitialwert(String initialwert) {
		if (initialwertProperty != null) {
			initialwertProperty.set(initialwert);
		}
		this.initialwert = initialwert;
	}
	
	public boolean hatGetter() {
		return hatGetter;
	}
	
	public void nutzeGetter(boolean hatGetter) {
		if (hatGetterProperty != null) {
			hatGetterProperty.set(hatGetter);
		}
		this.hatGetter = hatGetter;
	}
	
	public boolean hatSetter() {
		return hatSetter;
	}
	
	public void nutzeSetter(boolean hatSetter) {
		if (hatSetterProperty != null) {
			hatSetterProperty.set(hatSetter);
		}
		this.hatSetter = hatSetter;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ObjectProperty<Modifizierer> getSichtbarkeitProperty() {
		if (sichtbarkeitProperty == null) {
			this.sichtbarkeitProperty = new SimpleObjectProperty<>(sichtbarkeit);
		}
		return sichtbarkeitProperty;
	}
	
	public StringProperty getNameProperty() {
		if (nameProperty == null) {
			this.nameProperty = new SimpleStringProperty(name);
		}
		return nameProperty;
	}
	
	public ObjectProperty<Datentyp> getDatentypProperty() {
		if (datentypProperty == null) {
			this.datentypProperty = new SimpleObjectProperty<>(datentyp);
		}
		return datentypProperty;
	}
	
	public StringProperty getInitialwertProperty() {
		if (initialwertProperty == null) {
			this.initialwertProperty = new SimpleStringProperty(initialwert);
		}
		return initialwertProperty;
	}
	
	public BooleanProperty getHatGetterProperty() {
		if (hatGetterProperty == null) {
			this.hatGetterProperty = new SimpleBooleanProperty(hatGetter);
		}
		return hatGetterProperty;
	}
	
	public BooleanProperty getHatSetterProperty() {
		if (hatSetterProperty == null) {
			this.hatSetterProperty = new SimpleBooleanProperty(hatSetter);
		}
		return hatSetterProperty;
	}

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public int hashCode() {
		return Objects.hash(datentyp, hatGetter, hatSetter, initialwert, name, sichtbarkeit);
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
		return Objects.equals(datentyp, other.datentyp) && hatGetter == other.hatGetter
				&& hatSetter == other.hatSetter
				&& Objects.equals(initialwert, other.initialwert)
				&& Objects.equals(name, other.name)
				&& Objects.equals(sichtbarkeit, other.sichtbarkeit);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}