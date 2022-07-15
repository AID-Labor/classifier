/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.ProgrammDetails;


/**
 * Sammlung von betriebssystemspezifischen Operationen
 * 
 * @author Tim Muehle
 *
 */
public abstract sealed class OS permits Unix, Windows, OS.Unbekannt {
	
	private static Logger log = Logger.getLogger(OS.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static OS instanz;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Gibt eine Instanz von OS passend zum ausfuehrenden Betriebssystem zurueck.
	 * 
	 * @return Instanz von OS zum ausfuehrenden Betriebssystem
	 */
	public static OS getDefault() {
		if (instanz == null) {
			try {
				instanz = createInstanz();
			} catch (UnsupportedOSException e) {
				instanz = new Unbekannt();
			}
		}
		
		return instanz;
	}
	
	static final class Unbekannt extends OS {
		
		private Unbekannt() {
			/* keine Instanziierung ausserhalb zulassen */}
			
		@Override
		public String getKonfigurationsOrdner(ProgrammDetails programm) {
			return this.pfadAus(new StringBuilder(this.getNutzerOrdner()),
				"." + programm.name()).toString();
		}
		
		@Override
		public boolean systemNutztDarkTheme() {
			return false;
		}
	}
	
	private static OS createInstanz() throws UnsupportedOSException {
		final String os = System.getProperty("os.name").toLowerCase();
		log.config(() -> "erzeuge OS fuer os.name=" + os);
		if (os.contains("win")) {
			log.config(() -> "    -> Instanz von Windows");
			return new Windows();
		} else if (os.contains("mac")) {
			log.config(() -> "    -> Instanz von MacOS");
			return new MacOS();
		} else if (os.contains("linux")) {
			log.config(() -> "    -> Instanz von Linux");
			return new Linux();
		} else if (os.contains("unix")) {
			log.config(() -> "    -> Instanz von Unix");
			return new Unix();
		} else {
			var exception = new UnsupportedOSException("Unbekanntes OS: " + os);
			log.log(Level.WARNING, exception,
				() -> "    -> OS konnte nicht ermittelt werden!");
			throw exception;
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
	
	/**
	 * Seperator fuer Dateien und Ordner in Dateipfaden
	 */
	protected final String seperator;
	protected final String nutzerOrdner;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// Sichtbarkeit des Konstruktors auf package beschraenken
	OS() {
		this.seperator = System.getProperty("file.separator");
		this.nutzerOrdner = System.getProperty("user.home");
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// -------------------------------------------------------------------------------------
	// abstract ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	/**
	 * Gibt den Ordner zurueck, in dem Programm Dateien gespeichert werden
	 * 
	 * @implSpec Subklassen sollen den Konfigurationsordner anhand der Konventionen fuer das
	 *           spezifische Betriebssystem zurueckgeben.
	 * 			
	 * @param programm Details des Programms - wird genutzt, um einen programmspezifischen
	 *                 Ordner zurueck zu geben.
	 * @return Einstellungsordner fuer das spezifische Programm in dem
	 *         betriebssystemspezifischen Pfad fuer Programmeinstellungen
	 */
	public abstract String getKonfigurationsOrdner(final ProgrammDetails programm);
	
	/**
	 * Gibt an, ob das System einen Darkmode verwendet, sofern dies ermittelt werden konnte.
	 * 
	 * @return {@code true}, wenn sicher festgestellt wurde, dass ein Darkmode verwendet wird,
	 *         sonst {@code false}
	 */
	public abstract boolean systemNutztDarkTheme();
	
	// =====================================================================================
	// public ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	/**
	 * Ermittelt den Stammordner des Nutzers
	 * 
	 * @return Stammordner des Nutzers
	 */
	public String getNutzerOrdner() {
		return this.nutzerOrdner;
	}
	
	/**
	 * Ermittelt den Stammordner des Nutzers
	 * 
	 * @return Stammordner des Nutzers
	 */
	public Path getNutzerOrdnerPath() {
		return Path.of(this.getNutzerOrdner());
	}
	
	/**
	 * Ermittelt den Speicherort fuer Dokumente
	 * 
	 * @apiNote Gibt {@code null} zurueck, falls kein passender Ordner gefunden wird.
	 * 
	 * @implNote Sucht den Ordner im Nutzerordner (siehe {@link getNutzerOrdner}) anhand der
	 *           gaengigen deutschen und englischen Namen in folgender Reihenfolge:
	 *           "Dokumente", "dokumente", "Documents", "documents"
	 * 			
	 * @return Dokumente-Ordner des Nutzers
	 */
	public String getDokumenteOrdner() {
		String[] pfade = {
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "Dokumente").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "dokumente").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "Documents").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "documents").toString()
		};
		for (String ordner : pfade) {
			if (Files.isDirectory(Path.of(ordner))) {
				return ordner;
			}
		}
		
		return null;
	}
	
