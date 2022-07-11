/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.programmierung;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;


/**
 * Service zur Bereitstellung von Anforderungen und Konfigurationen von Programmiersprachen im Zusammenhang mit dem
 * UML-Klassendiagramm. Die {@code toString()}-Methode sollte überschrieben werden und den Namen der Programmiersprache
 * zurückgeben.
 * 
 * 
 * @author Tim Muehle
 *
 */
@JsonSerialize(converter = Programmiersprache.ProgrammierspracheZuStringConverter.class)
public interface Programmiersprache {
	static final Logger log = Logger.getLogger(Programmiersprache.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	public static class ProgrammierspracheZuStringConverter extends StdConverter<Programmiersprache, String> {
		@Override
		public String convert(Programmiersprache value) {
			return value.getName();
		}
	} 
	
	@JsonCreator
	public static Programmiersprache sucheProvier(String programmiersprache) {
		var sprache = ProgrammiersprachenVerwaltung.getProgrammiersprachen().stream()
				.filter(ps -> ps.getName().equals(programmiersprache)).findFirst();
		
		if (sprache.isEmpty()) {
			log.config(() -> "Keine Programmiersprache mit dem Namen %s gefunden!".formatted(programmiersprache));
			return null;
		} else {
			log.config(() -> "Gefundener Provieder für Programmiersprache %s: %s".formatted(programmiersprache,
					sprache.get().getClass().getName()));
			return sprache.get();
		}
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	public String getName();
	
	public ProgrammierEigenschaften getEigenschaften();
	
	public ExportImportVerarbeitung getVerarbeitung();
	
}