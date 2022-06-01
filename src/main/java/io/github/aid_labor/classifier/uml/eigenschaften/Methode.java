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
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonBooleanProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.EditierbarerBeobachter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Methode extends EditierbarBasis implements EditierbarerBeobachter {
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
	
	private final JsonObjectProperty<Modifizierer> sichtbarkeit;
	private final JsonStringProperty name;
	@JsonIgnore
	private final JsonObjectProperty<Datentyp> rueckgabeTyp;
	private ObservableList<Parameter> parameterListe;
	private final JsonBooleanProperty istAbstrakt;
	private final JsonBooleanProperty istFinal;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public Methode(Modifizierer sichtbarkeit, Datentyp datentyp) {
		this.sichtbarkeit = new JsonObjectProperty<>(sichtbarkeit);
		this.rueckgabeTyp = new JsonObjectProperty<>(datentyp);
		this.name = new JsonStringProperty("");
		this.istAbstrakt = new JsonBooleanProperty(false);
		this.istFinal = new JsonBooleanProperty(false);
		this.parameterListe = FXCollections.observableList(new LinkedList<>());
		
		this.ueberwachePropertyAenderung(this.sichtbarkeit);
		this.ueberwachePropertyAenderung(this.rueckgabeTyp);
		this.ueberwachePropertyAenderung(this.name);
		this.ueberwachePropertyAenderung(this.name);
		this.ueberwachePropertyAenderung(this.istAbstrakt);
		this.ueberwachePropertyAenderung(this.istFinal);
		this.ueberwacheListenAenderung(this.parameterListe);
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
	
	@JsonProperty("rueckgabeTyp")
	public Datentyp getRueckgabeTyp() {
		return rueckgabeTyp.get();
	}
	
	@JsonProperty("rueckgabeTyp")
	public void setRueckgabeTyp(Datentyp rueckgabeTyp) {
		this.rueckgabeTyp.set(rueckgabeTyp);
	}
	
	@JsonProperty("istAbstrakt")
	public boolean istAbstrakt() {
		return istAbstrakt.get();
	}
	
	public void setzeAbstrakt(boolean istAbstrakt) {
		this.istAbstrakt.set(istAbstrakt);
	}
	
	public boolean istFinal() {
		return istFinal.get();
	}
	
	public void setzeFinal(boolean istFinal) {
		this.istFinal.set(istFinal);
	}
	
	public ObservableList<Parameter> getParameterListe() {
		return parameterListe;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ObjectProperty<Modifizierer> getSichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public StringProperty getNameProperty() {
		return name;
	}
	
	public ObjectProperty<Datentyp> getRueckgabetypProperty() {
		return rueckgabeTyp;
	}
	
	public BooleanProperty getIstAbstraktProperty() {
		return istAbstrakt;
	}
	
	public BooleanProperty getIstFinalProperty() {
		return istFinal;
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
		return "Methode <%s %s %s(%s) {%s %s}>".formatted(
				getSichtbarkeit(),
				getRueckgabeTyp().getTypName(),
				getName(),
				getParameterListe().stream()
						.map(p -> String.join(" ", p.getDatentyp().getTypName(), p.getName()))
						.collect(Collectors.joining(", ")),
				istAbstrakt() ? " abstract" : "",
				istFinal() ? "final " : "");
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(istAbstrakt(), istFinal(), getName(), getParameterListe(),
				getRueckgabeTyp(), getSichtbarkeit());
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
		
		boolean istAbstraktGleich = istAbstrakt() == m.istAbstrakt();
		boolean istFinalGleich = istFinal() == m.istFinal();
		boolean nameGleich = Objects.equals(getName(), m.getName());
		boolean parameterListeGleich = ClassifierUtil.pruefeGleichheit(
				this.getParameterListe(),
				m.getParameterListe());
		boolean rueckgabeTypGleich = Objects.equals(getRueckgabeTyp(), m.getRueckgabeTyp());
		boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(), m.getSichtbarkeit());
		
		boolean istGleich = istAbstraktGleich && istFinalGleich && nameGleich
				&& parameterListeGleich && rueckgabeTypGleich && sichtbarkeitGleich;
		
		log.finest(() -> """
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

	public Methode erzeugeTiefeKopie() {
		var kopie = new Methode(getSichtbarkeit(), getRueckgabeTyp().erzeugeTiefeKopie());
		kopie.setName(getName());
		kopie.setzeAbstrakt(istAbstrakt());
		kopie.setzeFinal(istFinal());
		
		for(var parameter : parameterListe) {
			kopie.parameterListe.add(parameter.erzeugeTiefeKopie());
		}
		
		return kopie;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}