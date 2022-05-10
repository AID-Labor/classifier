/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.util;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Labeled;


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
	 * @param node        Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode    Code fuer das gewuenschte Icon
	 * @param groesse     Groesse des Icons (siehe {@link FontIcon#setIconSize(int)}
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
	 * @param node        Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode    Code fuer das gewuenschte Icon
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon fuegeIconHinzu(Labeled node, Ikon iconCode) {
		return fuegeIconHinzu(node, iconCode, 24, ContentDisplay.LEFT);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code fuegeIconHinzu(node, iconCode, groesse, ContentDisplay.TOP)}.
	 * 
	 * @param node        Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode    Code fuer das gewuenschte Icon
	 * @param groesse     Groesse des Icons (siehe {@link FontIcon#setIconSize(int)}
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon erzeugeIconNode(Labeled node, Ikon iconCode, int groesse) {
		return fuegeIconHinzu(node, iconCode, groesse, ContentDisplay.TOP);
	}
	
	/**
	 * Fuegt einem javaFX Node-Objekt ein Icon mit Ikonli hinzu. Shortcut fuer
	 * {@code  erzeugeIconNode(node, iconCode, 32)}.
	 * 
	 * @param node        Node-Objekt, dem das Icon hinzugefuegt wird
	 * @param iconCode    Code fuer das gewuenschte Icon
	 * @return das hinzugefuegte Icon
	 */
	public static FontIcon erzeugeIconNode(Labeled node, Ikon iconCode) {
		return erzeugeIconNode(node, iconCode, 32);
	}
	
	public static void macheUnfokussierbar(Node... nodes) {
		for(Node n : nodes) {
			n.setFocusTraversable(false);
		}
	}
	
	public static void macheUnfokussierbar(Iterable<Node> nodes) {
		for(Node n : nodes) {
			n.setFocusTraversable(false);
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
		// Hilfsklasse, nicht instanziierbar
	}
	
}