/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Hilfsklasse fuer Dateioperationen
 * 
 * @author Tim Muehle
 *
 */
public class DateiUtil {
	private static final Logger log = Logger.getLogger(DateiUtil.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Extrahiert einen Eintrag aus einer Jar-Datei in ein angegebenes Verzeichnis
	 * 
	 * @param jarDatei       Pfad zur Jar-Datei, aus der extrahiert werden soll
	 * @param jarEintragName Name des Eintrages, der extrahiert werden soll
	 * @param zielordner           Zielverzeichnis, in das extrahiert wird
	 * @throws IOException Wenn beim Extrahieren ein Fehler auftritt oder das Verzeichnis zur
	 *                     Jar-Datei ungueltig ist
	 */
	public static void extrahiereAusJar(URI jarDatei, String jarEintragName, Path zielordner)
			throws IOException {
		log.fine(() -> "Extrahiere %s aus %s  nach %s".formatted(jarEintragName, jarDatei,
				zielordner));
		try (JarFile jarfile = new JarFile(new File(jarDatei))) {
			Path wurzelEintrag = Path.of(jarEintragName);
			jarfile.stream()
					.filter(eintrag -> eintrag.getName().startsWith(jarEintragName))
					.forEach(eintrag -> {
						Path ziel = zielordner
								.resolve(wurzelEintrag.relativize(Path.of(eintrag.getName())));
						if (Files.notExists(ziel)) {
							log.finest(() -> "Kopiere " + eintrag.getName() + " nach " + ziel);
							if (eintrag.isDirectory()) {
								try {
									Files.createDirectories(ziel);
								} catch (IOException e) {
									log.log(Level.WARNING, e, () -> "Kopieren von "
											+ eintrag.getName() + " nach " + ziel 
											+ " fehlgeschlagen");
								}
							} else {
								try {
									Files.copy(jarfile.getInputStream(eintrag), ziel,
											StandardCopyOption.REPLACE_EXISTING);
								} catch (IOException e) {
									log.log(Level.WARNING, e, () -> "Kopieren von "
											+ eintrag.getName() + " nach " + ziel 
											+ " fehlgeschlagen");
								}
							}
						} else {
							log.finest(() -> "Ueberspringe " + eintrag.getName());
						}
					});
		}
	}
	
	/**
	 * Kopiert einen Dateibaum vollstaendig mit allen Ordnern und Dateien
	 * 
	 * @param von  Wurzelordner des zu kopierenden Dateibaumes
	 * @param nach Ziel, in das kopiert wird
	 * @throws IOException Wenn ein Fehler beim Kopiervorgang auftritt. Eventuell wurde nur
	 *                     ein Teil des Dateibaumes oder nichts kopiert.
	 */
	public static void kopiereDateibaum(final Path von, final Path nach) throws IOException {
		log.finer(() -> "Kopiere %s nach %s".formatted(von, nach));
		Files.walkFileTree(von, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path verzeichnis,
					BasicFileAttributes attrs)
					throws IOException {
				if (Files.notExists(nach.resolve(von.relativize(verzeichnis)))) {
					log.finer(() -> "Erstelle Verzeichnis: "
							+ nach.resolve(von.relativize(verzeichnis)));
					Files.createDirectories(nach.resolve(von.relativize(verzeichnis)));
				}
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path datei, BasicFileAttributes attrs)
					throws IOException {
				if (Files.notExists(nach.resolve(von.relativize(datei)))) {
					log.finer(() -> "kopiere Datei " + datei.getFileName() + " nach "
							+ nach.resolve(von.relativize(datei)));
					Files.copy(datei, nach.resolve(von.relativize(datei)));
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private DateiUtil() {
		// Hilfsklasse, nicht instanziierbar
	}
	
}