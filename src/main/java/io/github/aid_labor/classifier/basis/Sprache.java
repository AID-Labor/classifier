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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	/**
	 * Wrapper fuer Sprachdateien mit zugeordneter Sprache
	 * 
	 * @author Tim Muehle
	 * 
	 */
	public static record SprachDatei(Path datei, Locale sprache) {
		
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
		 * @return Liste mit allen gefundenen Sprachdateien, die dem Suchkriterium entsprechen
		 * @throws IOException
		 */
		public static List<SprachDatei> sucheSprachdateien(Path suchordner,
				String dateiPraefix) throws IOException {
			log.fine(() -> "Suche nach Sprachdateien mit dem Namen %s* im Ordner %s"
					.formatted(dateiPraefix, suchordner));
			
			List<SprachDatei> dateien = new ArrayList<>();
			
			Files.walk(suchordner).forEach(datei -> {
				log.finer(() -> "Pruefe Datei " + datei);
				if (Files.isRegularFile(datei)
						&& datei.getFileName().toString().startsWith(dateiPraefix)) {
					log.finer(() -> "gefunden: " + datei);
					dateien.add(new SprachDatei(datei, Locale
							.forLanguageTag(datei.getParent().getFileName().toString())));
				}
			});
			
			return dateien;
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
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private PropertyResourceBundle sprachpaket;
	private Map<Locale, Path> sprachen;
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
	 * @param defaultSprache  Zu benutzende Sprache
	 * @param weitereSprachen Weitere Sprachen, die mit {code nutzeSprache} eingestellt werden
	 *                        koennen
	 * @throws IOException Falls ein Fehler beim Lesen der default-Sprachdatei auftritt
	 */
	public Sprache(SprachDatei defaultSprache, SprachDatei... weitereSprachen)
			throws IOException {
		this();
		this.sprachpaket = new PropertyResourceBundle(
				Files.newBufferedReader(defaultSprache.datei(), codierung));
		this.aktuelleSprache = defaultSprache.sprache();
		this.sprachen.put(defaultSprache.sprache(), defaultSprache.datei());
		this.sprachenHinzufuegen(weitereSprachen);
	}
	
	/**
	 * Erzeugt eine neue Instanz, ohne Sprachen bekannt zu machen und ohne die genutze Sprache
	 * einzustellen.
	 */
	public Sprache() {
		this.sprachpaket = null;
		this.sprachen = new Hashtable<>();
		this.textProperties = new Hashtable<>();
		
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
					setSprache(Locale sprache) gesetzt werden
					""");
			log.throwing(Sprache.class.getName(), "getText(String)", exception);
			throw exception;
		}
		
		return this.sprachpaket.getString(schluessel);
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
					setSprache(Locale sprache) gesetzt werden
					""");
			log.throwing(Sprache.class.getName(), "getText(String, String)", exception);
			throw exception;
		}
		
		if (this.sprachpaket.containsKey(schluessel)) {
			return this.sprachpaket.getString(schluessel);
		} else {
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
					setSprache(Locale sprache) gesetzt werden
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
					setSprache(Locale sprache) gesetzt werden
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
	public void nutzeSprache(Locale sprache) throws IOException, MissingResourceException {
		if (!this.sprachen.containsKey(sprache)) {
			var exception = new MissingResourceException("""
					Die gesuchte Sprache %s (Code: "%s") konnte nicht gefunden werden. Bitte \
					stellen Sie eine passende Sprachdatei mit der Methode \
					addSprache(SprachDatei sprachdatei) bereit."""
					.formatted(sprache.getDisplayLanguage(), sprache.toLanguageTag()),
					"Sprache", sprache.getDisplayName());
			log.throwing(Sprache.class.getName(), "setSprache", exception);
			throw exception;
		}
		this.sprachpaket = new PropertyResourceBundle(
				Files.newBufferedReader(this.sprachen.get(sprache), codierung));
		this.aktuelleSprache = sprache;
		log.fine(() -> "Neue Sprache: %s [%s]".formatted(sprache.getDisplayLanguage(),
				sprache.toLanguageTag()));
		updateProperties();
	}
	
	/**
	 * Stellt die genutzte Sprache ein
	 * 
	 * @param hauptsprache      bevorzugte Sprache, die fuer alle Getter verwendet werden soll
	 * @param rueckfallSprachen weitere Sprachen, die in der Reihenfolge der Uebergabe
	 *                          versucht werden einzustellen, bis eine Sprache erfolgreich
	 *                          gesetzt werden konnte
	 * @throws IllegalStateException Falls keine der Uebergebenen Sprachen bekannt war oder
	 *                               falls keine der Sprachdateien gelesen und eingestellt
	 *                               werden konnte. Alle unterdrueckten Exceptions koennen mit
	 *                               {@code getSupressed()} abgefragt werden.
	 */
	public void nutzeSprache(Locale hauptsprache, Locale... rueckfallSprachen)
			throws IllegalStateException {
		boolean erfolg = false;
		Exception exception1 = null;
		Exception exception2 = null;
		try {
			this.nutzeSprache(hauptsprache);
			erfolg = true;
		} catch (MissingResourceException | IOException e) {
			exception1 = e;
			for (Locale sprache : rueckfallSprachen) {
				try {
					this.nutzeSprache(sprache);
					erfolg = true;
					break;
				} catch (MissingResourceException | IOException e2) {
					if (exception2 == null) {
						exception2 = e2;
					} else {
						e2.addSuppressed(exception2);
						exception2 = e2;
					}
					continue;
				}
			}
		}
		
		if (!erfolg) {
			var ex = new IllegalStateException(
					"Es konnte keine passende Sprache gesetzt werden");
			if (exception1 != null) {
				ex.addSuppressed(exception1);
			}
			if (exception2 != null) {
				ex.addSuppressed(exception2);
			}
			throw ex;
		}
	}
	
	/**
	 * Stellt die genutzte Sprache ein
	 * 
	 * @param hauptsprache      bevorzugte Sprache, die fuer alle Getter verwendet werden soll
	 * @param rueckfallSprachen weitere Sprachen, die in der Reihenfolge der Uebergabe
	 *                          versucht werden einzustellen, bis eine Sprache erfolgreich
	 *                          gesetzt werden konnte
	 * @throws IllegalStateException Falls keine der Uebergebenen Sprachen bekannt war oder
	 *                               falls keine der Sprachdateien gelesen und eingestellt
	 *                               werden konnte. Alle unterdrueckten Exceptions koennen mit
	 *                               {@code getSupressed()} abgefragt werden.
	 */
	public void nutzeSprache(Locale hauptsprache, Iterable<Locale> rueckfallSprachen)
			throws IllegalStateException {
		boolean erfolg = false;
		Exception exception1 = null;
		Exception exception2 = null;
		try {
			this.nutzeSprache(hauptsprache);
			erfolg = true;
		} catch (MissingResourceException | IOException e) {
			exception1 = e;
			for (Locale sprache : rueckfallSprachen) {
				try {
					this.nutzeSprache(sprache);
					erfolg = true;
					break;
				} catch (MissingResourceException | IOException e2) {
					if (exception2 == null) {
						exception2 = e2;
					} else {
						e2.addSuppressed(exception2);
						exception2 = e2;
					}
					continue;
				}
			}
		}
		
		if (!erfolg) {
			var ex = new IllegalStateException(
					"Es konnte keine passende Sprache gesetzt werden");
			if (exception1 != null) {
				ex.addSuppressed(exception1);
			}
			if (exception2 != null) {
				ex.addSuppressed(exception2);
			}
			throw ex;
		}
	}
	
	/**
	 * Fuegt eine Sprachdatei zu den bekannten Sprachen hinzu.
	 * 
	 * @param sprachdatei Sprachdatei, die hinzugefuegt (bekanntgemacht) wird
	 * @throws IllegalArgumentException Falls die zugehoerige Sprache bereits existiert
	 */
	public void spracheHinzufuegen(SprachDatei sprachdatei) throws IllegalArgumentException {
		if (this.sprachen.containsKey(sprachdatei.sprache)) {
			var exception = new IllegalArgumentException("Sprache %s bereits gesetzt!"
					.formatted(sprachdatei.sprache.getDisplayLanguage()));
			log.throwing(Sprache.class.getName(), "addSprache", exception);
			throw exception;
		}
		this.sprachen.put(sprachdatei.sprache, sprachdatei.datei());
	}
	
	/**
	 * Fuegt mehrere Sprachdateien zu den bekannten Sprachen hinzu. Die Sprachen werden nur
	 * alle zusammen hinzugefuegt. Im Fehlerfall wird keine der uebergebenen Sprachen
	 * hinzugefuegt.
	 * 
	 * @param sprachdateien Sprachdateien, die hinzugefuegt (bekanntgemacht) werden
	 * @throws IllegalArgumentException Falls eine der Sprachen bereits existiert
	 */
	public void sprachenHinzufuegen(SprachDatei... sprachdateien)
			throws IllegalArgumentException {
		// Zwischenspeicher verwenden, um sicherzustellen, dass keine Datei hinzugefuegt wird,
		// falls ein Fehler auftritt
		Map<Locale, Path> tempSprachen = new Hashtable<>(Objects.requireNonNull(sprachdateien,
				"Es muss mindestens eine Sprachdatei uebergeben werden").length);
		for (SprachDatei sprachdatei : sprachdateien) {
			if (this.sprachen.containsKey(sprachdatei.sprache)) {
				var exception = new IllegalArgumentException("Sprache %s bereits gesetzt!"
						.formatted(sprachdatei.sprache.getDisplayLanguage()));
				log.throwing(Sprache.class.getName(), "addSprache", exception);
				throw exception;
			}
			tempSprachen.put(sprachdatei.sprache, sprachdatei.datei());
		}
		
		this.sprachen.putAll(tempSprachen);
	}
	
	/**
	 * Fuegt mehrere Sprachdateien zu den bekannten Sprachen hinzu. Die Sprachen werden nur
	 * alle zusammen hinzugefuegt. Im Fehlerfall wird keine der uebergebenen Sprachen
	 * hinzugefuegt.
	 * 
	 * @param sprachdateien Sprachdateien, die hinzugefuegt (bekanntgemacht) werden
	 * @throws IllegalArgumentException Falls eine der Sprachen bereits existiert
	 */
	public void sprachenHinzufuegen(Collection<SprachDatei> sprachdateien)
			throws IllegalArgumentException {
		// Zwischenspeicher verwenden, um sicherzustellen, dass keine Datei hinzugefuegt wird,
		// falls ein Fehler auftritt
		Map<Locale, Path> tempSprachen = new Hashtable<>(Objects.requireNonNull(sprachdateien,
				"Es muss mindestens eine Sprachdatei uebergeben werden").size());
		for (SprachDatei sprachdatei : sprachdateien) {
			if (this.sprachen.containsKey(sprachdatei.sprache)) {
				var exception = new IllegalArgumentException("Sprache %s bereits gesetzt!"
						.formatted(sprachdatei.sprache.getDisplayLanguage()));
				log.throwing(Sprache.class.getName(), "addSprache", exception);
				throw exception;
			}
			tempSprachen.put(sprachdatei.sprache, sprachdatei.datei());
		}
		
		this.sprachen.putAll(tempSprachen);
	}
	
	/**
	 * Setzte die Ueberpruefung, ob eine Sprache gewaehlt wurde ausser Kraft. Eine ggf.
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
			log.finest(() -> "aktualisiere Schluessel " + eintrag.getKey());
			eintrag.getValue().set(this.sprachpaket.getString(eintrag.getKey()));
		});
	}
}