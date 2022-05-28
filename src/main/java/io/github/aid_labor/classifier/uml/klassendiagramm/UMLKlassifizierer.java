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

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.uml.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.eigenschaften.Datentyp;
import io.github.aid_labor.classifier.uml.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class UMLKlassifizierer implements UMLDiagrammElement, Datentyp {
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
	
	private KlassifiziererTyp typ;
	private final Programmiersprache programmiersprache;
	private String paket;
	private String name;
	private final ObservableList<Attribut> attribute;
	private final ObservableList<Methode> methoden;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKlassifizierer(KlassifiziererTyp typ, Programmiersprache programmiersprache,
			String paket, String name) {
		this.typ = typ;
		this.programmiersprache = programmiersprache;
		this.paket = paket;
		this.name = name;
		this.attribute = FXCollections.observableList(new LinkedList<>());
		this.methoden = FXCollections.observableList(new LinkedList<>());
	}
	
	@JsonCreator
	protected UMLKlassifizierer(
			@JsonProperty("typ") KlassifiziererTyp typ,
			@JsonProperty("programmiersprache") Programmiersprache programmiersprache,
			@JsonProperty("paket") String paket,
			@JsonProperty("name") String name,
			@JsonProperty("attribute") List<Attribut> attribute,
			@JsonProperty("methoden") List<Methode> methoden) {
		this(typ, programmiersprache, paket, name);
		this.attribute.addAll(attribute);
		this.methoden.addAll(methoden);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public String getTypName() {
		return name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public KlassifiziererTyp getTyp() {
		return typ;
	}
	
	public void setTyp(KlassifiziererTyp typ) {
		this.typ = typ;
	}
	
	public String getStereotyp() {
		return typ.getStereotyp();
	}
	
	public String getPaket() {
		return paket;
	}
	
	public void setPaket(String paket) {
		this.paket = paket;
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
	public int hashCode() {
		return Objects.hash(attribute, methoden, name, paket, programmiersprache, typ);
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
		
		boolean attributeGleich = ClassifierUtil.pruefeGleichheit(this.attribute,
				klassifizierer.attribute);
		boolean methodenGleich = ClassifierUtil.pruefeGleichheit(this.methoden,
				klassifizierer.methoden);
		boolean nameGleich = Objects.equals(name, klassifizierer.name);
		boolean paketGleich = Objects.equals(paket, klassifizierer.paket);
		boolean programmierspracheGleich = programmiersprache == klassifizierer.programmiersprache;
		boolean typGleich = typ == klassifizierer.typ;
		
		boolean istGleich = attributeGleich && methodenGleich && nameGleich && paketGleich
				&& programmierspracheGleich && typGleich;
		
		log.finer(() -> """
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}