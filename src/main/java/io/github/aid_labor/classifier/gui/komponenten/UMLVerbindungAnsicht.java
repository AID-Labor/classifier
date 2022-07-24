/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Objects;
import java.util.function.Consumer;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.Orientierung;
import io.github.aid_labor.classifier.uml.klassendiagramm.Position;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class UMLVerbindungAnsicht extends Group implements AutoCloseable {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final double VERERBUNGSPFEIL_GROESSE = 25;
	private static final double ASSOZIATIONSPFEIL_GROESSE = 15;
	private static final String VERERBUNGSPFEIL_CSS_KLASSE = "vererbungs-pfeil";
	private static final String ASSOZIATIONSPFEIL_CSS_KLASSE = "assoziations-pfeil";
	private static final String VERBINDUNGSLINIE_CSS_KLASSE = "verbindungslinie";
	
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
	private final DoubleProperty startXProperty;
	private final DoubleProperty startYProperty;
	private final DoubleProperty endeXProperty;
	private final DoubleProperty endeYProperty;
	private final DoubleProperty startXVerschiebungProperty;
	private final DoubleProperty startYVerschiebungProperty;
	private final DoubleProperty mitteVerschiebungProperty;
	private final DoubleProperty endeXVerschiebungProperty;
	private final DoubleProperty endeYVerschiebungProperty;
	private final Path linieStart;
	private final Path linieMitte;
	private final Path linieEnde;
	private final Path pfeil;
	private final ObjectProperty<Orientierung> orientierungStartProperty;
	private final ObjectProperty<Orientierung> orientierungEndeProperty;
	private final MoveTo startpunkt;
	private final MoveTo startpunktMitte;
	private final MoveTo startpunktEnde;
	private final LineTo linieA;
	private final LineTo linieB;
	private final LineTo linieC;
	private final ChangeListener<Number> positionsUeberwachung;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLVerbindungAnsicht(UMLVerbindung verbindung, UMLProjekt projekt) {
		this.verbindung = verbindung;
		this.projekt = projekt;
		this.startXProperty = new SimpleDoubleProperty(verbindung.getStartPosition().getX());
		this.startYProperty = new SimpleDoubleProperty(verbindung.getStartPosition().getY());
		this.endeXProperty = new SimpleDoubleProperty(verbindung.getEndPosition().getX());
		this.endeYProperty = new SimpleDoubleProperty(verbindung.getEndPosition().getY());
		this.startXVerschiebungProperty = new SimpleDoubleProperty(verbindung.getStartPosition().getBreite());
		this.startYVerschiebungProperty = new SimpleDoubleProperty(verbindung.getStartPosition().getHoehe());
		this.mitteVerschiebungProperty = new SimpleDoubleProperty(verbindung.getMitteVerschiebung());
		double mitteVerschiebung = verbindung.getMitteVerschiebung();
		this.endeXVerschiebungProperty = new SimpleDoubleProperty(verbindung.getEndPosition().getBreite());
		this.endeYVerschiebungProperty = new SimpleDoubleProperty(verbindung.getEndPosition().getHoehe());
		double startXVerschiebung = verbindung.getStartPosition().getBreite();
		double startYVerschiebung = verbindung.getStartPosition().getHoehe();
		double endeXVerschiebung = verbindung.getEndPosition().getBreite();
		double endeYVerschiebung = verbindung.getEndPosition().getHoehe();
		this.orientierungStartProperty = new SimpleObjectProperty<>(verbindung.getOrientierungStart());
		this.orientierungEndeProperty = new SimpleObjectProperty<>(verbindung.getOrientierungEnde());
		var orientierungStart = verbindung.getOrientierungStart();
		var orientierungEnde = verbindung.getOrientierungEnde();
		this.visibleProperty().bind(verbindung.ausgebelendetProperty().not());
		this.linieStart = new Path();
		this.linieMitte = new Path();
		this.linieEnde = new Path();
		this.pfeil = new Path();
		this.startpunkt = new MoveTo();
		this.startpunktMitte = new MoveTo();
		this.startpunktEnde = new MoveTo();
		this.linieA = new LineTo();
		this.linieB = new LineTo();
		this.linieC = new LineTo();
		this.positionsUeberwachung = (p, alt, neu) -> {
			updateStartPosition(verbindung.getStartElement(), verbindung.getEndElement());
			updateEndPosition(verbindung.getStartElement(), verbindung.getEndElement());
		};
		
		zeichneLinie();
		erstelleBindungen();
		updateStartPosition(verbindung.getStartElement(), verbindung.getEndElement());
		updateEndPosition(verbindung.getStartElement(), verbindung.getEndElement());
		
		updateLinie(orientierungStartProperty.get(), verbindung.getStartElement(), orientierungEndeProperty.get(),
				verbindung.getEndElement());
		
		switch (verbindung.getTyp()) {
			case VERERBUNG, SCHNITTSTELLEN_VERERBUNG -> {
				zeichneVererbungsPfeil();
				this.pfeil.getStyleClass().add(VERERBUNGSPFEIL_CSS_KLASSE);
			}
			case ASSOZIATION -> {
				zeichneAssoziationsPfeil();
				this.pfeil.getStyleClass().add(ASSOZIATIONSPFEIL_CSS_KLASSE);
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + verbindung.getTyp());
		}
		
		this.getChildren().add(pfeil);
		
		if (orientierungStart != null && orientierungEnde != null && !orientierungStart.equals(Orientierung.UNBEKANNT)
				&& !orientierungEnde.equals(Orientierung.UNBEKANNT)) {
			this.orientierungStartProperty.set(orientierungStart);
			this.orientierungEndeProperty.set(orientierungEnde);
			updateStart(orientierungStart, verbindung.getStartElement());
			updateEnde(orientierungEnde, verbindung.getEndElement());
		}
		
		this.startXVerschiebungProperty.set(startXVerschiebung);
		this.startYVerschiebungProperty.set(startYVerschiebung);
		this.endeXVerschiebungProperty.set(endeXVerschiebung);
		this.endeYVerschiebungProperty.set(endeYVerschiebung);
		this.mitteVerschiebungProperty.set(mitteVerschiebung);
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
	
	@Override
	public void close() throws Exception {
		loeseVerbindungen();
	}
	
	public void loeseVerbindungen() {
		startYProperty.unbindBidirectional(verbindung.getStartPosition().yProperty());
		startXVerschiebungProperty.unbindBidirectional(verbindung.getStartPosition().breiteProperty());
		startYVerschiebungProperty.unbindBidirectional(verbindung.getStartPosition().hoeheProperty());
		endeXProperty.unbindBidirectional(verbindung.getEndPosition().xProperty());
		endeYProperty.unbindBidirectional(verbindung.getEndPosition().yProperty());
		endeXVerschiebungProperty.unbindBidirectional(verbindung.getEndPosition().breiteProperty());
		endeYVerschiebungProperty.unbindBidirectional(verbindung.getEndPosition().hoeheProperty());
		orientierungStartProperty.unbindBidirectional(verbindung.orientierungStartProperty());
		orientierungEndeProperty.unbindBidirectional(verbindung.orientierungEndeProperty());
		mitteVerschiebungProperty.unbindBidirectional(verbindung.mitteVerschiebungProperty());
		
		NodeUtil.entferneSchwacheBeobachtung(this);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void erstelleBindungen() {
		startXProperty.bindBidirectional(verbindung.getStartPosition().xProperty());
		startYProperty.bindBidirectional(verbindung.getStartPosition().yProperty());
		startXVerschiebungProperty.bindBidirectional(verbindung.getStartPosition().breiteProperty());
		startYVerschiebungProperty.bindBidirectional(verbindung.getStartPosition().hoeheProperty());
		endeXProperty.bindBidirectional(verbindung.getEndPosition().xProperty());
		endeYProperty.bindBidirectional(verbindung.getEndPosition().yProperty());
		endeXVerschiebungProperty.bindBidirectional(verbindung.getEndPosition().breiteProperty());
		endeYVerschiebungProperty.bindBidirectional(verbindung.getEndPosition().hoeheProperty());
		orientierungStartProperty.bindBidirectional(verbindung.orientierungStartProperty());
		orientierungEndeProperty.bindBidirectional(verbindung.orientierungEndeProperty());
		mitteVerschiebungProperty.bindBidirectional(verbindung.mitteVerschiebungProperty());
		
		NodeUtil.beobachteSchwach(this, verbindung.startElementProperty(),
				start -> updateStartPosition(start, verbindung.getEndElement()));
		NodeUtil.beobachteSchwach(this, verbindung.endElementProperty(),
				ende -> updateEndPosition(verbindung.getStartElement(), ende));
		NodeUtil.beobachteSchwach(this, orientierungStartProperty, o -> updateLinie(o, verbindung.getStartElement(),
				orientierungEndeProperty.get(), verbindung.getEndElement()));
	}
	
	private void zeichneLinie() {
		startpunkt.xProperty().bind(startXProperty.add(startXVerschiebungProperty));
		startpunkt.yProperty().bind(startYProperty.add(startYVerschiebungProperty));
		
		startpunktMitte.xProperty().bind(linieA.xProperty());
		startpunktMitte.yProperty().bind(linieA.yProperty());
		
		startpunktEnde.xProperty().bind(linieB.xProperty());
		startpunktEnde.yProperty().bind(linieB.yProperty());
		
		linieC.xProperty().bind(endeXProperty.add(endeXVerschiebungProperty));
		linieC.yProperty().bind(endeYProperty.add(endeYVerschiebungProperty));
		
		this.linieStart.getElements().add(startpunkt);
		this.linieStart.getElements().add(linieA);
		this.linieMitte.getElements().add(startpunktMitte);
		this.linieMitte.getElements().add(linieB);
		this.linieEnde.getElements().add(startpunktEnde);
		this.linieEnde.getElements().add(linieC);
		
		for (var linie : new Path[] { linieStart, linieMitte, linieEnde }) {
			linie.getStyleClass().add(VERBINDUNGSLINIE_CSS_KLASSE);
			linie.setViewOrder(Double.MAX_VALUE);
			this.getChildren().add(linie);
		}
		
		var linieAPos = new Position(this, "linieAPos");
		linieAPos.xProperty().bind(startpunkt.xProperty());
		linieAPos.yProperty().bind(startpunkt.yProperty());
		linieAPos.breiteProperty().bind(linieA.xProperty());
		linieAPos.hoeheProperty().bind(linieA.yProperty());
		macheVerschiebbar(linieAPos, startXVerschiebungProperty, startYVerschiebungProperty,
				verbindung.startElementProperty(), orientierungStartProperty, verbindung.endElementProperty(),
				orientierungEndeProperty, neueStartOrientierung -> updateLinie(neueStartOrientierung,
						verbindung.getStartElement(), verbindung.getOrientierungEnde(), verbindung.getEndElement()));
		
		var linieBPos = new Position(this, "linieBPos");
		linieBPos.xProperty().bind(linieA.xProperty());
		linieBPos.yProperty().bind(linieA.yProperty());
		linieBPos.breiteProperty().bind(linieB.xProperty());
		linieBPos.hoeheProperty().bind(linieB.yProperty());
		macheVerschiebbarMitte(linieBPos, mitteVerschiebungProperty, verbindung.startElementProperty(),
				orientierungStartProperty, verbindung.endElementProperty());
		
		var linieCPos = new Position(this, "linieCPos");
		linieCPos.xProperty().bind(startpunktEnde.xProperty());
		linieCPos.yProperty().bind(startpunktEnde.yProperty());
		linieCPos.breiteProperty().bind(linieC.xProperty());
		linieCPos.hoeheProperty().bind(linieC.yProperty());
		macheVerschiebbar(linieCPos, endeXVerschiebungProperty, endeYVerschiebungProperty,
				verbindung.endElementProperty(), orientierungEndeProperty, verbindung.startElementProperty(),
				orientierungStartProperty, neueEndOrientierung -> updateLinie(verbindung.getOrientierungStart(),
						verbindung.getStartElement(), neueEndOrientierung, verbindung.getEndElement()));
	}
	
	private void updateLinie(Orientierung orientierungStart, UMLKlassifizierer start, Orientierung orientierungEnde,
			UMLKlassifizierer ende) {
		if (verbindung.istGeschlossen()) {
			return;
		}
		verbindung.setOrientierungStart(orientierungStart);
		verbindung.setOrientierungEnde(orientierungEnde);
		orientierungStartProperty.set(orientierungStart);
		orientierungEndeProperty.set(orientierungEnde);
		updateStart(orientierungStart, start);
		updateEnde(orientierungEnde, ende);
	}
	
	private void updateStart(Orientierung orientierungStart, UMLKlassifizierer start) {
		if (verbindung.istGeschlossen()) {
			return;
		}
		if (start == null || orientierungStart == null) {
			return;
		}
		switch (orientierungStart) {
			case OBEN -> setzeStartOben(start);
			case RECHTS -> setzeStartRechts(start);
			case UNTEN -> setzeStartUnten(start);
			case LINKS -> setzeStartLinks(start);
			default -> {
				/**/
			}
		}
	}
	
	private void setzeStartOben(UMLKlassifizierer start) {
		startXProperty.bind(start.getPosition().xProperty().add(start.getPosition().breiteProperty().divide(2)));
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty());
		startYVerschiebungProperty.set(0);
		
		var yMitte = startpunkt.yProperty().add(linieC.yProperty()).add(VERERBUNGSPFEIL_GROESSE).divide(2);
		mitteVerschiebungProperty.set(0);
		
		linieA.xProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
						.then(startXProperty.add(startXVerschiebungProperty))
						.otherwise(startXProperty.add(startXVerschiebungProperty)));
		linieA.yProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
						.then(endeYProperty.add(endeYVerschiebungProperty))
						.otherwise(yMitte.add(mitteVerschiebungProperty)));
		linieB.xProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))).then(linieA.xProperty())
						.otherwise(endeXProperty.add(endeXVerschiebungProperty)));
		linieB.yProperty().bind(linieA.yProperty());
	}
	
	private void setzeStartRechts(UMLKlassifizierer start) {
		startXProperty.bind(start.getPosition().xProperty().add(start.getPosition().breiteProperty()));
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty().add(start.getPosition().hoeheProperty().divide(2)));
		startYVerschiebungProperty.set(0);
		bindeStartLinksRechts();
	}
	
	private void setzeStartUnten(UMLKlassifizierer start) {
		startXProperty.bind(start.getPosition().xProperty().add(start.getPosition().breiteProperty().divide(2)));
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty().add(start.getPosition().hoeheProperty()));
		startYVerschiebungProperty.set(0);
		
		var yMitte = verbindung.getStartPosition().yProperty().add(verbindung.getEndPosition().yProperty())
				.subtract(VERERBUNGSPFEIL_GROESSE).divide(2);
		mitteVerschiebungProperty.set(0);
		
		linieA.xProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
						.then(startXProperty.add(startXVerschiebungProperty))
						.otherwise(startXProperty.add(startXVerschiebungProperty)));
		linieA.yProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
						.then(endeYProperty.add(endeYVerschiebungProperty))
						.otherwise(yMitte.add(mitteVerschiebungProperty)));
		linieB.xProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))).then(linieA.xProperty())
						.otherwise(endeXProperty.add(endeXVerschiebungProperty)));
		linieB.yProperty().bind(linieA.yProperty());
	}
	
	private void setzeStartLinks(UMLKlassifizierer start) {
		startXProperty.bind(start.getPosition().xProperty());
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty().add(start.getPosition().hoeheProperty().divide(2)));
		startYVerschiebungProperty.set(0);
		bindeStartLinksRechts();
	}
	
	private void bindeStartLinksRechts() {
		var xMitte = startpunkt.xProperty().add(startXVerschiebungProperty)
				.add(endeXProperty.add(endeXVerschiebungProperty)).add(VERERBUNGSPFEIL_GROESSE).divide(2);
		mitteVerschiebungProperty.set(0);
		
		if (Orientierung.UNTEN.equals(orientierungEndeProperty.get())
				|| Orientierung.OBEN.equals(orientierungEndeProperty.get())) {
			linieA.xProperty().bind(endeXProperty.add(endeXVerschiebungProperty));
			linieA.yProperty().bind(startYProperty.add(startYVerschiebungProperty));
			linieB.xProperty().bind(linieA.xProperty());
			linieB.yProperty().bind(linieA.yProperty());
		} else {
			linieA.xProperty().bind(xMitte.add(mitteVerschiebungProperty));
			linieA.yProperty().bind(startpunkt.yProperty());
			linieB.xProperty().bind(xMitte.add(mitteVerschiebungProperty));
			linieB.yProperty().bind(endeYProperty.add(endeYVerschiebungProperty));
		}
	}
	
	private void updateEnde(Orientierung orientierungEnde, UMLKlassifizierer ende) {
		if (verbindung.istGeschlossen()) {
			return;
		}
		if (ende == null || orientierungEnde == null) {
			return;
		}
		switch (orientierungEnde) {
			case OBEN -> setzeEndeOben(ende);
			case RECHTS -> setzeEndeRechts(ende);
			case UNTEN -> setzeEndeUnten(ende);
			case LINKS -> setzeEndeLinks(ende);
			default -> {
				/**/
			}
		}
	}
	
	private void setzeEndeOben(UMLKlassifizierer ende) {
		endeXProperty.bind(ende.getPosition().xProperty().add(ende.getPosition().breiteProperty().divide(2)));
		endeXVerschiebungProperty.set(0);
		endeYProperty.bind(ende.getPosition().yProperty());
		endeYVerschiebungProperty.set(0);
	}
	
	private void setzeEndeRechts(UMLKlassifizierer ende) {
		endeXProperty.bind(ende.getPosition().xProperty().add(ende.getPosition().breiteProperty()));
		endeXVerschiebungProperty.set(0);
		endeYProperty.bind(ende.getPosition().yProperty().add(ende.getPosition().hoeheProperty().divide(2)));
		endeYVerschiebungProperty.set(0);
	}
	
	private void setzeEndeUnten(UMLKlassifizierer ende) {
		endeXProperty.bind(ende.getPosition().xProperty().add(ende.getPosition().breiteProperty().divide(2)));
		endeXVerschiebungProperty.set(0);
		endeYProperty.bind(ende.getPosition().yProperty().add(ende.getPosition().hoeheProperty()));
		endeYVerschiebungProperty.set(0);
	}
	
	private void setzeEndeLinks(UMLKlassifizierer ende) {
		endeXProperty.bind(ende.getPosition().xProperty());
		endeXVerschiebungProperty.set(0);
		endeYProperty.bind(ende.getPosition().yProperty().add(ende.getPosition().hoeheProperty().divide(2)));
		endeYVerschiebungProperty.set(0);
	}
	
	private Node macheVerschiebbar(Position liniePos, DoubleProperty xVerschiebung, DoubleProperty yVerschiebung,
			ObjectProperty<UMLKlassifizierer> element, ObjectProperty<Orientierung> orientierungProperty,
			ObjectProperty<UMLKlassifizierer> anderesElement, ObjectProperty<Orientierung> andereOrientierungProperty,
			Consumer<Orientierung> update) {
		
		if (element == null || anderesElement == null || projekt == null) {
			return null;
		}
		Pane linienWahl = erstelleLinienUeberlagerung(liniePos);
		ueberwacheMausZeiger(linienWahl, orientierungProperty, false);
		linienWahl.getStyleClass().add("test");
		
		Position start = new Position(this, "macheVerschiebbarStartPos");
		BooleanProperty wirdBewegt = new SimpleBooleanProperty(false);
		ObjectProperty<UeberwachungsStatus> status = new SimpleObjectProperty<>();
		
		linienWahl.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			status.set(projekt.getUeberwachungsStatus());
			projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
			start.setX(event.getSceneX());
			start.setY(event.getSceneY());
			start.setBreite(xVerschiebung.get());
			start.setHoehe(yVerschiebung.get());
			wirdBewegt.set(true);
		});
		
		linienWahl.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
			if (wirdBewegt.get()) {
				if (element.get() == null || anderesElement.get() == null) {
					return;
				}
				var elementPos = element.get().getPosition();
				var anderesElementPos = anderesElement.get().getPosition();
				
				switch (orientierungProperty.get()) {
					case OBEN, UNTEN -> {
						double dX = event.getSceneX() - start.getX();
						double x = start.getBreite() + dX / getParent().getScaleX();
						verschiebeX(x, liniePos, xVerschiebung, orientierungProperty, andereOrientierungProperty.get(),
								elementPos, anderesElementPos, wirdBewegt, update);
					}
					case LINKS, RECHTS -> {
						double dY = event.getSceneY() - start.getY();
						double y = start.getHoehe() + dY / getParent().getScaleY();
						verschiebeY(y, liniePos, yVerschiebung, orientierungProperty, andereOrientierungProperty.get(),
								elementPos, anderesElementPos, wirdBewegt, update);
					}
					default -> {
						/**/}
				}
			}
		});
		
		linienWahl.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
			projekt.setUeberwachungsStatus(status.get());
			if (wirdBewegt.get()) {
				wirdBewegt.set(false);
			}
		});
		
		return linienWahl;
	}
	
	private void verschiebeX(double x, Position liniePos, DoubleProperty xVerschiebung,
			ObjectProperty<Orientierung> orientierungProperty, Orientierung andereOrientierung, Position elementPos,
			Position anderePos, BooleanProperty wirdBewegt, Consumer<Orientierung> update) {
		if (Einstellungen.getBenutzerdefiniert().linienRasterungAktivierenProperty().get()) {
			x = ((int) x / 10) * 10;	// Raster in 10er-Schritten
		}
		xVerschiebung.set(x);
		if (liniePos.xProperty().get() - 2 < elementPos.getX()) {
			if (Orientierung.orientierungErlaubt(Orientierung.LINKS, andereOrientierung, elementPos, anderePos, 0)) {
				orientierungProperty.set(Orientierung.LINKS);
				update.accept(Orientierung.LINKS);
			} else {
				x += 3;
				xVerschiebung.set(x);
				while (liniePos.xProperty().get() < elementPos.getX()) {
					xVerschiebung.set(++x);
				}
			}
			wirdBewegt.set(false);
		} else if (liniePos.xProperty().get() + 2 > elementPos.getMaxX()) {
			if (Orientierung.orientierungErlaubt(Orientierung.RECHTS, andereOrientierung, elementPos, anderePos, 0)) {
				orientierungProperty.set(Orientierung.RECHTS);
				update.accept(Orientierung.RECHTS);
			} else {
				x -= 3;
				xVerschiebung.set(x);
				while (liniePos.xProperty().get() > elementPos.getMaxX()) {
					xVerschiebung.set(--x);
				}
			}
			wirdBewegt.set(false);
		} else if (Math.abs(xVerschiebung.get()) < 5) {
			// in der Mitte einrasten
			xVerschiebung.set(0);
		}
	}
	
	private void verschiebeY(double y, Position liniePos, DoubleProperty yVerschiebung,
			ObjectProperty<Orientierung> orientierungProperty, Orientierung andereOrientierung, Position elementPos,
			Position anderePos, BooleanProperty wirdBewegt, Consumer<Orientierung> update) {
		if (Einstellungen.getBenutzerdefiniert().linienRasterungAktivierenProperty().get()) {
			y = ((int) y / 10) * 10;	// Raster in 10er-Schritten
		}
		yVerschiebung.set(y);
		if (liniePos.yProperty().get() + 2 < elementPos.getY()) {
			if (Orientierung.orientierungErlaubt(Orientierung.OBEN, andereOrientierung, elementPos, anderePos, 0)) {
				orientierungProperty.set(Orientierung.OBEN);
				update.accept(Orientierung.OBEN);
			} else {
				y += 3;
				yVerschiebung.set(y);
				while (liniePos.yProperty().get() < elementPos.getY()) {
					yVerschiebung.set(++y);
				}
			}
			wirdBewegt.set(false);
		} else if (liniePos.yProperty().get() + 2 > elementPos.getMaxY()) {
			if (Orientierung.orientierungErlaubt(Orientierung.UNTEN, andereOrientierung, elementPos, anderePos, 0)) {
				orientierungProperty.set(Orientierung.UNTEN);
				update.accept(Orientierung.UNTEN);
			} else {
				y -= 3;
				yVerschiebung.set(y);
				while (liniePos.yProperty().get() > elementPos.getMaxY()) {
					yVerschiebung.set(--y);
				}
			}
			wirdBewegt.set(false);
		} else if (Math.abs(yVerschiebung.get()) < 5) {
			// in der Mitte einrasten
			yVerschiebung.set(0);
		}
	}
	
	private Node macheVerschiebbarMitte(Position liniePos, DoubleProperty verschiebung,
			ObjectProperty<UMLKlassifizierer> element, ObjectProperty<Orientierung> orientierungProperty,
			ObjectProperty<UMLKlassifizierer> anderesElement) {
		
		if (element == null || anderesElement == null || projekt == null) {
			return null;
		}
		Pane linienWahl = erstelleLinienUeberlagerung(liniePos);
		ueberwacheMausZeiger(linienWahl, orientierungProperty, true);
		linienWahl.getStyleClass().add("test");
		
		Position start = new Position(this, "macheVerschiebbarStartPos");
		BooleanProperty wirdBewegt = new SimpleBooleanProperty(false);
		ObjectProperty<UeberwachungsStatus> status = new SimpleObjectProperty<>();
		
		linienWahl.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			status.set(projekt.getUeberwachungsStatus());
			projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
			start.setX(event.getSceneX());
			start.setY(event.getSceneY());
			start.setBreite(verschiebung.get());
			start.setHoehe(verschiebung.get());
			wirdBewegt.set(true);
		});
		
		linienWahl.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
			if (wirdBewegt.get()) {
				if (element.get() == null || anderesElement.get() == null) {
					return;
				}
				var elementPos = element.get().getPosition();
				var anderesElementPos = anderesElement.get().getPosition();
				
				switch (orientierungProperty.get()) {
					case OBEN -> {
						double dY = event.getSceneY() - start.getY();
						double y = start.getBreite() + dY / getParent().getScaleY();
						double minY = anderesElementPos.getMaxY() + VERERBUNGSPFEIL_GROESSE + 5;
						double maxY = elementPos.getY() - 5;
						verschiebeYMitte(y, liniePos, verschiebung, minY, maxY, wirdBewegt);
					}
					case UNTEN -> {
						double dY = event.getSceneY() - start.getY();
						double y = start.getBreite() + dY / getParent().getScaleY();
						double minY = elementPos.getMaxY() + 5;
						double maxY = anderesElementPos.getY() - VERERBUNGSPFEIL_GROESSE - 5;
						verschiebeYMitte(y, liniePos, verschiebung, minY, maxY, wirdBewegt);
					}
					case LINKS -> {
						double dX = event.getSceneX() - start.getX();
						double x = start.getHoehe() + dX / getParent().getScaleX();
						double minX = anderesElementPos.getMaxX() + VERERBUNGSPFEIL_GROESSE + 5;
						double maxX = elementPos.getX() - 5;
						verschiebeXMitte(x, liniePos, verschiebung, minX, maxX, wirdBewegt);
					}
					case RECHTS -> {
						double dX = event.getSceneX() - start.getX();
						double x = start.getHoehe() + dX / getParent().getScaleX();
						double minX = elementPos.getMaxX() + 5;
						double maxX = anderesElementPos.getX() - VERERBUNGSPFEIL_GROESSE - 5;
						verschiebeXMitte(x, liniePos, verschiebung, minX, maxX, wirdBewegt);
					}
					default -> {
						/**/}
				}
			}
		});
		
		linienWahl.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
			projekt.setUeberwachungsStatus(status.get());
			if (wirdBewegt.get()) {
				wirdBewegt.set(false);
			}
		});
		
		return linienWahl;
	}
	
	private void verschiebeYMitte(double y, Position liniePos, DoubleProperty verschiebung, double minY, double maxY,
			BooleanProperty wirdBewegt) {
		if (Einstellungen.getBenutzerdefiniert().linienRasterungAktivierenProperty().get()) {
			y = ((int) y / 10) * 10;	// Raster in 10er-Schritten
		}
		verschiebung.set(y);
		if (liniePos.yProperty().get() - 2 < minY) {
			y += 3;
			verschiebung.set(y);
			while (liniePos.yProperty().get() < minY) {
				verschiebung.set(++y);
			}
			wirdBewegt.set(false);
		} else if (liniePos.yProperty().get() + 2 > maxY) {
			y -= 3;
			verschiebung.set(y);
			while (liniePos.yProperty().get() > maxY) {
				verschiebung.set(--y);
			}
			wirdBewegt.set(false);
		} else if (Math.abs(verschiebung.get()) < 5) {
			// in der Mitte einrasten
			verschiebung.set(0);
		}
	}
	
	private void verschiebeXMitte(double x, Position liniePos, DoubleProperty verschiebung, double minX, double maxX, 
			BooleanProperty wirdBewegt) {
		if (Einstellungen.getBenutzerdefiniert().linienRasterungAktivierenProperty().get()) {
			x = ((int) x / 10) * 10;	// Raster in 10er-Schritten
		}
		verschiebung.set(x);
		if (liniePos.xProperty().get() + 2 < minX) {
			x += 3;
			verschiebung.set(x);
			while (liniePos.xProperty().get() < minX) {
				verschiebung.set(++x);
			}
			wirdBewegt.set(false);
		} else if (liniePos.xProperty().get() + 2 > maxX) {
			x -= 3;
			verschiebung.set(x);
			while (liniePos.xProperty().get() > maxX) {
				verschiebung.set(--x);
			}
			wirdBewegt.set(false);
		} else if (Math.abs(verschiebung.get()) < 5) {
			// in der Mitte einrasten
			verschiebung.set(0);
		}
	}
	
	private Pane erstelleLinienUeberlagerung(Position liniePos) {
		var deltaX = liniePos.xProperty().subtract(liniePos.breiteProperty()).add(10);
		var deltaY = liniePos.yProperty().subtract(liniePos.hoeheProperty()).add(10);
		var deltaXAbs = new When(Bindings.lessThan(0, deltaX)).then(deltaX).otherwise(deltaX.negate().add(10));
		var deltaYAbs = new When(Bindings.lessThan(0, deltaY)).then(deltaY).otherwise(deltaY.negate().add(10));
		
		Pane p = new Pane();
		p.translateXProperty().bind(Bindings.min(liniePos.xProperty(), liniePos.breiteProperty().subtract(5)));
		p.translateYProperty().bind(Bindings.min(liniePos.yProperty(), liniePos.hoeheProperty().subtract(5)));
		p.minHeightProperty().bind(deltaYAbs);
		p.prefHeightProperty().bind(deltaYAbs);
		p.maxHeightProperty().bind(deltaYAbs);
		p.minWidthProperty().bind(deltaXAbs);
		p.prefWidthProperty().bind(deltaXAbs);
		p.maxWidthProperty().bind(deltaXAbs);
		p.setViewOrder(-Double.MAX_VALUE);
		p.toFront();
		this.getChildren().add(p);
		return p;
	}
	
	private void ueberwacheMausZeiger(Pane linienWahl, ObjectProperty<Orientierung> orientierungProperty,
			boolean istMitte) {
		if (orientierungProperty.get() == null) {
			return;
		}
		linienWahl.addEventFilter(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
			if (istMitte) {
				switch (orientierungProperty.get()) {
					case OBEN, UNTEN -> this.setCursor(Cursor.V_RESIZE);
					default -> this.setCursor(Cursor.H_RESIZE);
				}
			} else {
				switch (orientierungProperty.get()) {
					case OBEN, UNTEN -> this.setCursor(Cursor.H_RESIZE);
					default -> this.setCursor(Cursor.V_RESIZE);
				}
			}
		});
		linienWahl.addEventFilter(MouseEvent.MOUSE_EXITED_TARGET, event -> this.setCursor(Cursor.DEFAULT));
	}
	
	private void zeichneVererbungsPfeil() {
		MoveTo startpunktPfeil = new MoveTo();
		startpunktPfeil.xProperty().bind(endeXProperty.add(endeXVerschiebungProperty));
		startpunktPfeil.yProperty().bind(endeYProperty.add(endeYVerschiebungProperty));
		
		// @formatter:off
		LineTo linieAPfeil = new LineTo();
		linieAPfeil.xProperty().bind(startpunktPfeil.xProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN)
					.or(orientierungEndeProperty.isEqualTo(Orientierung.OBEN)))
				.then(-VERERBUNGSPFEIL_GROESSE / 2).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(-VERERBUNGSPFEIL_GROESSE))));
		linieAPfeil.yProperty().bind(startpunktPfeil.yProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.OBEN))
				.then(-VERERBUNGSPFEIL_GROESSE).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(VERERBUNGSPFEIL_GROESSE/2))));
		
		LineTo linieBPfeil = new LineTo();
		linieBPfeil.xProperty().bind(startpunktPfeil.xProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN)
					.or(orientierungEndeProperty.isEqualTo(Orientierung.OBEN)))
				.then(VERERBUNGSPFEIL_GROESSE / 2).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(-VERERBUNGSPFEIL_GROESSE))));
		linieBPfeil.yProperty().bind(startpunktPfeil.yProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.OBEN))
				.then(-VERERBUNGSPFEIL_GROESSE).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(-VERERBUNGSPFEIL_GROESSE/2))));
		
		LineTo linieCPfeil = new LineTo();
		linieCPfeil.xProperty().bind(startpunktPfeil.xProperty());
		linieCPfeil.yProperty().bind(startpunktPfeil.yProperty());
		// @formatter:on
		
		this.pfeil.getElements().add(startpunktPfeil);
		this.pfeil.getElements().add(linieAPfeil);
		this.pfeil.getElements().add(linieBPfeil);
		this.pfeil.getElements().add(linieCPfeil);
		this.pfeil.getElements().add(new ClosePath());
	}
	
	private void zeichneAssoziationsPfeil() {
		MoveTo startpunktPfeil = new MoveTo();
		startpunktPfeil.xProperty().bind(endeXProperty.add(endeXVerschiebungProperty));
		startpunktPfeil.yProperty().bind(endeYProperty.add(endeYVerschiebungProperty));
		
		// @formatter:off
		LineTo linieAPfeil = new LineTo();
		linieAPfeil.xProperty().bind(startpunktPfeil.xProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN)
					.or(orientierungEndeProperty.isEqualTo(Orientierung.OBEN)))
				.then(-ASSOZIATIONSPFEIL_GROESSE / 1.3).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))
				.then(ASSOZIATIONSPFEIL_GROESSE).otherwise(-ASSOZIATIONSPFEIL_GROESSE))));
		linieAPfeil.yProperty().bind(startpunktPfeil.yProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.OBEN))
				.then(-ASSOZIATIONSPFEIL_GROESSE).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN))
				.then(ASSOZIATIONSPFEIL_GROESSE).otherwise(ASSOZIATIONSPFEIL_GROESSE/1.3))));
		
		MoveTo linieBPfeil = new MoveTo();
		linieBPfeil.xProperty().bind(startpunktPfeil.xProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN)
					.or(orientierungEndeProperty.isEqualTo(Orientierung.OBEN)))
				.then(ASSOZIATIONSPFEIL_GROESSE / 1.3).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))
				.then(ASSOZIATIONSPFEIL_GROESSE).otherwise(-ASSOZIATIONSPFEIL_GROESSE))));
		linieBPfeil.yProperty().bind(startpunktPfeil.yProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.OBEN))
				.then(-ASSOZIATIONSPFEIL_GROESSE).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN))
				.then(ASSOZIATIONSPFEIL_GROESSE).otherwise(-ASSOZIATIONSPFEIL_GROESSE/1.3))));
		
		LineTo linieCPfeil = new LineTo();
		linieCPfeil.xProperty().bind(startpunktPfeil.xProperty());
		linieCPfeil.yProperty().bind(startpunktPfeil.yProperty());
		// @formatter:on
		
		this.pfeil.getElements().add(startpunktPfeil);
		this.pfeil.getElements().add(linieAPfeil);
		this.pfeil.getElements().add(linieBPfeil);
		this.pfeil.getElements().add(linieCPfeil);
		this.pfeil.getElements().add(new ClosePath());
	}
	
	private UMLKlassifizierer startAlt = null;
	
	private void updateStartPosition(UMLKlassifizierer start, UMLKlassifizierer ende) {
		if (verbindung.istGeschlossen()) {
			return;
		}
		if (start == null) {
			startXProperty.unbind();
			startYProperty.unbind();
			return;
		}
		
		Position posStart = start.getPosition();
		Position posEnde = ende == null ? new Position(this, "posEnde") : ende.getPosition();
		
		if (start != startAlt) {
			if (startAlt != null) {
				startAlt.getPosition().xProperty().removeListener(positionsUeberwachung);
				startAlt.getPosition().yProperty().removeListener(positionsUeberwachung);
			}
			start.getPosition().xProperty().addListener(positionsUeberwachung);
			start.getPosition().yProperty().addListener(positionsUeberwachung);
			startAlt = start;
		}
		
		Orientierung prefStartOrientierung = orientierungStartProperty.get();
		Orientierung prefEndeOrientierung = orientierungEndeProperty.get();
		if (!Orientierung.orientierungErlaubt(prefStartOrientierung, prefEndeOrientierung, posStart, posEnde)
				|| Orientierung.UNBEKANNT.equals(prefStartOrientierung)) {
			prefStartOrientierung = testeOrientierung(posStart, orientierungEndeProperty.get(), posEnde);
		}
		
		if (prefStartOrientierung == null) {
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.UNTEN, posEnde);
			prefEndeOrientierung = Orientierung.UNTEN;
		}
		if (prefStartOrientierung == null) {
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.RECHTS, posEnde);
			prefEndeOrientierung = Orientierung.RECHTS;
		}
		if (prefStartOrientierung == null) {
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.LINKS, posEnde);
			prefEndeOrientierung = Orientierung.LINKS;
		}
		if (prefStartOrientierung == null) {
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.OBEN, posEnde);
			prefEndeOrientierung = Orientierung.OBEN;
		}
		
		if (!Objects.equals(prefStartOrientierung, orientierungStartProperty.get())
				|| !Objects.equals(prefEndeOrientierung, orientierungEndeProperty.get())) {
			orientierungStartProperty.set(prefStartOrientierung);
			orientierungEndeProperty.set(prefEndeOrientierung);
			updateLinie(prefStartOrientierung, start, prefEndeOrientierung, ende);
		}
	}
	
	private Orientierung testeOrientierung(Position position, Orientierung andereOrientierung,
			Position anderePosition) {
		boolean obenErlaubt = Orientierung.orientierungErlaubt(Orientierung.OBEN, andereOrientierung, position,
				anderePosition);
		boolean rechtsErlaubt = Orientierung.orientierungErlaubt(Orientierung.RECHTS, andereOrientierung, position,
				anderePosition);
		boolean untenErlaubt = Orientierung.orientierungErlaubt(Orientierung.UNTEN, andereOrientierung, position,
				anderePosition);
		boolean linksErlaubt = Orientierung.orientierungErlaubt(Orientierung.LINKS, andereOrientierung, position,
				anderePosition);
		
		if (verbindung.getTyp().equals(UMLVerbindungstyp.ASSOZIATION)) {
			return rechtsErlaubt ? Orientierung.RECHTS
					: linksErlaubt ? Orientierung.LINKS
							: untenErlaubt ? Orientierung.UNTEN : obenErlaubt ? Orientierung.OBEN : null;
		} else {
			return obenErlaubt ? Orientierung.OBEN
					: untenErlaubt ? Orientierung.UNTEN
							: rechtsErlaubt ? Orientierung.RECHTS : linksErlaubt ? Orientierung.LINKS : null;
		}
	}
	
	private UMLKlassifizierer endeAlt;
	
	private void updateEndPosition(UMLKlassifizierer start, UMLKlassifizierer ende) {
		if (verbindung.istGeschlossen()) {
			return;
		}
		if (ende == null) {
			endeXProperty.unbind();
			endeYProperty.unbind();
			return;
		}
		
		Position posStart = start == null ? new Position(this, "posStart") : start.getPosition();
		Position posEnde = ende.getPosition();
		
		// Wechsel auf OBEN => Mittig platzieren
		if (ende != endeAlt) {
			if (endeAlt != null) {
				endeAlt.getPosition().xProperty().removeListener(positionsUeberwachung);
				endeAlt.getPosition().yProperty().removeListener(positionsUeberwachung);
			}
			ende.getPosition().xProperty().addListener(positionsUeberwachung);
			ende.getPosition().yProperty().addListener(positionsUeberwachung);
			endeAlt = ende;
		}
		
		Orientierung prefEndeOrientierung = testeOrientierung(posEnde, orientierungStartProperty.get(), posStart);
		Orientierung prefStartOrientierung = orientierungStartProperty.get();
		
		if (prefEndeOrientierung == null) {
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.UNTEN, posStart);
			prefStartOrientierung = Orientierung.UNTEN;
		}
		if (prefEndeOrientierung == null) {
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.RECHTS, posStart);
			prefStartOrientierung = Orientierung.RECHTS;
		}
		if (prefEndeOrientierung == null) {
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.LINKS, posStart);
			prefStartOrientierung = Orientierung.LINKS;
		}
		if (prefEndeOrientierung == null) {
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.OBEN, posStart);
			prefStartOrientierung = Orientierung.OBEN;
		}
		
		if (!Objects.equals(prefEndeOrientierung, orientierungEndeProperty.get())
				|| !Objects.equals(prefStartOrientierung, orientierungStartProperty.get())) {
			orientierungStartProperty.set(prefStartOrientierung);
			orientierungEndeProperty.set(prefEndeOrientierung);
			updateLinie(prefStartOrientierung, start, prefEndeOrientierung, ende);
		}
	}
}