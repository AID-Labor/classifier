/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.util;

import java.util.logging.Logger;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import io.github.aid_labor.classifier.basis.io.Ressource;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
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
	
	public static void disable(Node... knoten) {
		for (Node node : knoten) {
			node.setDisable(true);
		}
	}
	
	public static void disable(MenuItem... knoten) {
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
	
	public static <T extends Region> void macheBeweglich(T element) {
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
			if (bewegung.aktionPosition.equals(AktionPosition.KEINE)) {
				log.finer(() -> "Starte Bewegung");
				element.setCache(true);
				element.setCacheHint(CacheHint.SPEED);
				bewegung.letzterCursor = element.getCursor();
				element.setCursor(Cursor.MOVE);
				bewegung.wirdBewegt = true;
				bewegung.mausStartX = event.getSceneX();
				bewegung.mausStartY = event.getSceneY();
				bewegung.positionStartX = element.getTranslateX();
				bewegung.positionStartY = element.getTranslateY();
			}
		});
		
		element.addEventFilter(MouseEvent.MOUSE_DRAGGED, event -> {
			if (bewegung.wirdBewegt) {
				log.finer(() -> "Bewege");
				double deltaX = event.getSceneX() - bewegung.mausStartX;
				double deltaY = event.getSceneY() - bewegung.mausStartY;
				double x = bewegung.positionStartX + deltaX;
				double y = bewegung.positionStartY + deltaY;
				element.setTranslateX(x < 0 ? 0 : x);
				element.setTranslateY(y < 0 ? 0 : y);
				
				Parent p = element.getParent();
				if (p != null && p instanceof Region container) {
					var elementLayout = element.getBoundsInParent();
					double breite = elementLayout.getMaxX() + 300;
					if(container.getWidth() < breite) {
						container.setMinWidth(breite);
						container.setPrefWidth(breite);
					}
					double hoehe = elementLayout.getMaxY() + 300;
					if(container.getHeight() < hoehe) {
						container.setMinHeight(hoehe);
						container.setPrefHeight(hoehe);
					}
				}
			}
		});
		
		element.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			log.finer(() -> "Beende Bewegung");
			element.setCache(false);
			element.setCacheHint(CacheHint.DEFAULT);
			element.setCursor(bewegung.letzterCursor);
			bewegung.wirdBewegt = false;
		});
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private static class BewegungsEinstellungen {
		private boolean wirdBewegt;
		private double mausStartX;
		private double mausStartY;
		private double positionStartX;
		private double positionStartY;
		private AktionPosition aktionPosition;
		private Cursor letzterCursor;
		
		public BewegungsEinstellungen() {
			wirdBewegt = false;
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