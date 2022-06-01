/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.github.aid_labor.classifier.basis.json.JsonDoubleProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.projekt.EditierBefehl;
import io.github.aid_labor.classifier.basis.projekt.EditierBeobachter;
import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.EditierbarerBeobachter;


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
@JsonSubTypes({
	@JsonSubTypes.Type(value = UMLKlassifizierer.class),
	@JsonSubTypes.Type(value = UMLKommentar.class)
})
abstract class UMLBasisElement extends EditierbarBasis
		implements UMLDiagrammElement, EditierBeobachter {
//	private static final Logger log = Logger.getLogger(UMLBasisElement.class.getName());
	
	public static class Position extends EditierbarBasis implements EditierbarerBeobachter {
		
		private JsonDoubleProperty x;
		private JsonDoubleProperty y;
		private JsonDoubleProperty hoehe;
		private JsonDoubleProperty breite;
		
		public Position() {
			this(0, 0, 0, 0);
		}
		
		public Position(double x, double y, double hoehe, double breite) {
			this.x = new JsonDoubleProperty(x);
			this.y = new JsonDoubleProperty(y);
			this.hoehe = new JsonDoubleProperty(hoehe);
			this.breite = new JsonDoubleProperty(breite);
			
			this.ueberwachePropertyAenderung(this.x);
			this.ueberwachePropertyAenderung(this.y);
			this.ueberwachePropertyAenderung(this.hoehe);
			this.ueberwachePropertyAenderung(this.breite);
		}

		public double getX() {
			return x.get();
		}

		public void setX(double x) {
			this.x.set(x);
		}

		public double getY() {
			return y.get();
		}

		public void setY(double y) {
			this.y.set(y);
		}

		public double getHoehe() {
			return hoehe.get();
		}

		public void setHoehe(double hoehe) {
			this.hoehe.set(hoehe);
		}

		public double getBreite() {
			return breite.get();
		}

		public void setBreite(double breite) {
			this.breite.set(breite);
		}
		
		public JsonDoubleProperty getXProperty() {
			return x;
		}
		
		public JsonDoubleProperty getYProperty() {
			return y;
		}
		
		public JsonDoubleProperty getHoeheProperty() {
			return hoehe;
		}
		
		public JsonDoubleProperty getBreiteProperty() {
			return breite;
		}
	}

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
	
	private final JsonObjectProperty<Position> position;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLBasisElement(Position position) {
		this.position = new JsonObjectProperty<>(position);
		this.ueberwachePropertyAenderung(this.position);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}