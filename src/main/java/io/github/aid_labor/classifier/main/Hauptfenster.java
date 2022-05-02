/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.main;

import static org.apache.commons.cli.Option.builder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
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

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.OS;
import io.github.aid_labor.classifier.basis.ProgrammDetails;
import javafx.application.Application;
import javafx.stage.Stage;


public class Hauptfenster extends Application {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final Logger log = Logger.getLogger(Hauptfenster.class.getName());
	private static final Properties logProperties = new Properties(10);
	
	private static final ProgrammDetails programm = new ProgrammDetails("0.0.1", "Classifier",
			null, null, "https://github.com/AID-Labor/classifier");
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static void main(String[] args) {
		initialisiereLogging();
		setzeLogLevel(Einstellungen.getBenutzerdefiniert().logLevelKonsole.get(),
				Einstellungen.getBenutzerdefiniert().logLevelDatei.get());
		CommandLine aufruf = werteArgumenteAus(args);
		loescheVeralteteLogs(Duration.ofDays(14));
		
		log.log(Level.ALL, () -> "%s gestartet  -  OS: %s_%s_%s  -  Java: %s %s".formatted(
				programm.getVersionName(),
				System.getProperty("os.name"),
				System.getProperty("os.version"),
				System.getProperty("os.arch"),
				System.getProperty("java.vm.name"),
				System.getProperty("java.vm.version")));
		log.finest(() -> "Args: " + Arrays.toString(aufruf.getArgs()));
		
		launch(args);
	}
	
	private static void initialisiereLogging() {
		// Handlers
		logProperties.setProperty("handlers",
				"java.util.logging.ConsoleHandler, java.util.logging.FileHandler");
		
		// FileHandler
		logProperties.setProperty("java.util.logging.FileHandler.limit", "0");
		logProperties.setProperty("java.util.logging.FileHandler.maxLocks", "100");
		
		// Formatierung
		logProperties.setProperty("java.util.logging.FileHandler.formatter",
				"java.util.logging.SimpleFormatter");
		logProperties.setProperty("java.util.logging.ConsoleHandler.formatter",
				"java.util.logging.SimpleFormatter");
		logProperties.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tF %1$tT %1$tZ] %2$s%n  %4$s: %5$s%6$s%n");
		
		// Log-Level
		logProperties.setProperty(".level", "INFO");
		logProperties.setProperty("java.util.logging.FileHandler.level", "INFO");
		logProperties.setProperty("java.util.logging.ConsoleHandler.level", "WARNING");
		
		// Einstellungen Anwenden
		setzeLoggingEinstellungen(logProperties);
		
		// Log-Datei einstellen
		OS os = OS.getDefault();
		StringBuilder logdatei = os.pfadAus(os.getKonfigurationsOrdner(programm), "log",
				"%1$tF_%1$tT".formatted(LocalDateTime.now()) + "_classifier_%u%g.log");
		try {
			Files.createDirectories(Path.of(logdatei.toString()).getParent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logProperties.setProperty("java.util.logging.FileHandler.pattern",
				logdatei.toString());
		
		// Einstellungen Anwenden
		setzeLoggingEinstellungen(logProperties);
		
		log.config(() -> "Logdatei: " + logdatei);
	}
	
	private static void loescheVeralteteLogs(Duration aufbewahrungsfrist) {
		Objects.requireNonNull(aufbewahrungsfrist, "Aufbewahrungsfrist darf nicht null sein");
		Path logOrdner = OS.getDefault().getKonfigurationsOrdnerPath(programm).resolve("log");
		try (var logs = Files.walk(logOrdner, 1)) {
			logs.forEach(logdatei -> {
				try {
					var bearbeitet = Files.getLastModifiedTime(logdatei).toInstant();
					var alter = Duration.between(bearbeitet, Instant.now());
					if (alter.compareTo(aufbewahrungsfrist) > 0
							&& logdatei.toString().endsWith(".log")
							&& logdatei.toString().contains("classifier")) {
						boolean geloescht = Files.deleteIfExists(logdatei);
						if (geloescht) {
							log.fine(
									() -> logdatei.toAbsolutePath().toString() + " geloescht");
						} else {
							log.fine(() -> logdatei.toAbsolutePath().toString()
									+ " konnte nicht geloscht werden");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Fehler beim loeschen alter Logdateien in " + logOrdner);
		}
	}
	
	private static void setzeLoggingEinstellungen(Properties logProperties) {
		try (var streamOut = new StringWriter()) {
			logProperties.store(streamOut, "Logging Einstellungen fuer Classifier");
			streamOut.flush();
			String konfiguration = streamOut.toString();
			try (var streamIn = new ByteArrayInputStream(konfiguration.getBytes())) {
				LogManager.getLogManager().readConfiguration(streamIn);
			}
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
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
				.type(String.class).build();
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
		
		if (aufruf.hasOption(debug)) {
			log.config("--debug");
			setzeLogLevel(Level.FINEST);
		} else if (aufruf.hasOption(info)) {
			log.config("--info");
			setzeLogLevel(Level.INFO);
		}
		
		if (aufruf.hasOption(loglevel)) {
			log.config("--log-level");
			Set<String> moeglicheLevel = Set.of(loglevels);
			try {
				String level = aufruf.getParsedOptionValue(loglevel).toString();
				if (moeglicheLevel.contains(level)) {
					setzeLogLevel(Level.parse(level));
				} else {
					zeigeFehlerUndBeende(optionen, "Fehler beim Auslesen der Option --" +
							loglevel.getLongOpt() + " " + level);
				}
			} catch (ParseException e) {
				zeigeFehlerUndBeende(optionen, loglevel);
			}
		}
		
		if (aufruf.hasOption(hilfe)) {
			log.config("--help");
			zeigeHilfeUndBeende(optionen);
		}
		
		if (aufruf.hasOption(version)) {
			log.config("--version");
			System.out.println(programm.getVersionName());
			System.exit(0);
		}
		
		return aufruf;
	}
	
	private static void setzeLogLevel(Level level) {
		setzeLogLevel(level, level);
	}
	
	private static void setzeLogLevel(Level consoleLevel, Level fileLevel) {
		Level hoechstes = consoleLevel.intValue() > fileLevel.intValue() ? consoleLevel
				: fileLevel;
		logProperties.setProperty(".level", hoechstes.toString());
		logProperties.setProperty("java.util.logging.FileHandler.level", fileLevel.toString());
		logProperties.setProperty("java.util.logging.ConsoleHandler.level",
				consoleLevel.toString());
		setzeLoggingEinstellungen(logProperties);
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
				
				Weitere Informationen: %s \n
				""".formatted(programm.homepage());
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
		
		
		primaryStage.setTitle(programm.name());
		primaryStage.show();
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
