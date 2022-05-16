/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.util;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import io.github.aid_labor.classifier.basis.io.Ressource;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;


/**
 * Sammlung mit haufig benoetigten Hilfsmethoden fuer verschiedene javaFX Node-Objekte
 * 
 * @author Tim Muehle
 *
 */
public final class NodeUtil {
//	private static final Logger log = Logger.getLogger(NodeUtil.class.getName());

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
		for(Node element : elemente) {
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
		for(Node element : elemente) {
			setzeHervorhebung(hervorheben.get(), element);
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
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