	/**
	 * Ermittelt den Speicherort fuer Bilder
	 * 
	 * @apiNote Gibt {@code null} zurueck, falls kein passender Ordner gefunden wird.
	 * 
	 * @implNote Sucht den Ordner im Nutzerordner (siehe {@link getNutzerOrdner}) anhand der
	 *           gaengigen deutschen und englischen Namen in folgender Reihenfolge:
	 *           "Bilder", "bilder", "Pictures", "pictures", "Images", "images"
	 * 			
	 * @return Bilder-Ordner des Nutzers
	 */
	public String getBilderOrdner() {
		String[] pfade = {
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "Bilder").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "bilder").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "Pictures").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "pictures").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "Images").toString(),
			this.pfadAus(new StringBuilder(this.nutzerOrdner), "images").toString()
		};
		for (String ordner : pfade) {
			if (Files.isDirectory(Path.of(ordner))) {
				return ordner;
			}
		}
		
		return getDokumenteOrdner();
	}
	

	/**
	 * Ermittelt den Speicherort fuer Bilder
	 * 
	 * @apiNote Gibt {@code null} zurueck, falls kein passender Ordner gefunden wird.
	 * 
	 * @return Bilder-Ordner des Nutzers
	 */
	public Path getBilderOrdnerPath(final ProgrammDetails programm) {
		var pfad = this.getBilderOrdner();
		if (pfad != null) {
			return Path.of(this.getBilderOrdner());
		} else {
			return null;
		}
	}
	
	/**
	 * Ermittelt den Speicherort fuer Dokumente
	 * 
	 * @apiNote Gibt {@code null} zurueck, falls kein passender Ordner gefunden wird.
	 * 
	 * @return Dokumente-Ordner des Nutzers
	 */
	public Path getDokumenteOrdnerPath(final ProgrammDetails programm) {
		var pfad = this.getDokumenteOrdner();
		if (pfad != null) {
			return Path.of(this.getDokumenteOrdner());
		} else {
			return null;
		}
	}
	
	/**
	 * Gibt den Ordner zurueck, in dem Programm Dateien gespeichert werden
	 * 
	 * @apiNote Falls dieser Ordner noch nicht existiert, wird versucht diesen zu erstellen.
	 * 
	 * @param programm Details des Programms - wird genutzt, um einen programmspezifischen
	 *                 Ordner zurueck zu geben.
	 * @return Einstellungsordner fuer das spezifische Programm in dem
	 *         betriebssystemspezifischen Pfad fuer Programmeinstellungen
	 */
	public Path getKonfigurationsOrdnerPath(final ProgrammDetails programm) {
		var ordner = Path.of(this.getKonfigurationsOrdner(programm));
		try {
			Files.createDirectories(ordner);
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Problem beim Anlegen des Ordners "
				+ this.getKonfigurationsOrdner(programm));
			return ordner;
		}
		return ordner;
	}
	
	/**
	 * Zeigt an, ob das ausfuehrende Betriebssystem linuxbasiert ist.
	 * 
	 * @implNote Die default-Implementierung gibt immer {@code false} zurueck, abgeleitete
	 *           Klassen sollten die Methode ggf. ueberschreiben
	 * 			
	 * @return {@code true}, wenn das ausfuehrende Betriebssystem Linux ist, sonst
	 *         {@code false}
	 */
	public boolean istLinux() {
		return false;
	}
	
	/**
	 * Zeigt an, ob das ausfuehrende Betriebssystem macOS ist.
	 * 
	 * @implNote Die default-Implementierung gibt immer {@code false} zurueck, abgeleitete
	 *           Klassen sollten die Methode ggf. ueberschreiben
	 * 			
	 * @return {@code true}, wenn das ausfuehrende Betriebssystem macOS ist, sonst
	 *         {@code false}
	 */
	public boolean istMacOS() {
		return false;
	}
	
	/**
	 * Zeigt an, ob das ausfuehrende Betriebssystem unixbasiert oder unixaehnlich ist.
	 * 
	 * @implNote Die default-Implementierung gibt immer {@code false} zurueck, abgeleitete
	 *           Klassen sollten die Methode ggf. ueberschreiben
	 * 			
	 * @return {@code true}, wenn das ausfuehrende Betriebssystem unixkonform ist, sonst
	 *         {@code false}
	 */
	public boolean istUnix() {
		return false;
	}
	
	/**
	 * Zeigt an, ob das ausfuehrende Betriebssystem Windows ist.
	 * 
	 * @implNote Die default-Implementierung gibt immer {@code false} zurueck, abgeleitete
	 *           Klassen sollten die Methode ggf. ueberschreiben
	 * 			
	 * @return {@code true}, wenn das ausfuehrende Betriebssystem Windows ist, sonst
	 *         {@code false}
	 */
	public boolean istWindows() {
		return false;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn Start des Pfades
	 * @param ende   Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final CharSequence beginn, final CharSequence ende) {
		return pfadAus(new StringBuilder(beginn), ende);
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn Start des Pfades
	 * @param ende   Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final StringBuilder beginn, final CharSequence ende) {
		beginn.append(this.seperator).append(ende);
		return beginn;
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn Start des Pfades
	 * @param ordner Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param ende   Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final CharSequence beginn, final CharSequence ordner,
		final CharSequence ende) {
		return pfadAus(new StringBuilder(beginn), ordner, ende);
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn Start des Pfades
	 * @param ordner Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param ende   Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final StringBuilder beginn, final CharSequence ordner,
		final CharSequence ende) {
		beginn.append(this.seperator).append(ordner)
			.append(this.seperator).append(ende);
		return beginn;
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn  Start des Pfades
	 * @param ordner1 Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param ordner2 Ordnername, der als zweites an den Pfad angefuegt wird
	 * @param ende    Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final StringBuilder beginn, final CharSequence ordner1,
		final CharSequence ordner2, final CharSequence ende) {
		beginn.append(this.seperator).append(ordner1)
			.append(this.seperator).append(ordner2)
			.append(this.seperator).append(ende);
		return beginn;
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn  Start des Pfades
	 * @param ordner1 Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param ordner2 Ordnername, der als zweites an den Pfad angefuegt wird
	 * @param ende    Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final CharSequence beginn, final CharSequence ordner1,
		final CharSequence ordner2, final CharSequence ende) {
		return pfadAus(new StringBuilder(beginn), ordner1, ordner2, ende);
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn  Start des Pfades
	 * @param ordner1 Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param ordner2 Ordnername, der als zweites an den Pfad angefuegt wird
	 * @param ordner3 Ordnername, der als drittes an den Pfad angefuegt wird
	 * @param ende    Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final StringBuilder beginn, final CharSequence ordner1,
		final CharSequence ordner2, final CharSequence ordner3, final CharSequence ende) {
		beginn.append(this.seperator).append(ordner1)
			.append(this.seperator).append(ordner2)
			.append(this.seperator).append(ordner3)
			.append(this.seperator).append(ende);
		return beginn;
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn  Start des Pfades
	 * @param ordner1 Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param ordner2 Ordnername, der als zweites an den Pfad angefuegt wird
	 * @param ordner3 Ordnername, der als drittes an den Pfad angefuegt wird
	 * @param ende    Datei- oder Orndername am Ende des Pfades
	 * 
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final CharSequence beginn, final CharSequence ordner1,
		final CharSequence ordner2, final CharSequence ordner3, final CharSequence ende) {
		return pfadAus(new StringBuilder(beginn), ordner1, ordner2, ordner3, ende);
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn Start des Pfades
	 * @param ordner Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param rest   Ordnernamen, die in der uebergebenen Reihenfolge angefuegt werden. Der
	 *               letzte Parameter darf ein Dateiname sein.
	 * 				
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final StringBuilder beginn, final CharSequence ordner,
		final CharSequence... rest) {
		beginn.append(this.seperator).append(beginn)
			.append(this.seperator).append(ordner);
		for (CharSequence naechster : rest) {
			beginn.append(this.seperator).append(naechster);
		}
		return beginn;
	}
	
	/**
	 * Erstellt einen Ordnerpfad aus den Uebergabeparametern und sepperiert diese mit dem
	 * systemspezifischen Dateiseparator
	 * 
	 * @param beginn Start des Pfades
	 * @param ordner Ordnername, der als erstes an den Pfad angefuegt wird
	 * @param rest   Ordnernamen, die in der uebergebenen Reihenfolge angefuegt werden. Der
	 *               letzte Parameter darf ein Dateiname sein.
	 * 				
	 * @return Das selbe Objekt, das als {@code beginn} uebergeben wurde
	 */
	public StringBuilder pfadAus(final CharSequence beginn, final CharSequence ordner,
		final CharSequence... rest) {
		return pfadAus(new StringBuilder(beginn), ordner, rest);
	}
	
}
