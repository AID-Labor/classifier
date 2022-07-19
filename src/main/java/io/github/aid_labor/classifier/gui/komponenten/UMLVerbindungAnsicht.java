/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Objects;

import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.Position;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class UMLVerbindungAnsicht extends Pane {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final double VERERBUNGSPFEIL_GROESSE = 25;
	private static final double ASSOZIATIONSPFEIL_GROESSE = 20;
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
	private final ChangeListener<Number> positionsUeberwachungStart;
	private final ChangeListener<Number> positionsUeberwachungEnde;
	
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
		this.startXVerschiebungProperty = new SimpleDoubleProperty();
		this.startYVerschiebungProperty = new SimpleDoubleProperty();
		this.endeXVerschiebungProperty = new SimpleDoubleProperty();
		this.endeYVerschiebungProperty = new SimpleDoubleProperty();
		this.orientierungStartProperty = new SimpleObjectProperty<>(Orientierung.UNBEKANNT);
		this.orientierungEndeProperty = new SimpleObjectProperty<>(Orientierung.UNBEKANNT);
//		this.visibleProperty().bind(verbindung.ausgebelendetProperty().not());
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
		this.positionsUeberwachungStart = (p, alt, neu) -> {
			updateStartPosition(verbindung.getStartElement(), verbindung.getEndElement());
		};
		this.positionsUeberwachungEnde = (p, alt, neu) -> {
			updateEndPosition(verbindung.getStartElement(), verbindung.getEndElement());
		};
		
		erstelleBindungen();
		updateStartPosition(verbindung.getStartElement(), verbindung.getEndElement());
		updateEndPosition(verbindung.getStartElement(), verbindung.getEndElement());
		
		zeichneLinie();
		updateLinie(orientierungStartProperty.get(), verbindung.getStartElement(), orientierungEndeProperty.get(),
				verbindung.getEndElement());
		
		switch (verbindung.getTyp()) {
			case VERERBUNG, SCHNITTSTELLEN_VERERBUNG -> {
				zeichneVererbungsPfeil();
				this.pfeil.getStyleClass().add(VERERBUNGSPFEIL_CSS_KLASSE);
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + verbindung.getTyp());
		}
		
		this.getChildren().add(pfeil);
	}
	
	int i = 1;
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
	
	private void erstelleBindungen() {
		startXProperty.bindBidirectional(verbindung.getStartPosition().xProperty());
		startYProperty.bindBidirectional(verbindung.getStartPosition().yProperty());
		startXVerschiebungProperty.bindBidirectional(verbindung.getStartPosition().breiteProperty());
		startYVerschiebungProperty.bindBidirectional(verbindung.getStartPosition().hoeheProperty());
		endeXProperty.bindBidirectional(verbindung.getEndPosition().xProperty());
		endeYProperty.bindBidirectional(verbindung.getEndPosition().yProperty());
		endeXVerschiebungProperty.bindBidirectional(verbindung.getEndPosition().breiteProperty());
		endeYVerschiebungProperty.bindBidirectional(verbindung.getEndPosition().hoeheProperty());
		
		NodeUtil.beobachteSchwach(this, verbindung.startElementProperty(),
				start -> updateStartPosition(start, verbindung.getEndElement()));
		NodeUtil.beobachteSchwach(this, verbindung.endElementProperty(),
				ende -> updateEndPosition(verbindung.getStartElement(), ende));
		NodeUtil.beobachteSchwach(this, orientierungStartProperty, o -> updateLinie(o, verbindung.getStartElement(),
				orientierungEndeProperty.get(), verbindung.getEndElement()));
	}
	
	private void zeichneLinie() {
//		updateStart(orientierungStartProperty.get());
		startpunkt.xProperty().bind(startXProperty.add(startXVerschiebungProperty));
		startpunkt.yProperty().bind(startYProperty.add(startYVerschiebungProperty));
		
		startpunktMitte.xProperty().bind(linieA.xProperty());
		startpunktMitte.yProperty().bind(linieA.yProperty());
		
		startpunktEnde.xProperty().bind(linieB.xProperty());
		startpunktEnde.yProperty().bind(linieB.yProperty());
		
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
			if (verbindung.istAusgebelendet()) {
				linie.setId("ROT" + "-" + i);
			} else {
				linie.setId("SCHWARZ" + "-" + i);
			}
			verbindung.ausgebelendetProperty().addListener((p, a, n) -> {
				if (n.booleanValue()) {
					linie.setId("ROT" + "-" + i);
				} else {
					linie.setId("SCHWARZ" + "-" + i);
				}
			});
			i++;
			linie.setOnMouseEntered(
					e -> System.out.println(verbindung.getStartElement() + " " + verbindung.getEndElement() + " "
							+ orientierungStartProperty.get() + " " + orientierungEndeProperty.get()));
		}
		
		var linieAPos = new Position();
		linieAPos.xProperty().bind(startpunkt.xProperty());
		linieAPos.yProperty().bind(startpunkt.yProperty());
		linieAPos.breiteProperty().bind(linieA.xProperty());
		linieAPos.hoeheProperty().bind(linieA.yProperty());
		macheVerschiebbar(linieAPos, startXVerschiebungProperty, startYVerschiebungProperty,
				verbindung.startElementProperty().get(), orientierungStartProperty,
				verbindung.endElementProperty().get(), orientierungEndeProperty);
	}
	
	private void updateLinie(Orientierung orientierungStart, UMLKlassifizierer start, Orientierung orientierungEnde,
			UMLKlassifizierer ende) {
		updateStart(orientierungStart, start, orientierungEnde);
		updateEnde(orientierungEnde, ende);
		
		linieC.xProperty().bind(verbindung.getEndPosition().xProperty());
		linieC.yProperty().bind(verbindung.getEndPosition().yProperty());
	}
	
	private void updateStart(Orientierung orientierungStart, UMLKlassifizierer start, Orientierung orientierungEnde) {
		if (start == null || orientierungStart == null) {
			return;
		}
		switch (orientierungStart) {
			case OBEN -> {
				setzeStartOben(start, orientierungEnde);
			}
			case RECHTS -> {
				setzeStartRechts(start, orientierungEnde);
			}
			case UNTEN -> {
				setzeStartUnten(start, orientierungEnde);
			}
			case LINKS -> {
				setzeStartLinks(start, orientierungEnde);
			}
			default -> {
				/**/
			}
		}
		
//		var yMitte = verbindung.getStartPosition().yProperty().add(verbindung.getEndPosition().yProperty())
//				.add(VERERBUNGSPFEIL_GROESSE).divide(2);
		switch (orientierungStart) {
			case OBEN, UNBEKANNT -> {
				
			}
			case UNTEN -> {
				var yMitte = verbindung.getStartPosition().yProperty().add(verbindung.getEndPosition().yProperty())
						.subtract(VERERBUNGSPFEIL_GROESSE).divide(2);
				linieA.xProperty().bind(startXProperty.add(startXVerschiebungProperty));
				linieA.yProperty().bind(yMitte);
				linieB.xProperty().bind(endeXProperty.add(endeXVerschiebungProperty));
				linieB.yProperty().bind(yMitte);
			}
			case LINKS, RECHTS -> {
				
			}
		}
	}
	
	private void setzeStartOben(UMLKlassifizierer start, Orientierung orientierungEnde) {
		startXProperty.bind(start.getPosition().xProperty().add(start.getPosition().breiteProperty().divide(2)));
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty());
		startYVerschiebungProperty.set(0);
		
		var yMitte = startpunkt.yProperty().add(linieC.yProperty()).add(VERERBUNGSPFEIL_GROESSE).divide(2);
		linieA.xProperty()
				.bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
						.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
						.then(startXProperty.add(startXVerschiebungProperty))
						.otherwise(startXProperty.add(startXVerschiebungProperty)));
		linieA.yProperty().bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
				.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
				.then(endeYProperty.add(endeYVerschiebungProperty)).otherwise(yMitte));
		linieB.xProperty().bind(new When(orientierungEndeProperty.isEqualTo(Orientierung.LINKS)
				.or(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS)))
				.then(linieA.xProperty()).otherwise(endeXProperty.add(endeXVerschiebungProperty)));
		linieB.yProperty().bind(linieA.yProperty());
	}
	
	private void setzeStartRechts(UMLKlassifizierer start, Orientierung orientierungEnde) {
		startXProperty.bind(start.getPosition().xProperty().add(start.getPosition().breiteProperty()));
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty().add(start.getPosition().hoeheProperty().divide(2)));
		startYVerschiebungProperty.set(0);
		bindeStartLinksRechts();
	}
	
	private void setzeStartUnten(UMLKlassifizierer start, Orientierung orientierungEnde) {
		startXProperty.bind(start.getPosition().xProperty().add(start.getPosition().breiteProperty().divide(2)));
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty().add(start.getPosition().hoeheProperty()));
		startYVerschiebungProperty.set(0);
	}
	
	private void setzeStartLinks(UMLKlassifizierer start, Orientierung orientierungEnde) {
		startXProperty.bind(start.getPosition().xProperty());
		startXVerschiebungProperty.set(0);
		startYProperty.bind(start.getPosition().yProperty().add(start.getPosition().hoeheProperty().divide(2)));
		startYVerschiebungProperty.set(0);
		bindeStartLinksRechts();
	}
	
	private void bindeStartLinksRechts() {
		var xMitte = startpunkt.xProperty().add(startXVerschiebungProperty).add(endeXProperty.add(endeXVerschiebungProperty))
				.add(VERERBUNGSPFEIL_GROESSE).divide(2);
		if (Orientierung.UNTEN.equals(orientierungEndeProperty.get())) {
			linieA.xProperty().bind(xMitte);
			linieA.yProperty().bind(startpunkt.yProperty());
			linieB.xProperty().bind(xMitte);
			linieB.yProperty().bind(linieA.yProperty());
		} else if (Orientierung.OBEN.equals(orientierungEndeProperty.get())) {
			linieA.xProperty().bind(endeXProperty.add(endeXVerschiebungProperty));
			linieA.yProperty().bind(startYProperty.add(startYVerschiebungProperty));
			linieB.xProperty().bind(linieA.xProperty());
			linieB.yProperty().bind(linieA.yProperty());
		} else {
			linieA.xProperty().bind(xMitte);
			linieA.yProperty().bind(startpunkt.yProperty());
			linieB.xProperty().bind(xMitte);
			linieB.yProperty().bind(endeYProperty.add(endeYVerschiebungProperty));
		}
	}
	
	private void updateEnde(Orientierung orientierungEnde, UMLKlassifizierer ende) {
		if (ende == null || orientierungEnde == null) {
			return;
		}
		switch (orientierungEnde) {
			case OBEN -> {
				setzeEndeOben(ende);
			}
			case RECHTS -> {
				setzeEndeRechts(ende);
			}
			case UNTEN -> {
				setzeEndeUnten(ende);
			}
			case LINKS -> {
				setzeEndeLinks(ende);
			}
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
			UMLKlassifizierer element, ObjectProperty<Orientierung> orientierungProperty,
			UMLKlassifizierer anderesElement, ObjectProperty<Orientierung> andereOrientierungProperty) {
		
		if (element == null || anderesElement == null) {
			return null;
		}
		Position elementPos = element.getPosition();
		Position anderePos = anderesElement.getPosition();
		Pane linienWahl = erstelleLinienUeberlagerung(liniePos);
		ueberwacheMausZeiger(linienWahl, orientierungProperty);
		
		Position start = new Position();
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
				switch (orientierungProperty.get()) {
					case OBEN, UNTEN -> {
						double dX = event.getSceneX() - start.getX();
						double x = start.getBreite() + dX / getParent().getScaleX();
						verschiebeX(x, liniePos, xVerschiebung, orientierungProperty, andereOrientierungProperty.get(),
								elementPos, anderePos, wirdBewegt);
					}
					case LINKS, RECHTS -> {
						double dY = event.getSceneY() - start.getY();
						double y = start.getHoehe() + dY / getParent().getScaleY();
						verschiebeY(y, liniePos, yVerschiebung, orientierungProperty, andereOrientierungProperty.get(),
								elementPos, anderePos, wirdBewegt);
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
			Position anderePos, BooleanProperty wirdBewegt) {
		xVerschiebung.set(x);
		if (liniePos.xProperty().get() - 2 < elementPos.getX()) {
			if (Orientierung.orientierungErlaubt(Orientierung.LINKS, andereOrientierung, elementPos, anderePos)) {
				xVerschiebung.set(-elementPos.getBreite() / 2);
				orientierungProperty.set(Orientierung.LINKS);
			} else {
				x += 3;
				xVerschiebung.set(x);
				while (liniePos.xProperty().get() < elementPos.getX()) {
					xVerschiebung.set(++x);
				}
			}
			wirdBewegt.set(false);
		} else if (liniePos.xProperty().get() + 2 > elementPos.getMaxX()) {
			if (Orientierung.orientierungErlaubt(Orientierung.RECHTS, andereOrientierung, elementPos, anderePos)) {
				xVerschiebung.set(liniePos.breiteProperty().get() - elementPos.getX() - elementPos.getBreite());
				orientierungProperty.set(Orientierung.RECHTS);
			} else {
				x -= 3;
				xVerschiebung.set(x);
				while (liniePos.xProperty().get() > elementPos.getMaxX()) {
					xVerschiebung.set(--x);
				}
			}
			wirdBewegt.set(false);
		} else if (Math.abs(((elementPos.getMaxX() - elementPos.getX()) / 2) + elementPos.getX()
				- liniePos.xProperty().get()) < 5) {
			// in der Mitte einrasten
			xVerschiebung.set(0);
		}
	}
	
	private void verschiebeY(double y, Position liniePos, DoubleProperty yVerschiebung,
			ObjectProperty<Orientierung> orientierungProperty, Orientierung andereOrientierung, Position elementPos,
			Position anderePos, BooleanProperty wirdBewegt) {
		
//		if (liniePos.yProperty().get() - 2 < elementPos.getY()) {
//			if (Orientierung.orientierungErlaubt(Orientierung.OBEN, andereOrientierung, elementPos, anderePos)) {
//				yVerschiebung.set(elementPos.getY());
//				orientierungProperty.set(Orientierung.OBEN);
//			} else {
//				y += 3;
//				yVerschiebung.set(y);
//				while (liniePos.yProperty().get() < elementPos.getY()) {
//					yVerschiebung.set(++y);
//				}
//			}
//			wirdBewegt.set(false);
//		} else if (liniePos.yProperty().get() + 2 > elementPos.getMaxY()) {
//			if (Orientierung.orientierungErlaubt(Orientierung.UNTEN, andereOrientierung, elementPos, anderePos)) {
//				yVerschiebung.set(elementPos.getY() + elementPos.getHoehe());
//				orientierungProperty.set(Orientierung.UNTEN);
//			} else {
//				y -= 3;
//				yVerschiebung.set(y);
//				while (liniePos.yProperty().get() > elementPos.getMaxY()) {
//					yVerschiebung.set(--y);
//				}
//			}
//			wirdBewegt.set(false);
//		} else if (Math.abs(((elementPos.getMaxY() - elementPos.getY()) / 2) + elementPos.getY() - liniePos.yProperty().get()) < 5) {
//			// in der Mitte einrasten
//			yVerschiebung.set(0);
//		}
		
		yVerschiebung.set(y);
		if (liniePos.yProperty().get() + 2 < elementPos.getY()) {
			System.out.println(1);
			if (Orientierung.orientierungErlaubt(Orientierung.OBEN, andereOrientierung, elementPos, anderePos)) {
				yVerschiebung.set(elementPos.getY());
				orientierungProperty.set(Orientierung.OBEN);
			} else {
//				y += 3;
//				yVerschiebung.set(y);
//				while (liniePos.yProperty().get() < elementPos.getY()) {
//					yVerschiebung.set(++y);
//				}
			}
			wirdBewegt.set(false);
		} else if (liniePos.yProperty().get() + 2 > elementPos.getMaxY()) {
			System.out.println(2);
			if (Orientierung.orientierungErlaubt(Orientierung.UNTEN, andereOrientierung, elementPos, anderePos)) {
				yVerschiebung.set(elementPos.getY() + elementPos.getHoehe());
				orientierungProperty.set(Orientierung.UNTEN);
			} else {
//				y -= 3;
//				yVerschiebung.set(y);
//				while (liniePos.yProperty().get() > elementPos.getMaxY()) {
//					yVerschiebung.set(--y);
//				}
			}
			wirdBewegt.set(false);
		} else if (Math.abs(((elementPos.getMaxY() - elementPos.getY()) / 2) + elementPos.getY()
				- liniePos.yProperty().get()) < 5) {
			System.out.println(3);
			// in der Mitte einrasten
			yVerschiebung.set(((elementPos.getMaxY() - elementPos.getY()) / 2) + elementPos.getY());
		}
	}
	
	private Pane erstelleLinienUeberlagerung(Position liniePos) {
		var deltaX = liniePos.xProperty().subtract(liniePos.breiteProperty()).add(10);
		var deltaY = liniePos.yProperty().subtract(liniePos.hoeheProperty()).add(10);
		Pane p = new Pane();
		p.translateXProperty().bind(liniePos.breiteProperty().subtract(5));
		p.translateYProperty().bind(liniePos.hoeheProperty().subtract(5));
		p.minHeightProperty().bind(deltaY);
		p.prefHeightProperty().bind(deltaY);
		p.maxHeightProperty().bind(deltaY);
		p.minWidthProperty().bind(deltaX);
		p.prefWidthProperty().bind(deltaX);
		p.maxWidthProperty().bind(deltaX);
		p.setViewOrder(0);
		this.getChildren().add(p);
		return p;
	}
	
	private void ueberwacheMausZeiger(Pane linienWahl, ObjectProperty<Orientierung> orientierungProperty) {
		linienWahl.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
			switch (orientierungProperty.get()) {
				case OBEN, UNTEN -> this.setCursor(Cursor.H_RESIZE);
				default -> this.setCursor(Cursor.V_RESIZE);
			}
		});
		linienWahl.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
			this.setCursor(Cursor.DEFAULT);
		});
	}
	
	private void zeichneVererbungsPfeil() {
		MoveTo startpunkt = new MoveTo();
		startpunkt.xProperty().bind(verbindung.getEndPosition().xProperty());
		startpunkt.yProperty().bind(verbindung.getEndPosition().yProperty());
		
		// @formatter:off
		LineTo linieA = new LineTo();
		linieA.xProperty().bind(startpunkt.xProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN)
					.or(orientierungEndeProperty.isEqualTo(Orientierung.OBEN)))
				.then(-VERERBUNGSPFEIL_GROESSE / 2).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(-VERERBUNGSPFEIL_GROESSE))));
		linieA.yProperty().bind(startpunkt.yProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.OBEN))
				.then(-VERERBUNGSPFEIL_GROESSE).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(VERERBUNGSPFEIL_GROESSE/2))));
