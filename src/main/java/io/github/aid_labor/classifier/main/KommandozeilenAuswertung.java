/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.main;

import static org.apache.commons.cli.Option.builder;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.github.aid_labor.classifier.basis.ProgrammDetails;

class KommandozeilenAuswertung {
	private static final Logger log = Logger
			.getLogger(KommandozeilenAuswertung.class.getName());

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	public final Option debug;
	public final Option debugCSS;
	public final Option info;
	public final Option loglevel;
	public final Option loglevelJavaFX;
	public final Option hilfe;
	public final Option version;
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	private final ProgrammDetails programm;
	private final String[] args;
	private final String[] loglevels = { "ALL", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", 
		"FINER", "FINEST", "OFF" };
	private final Options optionen;
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	KommandozeilenAuswertung(String[] args, ProgrammDetails programm) {
		this.args = args;
		this.programm = programm;
		debug = builder("d").longOpt("debug").desc("Schlatet Debug-Meldungen ein")
				.build();
		debugCSS = builder().longOpt("debug-css").desc("Schlatet CSS-Auto-Update ein")
				.build();
		info = builder("i").longOpt("info").desc("Schlatet Info-Meldungen ein").build();
		
		loglevel = builder().longOpt("log").argName("level").hasArg().desc("""
				Bestimmt, welche Logging-Informationen ausgegeben werden. Moegliche Werte sind:
				%s
				Der Standardwert ist WARNING""".formatted(String.join(", ", loglevels)))
				.type(String.class).build();
		loglevelJavaFX = builder().longOpt("log-javafx").argName("level").hasArg()
				.desc("""
						Bestimmt, welche Logging-Informationen von javaFX ausgegeben werden. \
						Moegliche Werte sind:
						%s
						Der Standardwert ist WARNING"""
						.formatted(String.join(", ", loglevels)))
				.type(String.class).build();
		
		hilfe = builder("h").longOpt("help").desc("Zeigt diese Hilfe an").build();
		version = builder("v").longOpt("version").desc("Zeigt die Version an").build();
		
		
		optionen = new Options();
		erzeugeOptionen();
	}


//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	CommandLine werteArgumenteAus() {
		CommandLine aufruf = null;
		try {
			aufruf = new DefaultParser().parse(optionen, args);
		} catch (ParseException e) {
			zeigeFehlerUndBeende( "Fehlerhafter Aufruf: " + e.getLocalizedMessage());
		}
		
		if (aufruf.hasOption(debug)) {
			log.config("--debug");
			LoggingEinstellung.setzeLogLevel(Level.FINEST);
		} else if (aufruf.hasOption(info)) {
			log.config("--info");
			LoggingEinstellung.setzeLogLevel(Level.INFO);
		}
		
		if (aufruf.hasOption(loglevel)) {
			log.config("--log");
			Set<String> moeglicheLevel = Set.of(loglevels);
			try {
				String level = aufruf.getParsedOptionValue(loglevel).toString();
				if (moeglicheLevel.contains(level)) {
					LoggingEinstellung.setzeLogLevel(Level.parse(level));
				} else {
					zeigeFehlerUndBeende("Fehler beim Auslesen der Option --" +
							loglevel.getLongOpt() + " " + level);
				}
			} catch (ParseException e) {
				zeigeFehlerUndBeende(loglevel);
			}
		}
		
		if (aufruf.hasOption(loglevelJavaFX)) {
			log.config("--log-javafx");
			Set<String> moeglicheLevel = Set.of(loglevels);
			try {
				String level = aufruf.getParsedOptionValue(loglevelJavaFX).toString();
				if (moeglicheLevel.contains(level)) {
					LoggingEinstellung.setzeEinstellung("javafx.level", level, true);
				} else {
					zeigeFehlerUndBeende("Fehler beim Auslesen der Option --" +
							loglevelJavaFX.getLongOpt() + " " + level);
				}
			} catch (ParseException e) {
				zeigeFehlerUndBeende(loglevelJavaFX);
			}
		}
		
		if (aufruf.hasOption(hilfe)) {
			log.config("--help");
			zeigeHilfeUndBeende();
		}
		
		if (aufruf.hasOption(version)) {
			log.config("--version");
			System.out.println(programm.getVersionName());
			System.exit(0);
		}
		
		return aufruf;
	}
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void erzeugeOptionen() {
		OptionGroup loggingOptionen = new OptionGroup();
		loggingOptionen.addOption(debug).addOption(info);
		
		optionen.addOptionGroup(loggingOptionen)
				.addOption(debugCSS)
				.addOption(loglevel)
				.addOption(loglevelJavaFX)
				.addOption(hilfe)
				.addOption(version);
	}
	
	private void zeigeFehlerUndBeende(final Option fehler) {
		zeigeFehlerUndBeende("Fehler beim Auslesen der Option --" + fehler.getLongOpt());
	}
	
	private void zeigeFehlerUndBeende(final String meldung) {
		System.err.println(meldung + "\n");
		zeigeHilfeUndBeende();
	}
	
	private void zeigeHilfeUndBeende() {
		HelpFormatter hilfeFormat = new HelpFormatter();
		String syntax = "classifier [-d | -i] [Optionen] [<Datei_1> [<Datei_2> "
				+ "[<Datei_3> ...]]]";
		String beschreibung = """
				UML-Klassendiagramme modellieren
				
				Optionen:
				""";
		String anmerkung = """
				
				 <Datei_?>                 Optional eine oder mehrere Projektdateien oeffnen
				
				Weitere Informationen: %s \n
				""".formatted(programm.homepage());
		hilfeFormat.printHelp(80, syntax, beschreibung, optionen, anmerkung);
		
		System.exit(0);
	}
	
	
}