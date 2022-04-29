/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;


non-sealed class Linux extends Unix {
	
	private static Logger log = Logger.getLogger(Linux.class.getName());
	
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
	Linux() {
		super();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public boolean istLinux() {
		return true;
	}
	
	private boolean istDark = false;
	
	@Override
	public boolean systemNutztDarkTheme() {
		String[] befehl = {"gsettings", "get", "org.gnome.desktop.interface", "gtk-theme"};
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
				if (ausgabeKlein.contains("dark") 
					|| ausgabeKlein.contains("black")
					|| ausgabeKlein.contains("grey")) {
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