//		linieA.xProperty().bind(startpunkt.xProperty().subtract(VERERBUNGSPFEIL_GROESSE / 2));
//		linieA.yProperty().bind(startpunkt.yProperty().add(VERERBUNGSPFEIL_GROESSE));
		
		LineTo linieB = new LineTo();
		linieB.xProperty().bind(startpunkt.xProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN)
					.or(orientierungEndeProperty.isEqualTo(Orientierung.OBEN)))
				.then(VERERBUNGSPFEIL_GROESSE / 2).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.RECHTS))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(-VERERBUNGSPFEIL_GROESSE))));
		linieB.yProperty().bind(startpunkt.yProperty().add(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.OBEN))
				.then(-VERERBUNGSPFEIL_GROESSE).otherwise(
				new When(orientierungEndeProperty.isEqualTo(Orientierung.UNTEN))
				.then(VERERBUNGSPFEIL_GROESSE).otherwise(-VERERBUNGSPFEIL_GROESSE/2))));
//		linieB.xProperty().bind(startpunkt.xProperty().add(VERERBUNGSPFEIL_GROESSE / 2));
//		linieB.yProperty().bind(startpunkt.yProperty().add(VERERBUNGSPFEIL_GROESSE));
		
		LineTo linieC = new LineTo();
		linieC.xProperty().bind(startpunkt.xProperty());
		linieC.yProperty().bind(startpunkt.yProperty());
		// @formatter:on
		
		this.pfeil.getElements().add(startpunkt);
		this.pfeil.getElements().add(linieA);
		this.pfeil.getElements().add(linieB);
		this.pfeil.getElements().add(linieC);
		this.pfeil.getElements().add(new ClosePath());
	}
	
