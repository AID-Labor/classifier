/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarerBeobachter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Konstruktor extends EditierbarBasis implements EditierbarerBeobachter, HatParameterListe {
	private static final Logger log = Logger.getLogger(Konstruktor.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static long naechsteId = 0;
	
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
	private ObservableList<Parameter> parameterListe;
	@JsonIgnore
	private final List<Object> beobachterListe;
	@JsonIgnore
	private final long id;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public Konstruktor(Modifizierer sichtbarkeit) {
		this.id = naechsteId++;
		this.beobachterListe = new LinkedList<>();
		
		this.name = new JsonStringProperty(this, "konstruktornname", "");
		this.parameterListe = FXCollections.observableList(new LinkedList<>());
		
		this.sichtbarkeit = new JsonObjectProperty<>(this, "sichtbarkeit", sichtbarkeit);
		
		this.ueberwachePropertyAenderung(this.sichtbarkeit, id + "_konstruktor_sichtbarkeit");
		this.ueberwachePropertyAenderung(this.name, id + "_konstruktor_name");
		
		this.ueberwacheListenAenderung(this.parameterListe);
	}
	
	@JsonCreator
	protected Konstruktor(@JsonProperty("sichtbarkeit") Modifizierer sichtbarkeit, @JsonProperty("name") String name,
			@JsonProperty("parameterListe") List<Parameter> parameterListe) {
		this(sichtbarkeit);
		this.parameterListe.addAll(parameterListe);
		this.setName(name);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public List<Object> getBeobachterListe() {
		return beobachterListe;
	}
	
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
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public ObservableList<Parameter> parameterListeProperty() {
		return parameterListe;
	}
	
	public ObjectProperty<Modifizierer> sichtbarkeitProperty() {
		return sichtbarkeit;
	}
	
	public StringProperty nameProperty() {
		return name;
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
		return "Methode <%s %s(%s)>".formatted(getSichtbarkeit(), getName(),
				parameterListeProperty().stream().map(p -> String.join(" ", p.getDatentyp().getTypName(), p.getName()))
						.collect(Collectors.joining(", ")));
	}
	
	@Override
	public int hashCode() {
		return ClassifierUtil.hashAlle(getName(), parameterListeProperty(), getSichtbarkeit());
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
		Konstruktor k = (Konstruktor) obj;
		
		boolean nameGleich = Objects.equals(getName(), k.getName());
		boolean parameterListeGleich = ClassifierUtil.pruefeGleichheit(this.parameterListeProperty(),
				k.parameterListeProperty());
		boolean sichtbarkeitGleich = Objects.equals(getSichtbarkeit(), k.getSichtbarkeit());
		
		boolean istGleich = nameGleich && parameterListeGleich && sichtbarkeitGleich;
		
		log.finest(() -> """
				istGleich: %s
				   |-- nameGleich: %s
				   |-- parameterListeGleich: %s
				   ╰-- sichtbarkeitGleich: %s""".formatted(istGleich, nameGleich, parameterListeGleich,
				sichtbarkeitGleich));
		
		return istGleich;
	}
	
	public Konstruktor erzeugeTiefeKopie() {
		var kopie = new Konstruktor(getSichtbarkeit());
		kopie.setName(getName());
		
		for (var parameter : parameterListe) {
			kopie.parameterListe.add(parameter.erzeugeTiefeKopie());
		}
		
		return kopie;
	}
	
	@Override
	public void close() throws Exception {
		log.finest(() -> this + " leere Parameter");
		for (var param : parameterListe) {
			param.close();
		}
		try {
			parameterListe.clear();
		} catch (Exception e) {
			// ignorieren fuer Empty List
		}
		log.finest(() -> this + " leere listeners");
		beobachterListe.clear();
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}