/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.json.JsonUtil;

/**
 * Wrapper fuer Informationen zu Bibliotheken von Drittanbietern. Die Informationen werden aus
 * einer json-Datei geladen. Alle json-Schluessel haben den gleichen Namen und Aufbau wie die
 * Record-Komponenten.
 * 
 * @author Tim Muehle
 *
 */
// @formatter:off
public record BibliothekInfo (
		String name,
		String version,
		String beschreibung,
		String github,
		String link,
		LizenzInfo license
) {

	private static final Logger log = Logger.getLogger(BibliothekInfo.class.getName());
	
	/**
	 * Informationen zu einer Lizenz
	 * 
	 * @author Tim Muehle
	 *
	 */
	public record LizenzInfo (
			String name,
			String link
	) {
		// nichts weiter benoetigt
	}
	
// @formatter:on
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	/**
	 * Läd Informationen zu einer Bibliothek aus einer JSON-Datei.
	 * Die JSON-Datei muss dabei Schlüssel-Werte-Paare für alle Record-Komponenten dieser Klasse und von 
	 * {@link LizenzInfo} haben.
	 */
	public static BibliothekInfo ladeAusJson(Path jsondatei) {
		try (var json = JsonUtil.getUTF8JsonParser(jsondatei)) {
			return json.readValueAs(BibliothekInfo.class);
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "BibliothekInfo konnte nicht aus json %s erzeugt werden".formatted(jsondatei));
		}
		
		return null;
	}
	
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
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	// impliziter Konstruktor fuer Record-Komponenten
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
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
	
}