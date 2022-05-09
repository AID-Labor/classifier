/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.Sprache.SprachDatei;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;


/**
 * Hilfsklasse zum Umgang mit Sprachen
 * 
 * @author Tim Muehle
 *
 */
public final class SprachUtil {
	private static final Logger log = Logger.getLogger(SprachUtil.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// public ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	// protected ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	// package ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	// private ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// public ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	/**
	 * Stellt default-Werte fuer ein Sprachobjekt ein
	 * 
	 * @param sprache      Sprachobjekt, das initialisiert wird
	 * @param suchordner   Ordner, in dem nach Sprachdateien gesucht wird (siehe
	 *                     {@link SprachDatei#sucheSprachdateien(Path, String)})
	 * @param dateiPraefix Praefix im Dateinamen zur Suche nach Sprachdateien (siehe
	 *                     {@link SprachDatei#sucheSprachdateien(Path, String)})
	 * @return {@code true}, wenn eine Sprache erfolgreich gesetzt wurde, sonst {@code false}
	 */
	@SuppressWarnings("null")
	public static boolean setUpSprache(Sprache sprache, Path suchordner, String dateiPraefix) {
		List<SprachDatei> sprachdateien = null;
		boolean erfolg = false;
		try {
			sprachdateien = SprachDatei.sucheSprachdateien(suchordner, dateiPraefix);
			sprache.sprachenHinzufuegen(sprachdateien);
		} catch (IllegalArgumentException | IllegalStateException | IOException e) {
			log.log(Level.SEVERE, e, () -> "Fehler beim Suchen der Sprachdateien");
		}
		
		try {
			sprache.nutzeSprache(Einstellungen.getBenutzerdefiniert().sprachEinstellung.get(),
					Einstellungen.getDefault().sprachEinstellung.get(),
					Locale.getDefault(), Locale.GERMAN, Locale.GERMANY, Locale.ENGLISH);
			erfolg = true;
		} catch (IllegalStateException e) {
			try {
				sprache.nutzeSprache(Locale.getDefault(),
						sprachdateien.stream().map(sd -> sd.sprache()).toList());
				erfolg = true;
			} catch (Exception e2) {
				e2.addSuppressed(e);
				log.log(Level.SEVERE, e2, () -> "Keine passende Sprache gefunden");
			}
		}
		
		return erfolg;
	}
	
	/**
	 * Bindet die textProperty eines MenuItem an das textProperty der uebergebenen Sprache
	 * zu dem textSchluessel. Falls der Schluessel nicht existiert wird der alternative Text
	 * gesetzt.
	 * 
	 * @param item           Item, dessen textProperty gebunden wird.
	 * @param sprache        Sprache, die das textProperty bereitstellt
	 * @param textSchluessel Schlussel fuer den zu suchenden Text
	 * @param alternativText Alternativer Text, falls der Schluessel nicht gefunden wurde
	 * @return
	 * @return das uebergebene MenuItem
	 */
	public static <T extends MenuItem> T bindText(T item, Sprache sprache,
			String textSchluessel, String alternativText) {
		item.textProperty().bind(sprache.getTextProperty(textSchluessel, alternativText));
		return item;
	}
	
	/**
	 * Bindet die textProperty eines Labeled-Node an das textProperty der uebergebenen Sprache
	 * zu dem textSchluessel. Falls der Schluessel nicht existiert wird der alternative Text
	 * gesetzt.
	 * 
	 * @param node           Node, dessen textProperty gebunden wird.
	 * @param sprache        Sprache, die das textProperty bereitstellt
	 * @param textSchluessel Schlussel fuer den zu suchenden Text
	 * @param alternativText Alternativer Text, falls der Schluessel nicht gefunden wurde
	 * @return das uebergebene Labeled-Objekt
	 */
	public static <T extends Labeled> T bindText(T node, Sprache sprache,
			String textSchluessel, String alternativText) {
		node.textProperty().bind(sprache.getTextProperty(textSchluessel, alternativText));
		return node;
	}
	
	// protected ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	// package ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	// private ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
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
	
	private SprachUtil() {
		// Hilfsklasse, nicht instanziierbar
	}
	
}