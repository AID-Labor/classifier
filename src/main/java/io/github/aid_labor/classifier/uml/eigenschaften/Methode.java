/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.eigenschaften;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Methode {
	private static final Logger log = Logger.getLogger(Methode.class.getName());
	
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
	private ObservableList<Parameter> parameterListe;
	private Datentyp rueckgabeTyp;
	private boolean istAbstrakt;
	private boolean istFinal;
	
	// @formatter:off
	@JsonIgnore private ObjectProperty<Modifizierer> sichtbarkeitProperty;
	@JsonIgnore private StringProperty nameProperty;
	@JsonIgnore private ObjectProperty<Datentyp> rueckgabetypProperty;
	@JsonIgnore private BooleanProperty istAbstraktProperty;
	@JsonIgnore private BooleanProperty istFinalProperty;
	// @formatter:on
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public Methode(Modifizierer sichtbarkeit, Datentyp datentyp) {
		this.sichtbarkeit = sichtbarkeit;
		this.rueckgabeTyp = datentyp;
		this.name = "";
		this.istAbstrakt = false;
		this.istFinal = false;
		this.parameterListe = FXCollections.observableList(new LinkedList<>());
	}
	
	@JsonCreator
	protected Methode(
			@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit,
			@JsonProperty("datentyp") Datentyp datentyp,
			@JsonProperty("parameterListe") List<Parameter> parameterListe) {
		this(sichtbarkeit, datentyp);
		if (parameterListe != null) {
			this.parameterListe.addAll(parameterListe);
		}
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
	
	public Datentyp getRueckgabeTyp() {
		return rueckgabeTyp;
	}
	
	public void setRueckgabeTyp(Datentyp rueckgabeTyp) {
		if (rueckgabetypProperty != null) {
			rueckgabetypProperty.set(rueckgabeTyp);
		}
		this.rueckgabeTyp = rueckgabeTyp;
	}
	
	@JsonProperty("istAbstrakt")
	public boolean istAbstrakt() {
		return istAbstrakt;
	}
	
	@JsonProperty("istAbstrakt")
	public void setzeAbstrakt(boolean istAbstrakt) {
		if (istAbstraktProperty != null) {
			istAbstraktProperty.set(istAbstrakt);
		}
		this.istAbstrakt = istAbstrakt;
	}
	
	@JsonProperty("istFinal")
	public boolean istFinal() {
		return istFinal;
	}
	
	@JsonProperty("istFinal")
	public void setzeFinal(boolean istFinal) {
		if (istFinalProperty != null) {
			istFinalProperty.set(istFinal);
		}
		this.istFinal = istFinal;
	}
	
	public ObservableList<Parameter> getParameterListe() {
		return parameterListe;
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
	
	public ObjectProperty<Datentyp> getRueckgabetypProperty() {
		if (rueckgabetypProperty == null) {
			this.rueckgabetypProperty = new SimpleObjectProperty<>(rueckgabeTyp);
		}
		return rueckgabetypProperty;
	}
	
	public BooleanProperty getIstAbstraktProperty() {
		if (istAbstraktProperty == null) {
			this.istAbstraktProperty = new SimpleBooleanProperty(istAbstrakt);
		}
		return istAbstraktProperty;
	}
	
	public BooleanProperty getIstFinalProperty() {
		if (istFinalProperty == null) {
			this.istFinalProperty = new SimpleBooleanProperty(istFinal);
		}
		return istFinalProperty;
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
		return Objects.hash(istAbstrakt, istFinal, name, parameterListe, rueckgabeTyp,
				sichtbarkeit);
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
		Methode m = (Methode) obj;
		
		boolean istAbstraktGleich = istAbstrakt == m.istAbstrakt;
		boolean istFinalGleich = istFinal == m.istFinal;
		boolean nameGleich = Objects.equals(name, m.name);
		boolean parameterListeGleich = ClassifierUtil.pruefeGleichheit(this.parameterListe,
				m.parameterListe);
		boolean rueckgabeTypGleich = Objects.equals(rueckgabeTyp, m.rueckgabeTyp);
		boolean sichtbarkeitGleich = Objects.equals(sichtbarkeit, m.sichtbarkeit);
		
		boolean istGleich = istAbstraktGleich && istFinalGleich && nameGleich
				&& parameterListeGleich && rueckgabeTypGleich && sichtbarkeitGleich;
		
		log.finer(() -> """
				istGleich: %s
				   |-- istAbstraktGleich: %s
				   |-- istFinalGleich: %s
				   |-- nameGleich: %s
				   |-- parameterListeGleich: %s
				   |-- rueckgabeTypGleich: %s
				   ╰-- sichtbarkeitGleich: %s"""
				.formatted(istGleich, istAbstraktGleich, istFinalGleich, nameGleich,
						parameterListeGleich, rueckgabeTypGleich, sichtbarkeitGleich));
		
		return istGleich;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}