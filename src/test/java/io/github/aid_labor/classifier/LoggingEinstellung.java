/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;

public class LoggingEinstellung {

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static  final Properties logProperties = new Properties(10);
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	
	public static void initialisiereTestLogging() {
		// Handlers
		logProperties.setProperty("handlers",
				"java.util.logging.ConsoleHandler, java.util.logging.FileHandler");
		
		// Formatierung
		logProperties.setProperty("java.util.logging.FileHandler.formatter",
				"java.util.logging.SimpleFormatter");
		logProperties.setProperty("java.util.logging.ConsoleHandler.formatter",
				"java.util.logging.SimpleFormatter");
		logProperties.setProperty("java.util.logging.SimpleFormatter.format",
				"[%1$tF %1$tT %1$tZ] %2$s%n  %4$s: %5$s%6$s%n");
		
		// Log-Level
		logProperties.setProperty(".level", "ALL");
		logProperties.setProperty("java.util.logging.ConsoleHandler.level", "ALL");
		logProperties.setProperty("javafx.level", "INFO");
		
		// Einstellungen Anwenden
		wendeLoggingEinstellungenAn();
	}
	
	public static void setzeEinstellung(String schluessel, String wert, boolean ueberschreiben) {
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
	
	public static void setzeLogLevel(Level consoleLevel) {
		logProperties.setProperty(".level", consoleLevel.toString());
		logProperties.setProperty("java.util.logging.ConsoleHandler.level",
				consoleLevel.toString());
		wendeLoggingEinstellungenAn();
	}
}