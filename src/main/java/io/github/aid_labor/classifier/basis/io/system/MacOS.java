/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io.system;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.io.ProgrammDetails;


non-sealed class MacOS extends Unix {
	
	private static Logger log = Logger.getLogger(MacOS.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// Sichtbarkeit des Konstruktors auf package beschraenken
	MacOS() {
		super();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public boolean istMacOS() {
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote Als Konfigurationsordner wird {@code $HOME/Library/{programm.name()}}
	 *           eingestellt
	 */
	@Override
	public String getKonfigurationsOrdner(ProgrammDetails programm) {
		return this.pfadAus(new StringBuilder(this.nutzerOrdner), "Library", programm.name())
			.toString();
	}
	
	
	private boolean istDark = false;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote Nutzt zum ermitteln den nativen Befehl {@code defaults}, um die 
	 *           Systemeinstellungen auszulesen.
	 */
	@Override
	public boolean systemNutztDarkTheme() {
		String[] befehl = {"defaults", "read", "-g", "AppleInterfaceStyle"};
		String ausgabeExceptionStr = "<noch nicht gesetzt>";
		this.istDark = false;
		Process prozess;
		try {
			prozess = new ProcessBuilder(befehl)
				.redirectErrorStream(true)
				.start();
			prozess.waitFor(10, TimeUnit.SECONDS);
			var reader = prozess.inputReader();
			Optional<String> zeilen = reader.lines()
				.reduce((zeile1, zeile2) -> zeile1 + "\n" + zeile2);
			ausgabeExceptionStr = zeilen.orElse("null <Optional leer>");
			zeilen.ifPresentOrElse(inhalt -> {
				log.finest(() -> "nativer befehl: _> " + String.join(" ", befehl) + "\n" + inhalt);
				String ausgabeKlein = inhalt.toLowerCase();
				if(ausgabeKlein.contains("dark")) {
					this.istDark = true;
				}
			}, () -> {
				log.warning(() -> """
					Darkmode konnte nicht nativ ermittelt werden!
					Systembefehl: %s
					Ausgabe: null <Optional ist leer>
					""".formatted(String.join(" ", befehl)));
			});
		} catch (Exception e) {
			final String exceptionAusgabe = ausgabeExceptionStr;
			log.log(Level.WARNING, e, () -> """
				Darkmode konnte nicht nativ ermittelt werden!
				Systembefehl: %s
				Ausgabe: %s
				""".formatted(String.join(" ", befehl), exceptionAusgabe));
		}
		
		return this.istDark;
	}
}
