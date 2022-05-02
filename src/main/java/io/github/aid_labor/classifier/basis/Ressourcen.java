/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.Objects;
import java.util.logging.Logger;


public class Ressourcen {
	
	private static Logger log = Logger.getLogger(Ressourcen.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	private static Ressourcen instanz;
	private static ProgrammDetails programm;

	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	public static Ressourcen get() {
		if (programm == null) {
			var exception = new IllegalStateException("Bevor eine Instanz erzeugt werden kann "
					+ "muessen mit Ressourcen.setProgrammDetails die "
					+ "Programminformationen mitgeteilt werden!");
			log.throwing(Ressourcen.class.getName(), "get", exception);
			throw exception;
		}
		if (instanz == null) {
			instanz = new Ressourcen(programm);
		}
		
		return instanz;
	}
	
	public static void setProgrammDetails(ProgrammDetails programm) {
		Ressourcen.programm = Objects.requireNonNull(programm);
	}
	
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public final Ressource LIGHT_THEME_CSS;
	public final Ressource DARK_THEME_CSS;
	public final Ressource NUTZER_THEME_CSS;
	
	public final Ressource NUTZER_EINSTELLUNGEN;
	
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Ressourcen(ProgrammDetails programm) {
		RessourceBuilder builder = new RessourceBuilder(programm);
		this.LIGHT_THEME_CSS = builder.css("lightTheme.css");
		this.DARK_THEME_CSS = builder.css("darkTheme.css");
		this.NUTZER_THEME_CSS = builder.css("customTheme.css");
		this.NUTZER_EINSTELLUNGEN = builder.konfigurationsdatei("nutzerEinstellung.json");
	}
	
}