//	private void updateVererbungsPfeil(Orientierung orientierungEnde) {
//		MoveTo startpunkt = new MoveTo();
//		startpunkt.xProperty().bind(verbindung.getEndPosition().xProperty());
//		startpunkt.yProperty().bind(verbindung.getEndPosition().yProperty());
//		
//		LineTo linieA = new LineTo();
//		linieA.xProperty().bind(startpunkt.xProperty().subtract(VERERBUNGSPFEIL_GROESSE / 2));
//		linieA.yProperty().bind(startpunkt.yProperty().add(VERERBUNGSPFEIL_GROESSE));
//		
//		LineTo linieB = new LineTo();
//		linieB.xProperty().bind(startpunkt.xProperty().add(VERERBUNGSPFEIL_GROESSE / 2));
//		linieB.yProperty().bind(startpunkt.yProperty().add(VERERBUNGSPFEIL_GROESSE));
//		
//		LineTo linieC = new LineTo();
//		linieC.xProperty().bind(startpunkt.xProperty());
//		linieC.yProperty().bind(startpunkt.yProperty());
//		
//		this.pfeil.getElements().add(startpunkt);
//		this.pfeil.getElements().add(linieA);
//		this.pfeil.getElements().add(linieB);
//		this.pfeil.getElements().add(linieC);
//		this.pfeil.getElements().add(new ClosePath());
//	}
	
	private UMLKlassifizierer startAlt = null;
	boolean obenErlaubt;
	boolean rechtsErlaubt;
	boolean untenErlaubt;
	boolean linksErlaubt;
	
	private void updateStartPosition(UMLKlassifizierer start, UMLKlassifizierer ende) {
		if (start == null) {
			startXProperty.unbind();
			startYProperty.unbind();
			return;
		}
		
		Position posStart = start.getPosition();
		Position posEnde = ende == null ? new Position() : ende.getPosition();
		
//		var xBindung = .add(start.getPosition().breiteProperty().divide(2));
		// Wechsel auf OBEN => Mittig platzieren
		if (start != startAlt) {
			if (startAlt != null) {
				startAlt.getPosition().xProperty().removeListener(positionsUeberwachungStart);
				startAlt.getPosition().yProperty().removeListener(positionsUeberwachungStart);
			}
			start.getPosition().xProperty().addListener(positionsUeberwachungStart);
			start.getPosition().yProperty().addListener(positionsUeberwachungStart);
			startAlt = start;
		}
		
		System.out.println(">>>>>>>>>>>>>>>> #" + start + "# -- " + ende);
		Orientierung prefStartOrientierung = testeOrientierung(posStart, orientierungEndeProperty.get(), posEnde);
		Orientierung prefEndeOrientierung = orientierungEndeProperty.get();
		
		if (prefStartOrientierung == null) {
			System.out.println(">>>> Test UNTEN ");
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.UNTEN, posEnde);
			prefEndeOrientierung = Orientierung.UNTEN;
		}
		if (prefStartOrientierung == null) {
			System.out.println(">>>> Test RECHTS ");
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.RECHTS, posEnde);
			prefEndeOrientierung = Orientierung.RECHTS;
		}
		if (prefStartOrientierung == null) {
			System.out.println(">>>> Test LINKS ");
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.LINKS, posEnde);
			prefEndeOrientierung = Orientierung.LINKS;
		}
		if (prefStartOrientierung == null) {
			System.out.println(">>>> Test OBEN ");
			prefStartOrientierung = testeOrientierung(posStart, Orientierung.OBEN, posEnde);
			prefEndeOrientierung = Orientierung.OBEN;
		}
		
		System.out.println("<<<<<<<<<<<<<<<<< #" + start + "# -- " + ende + " -> " + prefStartOrientierung + " - "
				+ prefEndeOrientierung);
		if (!Objects.equals(prefStartOrientierung, orientierungStartProperty.get())
				|| !Objects.equals(prefEndeOrientierung, orientierungEndeProperty.get())) {
			System.out.println(prefStartOrientierung + " -- " + prefEndeOrientierung);
			orientierungStartProperty.set(prefStartOrientierung);
			orientierungEndeProperty.set(prefEndeOrientierung);
			updateLinie(prefStartOrientierung, start, prefEndeOrientierung, ende);
		}
		System.out.println("START: " + verbindung.getStartElement() + " " + verbindung.getEndElement() + " "
				+ orientierungStartProperty.get() + " " + orientierungEndeProperty.get());
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
					: rechtsErlaubt ? Orientierung.RECHTS
							: linksErlaubt ? Orientierung.LINKS : untenErlaubt ? Orientierung.UNTEN : null;
		}
	}
	
	private UMLKlassifizierer endeAlt;
	
	private void updateEndPosition(UMLKlassifizierer start, UMLKlassifizierer ende) {
		if (ende == null) {
			endeXProperty.unbind();
			endeYProperty.unbind();
			return;
		}
		
		Position posStart = start == null ? new Position() : start.getPosition();
		Position posEnde = ende.getPosition();
		
//		var xBindung = .add(start.getPosition().breiteProperty().divide(2));
		// Wechsel auf OBEN => Mittig platzieren
		if (ende != endeAlt) {
			if (endeAlt != null) {
				endeAlt.getPosition().xProperty().removeListener(positionsUeberwachungStart);
				endeAlt.getPosition().yProperty().removeListener(positionsUeberwachungStart);
			}
			ende.getPosition().xProperty().addListener(positionsUeberwachungStart);
			ende.getPosition().yProperty().addListener(positionsUeberwachungStart);
			endeAlt = ende;
		}
		
		System.out.println(">>>>>>>>>>>>>>>> " + start + " -- #" + ende + "#");
		Orientierung prefEndeOrientierung = testeOrientierung(posEnde, orientierungStartProperty.get(), posStart);
		Orientierung prefStartOrientierung = orientierungStartProperty.get();
		
		if (prefEndeOrientierung == null) {
			System.out.println(">>>> Test UNTEN ");
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.UNTEN, posStart);
			prefStartOrientierung = Orientierung.UNTEN;
		}
		if (prefEndeOrientierung == null) {
			System.out.println(">>>> Test RECHTS ");
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.RECHTS, posStart);
			prefStartOrientierung = Orientierung.RECHTS;
		}
		if (prefEndeOrientierung == null) {
			System.out.println(">>>> Test LINKS ");
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.LINKS, posStart);
			prefStartOrientierung = Orientierung.LINKS;
		}
		if (prefEndeOrientierung == null) {
			System.out.println(">>>> Test OBEN ");
			prefEndeOrientierung = testeOrientierung(posEnde, Orientierung.OBEN, posStart);
			prefStartOrientierung = Orientierung.OBEN;
		}
		System.out.println("<<<<<<<<<<<<<<<<< " + start + " -- #" + ende + "# -> " + prefStartOrientierung + " - "
				+ prefEndeOrientierung);
		
		if (!Objects.equals(prefEndeOrientierung, orientierungEndeProperty.get())
				|| !Objects.equals(prefStartOrientierung, orientierungStartProperty.get())) {
			System.out.println(prefEndeOrientierung + " -- " + prefStartOrientierung);
			orientierungStartProperty.set(prefStartOrientierung);
			orientierungEndeProperty.set(prefEndeOrientierung);
			updateLinie(prefStartOrientierung, start, prefEndeOrientierung, ende);
		}
		
		System.out.println("ENDE: " + verbindung.getStartElement() + " " + verbindung.getEndElement() + " "
				+ orientierungStartProperty.get() + " " + orientierungEndeProperty.get());
	}
}