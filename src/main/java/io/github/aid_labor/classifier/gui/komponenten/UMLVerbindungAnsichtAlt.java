/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.Orientierung;
import io.github.aid_labor.classifier.uml.klassendiagramm.Position;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;


public class UMLVerbindungAnsichtAlt extends Pane {
	
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
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLVerbindungAnsichtAlt(UMLVerbindung verbindung, UMLProjekt projekt) {
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
		this.orientierungStartProperty = new SimpleObjectProperty<>(Orientierung.OBEN);
		this.orientierungEndeProperty = new SimpleObjectProperty<>(Orientierung.UNTEN);
//		this.visibleProperty().bind(verbindung.ausgebelendetProperty().not());
		this.linieStart = new Path();
		this.linieMitte = new Path();
		this.linieEnde = new Path();
		this.pfeil = new Path();
		this.startpunkt = new MoveTo();
		this.startpunktMitte = new MoveTo();
		
		NodeUtil.beobachteSchwach(this, startXProperty, verbindung.getStartPosition().xProperty()::setValue);
		NodeUtil.beobachteSchwach(this, startYProperty, verbindung.getStartPosition().yProperty()::setValue);
		startXVerschiebungProperty.bindBidirectional(verbindung.getStartPosition().breiteProperty());
		startYVerschiebungProperty.bindBidirectional(verbindung.getStartPosition().hoeheProperty());
		NodeUtil.beobachteSchwach(this, endeXProperty, verbindung.getEndPosition().xProperty()::setValue);
		NodeUtil.beobachteSchwach(this, endeYProperty, verbindung.getEndPosition().yProperty()::setValue);
		
		NodeUtil.beobachteSchwach(this, verbindung.startElementProperty(), this::updateStartPosition);
		NodeUtil.beobachteSchwach(this, verbindung.endElementProperty(), this::updateEndPosition);
		
		updateStartPosition(verbindung.getStartElement());
		updateEndPosition(verbindung.getEndElement());
		
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
		}
		zeichneLinie();
		
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
	
	private void zeichneLinie() {
		var yMitte = verbindung.getStartPosition().yProperty().add(verbindung.getEndPosition().yProperty())
				.add(VERERBUNGSPFEIL_GROESSE).divide(2);
		
		updateStart(orientierungStartProperty.get());
		NodeUtil.beobachteSchwach(this, orientierungStartProperty, o -> updateStart(o));
		
		LineTo linieB = new LineTo();
		linieB.xProperty().bind(verbindung.getEndPosition().xProperty());
		linieB.yProperty().bind(yMitte);
		
		MoveTo startpunktEnde = new MoveTo();
		startpunktEnde.xProperty().bind(linieB.xProperty());
		startpunktEnde.yProperty().bind(linieB.yProperty());
		LineTo linieC = new LineTo();
		linieC.xProperty().bind(verbindung.getEndPosition().xProperty());
		linieC.yProperty().bind(verbindung.getEndPosition().yProperty());
		
		
		this.linieMitte.getElements().add(startpunktMitte);
		this.linieMitte.getElements().add(linieB);
		this.linieEnde.getElements().add(startpunktEnde);
		this.linieEnde.getElements().add(linieC);
	}
	
	private void updateStart(Orientierung orientierung) {
		startpunkt.xProperty().bind(verbindung.getStartPosition().xProperty());
		startpunkt.yProperty().bind(verbindung.getStartPosition().yProperty());
		LineTo linieA = new LineTo();
		var yMitte = verbindung.getStartPosition().yProperty().add(verbindung.getEndPosition().yProperty())
				.add(VERERBUNGSPFEIL_GROESSE).divide(2);
		switch (orientierung) {
			case OBEN, UNTEN, UNBEKANNT -> {
				linieA.xProperty().bind(verbindung.getStartPosition().xProperty());
				linieA.yProperty().bind(yMitte);
			}
			case LINKS, RECHTS -> {
				linieA.xProperty().bind(verbindung.getEndPosition().xProperty());
				linieA.yProperty().bind(verbindung.getStartPosition().yProperty());
			}
		}
		
		startpunktMitte.xProperty().bind(linieA.xProperty());
		startpunktMitte.yProperty().bind(linieA.yProperty());
		
		this.linieStart.getElements().clear();
		this.linieStart.getElements().add(startpunkt);
		this.linieStart.getElements().add(linieA);
		var linieAPos = new Position();
		linieAPos.xProperty().bind(startpunkt.xProperty());
		linieAPos.yProperty().bind(startpunkt.yProperty());
		linieAPos.breiteProperty().bind(linieA.xProperty());
		linieAPos.hoeheProperty().bind(linieA.yProperty());
		if (linieAVerschiebung != null) {
			this.getChildren().remove(linieAVerschiebung);
		}
		linieAVerschiebung = macheVerschiebbar(linieAPos, startXVerschiebungProperty, startYVerschiebungProperty,
				verbindung.startElementProperty().get(), orientierungStartProperty, verbindung.endElementProperty().get(), 
				orientierungEndeProperty);
	}
	
	private Node linieAVerschiebung;
	
	private Node macheVerschiebbar(Position liniePos, DoubleProperty xVerschiebung, DoubleProperty yVerschiebung,
			UMLKlassifizierer element, ObjectProperty<Orientierung> orientierungProperty, UMLKlassifizierer anderesElement,
			ObjectProperty<Orientierung> andereOrientierungProperty) {
		
		if (element == null || anderesElement == null) {
			return null;
		}
		Position elementPos = element.getPosition();
		Position anderePos = anderesElement.getPosition();
		Pane linienWahl = erstelleLinienUeberlage(liniePos);
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
						verschiebeX(x, liniePos, xVerschiebung, orientierungProperty, andereOrientierungProperty.get(), elementPos, anderePos, wirdBewegt);
					}
					case LINKS, RECHTS -> {
						double dY = event.getSceneY() - start.getY();
						double y = start.getHoehe() + dY / getParent().getScaleY();
						verschiebeY(y, liniePos, yVerschiebung, orientierungProperty, andereOrientierungProperty.get(), elementPos, anderePos, wirdBewegt);
					}
					default -> {/**/}
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
		} else if (Math.abs(((elementPos.getMaxX() - elementPos.getX()) / 2) + elementPos.getX() - liniePos.xProperty().get()) < 5) {
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
		} else if (Math.abs(((elementPos.getMaxY() - elementPos.getY()) / 2) + elementPos.getY() - liniePos.yProperty().get()) < 5) {
			System.out.println(3);
			// in der Mitte einrasten
			yVerschiebung.set(((elementPos.getMaxY() - elementPos.getY()) / 2) + elementPos.getY());
		}
	}

	private Pane erstelleLinienUeberlage(Position liniePos) {
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
		startXProperty.bind(xBindung.add(startXVerschiebungProperty));
		startYProperty.bind(start.getPosition().yProperty().add(startYVerschiebungProperty));
		updateStart(Orientierung.OBEN);
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