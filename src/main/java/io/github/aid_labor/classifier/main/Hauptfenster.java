/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.main;

import static org.apache.commons.cli.Option.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import javafx.application.Application;
import javafx.stage.Stage;


public class Hauptfenster extends Application {
	
	private static final Logger log;
	private static final Properties logProperties;
	
	static {
		// Handlers
		logProperties = new Properties(10);
		logProperties.setProperty("handlers",
			"java.util.logging.ConsoleHandler, java.util.logging.FileHandler");
		
		// FileHandler
		String startzeit = "%1$tF%1$tT".formatted(LocalDateTime.now());
		logProperties.setProperty("java.util.logging.FileHandler.pattern",
			"%h/classifier/" + startzeit + "_classifier_.log");
		logProperties.setProperty("java.util.logging.FileHandler.limit", "50000");
		logProperties.setProperty("java.util.logging.FileHandler.maxLocks", "100");
		
		// Formatierung
		logProperties.setProperty("java.util.logging.FileHandler.formatter",
			"java.util.logging.SimpleFormatter");
		logProperties.setProperty("java.util.logging.ConsoleHandler.formatter",
			"java.util.logging.SimpleFormatter");
		logProperties.setProperty("java.util.logging.SimpleFormatter.format",
			"[%1$tF %1$tT %1$tZ] %2$s%n  %4$s: %5$s%6$s%n");
		
		// Log-Level
		logProperties.setProperty(".level", "ALL");
		logProperties.setProperty("java.util.logging.FileHandler.level", "ALL");
		logProperties.setProperty("java.util.logging.ConsoleHandler.level", "ALL");
		
		try (var streamOut = new StringWriter()) {
				logProperties.store(streamOut, startzeit);
				streamOut.flush();
				String konfiguration = streamOut.toString();
				try (var streamIn = new ByteArrayInputStream(konfiguration.getBytes())) {
					LogManager.getLogManager().readConfiguration(streamIn);
				}
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		// Logger erzeugen
		log = Logger.getLogger(Hauptfenster.class.getName());
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	static final ProgrammDetails programm = new ProgrammDetails("0.0.1", "Classifier",
		null, null);
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	static int a = 0;
	
	public static void main(String[] args) {
		log.log(Level.ALL, "ALL");
		log.log(Level.SEVERE, "SEVERE");
		log.log(Level.WARNING, "WARNING");
		log.log(Level.INFO, "INFO");
		log.log(Level.CONFIG, "CONFIG");
		log.log(Level.FINE, "FINE");
		log.log(Level.FINER, "FINER");
		log.log(Level.FINEST, "FINEST");
		CommandLine aufruf = werteArgumenteAus(args);
		
		log.info(() -> "%s gestartet  -  OS: %s_%s_%s  -  Java: %s %s".formatted(
			programm.getVersionName(),
			System.getProperty("os.name"),
			System.getProperty("os.version"),
			System.getProperty("os.arch"),
			System.getProperty("java.vm.name"),
			System.getProperty("java.vm.version")));
		
		launch(args);
	}
	
	private static CommandLine werteArgumenteAus(final String[] args) {
		Option debug = builder("d").longOpt("debug").desc("Schlatet Debug-Meldungen ein")
			.build();
		Option info = builder("i").longOpt("info").desc("Schlatet Info-Meldungen ein").build();
		String[] loglevels = { "ALL", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER",
			"FINEST", "OFF" };
		Option loglevel = builder().longOpt("log-level").argName("level").hasArg().desc("""
			Bestimmt, welche Logging-Informationen ausgegeben werden. Moegliche Werte sind:
			%s
			Der Standardwert ist WARNING""".formatted(String.join(", ", loglevels)))
			.type(Level.class).build();
		Option hilfe = builder("h").longOpt("help").desc("Zeigt diese Hilfe an").build();
		Option version = builder("v").longOpt("version").desc("Zeigt die Version an").build();
		
		OptionGroup loggingOptionen = new OptionGroup();
		loggingOptionen.addOption(debug).addOption(info);
		
		Options optionen = new Options();
		optionen.addOptionGroup(loggingOptionen)
			.addOption(loglevel)
			.addOption(hilfe)
			.addOption(version);
		
		CommandLine aufruf = null;
		try {
			aufruf = new DefaultParser().parse(optionen, args);
		} catch (ParseException e) {
			zeigeFehlerUndBeende(optionen, "Fehlerhafter Aufruf: " + e.getLocalizedMessage());
		}
		
		System.out.println("Options: " + Arrays.toString(aufruf.getOptions()));
		System.out.println("Args: " + Arrays.toString(aufruf.getArgs()));
		
		if (aufruf.hasOption(hilfe)) {
			log.info("--help");
			zeigeHilfeUndBeende(optionen);
		}
		
		if (aufruf.hasOption(version)) {
			log.info("--version");
			System.out.println(programm.getVersionName());
			System.exit(0);
		}
		
		if (aufruf.hasOption(debug)) {
			log.info("--debug");
			System.setProperty("java.util.logging.ConsoleHandler.level", "ALL");
		} else if (aufruf.hasOption(info)) {
			log.info("--info");
			System.setProperty("java.util.logging.ConsoleHandler.level", "INFO");
		}
		
		if (aufruf.hasOption(loglevel)) {
			log.info("--log-level");
			Set<String> moeglicheLevel = Set.of(loglevels);
			try {
				if (moeglicheLevel.contains(aufruf.getParsedOptionValue(loglevel))) {
					System.setProperty("java.util.logging.ConsoleHandler.level",
						loglevel.getValue());
				} else {
					zeigeFehlerUndBeende(optionen, "Fehler beim Auslesen der Option --" +
						loglevel.getLongOpt() + " " + aufruf.getParsedOptionValue(loglevel));
				}
			} catch (ParseException e) {
				zeigeFehlerUndBeende(optionen, loglevel);
			}
		}
		
		return aufruf;
	}
	
	private static void zeigeFehlerUndBeende(final Options optionen, final Option fehler) {
		zeigeFehlerUndBeende(optionen,
			"Fehler beim Auslesen der Option --" + fehler.getLongOpt());
	}
	
	private static void zeigeFehlerUndBeende(final Options optionen, final String meldung) {
		System.err.println(meldung + "\n");
		zeigeHilfeUndBeende(optionen);
	}
	
	private static void zeigeHilfeUndBeende(final Options optionen) {
		HelpFormatter hilfeFormat = new HelpFormatter();
		String syntax = "classifier";// [-d | -i] [Optionen] [<Datei_1> [<Datei_2>
										// [<Datei_3> ...]]]";
		String beschreibung = """
			UML-Klassendiagramme modellieren
			
			Optionen:
			""";
		String anmerkung = """
			
			 <Datei_?>                Optional eine oder mehrere Projektdateien oeffnen
			
			Weitere Informationen: https://github.com/AID-Labor/classifier \n
			""";
		hilfeFormat.printHelp(80, syntax, beschreibung, optionen, anmerkung, true);
		
		System.exit(0);
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
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
