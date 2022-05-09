/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


/**
 * Bereitstellung von Sprachabhaengigen Strings. Kompatibel mit dem
 * Beobachter-Entwurfsmuster durch Nutzung von {@link ReadOnlyStringProperty} aus
 * javafx.base.
 * 
 * Diese Klasse nutzt im Hintergrund ein {@link PropertyResourceBundle} zur Verwaltung der
 * Sprachen und benoetigt Sprachdateien, die im Java-Properties-Format vorliegen. Jeder
 * benoetigte Text muss darin mit einem Schluessel im Format "{@code schluessel=text}"
 * vorliegen. <strong>Alle Sprachdateien muessen UTF-8 codiert sein!</strong>
 * 
 * @author Tim Muehle
 *
 */
public class Sprache {
	
	private static final Logger log = Logger.getLogger(Sprache.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Wrapper fuer Sprachdateien mit zugeordneter Sprache
	 * 
	 * @author Tim Muehle
	 * 
	 */
	public static record SprachDatei(Path datei, Locale sprache) {
		// keine weiteren Methoden benötigt
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private PropertyResourceBundle sprachpaket;
	private Map<String, StringProperty> textProperties;
	private Charset codierung;
	private Locale aktuelleSprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Erzeugt eine neue Instanz, die alle uebergebenen Sprachen kennt. Als genutzte Sprache
	 * wird der erste Parameter eingestellt.
	 * 
	 * @param sprachdatei     Zu benutzende Sprache
	 * @param weitereSprachen Weitere Sprachen, die mit {code nutzeSprache} eingestellt werden
	 *                        koennen
	 * @throws IOException Falls ein Fehler beim Lesen der default-Sprachdatei auftritt
	 */
	public Sprache(SprachDatei sprachdatei)
			throws IOException {
		this();
		this.sprachpaket = new PropertyResourceBundle(
				Files.newBufferedReader(sprachdatei.datei(), codierung));
		this.aktuelleSprache = sprachdatei.sprache();
	}
	
	/**
	 * Erzeugt eine neue Instanz, ohne Sprachen bekannt zu machen und ohne die genutze Sprache
	 * einzustellen.
	 */
	public Sprache() {
		this.sprachpaket = null;
		this.textProperties = new HashMap<>();
		
		try {
			codierung = Charset.forName("UTF-8");
		} catch (Exception e) {
			log.log(Level.WARNING, e, () -> "Charset UTF-8 nicht gefunden");
		}
		codierung = Objects.requireNonNullElse(codierung, Charset.defaultCharset());
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Gibt die eingestellte Sprache zurück
	 * 
	 * @return aktuell eingestellte Sprache
	 */
	public Locale getAktuelleSprache() {
		return aktuelleSprache;
	}
	
	/**
	 * Gibt den Text zum gegebenen Schluessel in der genutzten Sprache zurueck
	 * 
	 * @param schluessel Suchschluessel
	 * @return Text, der mit dem Suchschluessel verknueft ist
	 * @throws MissingResourceException Falls der Suchschluessel nicht gefunden wurde
	 * @throws IllegalStateException    Falls die Sprache noch nicht eingestellt wurde
	 */
	public String getText(String schluessel)
			throws MissingResourceException, IllegalStateException {
		if (this.sprachpaket == null) {
			var exception = new IllegalStateException("""
					Vor der Verwendung muss zuerst die Sprache mit \
					nutzeSprache(SprachDatei sprachdatei) gesetzt werden
					""");
			log.throwing(Sprache.class.getName(), "getText(String)", exception);
			throw exception;
		}
		try {
			String text = this.sprachpaket.getString(schluessel);
			log.finest(() -> "gefundener Text fuer Schluessel \"%s\": %s".formatted(schluessel,
					text));
			return text;
		} catch (Exception e) {
			log.warning(
					() -> "Kein Text fuer Schluessel \"%s\" gefunden".formatted(schluessel));
			throw e;
		}
	}
	
	/**
	 * Gibt den Text zum gegebenen Schluessel in der genutzten Sprache zurueck oder einen
	 * alternativen Wert, falls der Suchschluessel nicht existiert
	 * 
	 * @param schluessel Suchschluessel
	 * @param alternativ alternativer Text, falls der Suchschluessel nicht existiert
	 * @return Text, der mit dem Suchschluessel verknueft ist oder einen
	 *         alternativen Wert, falls der Suchschluessel nicht existiert
	 * 		
	 * @throws IllegalStateException Falls die Sprache noch nicht eingestellt wurde
	 */
	public String getText(String schluessel, String alternativ) throws IllegalStateException {
		if (this.sprachpaket == null) {
			var exception = new IllegalStateException("""
					Vor der Verwendung muss zuerst die Sprache mit \
					nutzeSprache(SprachDatei sprachdatei) gesetzt werden
					""");
			log.throwing(Sprache.class.getName(), "getText(String, String)", exception);
			throw exception;
		}
		
		if (this.sprachpaket.containsKey(schluessel)) {
			String text = this.sprachpaket.getString(schluessel);
			log.finest(() -> "gefundener Text fuer Schluessel \"%s\": %s".formatted(schluessel,
					text));
			return text;
		} else {
			log.finest(
					() -> "kein Text fuer Schluessel \"%s\" gefunden".formatted(schluessel));
			return alternativ;
		}
	}
	
	/**
	 * Gibt den Text zum gegebenen Schluessel in der genutzten Sprache verpackt in einer
	 * {@link ReadOnlyStringProperty} zurueck.
	 * 
	 * Wenn sich die eingestellte Sprache aendert werden alle erzeugten Objekte von
	 * {@link ReadOnlyStringProperty} automatisch aktualisiert.
	 * 
	 * @param schluessel Suchschluessel
	 * @return Text, der mit dem Suchschluessel verknueft ist oder einen
	 *         alternativen Wert, falls der Suchschluessel nicht existiert
	 * 		
	 * @throws MissingResourceException Falls der Suchschluessel nicht gefunden wurde
	 * @throws IllegalStateException    Falls die Sprache noch nicht eingestellt wurde
	 */
	public ReadOnlyStringProperty getTextProperty(String schluessel)
			throws MissingResourceException {
		if (this.sprachpaket == null) {
			var exception = new IllegalStateException("""
					Vor der Verwendung muss zuerst die Sprache mit \
					nutzeSprache(SprachDatei sprachdatei) gesetzt werden
					""");
			log.throwing(Sprache.class.getName(), "getTextProperty(String)", exception);
			throw exception;
		}
		
		StringProperty text = this.textProperties.get(schluessel);
		if (text == null) {
			text = new SimpleStringProperty(getText(schluessel));
			this.textProperties.put(schluessel, text);
		}
		
		return text;
	}
	
	/**
	 * Gibt den Text zum gegebenen Schluessel in der genutzten Sprache verpackt in einer
	 * {@link ReadOnlyStringProperty} zurueck oder einen alternativen Wert, falls der
	 * Suchschluessel nicht existiert.
	 * 
	 * Wenn sich die eingestellte Sprache aendert werden alle erzeugten Objekte von
	 * {@link ReadOnlyStringProperty} automatisch aktualisiert.
	 * 
	 * @param schluessel Suchschluessel
	 * @param alternativ alternativer Text, falls der Suchschluessel nicht existiert
	 * @return Text, der mit dem Suchschluessel verknueft ist oder einen
	 *         alternativen Wert, falls der Suchschluessel nicht existiert
	 * 		
	 * @throws IllegalStateException Falls die Sprache noch nicht eingestellt wurde
	 */
	public ReadOnlyStringProperty getTextProperty(String schluessel, String alternativ) {
		if (this.sprachpaket == null) {
			var exception = new IllegalStateException("""
					Vor der Verwendung muss zuerst die Sprache mit \
					nutzeSprache(SprachDatei sprachdatei) gesetzt werden
					""");
			log.throwing(Sprache.class.getName(), "getTextProperty(String, String)",
					exception);
			throw exception;
		}
		
		StringProperty text = this.textProperties.get(schluessel);
		if (text == null) {
			text = new SimpleStringProperty(getText(schluessel, alternativ));
			this.textProperties.put(schluessel, text);
		}
		
		return text;
	}
	
	/**
	 * Gibt an, welche Sprache aktuell eingestellt ist
	 * 
	 * @return aktuelle eingestellte Sprache
	 */
	public Locale getLocale() {
		return this.aktuelleSprache;
	}
	
	/**
	 * Stellt die genutzte Sprache ein
	 * 
	 * @param sprache Sprache, die fuer alle Getter verwendet werden soll
	 * @throws IOException              Falls beim Lesen der Sprachdatei ein Fehler auftritt
	 * @throws MissingResourceException Falls die Sprachdatei zur uebergebenen Sprache nicht
	 *                                  mit {@link #spracheHinzufuegen(SprachDatei)} bekannt
	 *                                  gemacht wurde
	 */
	public void nutzeSprache(SprachDatei sprachdatei) throws IOException {
		this.aktuelleSprache = sprachdatei.sprache();
		this.sprachpaket = new PropertyResourceBundle(
				Files.newBufferedReader(sprachdatei.datei(), codierung));
		log.fine(() -> "Neue Sprache: %s [%s]".formatted(aktuelleSprache.getDisplayLanguage(),
				aktuelleSprache.toLanguageTag()));
		updateProperties();
	}
	
	/**
	 * Setzte die Ueberpruefung, ob eine Sprache gewaehlt wurde, ausser Kraft. Eine ggf.
	 * eingestellte Sprache wird ueberschrieben. <strong>Achtung: es koennen keine gueltigen
	 * Schluessel mehr gelesen werden, bis eine neue Sprache eingestellt wird!</strong>
	 */
	public void ignoriereSprachen() {
		this.aktuelleSprache = new Locale("unbekannt");
		try {
			this.sprachpaket = new PropertyResourceBundle(new StringReader(""));
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Fehler beim erzeugen eines leeren PropertyResoucesBundles");
		}
		log.info(() -> "Sprach-Einstellungen sind ausser Kraft gesetzt");
	}
	
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
	
	private void updateProperties() {
		log.finer(() -> "aktualisiere Sprach-Properties");
		this.textProperties.entrySet().parallelStream().forEach(eintrag -> {
			Platform.runLater(() -> {
				log.finest(() -> "aktualisiere Schluessel " + eintrag.getKey());
				try {
					eintrag.getValue().set(this.sprachpaket.getString(eintrag.getKey()));
				} catch (Exception e) {
					log.log(Level.WARNING, e,
							() -> "Schluessel \"%s\" fuer Sprache %s [%s] nicht gefunden"
									.formatted(
											eintrag.getKey(),
											this.aktuelleSprache.getDisplayLanguage(),
											this.aktuelleSprache.toLanguageTag()));
				}
			});
		});
	}
}