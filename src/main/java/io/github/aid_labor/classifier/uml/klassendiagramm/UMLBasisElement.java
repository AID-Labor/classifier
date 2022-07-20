/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.github.aid_labor.classifier.basis.projekt.editierung.EditierBefehl;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierBeobachter;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;


/**
 * Basis-Implementierung fuer UML-Diagrammelemente.
 * 
 * Diese Klasse stellt die Funktion zum Registrieren, Entfernen und Informieren von
 * Beobachtern
 * bereit. Unterklassen muessen bei Editierung die Methode
 * {@link #informiere(EditierBefehl)}
 * aufrufen, um ihre Beobachter zu informieren.
 * 
 * @author Tim Muehle
 *
 */
@JsonSubTypes({ @JsonSubTypes.Type(value = UMLKlassifizierer.class), @JsonSubTypes.Type(value = UMLKommentar.class) })
abstract class UMLBasisElement extends EditierbarBasis implements UMLDiagrammElement, EditierBeobachter {
//	private static final Logger log = Logger.getLogger(UMLBasisElement.class.getName());

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
	
	private final Position position;
	@JsonIgnore
	private long id;
	@JsonIgnore
	private final List<Object> beobachterListe;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	UMLBasisElement() {
		this.position = new Position(10, 10, -1, -1, this);
		this.beobachterListe = new LinkedList<>();
		this.ueberwachePropertyAenderung(this.position.xProperty(), getId() + "_x_position");
		this.ueberwachePropertyAenderung(this.position.yProperty(), getId() + "_y_position");
		this.ueberwachePropertyAenderung(this.position.hoeheProperty(), getId() + "_hoehe");
		this.ueberwachePropertyAenderung(this.position.breiteProperty(), getId() + "_breite");
	}
	
	@JsonCreator
	UMLBasisElement(Position position) {
		this();
		this.position.set(position);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public Position getPosition() {
		return position;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public List<Object> getBeobachterListe() {
		return beobachterListe;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Dieses Objekt leitet alle Informationen zu Editierungen weiter an seine eigenen
	 * Beobachter
	 */
	@Override
	public void verarbeiteEditierung(EditierBefehl editierung) {
		log.finest(() -> "verarbeite " + editierung);
		informiere(editierung);
	}
	
	@Override
	public void schliesse() throws Exception {
		log.finest(() -> this + " leere editierBeobachter");
		beobachterListe.clear();
		
		for (var attribut : this.getClass().getDeclaredFields()) {
			try {
				if (!Modifier.isStatic(attribut.getModifiers())) {
					attribut.setAccessible(true);
					attribut.set(this, null);
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}
	
	@Override
	public final void close() throws Exception {
		UMLDiagrammElement.super.close();
	}
	
	private boolean darfGeschlossenWerden = false;
	
	@Override
	public boolean darfGeschlossenWerden() {
		return darfGeschlossenWerden;
	}
	
	@Override
	public void setDarfGeschlossenWerden(boolean wert) {
		darfGeschlossenWerden = wert;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}