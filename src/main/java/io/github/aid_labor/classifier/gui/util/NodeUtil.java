/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.util;

import java.util.function.BiConsumer;
import java.util.logging.Logger;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import io.github.aid_labor.classifier.basis.io.Ressource;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


/**
 * Sammlung mit haufig benoetigten Hilfsmethoden fuer verschiedene javaFX Node-Objekte
 * 
 * @author Tim Muehle
 *
 */
public final class NodeUtil {
	private static final Logger log = Logger.getLogger(NodeUtil.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Fuegt einem javaFX Node-Objekt eine Grafik hinzu.
	 * 
	 * @param node        Node-Objekt, dem die hinzugefuegt wird
	 * @param grafik      Grafik, die hinzugefuegt wird
	 * @param hoehe       Hoehe der Grafik
	 * @param anzeigeStil Darstellungsstil von Text und Icon
	 * @return die hinzugefuegte Grafik
	 */
	public static ImageView fuegeGrafikHinzu(Labeled node, Ressource grafik, int hoehe,
			ContentDisplay anzeigeStil) {
		ImageView grafikNode = new ImageView(grafik.externeForm());
		grafikNode.setPreserveRatio(true);
		grafikNode.setFitHeight(hoehe);
		node.setGraphic(grafikNode);
		node.setContentDisplay(anzeigeStil);
		return grafikNode;
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt eine Grafik hinzu. Shortcut fuer
	 * {@code fuegeGrafikHinzu(node, grafikPfad, hoehe, ContentDisplay.TOP)}.
	 * 
	 * @param node   Node-Objekt, dem die hinzugefuegt wird
	 * @param grafik Grafik, die hinzugefuegt wird
	 * @param hoehe  Hoehe der Grafik
	 * @return die hinzugefuegte Grafik
	 */
	public static ImageView fuegeGrafikHinzu(Labeled node, Ressource grafik, int hoehe) {
		return fuegeGrafikHinzu(node, grafik, hoehe, ContentDisplay.TOP);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu.
	 * 
	 * @param node        Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode    Code fuer das gewuenschte Icon
	 * @param groesse     Groesse des Icons (siehe {@link FontIcon#setIconSize(int)}
	 * @param anzeigeStil Darstellungsstil von Text und Icon
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon fuegeIconHinzu(Labeled node, Ikon iconCode, int groesse,
			ContentDisplay anzeigeStil) {
		FontIcon symbol = new FontIcon(iconCode);
		symbol.setIconSize(groesse);
		node.setGraphic(symbol);
		node.setContentDisplay(anzeigeStil);
		return symbol;
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code fuegeIconHinzu(node, iconCode, groesse, ContentDisplay.LEFT)}.
	 * 
	 * @param node     Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode Code fuer das gewuenschte Icon
	 * @param groesse  Groesse des Icons (siehe {@link FontIcon#setIconSize(int)}
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon fuegeIconHinzu(Labeled node, Ikon iconCode, int groesse) {
		return fuegeIconHinzu(node, iconCode, groesse, ContentDisplay.LEFT);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code fuegeIconHinzu(node, iconCode, 24, anzeigeStil)}.
	 * 
	 * @param node        Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode    Code fuer das gewuenschte Icon
	 * @param anzeigeStil Darstellungsstil von Text und Icon
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon fuegeIconHinzu(Labeled node, Ikon iconCode,
			ContentDisplay anzeigeStil) {
		return fuegeIconHinzu(node, iconCode, 24, anzeigeStil);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code fuegeIconHinzu(node, iconCode, 24, ContentDisplay.LEFT)}.
	 * 
	 * @param node     Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode Code fuer das gewuenschte Icon
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon fuegeIconHinzu(Labeled node, Ikon iconCode) {
		return fuegeIconHinzu(node, iconCode, 24, ContentDisplay.LEFT);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code fuegeIconHinzu(node, iconCode, groesse, ContentDisplay.TOP)}.
	 * 
	 * @param node     Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode Code fuer das gewuenschte Icon
	 * @param groesse  Groesse des Icons (siehe {@link FontIcon#setIconSize(int)}
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon erzeugeIconNode(Labeled node, Ikon iconCode, int groesse) {
		return fuegeIconHinzu(node, iconCode, groesse, ContentDisplay.TOP);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code  erzeugeIconNode(node, iconCode, 32)}.
	 * 
	 * @param node     Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode Code fuer das gewuenschte Icon
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon erzeugeIconNode(Labeled node, Ikon iconCode) {
		return erzeugeIconNode(node, iconCode, 32);
	}
	
	public static void macheUnfokussierbar(Node... nodes) {
		for (Node n : nodes) {
			n.setFocusTraversable(false);
		}
	}
	
	public static void macheUnfokussierbar(Iterable<Node> nodes) {
		for (Node n : nodes) {
			n.setFocusTraversable(false);
		}
	}
	
	/**
	 * Fuegt einem javaFX Knoten ein Plus-Icon hinzu und verpackt beides in einem neuen
	 * Container. Das Icon wird mit der Id {@code hinzufuegenIcon} gekennzeichnet
	 * 
	 * @param n Knoten, der ein Plus-Icon erhalten soll
	 * @return neuer Container mit dem urspruenglichen Knoten und dem Pluszeichen
	 */
	public static Node plusIconHinzufuegen(Node n) {
		StackPane.setMargin(n, new Insets(10, 10, 0, 0));
		StackPane container = new StackPane(n);
		FontIcon plus = new FontIcon(CarbonIcons.ADD_FILLED);
		plus.setId("hinzufuegenIcon");
		container.getChildren().add(plus);
		StackPane.setAlignment(plus, Pos.TOP_RIGHT);
		return container;
	}
	
	public static void deaktivieren(Node... knoten) {
		for (Node node : knoten) {
			node.setDisable(true);
		}
	}
	
	public static void deaktivieren(MenuItem... knoten) {
		for (MenuItem node : knoten) {
			node.setDisable(true);
		}
	}
	
	/**
	 * Setzt bei dem Element die Id {@code "HERVORHEBUNG"} oder entfernt diese.
	 * 
	 * @param hervorheben {@code true}, wenn die Id gesetzt werden soll oder {@code false} zum
	 *                    Aufheben
	 * @param element     Element, das die Id erhaelt
	 */
	public static void setzeHervorhebung(boolean hervorheben, Node element) {
		if (hervorheben) {
			element.setId("HERVORHEBUNG");
		} else {
			element.setId(null);
		}
	}
	
	/**
	 * Setzt bei mehreren Elementen die Id {@code "HERVORHEBUNG"} oder entfernt diese.
	 * 
	 * @param hervorheben {@code true}, wenn die Id gesetzt werden soll oder {@code false} zum
	 *                    Aufheben
	 * @param elemente    Elemente, die die Id erhalten
	 */
	public static void setzeHervorhebung(boolean hervorheben, Node... elemente) {
		for (Node element : elemente) {
			setzeHervorhebung(hervorheben, element);
		}
	}
	
	/**
	 * Setzt bei dem Element die Id {@code "HERVORHEBUNG"} oder entfernt diese.
	 * 
	 * @param hervorheben {@code true}, wenn die Id gesetzt werden soll oder {@code false} zum
	 *                    Aufheben
	 * @param element     Element, das die Id erhaelt
	 */
	public static void setzeHervorhebung(ReadOnlyBooleanProperty hervorheben, Node element) {
		setzeHervorhebung(hervorheben.get(), element);
	}
	
	/**
	 * Setzt bei mehreren Elementen die Id {@code "HERVORHEBUNG"} oder entfernt diese.
	 * 
	 * @param hervorheben {@code true}, wenn die Id gesetzt werden soll oder {@code false} zum
	 *                    Aufheben
	 * @param elemente    Elemente, die die Id erhalten
	 */
	public static void setzeHervorhebung(ReadOnlyBooleanProperty hervorheben,
			Node... elemente) {
		for (Node element : elemente) {
			setzeHervorhebung(hervorheben.get(), element);
		}
	}
	
	public static boolean wirdBewegt(Node n) {
		Object obj = n.getProperties().getOrDefault("bewegungsEinstellung",
				new BewegungsEinstellungen());
		if (!(obj instanceof BewegungsEinstellungen bewegung)) {
			return false;
		}
		
		return bewegung.wirdBewegt;
	}
	
	public static void macheBeweglich(Node element, Runnable vorBewegung,
			BiConsumer<Bounds, Bounds> nachBewegung) {
		Object obj = element.getProperties().getOrDefault("bewegungsEinstellung",
				new BewegungsEinstellungen());
		if (!(obj instanceof BewegungsEinstellungen bewegung)) {
			log.warning(() -> "unbekanntes Objekt fuer Schluessel 'bewegungsEinstellung'. "
					+ " Node " + element + " kann nicht beweglich gemacht werden!");
			return;
		}
		
		element.getProperties().put("bewegungsEinstellung", bewegung);
		
		bewegung.letzterCursor = element.getCursor();
		
		element.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
			element.setCursor(Cursor.HAND);
		});
		
		element.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			if (bewegung.aktionPosition.equals(AktionPosition.KEINE)
					&& istEinfacherMausKlick(event)) {
				log.finer(() -> "Starte Bewegung");
				if (vorBewegung != null) {
					vorBewegung.run();
				}
				bewegung.ausgangsPosition = element.getBoundsInParent();
				bewegung.letzterCursor = element.getCursor();
				element.setCursor(Cursor.CLOSED_HAND);
				bewegung.wirdBewegt = true;
				bewegung.mausStartX = event.getSceneX();
				bewegung.mausStartY = event.getSceneY();
				bewegung.positionStartX = element.getTranslateX();
				bewegung.positionStartY = element.getTranslateY();
			} else {
				bewegung.wirdBewegt = false;
			}
		});
		
		element.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
			if (bewegung.wirdBewegt && !bewegung.wirdGroesseVeraendert) {
				log.finest(() -> "Bewege");
				double deltaX = event.getSceneX() - bewegung.mausStartX;
				double deltaY = event.getSceneY() - bewegung.mausStartY;
				double x = bewegung.positionStartX + deltaX / element.getParent().getScaleX();
				double y = bewegung.positionStartY + deltaY / element.getParent().getScaleY();
				element.setTranslateX(x < 0 ? 0 : x);
				element.setTranslateY(y < 0 ? 0 : y);
			}
		});
		
		element.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
			if (bewegung.wirdBewegt) {
				log.finer(() -> "Beende Bewegung");
				element.setCursor(bewegung.letzterCursor);
				bewegung.wirdBewegt = false;
				if (nachBewegung != null) {
					nachBewegung.accept(bewegung.ausgangsPosition,
							element.getBoundsInParent());
				}
			}
		});
	}
	
	public static boolean wirdGroesseVeraendert(Node n) {
		Object obj = n.getProperties().getOrDefault("bewegungsEinstellung",
				new BewegungsEinstellungen());
		if (!(obj instanceof BewegungsEinstellungen bewegung)) {
			return false;
		}
		
		return bewegung.wirdGroesseVeraendert;
	}
	
	public static void macheGroessenVeraenderlich(Region element,
			Runnable vorGroessenAenderung, BiConsumer<Bounds, Bounds> nachGroessenAenderung) {
		Object obj = element.getProperties().getOrDefault("bewegungsEinstellung",
				new BewegungsEinstellungen());
		if (!(obj instanceof BewegungsEinstellungen bewegung)) {
			log.warning(() -> "unbekanntes Objekt fuer Schluessel 'bewegungsEinstellung'. "
					+ " Node " + element + " kann nicht beweglich gemacht werden!");
			return;
		}
		
		element.getProperties().put("bewegungsEinstellung", bewegung);
		
		bewegung.letzterCursor = element.getCursor();
		
		beobachteMausBewegung(element, bewegung);
		
		element.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
			if (!bewegung.aktionPosition.equals(AktionPosition.KEINE)
					&& istEinfacherMausKlick(event)) {
				log.finer(() -> "Starte Groessenaenderung");
				if (vorGroessenAenderung != null) {
					vorGroessenAenderung.run();
				}
				bewegung.ausgangsPosition = element.getBoundsInParent();
				bewegung.ausgangsPosition = element.getBoundsInParent();
				bewegung.wirdGroesseVeraendert = true;
				bewegung.mausStartX = event.getSceneX();
				bewegung.mausStartY = event.getSceneY();
				bewegung.positionStartX = element.getTranslateX();
				bewegung.positionStartY = element.getTranslateY();
			} else {
				bewegung.wirdGroesseVeraendert = false;
			}
		});
		
