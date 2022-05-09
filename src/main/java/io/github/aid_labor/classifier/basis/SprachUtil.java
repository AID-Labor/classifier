/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
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
	 * Sucht nach Sprachdateien und stellt die erte gefundene Sprache aus der Reihenfolge ein.
	 * 
	 * @param sprache         Sprach-Objekt, fuer das die Sprache gesetzt wird
	 * @param suchordner      Ordner, in dem nach Sprachdateien gesucht wird
	 * @param dateiPraefix    Prafix im Dateinamen der Sprachdateien
	 * @param hauptsprache    Hauptsprache, die versucht wird einzustellen
	 * @param weitereSprachen Reihenfolge, in der die alternativen Sprachen geprueft werden,
	 *                        falls die Hauptsprache nicht gesetzt werden konnte
	 * @return @code true}, wenn eine Sprache erfolgreich gesetzt wurde, sonst {@code false}
	 */
	public static boolean setUpSprache(Sprache sprache, Path suchordner,
			String dateiPraefix, Locale hauptsprache, Locale... weitereSprachen) {
		Map<Locale, SprachDatei> gefundeneSprachen = Collections.emptyMap();
		try {
			gefundeneSprachen = sucheSprachdateien(suchordner, dateiPraefix);
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Fehler beim Suchen von Sprachdateien im Ordner %s mit dem Praefix %s"
							.formatted(suchordner, dateiPraefix));
		}
		
		var sortierteSprachen = sortiereSprachdateien(gefundeneSprachen, hauptsprache,
				weitereSprachen);
		
		return setzeSprache(sprache, sortierteSprachen);
	}
	
	/**
	 * Sucht nach Sprachdateien und stellt die erte gefundene Sprache aus der folgenden
	 * Reihenfolge ein:
	 * <ol>
	 * <li> {@link Einstellungen#sprachEinstellung aus Einstellungen#getBenutzerdefiniert()}
	 * <li> {@link Einstellungen#sprachEinstellung aus Einstellungen#getDefault()}
	 * <li> {@link Locale#GERMAN}
	 * <li> {@link Locale#GERMANY}
	 * <li> {@link Locale#ENGLISH}
	 * <li> {@link Locale#UK}
	 * <li> {@link Locale#US}
	 * </ol>
	 * 
	 * @param sprache         Sprach-Objekt, fuer das die Sprache gesetzt wird
	 * @param suchordner      Ordner, in dem nach Sprachdateien gesucht wird
	 * @param dateiPraefix    Prafix im Dateinamen der Sprachdateien
	 * @param hauptsprache    Hauptsprache, die versucht wird einzustellen
	 * @param weitereSprachen Reihenfolge, in der die alternativen Sprachen geprueft werden,
	 *                        falls die Hauptsprache nicht gesetzt werden konnte
	 * @return @code true}, wenn eine Sprache erfolgreich gesetzt wurde, sonst {@code false}
	 */
	public static boolean setUpSprache(Sprache sprache, Path suchordner, String dateiPraefix) {
		return setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"HauptAnsicht", Einstellungen.getBenutzerdefiniert().sprachEinstellung.get(),
				Einstellungen.getDefault().sprachEinstellung.get(), Locale.GERMAN,
				Locale.GERMANY, Locale.ENGLISH, Locale.UK, Locale.US);
	}
	
	/**
	 * Stellt die Sprachdatei fuer ein Sprach-Objekt ein. Die Schlange mit Sprachdateien wird
	 * solange geleert, bis eine Sprachdatei gesetzt werden konnte
	 * 
	 * @param sprache  Sprach-Objekt, das initialisiert wird
	 * @param sprachen Schlange mit Sprachdateien
	 * @return {@code true}, wenn eine Sprache erfolgreich gesetzt wurde, sonst {@code false}
	 */
	public static boolean setzeSprache(Sprache sprache, Queue<SprachDatei> sprachen) {
		boolean erfolg = false;
		while (!sprachen.isEmpty() && !erfolg) {
			SprachDatei sprachdatei = sprachen.remove();
			try {
				sprache.nutzeSprache(sprachdatei);
				erfolg = true;
			} catch (Exception e) {
				log.log(Level.INFO, e, () -> "Sprachdatei fuer %s konnte nicht gelesen werden"
						.formatted(sprachdatei));
			}
		}
		
		return erfolg;
	}
	
	/**
	 * Sucht in dem vorgegebenen Ordner nach Sprachdateien. Die Dateien muessen in
	 * Unterordnern liegen, die entsprechend der Sprache nach der Spezifikation des "IETF
	 * BCP 47 language tag" benannt sein muessen. Es werden alle Sprachdateien der Liste
	 * hinzugefuegt, deren Dateiname mit dem gegebenen Preafix beginnt. Das
	 * {@link Locale}-Objekt wird aus dem Ordnernamen erzeugt, in dem die Sprachdatei
	 * gefunden wurde (siehe {@link Locale#forLanguageTag(String)}).
	 * 
	 * @param suchordner   Ordner, in dem nach Sprachdateien gesucht wird
	 * @param dateiPraefix Praefix, mit dem die gesuchten Dateien beginnen
	 * @return Map mit allen gefundenen Sprachdateien, die dem Suchkriterium entsprechen
	 * @throws IOException
	 */
	public static Map<Locale, SprachDatei> sucheSprachdateien(Path suchordner,
			String dateiPraefix) throws IOException {
		log.finer(() -> "Suche nach Sprachdateien mit dem Namen %s* im Ordner %s"
				.formatted(dateiPraefix, suchordner));
		
		Map<Locale, SprachDatei> dateien = new HashMap<>();
		
		Files.walk(suchordner).forEach(datei -> {
			log.finer(() -> "Pruefe Datei " + datei);
			if (Files.isRegularFile(datei)
					&& datei.getFileName().toString().startsWith(dateiPraefix)) {
				Locale l = Locale
						.forLanguageTag(datei.getParent().getFileName().toString());
				log.finest(() -> "gefunden: %s [%s - %s]".formatted(datei,
						l.getDisplayLanguage(), l.toLanguageTag()));
				dateien.put(l, new SprachDatei(datei, l));
			}
		});
		
		return dateien;
	}
	
	/**
	 * Sortiert Sprachdateien in eine Schlange. Kann eine Sprache nicht gefunden werden, wird
	 * diese automatisch uebersprungen.
	 * 
	 * @param dateien         Map mit verfuegbaren Sprachdateien
	 * @param hauptSprache    Hauptsprache, die (falls verfuegbar) als erstes in die Schlange
	 *                        eingefuegt wird
	 * @param weitereSprachen Reihenfolge, in der die alternativen Sprachdateien anhand der
	 *                        Sprache in der Schlange sortiert werden
	 * @return Sortierte Schlange mit Sprachdateien
	 */
	public static Queue<SprachDatei> sortiereSprachdateien(Map<Locale, SprachDatei> dateien,
			Locale hauptSprache, Locale... weitereSprachen) {
		Queue<SprachDatei> schlange = new LinkedList<>();
		
		sprachdateiHinzufuegen(schlange, dateien, hauptSprache);
		
		for (Locale sprache : weitereSprachen) {
			sprachdateiHinzufuegen(schlange, dateien, sprache);
		}
		
		return schlange;
	}
	
	private static void sprachdateiHinzufuegen(Queue<SprachDatei> schlange,
			Map<Locale, SprachDatei> dateien, Locale sprache) {
		try {
			var sprachdatei = dateien.get(sprache);
			if (schlange.offer(sprachdatei)) {
				log.finest(() -> "Sprache %s [%s] in Schlange eingereiht".formatted(
						sprache.getDisplayLanguage(), sprache.toLanguageTag()));
			} else {
				log.config(() -> "Sprache %s [%s] nicht gefunden".formatted(
						sprache.getDisplayLanguage(), sprache.toLanguageTag()));
			}
		} catch (Exception e) {
			log.log(Level.WARNING, e, () -> "Sprache %s [%s] nicht gefunden".formatted(
					sprache.getDisplayLanguage(), sprache.toLanguageTag()));
		}
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