/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Sammlung von verschiedenen Programmressourcen
 * 
 * @author Tim Muehle
 *
 */
public class Ressourcen {
	
	private static Logger log = Logger.getLogger(Ressourcen.class.getName());
	
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
	
	// public ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	public final Ressource LIGHT_THEME_CSS;
	public final Ressource DARK_THEME_CSS;
	public final Ressource NUTZER_THEME_CSS;
	
	public final Ressource KONFIGURATIONSORDNER;
	public final Ressource NUTZER_EINSTELLUNGEN;
	public final Ressource LIZENZ_DATEI;
	
	public final Ressource CLASSIFIER_LOGO_M;
	public final Ressource CLASSIFIER_LOGO_L;
	public final Ressource CLASSIFIER_ICON_M;
	public final Ressource CLASSIFIER_ICON_L;
	
	// private ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	private final ProgrammDetails programm;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Ressourcen(ProgrammDetails programm) {
		log.config(() -> "Erzeuge Ressourcen");
		RessourceBuilder builder = new RessourceBuilder(programm);
		this.programm = programm;
		this.LIGHT_THEME_CSS = builder.css("lightTheme.css");
		this.DARK_THEME_CSS = builder.css("darkTheme.css");
		this.NUTZER_THEME_CSS = builder.css("customTheme.css");
		this.KONFIGURATIONSORDNER = builder.konfigurationsdatei(null);
		this.NUTZER_EINSTELLUNGEN = builder.konfigurationsdatei("nutzerEinstellung.json");
		this.LIZENZ_DATEI = builder.erzeuge("LICENSE.txt");
		this.CLASSIFIER_LOGO_M = builder.grafik("Classifier-Logo@1x.png");
		this.CLASSIFIER_LOGO_L = builder.grafik("Classifier-Logo@2x.png");
		this.CLASSIFIER_ICON_M = builder.grafik("Classifier-Icon-mittel.png");
		this.CLASSIFIER_ICON_L = builder.grafik("Classifier-Icon-gross.png");
		erstelleRessourcenOrdner();
	}
	
	private void erstelleRessourcenOrdner() {
		try {
			URI codePfad = Ressourcen.class.getProtectionDomain().getCodeSource()
					.getLocation().toURI();
			
			if (codePfad.toString().endsWith(".jar")) {
				// Programm wird nicht in einer jar-Datei ausgefuehrt
				log.fine(() -> "Aus Jar Extrahieren");
				try {
					DateiUtil.extrahiereAusJar(codePfad, "ressourcen",
							OS.getDefault().getKonfigurationsOrdnerPath(programm));
				} catch (IOException e) {
					log.log(Level.WARNING, e,
							() -> "Exportieren der Ressourcen aus Jar fehlgeschlagen");
				}
			} else {
				// Programm wird nicht in einer jar-Datei ausgefuehrt (z.B. Aufruf aus IDE)
				try {
					Path von = Path.of(codePfad).resolve("ressourcen");
					Path nach = OS.getDefault().getKonfigurationsOrdnerPath(programm);
					DateiUtil.kopiereDateibaum(von, nach);
				} catch (IOException e) {
					log.log(Level.WARNING, e, () -> "Kopieren der Ressourcen fehlgeschlagen");
				}
			}
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, e,
					() -> "Ausfuehrpfad der Klasse konnte nicht festgestellt werden");
		}
	}
	
}