/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.system.OS;

class LoggingEinstellung {
	private static final Logger log = Logger.getLogger(LoggingEinstellung.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static  final Properties logProperties = new Properties(10);
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	
	static void initialisiereLogging(ProgrammDetails programm) {
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
		logProperties.setProperty("javafx.level", "INFO");
		
		// Einstellungen Anwenden
		wendeLoggingEinstellungenAn();
		
		// Log-Datei einstellen
		OS os = OS.getDefault();
		StringBuilder logdatei = os.pfadAus(os.getKonfigurationsOrdner(programm), "log",
				"%1$tF_%1$tH-%1$tM-%1$tS".formatted(LocalDateTime.now()) + "_classifier_%u%g.log");
		try {
			Files.createDirectories(Path.of(logdatei.toString()).getParent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		logProperties.setProperty("java.util.logging.FileHandler.pattern",
				logdatei.toString());
		
		// Einstellungen Anwenden
		wendeLoggingEinstellungenAn();
		
		log.config(() -> "Logdatei: " + logdatei);
	}
	
	static void setzeEinstellung(String schluessel, String wert, boolean ueberschreiben) {
		if (ueberschreiben || !logProperties.containsKey(schluessel)) {
			logProperties.setProperty(schluessel, wert);
			wendeLoggingEinstellungenAn();
		}
	}
	
	private static void wendeLoggingEinstellungenAn() {
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
	
	static void setzeLogLevel(Level level) {
		setzeLogLevel(level, level);
	}
	
	static void setzeLogLevel(Level consoleLevel, Level fileLevel) {
		Level hoechstes = consoleLevel.intValue() > fileLevel.intValue() ? consoleLevel
				: fileLevel;
		logProperties.setProperty(".level", hoechstes.toString());
		logProperties.setProperty("java.util.logging.FileHandler.level",
				fileLevel.toString());
		logProperties.setProperty("java.util.logging.ConsoleHandler.level",
				consoleLevel.toString());
		wendeLoggingEinstellungenAn();
	}
}