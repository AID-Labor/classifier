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

import io.github.aid_labor.classifier.basis.ProgrammDetails;


final class Windows extends OS {
	
	private static Logger log = Logger.getLogger(Windows.class.getName());
	
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
	Windows() {
		super();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote Als Konfigurationsordner wird {@code %LOCALAPPDATA%\{programm.name()}}
	 *           eingestellt
	 */
	@Override
	public String getKonfigurationsOrdner(ProgrammDetails programm) {
		return this.pfadAus(new StringBuilder(System.getenv("LocalAppData")), programm.name())
			.toString();
	}
	
	
	private boolean istDark = false;
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote Nutzt zum ermitteln den nativen Befehl {@code REG}, um den Registry-Schluessel
	 *           HKEY_CURRENT_USER\SOFTWARE\Microsoft\Windows\CurrentVersion\Themes\Personalize\AppsUseLightTheme
	 *           auszulesen.
	 */
	@Override
	public boolean systemNutztDarkTheme() {
		String[] befehl = { "REG", "QUERY",
			"HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
			"/v", "AppsUseLightTheme" };
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
				final String inhaltKopie = inhalt;
				log.finest(() -> "nativer befehl: _> " + String.join(" ", befehl) + "\n"
					+ inhaltKopie);
				if (prozess.exitValue() == 0) {
					inhalt = inhalt.strip();
					if (inhalt.endsWith("0x0")) {
						this.istDark = true;
					}
				} else {
					log.fine(
						() -> "Schluessel HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\"
							+ "CurrentVersion\\Themes\\Personalize\\AppsUseLightTheme wurde "
							+ "nicht gefunden");
				}
			}, () -> {
				log.warning(() -> """
					Darkmode konnte nicht nativ ermittelt werden!
					Systembefehl: %s
					Ausgabe: null <Optional ist leer>
					""".formatted(String.join(" ", befehl)));
			});
		} catch (Exception e) {
			final String exeptionAusgabe = ausgabeExceptionStr;
			log.log(Level.WARNING, e, () -> """
				Darkmode konnte nicht nativ ermittelt werden!
				Systembefehl: %s
				Ausgabe: %s
				""".formatted(String.join(" ", befehl), exeptionAusgabe));
		}
		
		return this.istDark;
	}
	
	@Override
	public boolean istWindows() {
		return true;
	}
	
}
