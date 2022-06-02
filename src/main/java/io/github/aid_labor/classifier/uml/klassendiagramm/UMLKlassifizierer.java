/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty.EnumPropertyZuStringKonverter;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.ListenUeberwacher;
import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class UMLKlassifizierer extends UMLBasisElement {
	private static final Logger log = Logger.getLogger(UMLKlassifizierer.class.getName());
	
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
	
	@JsonSerialize(converter = EnumPropertyZuStringKonverter.class)
	private final JsonEnumProperty<KlassifiziererTyp> typ;
	private final JsonObjectProperty<Modifizierer> sichtbarkeit;
	private final Programmiersprache programmiersprache;
	private final JsonStringProperty paket;
	private final JsonStringProperty name;
	private final JsonStringProperty superklasse;
	private final JsonStringProperty interfaces;
	private final ObservableList<Attribut> attribute;
	private final ObservableList<Methode> methoden;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKlassifizierer(KlassifiziererTyp typ, Programmiersprache programmiersprache,
			String name) {
		this.typ = new JsonEnumProperty<>(typ);
		this.sichtbarkeit = new JsonObjectProperty<>(Modifizierer.PUBLIC);
		this.programmiersprache = programmiersprache;
		this.paket = new JsonStringProperty("");
		this.name = new JsonStringProperty(name);
		this.superklasse = new JsonStringProperty("");
		this.interfaces = new JsonStringProperty("");
		this.attribute = FXCollections.observableList(new LinkedList<>());
		this.methoden = FXCollections.observableList(new LinkedList<>());
		
		this.attribute.addListener(new ListenUeberwacher<>(this.attribute, this));
		this.methoden.addListener(new ListenUeberwacher<>(this.methoden, this));
		this.ueberwachePropertyAenderung(this.name);
		this.ueberwachePropertyAenderung(this.paket);
		this.ueberwachePropertyAenderung(this.typ);
		this.ueberwachePropertyAenderung(this.superklasse);
		this.ueberwachePropertyAenderung(this.interfaces);
	}
	
	@JsonCreator
	protected UMLKlassifizierer(
			@JsonProperty("typ") KlassifiziererTyp typ,
			@JsonProperty("programmiersprache") Programmiersprache programmiersprache,
			@JsonProperty("name") String name,
			@JsonProperty("position") Position position,
			@JsonProperty("attribute") List<Attribut> attribute,
			@JsonProperty("methoden") List<Methode> methoden) {
		this(typ, programmiersprache, name);
		this.getPosition().setPosition(position);
		this.attribute.addAll(attribute);
		this.methoden.addAll(methoden);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public String getName() {
		return name.get();
	}
	
	public void setName(String name) {
		this.name.set(name);
	}
	
	public StringProperty nameProperty() {
		return name;
	}
	
	public KlassifiziererTyp getTyp() {
		return typ.get();
	}
	
	public void setTyp(KlassifiziererTyp typ) {
		this.typ.set(typ);
	}
	
	public ObjectProperty<KlassifiziererTyp> getTypProperty() {
		return typ;
	}
	
	public String getSuperklasse() {
		return superklasse.get();
	}
	
	public void setSuperklasse(String superklasse) {
		this.superklasse.set(superklasse);
	}
	
	public StringProperty superklasseProperty() {
		return superklasse;
	}
	
	public String getInterfaces() {
		return interfaces.get();
	}
	
	public void setInterfaces(String interfaces) {
		this.interfaces.set(interfaces);
	}
	
	public StringProperty interfacesProperty() {
		return interfaces;
	}
	
	public Modifizierer getSichtbarkeit() {
		return sichtbarkeit.get();
	}
	
	public void setSichtbarkeit(Modifizierer sichtbarkeit) {
		this.sichtbarkeit.set(sichtbarkeit);
	}
	
	public ObjectProperty<Modifizierer> getSichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public String getStereotyp() {
		return typ.get().getStereotyp();
	}
	
	public String getPaket() {
		return paket.get();
	}
	
	public void setPaket(String paket) {
		this.paket.set(paket);
	}
	
	public StringProperty getPaketProperty() {
		return paket;
	}
	
	public Programmiersprache getProgrammiersprache() {
		return programmiersprache;
	}
	
	public ObservableList<Attribut> getAttribute() {
		return attribute;
	}
	
	public ObservableList<Methode> getMethoden() {
		return methoden;
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
		return """
				%s@%s {
				     typ: %s
				     paket: %s
				     name: %s
				  }"""
				.formatted(this.getClass().getSimpleName(),
						Integer.toHexString(((Object) this).hashCode()), this.getTyp().name(),
						this.getPaket(), this.getName());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getAttribute(), getMethoden(), getName(), getPaket(),
				getProgrammiersprache(), getTyp());
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
		UMLKlassifizierer klassifizierer = (UMLKlassifizierer) obj;
		
		boolean attributeGleich = ClassifierUtil.pruefeGleichheit(this.getAttribute(),
				klassifizierer.getAttribute());
		boolean methodenGleich = ClassifierUtil.pruefeGleichheit(this.getMethoden(),
				klassifizierer.getMethoden());
		boolean nameGleich = Objects.equals(getName(), klassifizierer.getName());
		boolean paketGleich = Objects.equals(getPaket(), klassifizierer.getPaket());
		boolean programmierspracheGleich = getProgrammiersprache()
				.equals(klassifizierer.getProgrammiersprache());
		boolean typGleich = getTyp().equals(klassifizierer.getTyp());
		
		boolean istGleich = attributeGleich && methodenGleich && nameGleich && paketGleich
				&& programmierspracheGleich && typGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- attributeGleich: %s
				   |-- methodenGleich: %s
				   |-- nameGleich: %s
				   |-- paketGleich: %s
				   |-- programmierspracheGleich: %s
				   ╰-- typGleich: %s"""
				.formatted(istGleich, attributeGleich, methodenGleich, nameGleich, paketGleich,
						programmierspracheGleich, typGleich));
		
		return istGleich;
	}
	
	public UMLKlassifizierer erzeugeTiefeKopie() {
		var kopie = new UMLKlassifizierer(getTyp(), getProgrammiersprache(), getName());
		kopie.setPaket(getPaket());
		kopie.getPosition().set(getPosition());
		
		for (var attribut : attribute) {
			kopie.attribute.add(attribut.erzeugeTiefeKopie());
		}
		
		for (var methode : methoden) {
			kopie.methoden.add(methode.erzeugeTiefeKopie());
		}
		
		return kopie;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}