		element.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
			if (bewegung.wirdGroesseVeraendert) {
				log.finest(() -> "Veraendere Groesse");
				
				Point2D mausposition = element.getParent().sceneToLocal(event.getSceneX(),
						event.getSceneY());
				
				if (bewegung.aktionPosition.istObenSelektiert()) {
					double minYNeu = mausposition.getY();
					double deltaY = element.getBoundsInParent().getMinY() - minYNeu;
					double hoehe = element.getHeight() + deltaY;
					if (hoehe >= element.minHeight(Double.NaN) && minYNeu >= 0) {
						element.setPrefHeight(hoehe);
						element.setTranslateY(minYNeu);
					}
				}
				if (bewegung.aktionPosition.istRechtsSelektiert()) {
					double maxXNeu = element.parentToLocal(mausposition).getX();
					double breite = maxXNeu - element.getBoundsInLocal().getMinX();
					if (breite >= element.getMinWidth()) {
						element.setPrefWidth(breite);
					}
				}
				if (bewegung.aktionPosition.istUntenSelektiert()) {
					double maxYNeu = element.parentToLocal(mausposition).getY();
					double hoehe = maxYNeu - element.getBoundsInLocal().getMinY();
					if (hoehe >= element.getMinHeight()) {
						element.setPrefHeight(hoehe);
					}
				}
				if (bewegung.aktionPosition.istLinksSelektiert()) {
					double minXNeu = mausposition.getX();
					double deltaX = element.getBoundsInParent().getMinX() - minXNeu;
					double breite = element.getWidth() + deltaX;
					if (breite >= element.minWidth(Double.NaN) && minXNeu >= 0) {
						element.setPrefWidth(breite);
						element.setTranslateX(minXNeu);
					}
				}
			}
		});
		
		element.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
			if (bewegung.wirdGroesseVeraendert) {
				log.finer(() -> "Beende Groessenaenderung");
				element.setCursor(bewegung.letzterCursor);
				bewegung.wirdGroesseVeraendert = false;
				if (nachGroessenAenderung != null) {
					nachGroessenAenderung.accept(bewegung.ausgangsPosition,
							element.getBoundsInParent());
				}
			}
		});
	}
	
	private static void beobachteMausBewegung(Region element,
			BewegungsEinstellungen bewegung) {
		element.addEventHandler(MouseEvent.MOUSE_MOVED, event -> {
			double minX = element.getBoundsInLocal().getMinX();
			double minY = element.getBoundsInLocal().getMinY();
			double maxX = element.getBoundsInLocal().getMaxX();
			double maxY = element.getBoundsInLocal().getMaxY();
			
			double randabstand = 8;
			
			Point2D mausposition = element.sceneToLocal(event.getSceneX(), event.getSceneY());
			
			double mausX = mausposition.getX();
			double mausY = mausposition.getY();
			boolean obereZone = mausY - randabstand <= minY;
			boolean untereZone = mausY + randabstand >= maxY;
			boolean rechteZone = mausX + randabstand >= maxX;
			boolean linkeZone = mausX - randabstand <= minX;
			
			if (obereZone && linkeZone) {
				bewegung.aktionPosition = AktionPosition.OBEN_LINKS;
				element.setCursor(Cursor.SE_RESIZE);
			} else if (obereZone && rechteZone) {
				bewegung.aktionPosition = AktionPosition.OBEN_RECHTS;
				element.setCursor(Cursor.SW_RESIZE);
			} else if (untereZone && linkeZone) {
				bewegung.aktionPosition = AktionPosition.UNTEN_LINKS;
				element.setCursor(Cursor.NE_RESIZE);
			} else if (untereZone && rechteZone) {
				bewegung.aktionPosition = AktionPosition.UNTEN_RECHTS;
				element.setCursor(Cursor.NW_RESIZE);
			} else if (obereZone) {
				bewegung.aktionPosition = AktionPosition.OBEN;
				element.setCursor(Cursor.V_RESIZE);
			} else if (rechteZone) {
				bewegung.aktionPosition = AktionPosition.RECHTS;
				element.setCursor(Cursor.H_RESIZE);
			} else if (untereZone) {
				bewegung.aktionPosition = AktionPosition.UNTEN;
				element.setCursor(Cursor.V_RESIZE);
			} else if (linkeZone) {
				bewegung.aktionPosition = AktionPosition.LINKS;
				element.setCursor(Cursor.H_RESIZE);
			} else {
				bewegung.aktionPosition = AktionPosition.KEINE;
				element.setCursor(Cursor.HAND);
			}
		});
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static boolean istEinfacherMausKlick(MouseEvent event) {
		return event.isPrimaryButtonDown()
				&& !event.isAltDown()
				&& !event.isBackButtonDown()
				&& !event.isControlDown()
				&& !event.isForwardButtonDown()
				&& !event.isMetaDown()
				&& !event.isMiddleButtonDown()
				&& !event.isSecondaryButtonDown()
				&& !event.isShiftDown()
				&& !event.isShortcutDown();
	}
	
	private static class BewegungsEinstellungen {
		private boolean wirdBewegt;
		private boolean wirdGroesseVeraendert;
		private double mausStartX;
		private double mausStartY;
		private double positionStartX;
		private double positionStartY;
		private AktionPosition aktionPosition;
		private Cursor letzterCursor;
		private Bounds ausgangsPosition;
		
		public BewegungsEinstellungen() {
			wirdBewegt = false;
			wirdGroesseVeraendert = false;
			mausStartX = 0;
			mausStartY = 0;
			aktionPosition = AktionPosition.KEINE;
		}
	}
	
	private static enum AktionPosition {
		KEINE(false, false, false, false),
		OBEN(true, false, false, false),
		RECHTS(false, true, false, false),
		UNTEN(false, false, true, false),
		LINKS(false, false, false, true),
		OBEN_LINKS(true, false, false, true),
		OBEN_RECHTS(true, true, false, false),
		UNTEN_LINKS(false, false, true, true),
		UNTEN_RECHTS(false, true, true, false);
		
		private boolean obenSelektiert;
		private boolean rechtsSelektiert;
		private boolean untenSelektiert;
		private boolean linksSelektiert;
		
		private AktionPosition(boolean obenSelektiert, boolean rechtsSelektiert,
				boolean untenSelektiert, boolean linksSelektiert) {
			this.obenSelektiert = obenSelektiert;
			this.rechtsSelektiert = rechtsSelektiert;
			this.untenSelektiert = untenSelektiert;
			this.linksSelektiert = linksSelektiert;
		}
		
		public boolean istObenSelektiert() {
			return obenSelektiert;
		}
		
		public boolean istRechtsSelektiert() {
			return rechtsSelektiert;
		}
		
		public boolean istUntenSelektiert() {
			return untenSelektiert;
		}
		
		public boolean istLinksSelektiert() {
			return linksSelektiert;
		}
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private NodeUtil() {
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
	
}