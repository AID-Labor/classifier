/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.system.OS;


/**
 * Sammlung von verschiedenen Programmressourcen
 * 
 * @author Tim Muehle
 *
 */
public class Ressourcen {
	
	private static final Logger log = Logger.getLogger(Ressourcen.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static Ressourcen instanz;
	private static ProgrammDetails programmDetails;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Gibt die Sinleton-Instanz zurueck.
	 * 
	 * @apiNote Bevor diese Methode aufgerufen wird, muss das Programm mit der Methode
	 *          {@link Ressourcen#setProgrammDetails(ProgrammDetails)} initialisiert werden.
	 * @return Singleton-Instanz
	 * @throws IllegalStateException Wenn das Programm noch nicht mit
	 *                               {@link Ressourcen#setProgrammDetails(ProgrammDetails)}
	 *                               initialisiert wurde
	 */
	public static Ressourcen get() throws IllegalStateException {
		if (programmDetails == null) {
			var exception = new IllegalStateException("Bevor eine Instanz erzeugt werden kann "
					+ "muessen mit Ressourcen.setProgrammDetails die "
					+ "Programminformationen mitgeteilt werden!");
			log.throwing(Ressourcen.class.getName(), "get", exception);
			throw exception;
		}
		if (instanz == null) {
			instanz = new Ressourcen(programmDetails);
		}
		
		return instanz;
	}
	
	/**
	 * Initialisiert die Programmdetails fuer die Singleton-Instanz. Wird verwendet, um Pfade
	 * systemspezifisch fuer den Programmnamen aufzuloesen
	 * 
	 * @apiNote Diese Methode darf nur ein einziges Mal aufgerufen werden.
	 * 
	 * @param programm Details des Programmes, fuer das Ressourcen verwaltet werden. Darf
	 *                 nicht {@code null sein}!
	 * @throws IllegalStateException Wenn diese Methode mehr als einmal aufgerufen wird
	 */
	public static void setProgrammDetails(ProgrammDetails programm) {
		if (programmDetails != null) {
			var exception = new IllegalStateException("Es wurde bereits ein programm gesetzt!"
					+ "Der erneute Aufruf dieser Methode ist nicht erlaubt.");
			log.throwing(Ressourcen.class.getName(), "setProgrammDetails", exception);
			throw exception;
		}
		Ressourcen.programmDetails = Objects.requireNonNull(programm);
		var aufruf = Thread.currentThread().getStackTrace()[2];
		log.log(Level.FINE,
				() -> "Setze ProgrammDetails: %s -> Aufruf von: %s %s (Zeile %s)".formatted(
						programmDetails.getVersionName(),
						aufruf.getClassName(),
						aufruf.getMethodName(),
						aufruf.getLineNumber()));
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
	public final Ressource BASIS_CSS;
	public final Ressource LIGHT_THEME_CSS;
	public final Ressource DARK_THEME_CSS;
	public final Ressource NUTZER_THEME_CSS;
	
	public final Ressource KONFIGURATIONSORDNER;
	public final Ressource NUTZER_EINSTELLUNGEN;
	public final Ressource LIZENZ_DATEI;
	public final Ressource LIZENZ_INFO_ORDNER;
	
	public final Ressource SPRACHDATEIEN_ORDNER;
	
	public final Ressource CLASSIFIER_LOGO_M;
	public final Ressource CLASSIFIER_LOGO_L;
	public final Ressource CLASSIFIER_ICON_M;
	public final Ressource CLASSIFIER_ICON_L;
	public final Ressource UML_VERERBUNGS_PFEIL;
	public final Ressource UML_ASSOZIATIONS_PFEIL;
	public final Ressource UML_KOMMENTAR;
	
	public final Ressource JAVA_BEKANNTE_KLASSEN;
	public final Ressource JAVA_BEKANNTE_INTERFACES;
	public final Ressource JAVA_BEKANNTE_ENUMERATIONEN;
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	private final ProgrammDetails programm;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Ressourcen(ProgrammDetails programm) {
		log.config(() -> "Erzeuge Ressourcen");
		RessourceErzeuger erzeuger = new RessourceErzeuger(programm);
		this.programm = programm;
		this.BASIS_CSS = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.CSS)
				.name("classifier.css")
				.erzeuge();
		this.LIGHT_THEME_CSS = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.CSS)
				.name("lightTheme.css").erzeuge();
		this.DARK_THEME_CSS = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.CSS)
				.name("darkTheme.css")
				.erzeuge();
		this.NUTZER_THEME_CSS = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.CSS)
				.name("customTheme.css")
				.erzeuge();
		this.KONFIGURATIONSORDNER = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.KONFIGURATIONSDATEI)
				.erzeuge();
		this.NUTZER_EINSTELLUNGEN = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.KONFIGURATIONSDATEI)
				.name("nutzerEinstellung.json")
				.erzeuge();
		this.LIZENZ_DATEI = erzeuger
				.konfigurationsOrdner()
				.name("LICENSE.txt")
				.erzeuge();
		this.LIZENZ_INFO_ORDNER = erzeuger
				.konfigurationsOrdner()
				.name("lizenzinfo")
				.erzeuge();
		this.CLASSIFIER_LOGO_M = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("Classifier-Logo_1x.png")
				.erzeuge();
		this.CLASSIFIER_LOGO_L = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("Classifier-Logo_2x.png")
				.erzeuge();
		this.CLASSIFIER_ICON_M = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("Classifier-Icon-mittel.png")
				.erzeuge();
		this.CLASSIFIER_ICON_L = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("Classifier-Icon-gross.png")
				.erzeuge();
		this.UML_VERERBUNGS_PFEIL = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("vererbungs_pfeil.png")
				.erzeuge();
		this.UML_ASSOZIATIONS_PFEIL = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("assoziations_pfeil.png")
				.erzeuge();
		this.UML_KOMMENTAR = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.GRAFIK)
				.name("kommentar.png")
				.erzeuge();
		this.SPRACHDATEIEN_ORDNER = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.SPRACHDATEI)
				.erzeuge();
		this.JAVA_BEKANNTE_KLASSEN = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.JAVA)
				.name("bekannte_klassen")
				.erzeuge();
		this.JAVA_BEKANNTE_INTERFACES = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.JAVA)
				.name("bekannte_interfaces")
				.erzeuge();
		this.JAVA_BEKANNTE_ENUMERATIONEN = erzeuger
				.konfigurationsOrdner()
				.alsTyp(RessourceTyp.JAVA)
				.name("bekannte_enumerationen")
				.erzeuge();
		erstelleRessourcenOrdner();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private void erstelleRessourcenOrdner() {
		var umgebung = AusfuehrUmgebung.getFuerKlasse(programm.hauptklasse());
		switch (umgebung) {
			case IDE -> {
				try {
					Path von = Path
							.of(AusfuehrUmgebung.getAusfuehrPfad(programm.hauptklasse()))
							.resolve("ressourcen");
					Path nach = OS.getDefault().getKonfigurationsOrdnerPath(programm);
					DateiUtil.kopiereDateibaum(von, nach);
				} catch (IOException e) {
					log.log(Level.WARNING, e, () -> "Kopieren der Ressourcen fehlgeschlagen");
				}
			}
			case JAR -> {
				log.fine(() -> "Aus Jar Extrahieren");
				try {
					DateiUtil.extrahiereAusJar(
							AusfuehrUmgebung.getAusfuehrPfad(programm.hauptklasse()),
							"ressourcen",
							OS.getDefault().getKonfigurationsOrdnerPath(programm));
				} catch (IOException e) {
					log.log(Level.WARNING, e,
							() -> "Exportieren der Ressourcen aus Jar fehlgeschlagen");
				}
			}
			case JMOD -> {
				log.fine(() -> "Aus jmod Extrahieren");
				try {
					DateiUtil.extrahiereAusJmod(
							OS.getDefault().getKonfigurationsOrdnerPath(programm),
							"modules", this.getClass().getModule().getName(), "ressourcen");
				} catch (IOException e) {
					log.log(Level.WARNING, e,
							() -> "Exportieren der Ressourcen aus Jmod fehlgeschlagen");
				}
			}
			default ->
				log.severe(() -> "Ausfuehrpfad der Klasse konnte nicht festgestellt werden");
		}
	}
	
}