/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Informationen zur Umgebung, in dem ein Programm ausgefuehrt wird
 * 
 * @author Tim Muehle
 *
 */
public enum AusfuehrUmgebung {
	
	JAR, IDE, JMOD, UNBEKANNT;
	
	private static Logger log = Logger.getLogger(AusfuehrUmgebung.class.getName());
	
	/**
	 * Ermittelt die Umgebung, in der eine Klasse ausgefuehrt wird
	 * 
	 * @param klasse Klasse, fuer die die Umgebung ermittelt wird
	 * @return Art der Ausfuehr-Umgebung, falls diese ermittelt werden kann
	 */
	public static AusfuehrUmgebung getFuerKlasse(Class<?> klasse) {
		try {
			URI codePfad = klasse.getProtectionDomain().getCodeSource().getLocation().toURI();
			
			if (codePfad.getPath().contains("classes")) {
				// Programm wird nicht in einer jar-Datei ausgefuehrt (z.B. Aufruf aus IDE)
				return IDE;
			} else if (codePfad.toString().endsWith(".jar")) {
				// Programm wird in einer jar-Datei ausgefuehrt
				return JAR;
			} else if (codePfad.toString().contains("jrt:/")) {
				// Programm wird aus jmod ausgefuehrt
				return JMOD;
			}
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, e,
					() -> "Ausfuehrpfad der Klasse konnte nicht festgestellt werden");
		}
		
		return UNBEKANNT;
	}
	
	/**
	 * Pfad, in dem die Klasse ausgefuehrt wird
	 * 
	 * @param klasse Klasse, deren Ausfuehrpfad ermittelt werden soll
	 * @return Pfad, in dem die Klasse ausgefuehrt wird
	 */
	public static URI getAusfuehrPfad(Class<?> klasse) {
		try {
			return klasse.getProtectionDomain().getCodeSource().getLocation().toURI();
		} catch (URISyntaxException e) {
			log.log(Level.WARNING, e,
					() -> "Ausfuehrpfad der Klasse konnte nicht festgestellt werden");
			return URI.create("");
		}
	}
}