/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Objects;
import java.util.function.Predicate;

import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class UMLVerbindungAnsicht extends Pane {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static double VERERBUNGSPFEIL_GROESSE = 25;
	private static double ASSOZIATIONSPFEIL_GROESSE = 20;
	private static String VERERBUNGSPFEIL_CSS_KLASSE = "vererbungs-pfeil";
	private static String ASSOZIATIONSPFEIL_CSS_KLASSE = "assoziations-pfeil";
	private static String VERBINDUNGSLINIE_CSS_KLASSE = "verbindungslinie";

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
	
	private final UMLVerbindung verbindung;
	private final UMLProjekt projekt;
	private final ObjectBinding<UMLKlassifizierer> startElement;
	private final ObjectBinding<UMLKlassifizierer> endElement;
	private final DoubleProperty startXProperty;
	private final DoubleProperty startYProperty;
	private final DoubleProperty endeXProperty;
	private final DoubleProperty endeYProperty;
	private final Path linie;
	private final Path pfeil;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLVerbindungAnsicht(UMLVerbindung verbindung, UMLProjekt projekt) {
		this.verbindung = verbindung;
		this.projekt = projekt;
		this.startXProperty = new SimpleDoubleProperty();
		this.startYProperty = new SimpleDoubleProperty();
		this.endeXProperty = new SimpleDoubleProperty();
		this.endeYProperty = new SimpleDoubleProperty();
//		this.visibleProperty().bind(verbindung.ausgebelendetProperty().not());
		this.linie = new Path();
		this.pfeil = new Path();
		
		NodeUtil.beobachteSchwach(this, startXProperty, verbindung.getStartPosition().xProperty()::setValue);
		NodeUtil.beobachteSchwach(this, startYProperty, verbindung.getStartPosition().yProperty()::setValue);
		NodeUtil.beobachteSchwach(this, endeXProperty, verbindung.getEndPosition().xProperty()::setValue);
		NodeUtil.beobachteSchwach(this, endeYProperty, verbindung.getEndPosition().yProperty()::setValue);
		
		startElement = sucheKlassifizierer(verbindung.verbindungsStartProperty());
		endElement = sucheKlassifizierer(verbindung.verbindungsEndeProperty());
		
		NodeUtil.beobachteSchwach(this, startElement, this::updateStartPosition);
		NodeUtil.beobachteSchwach(this, endElement, this::updateEndPosition);
		
		updateStartPosition(startElement.get());
		updateEndPosition(endElement.get());
		zeichneLinie(startElement.get(), endElement.get());
		
		switch (verbindung.getTyp()) {
			case VERERBUNG -> {
				zeichneVererbungsPfeil();
				this.pfeil.getStyleClass().add(VERERBUNGSPFEIL_CSS_KLASSE);
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + verbindung.getTyp());
		}
		
		this.linie.getStyleClass().add(VERBINDUNGSLINIE_CSS_KLASSE);
		this.getChildren().addAll(linie, pfeil);
		if (verbindung.istAusgebelendet()) {
			this.linie.setId("ROT");
		} else {
			this.linie.setId("SCHWARZ");
		}
		verbindung.ausgebelendetProperty().addListener((p, a, n) -> {
			if (n) {
				this.linie.setId("ROT");
			} else {
				this.linie.setId("SCHWARZ");
			}
		});
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private Predicate<UMLDiagrammElement> erzeugeNameVergleich(String gesuchterName) {
		return e -> e instanceof UMLKlassifizierer k && Objects.equals(gesuchterName, k.getName());
	}
	
	private ObjectBinding<UMLKlassifizierer> sucheKlassifizierer(StringProperty name) {
		var startelemente = projekt.getDiagrammElemente().filtered(erzeugeNameVergleich(name.get()));
		startelemente.predicateProperty()
				.bind(Bindings.createObjectBinding(() -> erzeugeNameVergleich(name.get()), name));
		var hilfe = Bindings.valueAt(startelemente, 0);
		return Bindings.createObjectBinding(() -> (UMLKlassifizierer) hilfe.get(), hilfe);
	}
	
	private void zeichneLinie(UMLKlassifizierer start, UMLKlassifizierer ende) {
		var yMitte = verbindung.getStartPosition().yProperty().add(verbindung.getEndPosition().yProperty()).divide(2);
		
		MoveTo startpunkt = new MoveTo();
		startpunkt.xProperty().bind(verbindung.getStartPosition().xProperty());
		startpunkt.yProperty().bind(verbindung.getStartPosition().yProperty());
		LineTo linieA = new LineTo();
		linieA.xProperty().bind(verbindung.getStartPosition().xProperty());
		linieA.yProperty().bind(yMitte);
		
		LineTo linieB = new LineTo();
		linieB.xProperty().bind(verbindung.getEndPosition().xProperty());
		linieB.yProperty().bind(yMitte);
		
		LineTo linieC = new LineTo();
		linieC.xProperty().bind(verbindung.getEndPosition().xProperty());
		linieC.yProperty().bind(verbindung.getEndPosition().yProperty());
		
		this.linie.getElements().add(startpunkt);
		this.linie.getElements().add(linieA);
		this.linie.getElements().add(linieB);
		this.linie.getElements().add(linieC);
	}
	
	private void zeichneVererbungsPfeil() {
		MoveTo startpunkt = new MoveTo();
		startpunkt.xProperty().bind(verbindung.getEndPosition().xProperty());
		startpunkt.yProperty().bind(verbindung.getEndPosition().yProperty());
		
		LineTo linieA = new LineTo();
		linieA.xProperty().bind(startpunkt.xProperty().subtract(VERERBUNGSPFEIL_GROESSE / 2));
		linieA.yProperty().bind(startpunkt.yProperty().add(VERERBUNGSPFEIL_GROESSE));
		
		LineTo linieB = new LineTo();
		linieB.xProperty().bind(startpunkt.xProperty().add(VERERBUNGSPFEIL_GROESSE / 2));
		linieB.yProperty().bind(startpunkt.yProperty().add(VERERBUNGSPFEIL_GROESSE));
		
		LineTo linieC = new LineTo();
		linieC.xProperty().bind(startpunkt.xProperty());
		linieC.yProperty().bind(startpunkt.yProperty());
		
		this.pfeil.getElements().add(startpunkt);
		this.pfeil.getElements().add(linieA);
		this.pfeil.getElements().add(linieB);
		this.pfeil.getElements().add(linieC);
		this.pfeil.getElements().add(new ClosePath());
	}
	
	private void updateStartPosition(UMLKlassifizierer start) {
		if (start == null) {
			startXProperty.unbind();
			startYProperty.unbind();
			return;
		}
		
		var xBindung = start.getPosition().xProperty().add(start.getPosition().breiteProperty().divide(2));
		startXProperty.bind(xBindung);
		startYProperty.bind(start.getPosition().yProperty());
	}
	
	private void updateEndPosition(UMLKlassifizierer ende) {
		if (ende == null) {
			endeXProperty.unbind();
			endeYProperty.unbind();
			return;
		}
		
		var xBindung = ende.getPosition().xProperty().add(ende.getPosition().breiteProperty().divide(2));
		endeXProperty.bind(xBindung);
		
		var yBindung = ende.getPosition().yProperty().add(ende.getPosition().hoeheProperty());
		endeYProperty.bind(yBindung);
	}
}