/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import io.github.aid_labor.classifier.basis.json.JsonDoubleProperty;
import io.github.aid_labor.classifier.basis.projekt.EditierBefehl;
import io.github.aid_labor.classifier.basis.projekt.EditierBeobachter;
import io.github.aid_labor.classifier.basis.projekt.EditierbarBasis;


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
	
	public static class Position {
		
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
		
		@JsonIgnore
		public JsonDoubleProperty getXProperty() {
			return x;
		}
		
		@JsonIgnore
		public JsonDoubleProperty getYProperty() {
			return y;
		}
		
		@JsonIgnore
		public JsonDoubleProperty getHoeheProperty() {
			return hoehe;
		}
		
		@JsonIgnore
		public JsonDoubleProperty getBreiteProperty() {
			return breite;
		}

		@JsonIgnore
		public void setPosition(Position position) {
			this.setX(position.getX());
			this.setY(position.getY());
			this.setHoehe(position.getHoehe());
			this.setBreite(position.getBreite());
		}

		public void set(Position position) {
			this.setX(position.getX());
			this.setY(position.getY());
			this.setBreite(position.getBreite());
			this.setHoehe(position.getHoehe());
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
	
	private final Position position;
	@JsonIgnore private int id;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLBasisElement() {
		this.position = new Position(10, 10, -1, -1);
		this.ueberwachePropertyAenderung(this.position.x);
		this.ueberwachePropertyAenderung(this.position.y);
		this.ueberwachePropertyAenderung(this.position.hoehe);
		this.ueberwachePropertyAenderung(this.position.breite);
	}
	
	public UMLBasisElement(Position position) {
		this.position = position;
		this.ueberwachePropertyAenderung(this.position.x);
		this.ueberwachePropertyAenderung(this.position.y);
		this.ueberwachePropertyAenderung(this.position.hoehe);
		this.ueberwachePropertyAenderung(this.position.breite);
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
	public int getId() {
		return id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}