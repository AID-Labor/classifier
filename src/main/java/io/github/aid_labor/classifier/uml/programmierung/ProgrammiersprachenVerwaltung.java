/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.programmierung;

import java.util.List;
import java.util.ServiceLoader;

public class ProgrammiersprachenVerwaltung {
//	private static final Logger log = Logger.getLogger(Programmiersprache.class.getName());
	

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static List<Programmiersprache> programmiersprachen;
	private static final Programmiersprache standardProgrammiersprache = JavaProvider.provider();
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static List<Programmiersprache> getProgrammiersprachen() {
		if (programmiersprachen == null) {
			programmiersprachen = ServiceLoader.load(Programmiersprache.class).stream()
					.map(p -> p.get()).toList();
		}
		return programmiersprachen;
	}
	
	public static Programmiersprache getStandardProgrammiersprache() {
		return standardProgrammiersprache;
	}

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	private ProgrammiersprachenVerwaltung() {
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
	
}