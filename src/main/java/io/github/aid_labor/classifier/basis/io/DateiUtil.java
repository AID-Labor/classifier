/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.system.OS;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 * Hilfsklasse fuer Dateioperationen
 * 
 * @author Tim Muehle
 *
 */
public final class DateiUtil {
	private static final Logger log = Logger.getLogger(DateiUtil.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	/**
	 * Prüft bei Linux-Systemen, ob die Dateierweiterung korrekt ist und ergänzt bei Notwendigkeit die erste
	 * Dateierweiterung aus der übergebenen Liste. Dies ist nötig, da der FileChooser auf Linux die Dateierweiterung
	 * nicht korrekt setzt.
	 * 
	 * @param speicherOrt          Zu prüfende Datei
	 * @param erweierterungsFilter mögliche Dateierweiterungen
	 * @return korekkter Speicherort mit passender Dateierweiterung
	 */
	public static File pruefeUndKorrigiereDateierweiterung(File speicherOrt,
			List<ExtensionFilter> erweierterungsFilter) {
		if (OS.getDefault().istLinux()) { // Workaround fuer Dateierweiterung auf Linux
			var erweiterungen = erweierterungsFilter.stream().flatMap(e -> e.getExtensions().stream()).toList();
			if (!erweiterungen.isEmpty()) {
				boolean hatErweiterung = false;
				for (var erweiterung : erweiterungen) {
					log.finer(() -> "Pruefe Erweiterung " + erweiterung);
					if (speicherOrt.getName().matches(erweiterung.replace(".", "\\.").replace("*", ".+"))) {
						log.finer(() -> "Erweiterung vorhanden: " + erweiterung);
						hatErweiterung = true;
						break;
					}
				}
				
				if (!hatErweiterung) {
					speicherOrt = new File(speicherOrt.getAbsolutePath() + erweiterungen.get(0).replace("*", ""));
					String datei = speicherOrt.getAbsolutePath();
					log.finer(() -> "Keine Erweiterung vorhanden. Neue Datei: " + datei);
				}
			}
		}
		
		return speicherOrt;
	}
	
	/**
	 * Extrahiert einen Eintrag aus einer Jmod-Datei in ein angegebenes Verzeichnis
	 * 
	 * @param zielordner   Zielverzeichnis, in das extrahiert wird
	 * @param ordner       Wurzelverzeichnis im Jmod-Dateisystem
	 * @param weitererPfad Weitere Unterordner im Jmod-Dateisystem
	 * @throws IOException Wenn beim entpacken ein Fehler auftritt
	 */
	public static void extrahiereAusJmod(Path zielordner, String ordner, String... weitererPfad) throws IOException {
		log.fine(() -> "extrahiere %s aus jmod nach %s".formatted(ordner + "/" + String.join("/", weitererPfad),
				zielordner));
		FileSystem dateisystem = FileSystems.getFileSystem(URI.create("jrt:/"));
		
		Path von = Path.of(ordner, weitererPfad);
		log.finest(() -> "Dateisystem durchlaufen");
		Files.walkFileTree(dateisystem.getPath(ordner, weitererPfad), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path verzeichnis, BasicFileAttributes attrs) throws IOException {
				log.finest(() -> "bearbeite " + verzeichnis);
				Path ziel = zielordner.resolve(von.relativize(Path.of(verzeichnis.toString())));
				if (Files.notExists(ziel)) {
					log.finer(() -> "Erstelle Verzeichnis: " + ziel);
					Files.createDirectories(ziel);
				}
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path datei, BasicFileAttributes attrs) throws IOException {
				log.finest(() -> "bearbeite " + datei);
				Path ziel = zielordner.resolve(von.relativize(Path.of(datei.toString())));
				if (Files.notExists(ziel)) {
					log.finer(() -> "kopiere Datei " + datei.getFileName() + " nach " + ziel);
					Files.copy(datei, ziel);
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	/**
	 * Extrahiert einen Eintrag aus einer Jar-Datei in ein angegebenes Verzeichnis
	 * 
	 * @param jarDatei       Pfad zur Jar-Datei, aus der extrahiert werden soll
	 * @param jarEintragName Name des Eintrages, der extrahiert werden soll
	 * @param zielordner     Zielverzeichnis, in das extrahiert wird
	 * @throws IOException Wenn beim Extrahieren ein Fehler auftritt oder das Verzeichnis zur
	 *                     Jar-Datei ungueltig ist
	 */
	public static void extrahiereAusJar(URI jarDatei, String jarEintragName, Path zielordner) throws IOException {
		log.fine(() -> "Extrahiere %s aus %s  nach %s".formatted(jarEintragName, jarDatei, zielordner));
		try (JarFile jarfile = new JarFile(new File(jarDatei))) {
			Path wurzelEintrag = Path.of(jarEintragName);
			jarfile.stream().filter(eintrag -> eintrag.getName().startsWith(jarEintragName)).forEach(eintrag -> {
				Path ziel = zielordner.resolve(wurzelEintrag.relativize(Path.of(eintrag.getName())));
				if (Files.notExists(ziel)) {
					log.finest(() -> "Kopiere " + eintrag.getName() + " nach " + ziel);
					if (eintrag.isDirectory()) {
						try {
							Files.createDirectories(ziel);
						} catch (IOException e) {
							log.log(Level.WARNING, e,
									() -> "Kopieren von " + eintrag.getName() + " nach " + ziel + " fehlgeschlagen");
						}
					} else {
						try {
							Files.copy(jarfile.getInputStream(eintrag), ziel, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							log.log(Level.WARNING, e,
									() -> "Kopieren von " + eintrag.getName() + " nach " + ziel + " fehlgeschlagen");
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
			public FileVisitResult preVisitDirectory(Path verzeichnis, BasicFileAttributes attrs) throws IOException {
				if (Files.notExists(nach.resolve(von.relativize(verzeichnis)))) {
					log.finer(() -> "Erstelle Verzeichnis: " + nach.resolve(von.relativize(verzeichnis)));
					Files.createDirectories(nach.resolve(von.relativize(verzeichnis)));
				}
				return FileVisitResult.CONTINUE;
			}
			
			@Override
			public FileVisitResult visitFile(Path datei, BasicFileAttributes attrs) throws IOException {
				if (Files.notExists(nach.resolve(von.relativize(datei)))) {
					log.finer(() -> "kopiere Datei " + datei.getFileName() + " nach "
							+ nach.resolve(von.relativize(datei)));
					Files.copy(datei, nach.resolve(von.relativize(datei)));
				}
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	/**
	 * Loescht Dateien und Ordner. Ordner werden samt Inhalt geloescht.
	 * 
	 * @param pfad Zu loeschender Pfad
	 */
	public static void loescheDateiOderOrdner(Path pfad) {
		log.info(() -> "Loesche " + pfad);
		if (Files.isDirectory(pfad)) {
			try (var inhalt = Files.newDirectoryStream(pfad)) {
				for (var kind : inhalt) {
					loescheDateiOderOrdner(kind);
				}
			} catch (IOException e) {
				log.log(Level.WARNING, e, () -> "Fehler beim Loeschen von " + pfad);
			}
		}
		
		try {
			Files.delete(pfad);
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Fehler beim Loeschen von " + pfad);
		}
	}
	
	/**
	 * Loescht alte Logging-Dateien aus dem systemspezifischen Konfigurationsordner des
	 * Programmes
	 * 
	 * @param aufbewahrungsfrist Zeitraum, fuer den Logging-Dateien behalten werden sollen
	 * @param programm           Programm, fuer das die Logging-Dateien geloescht werden
	 */
	public static void loescheVeralteteLogs(Duration aufbewahrungsfrist, ProgrammDetails programm) {
		Objects.requireNonNull(aufbewahrungsfrist, "Aufbewahrungsfrist darf nicht null sein");
		Path logOrdner = OS.getDefault().getKonfigurationsOrdnerPath(programm).resolve("log");
		try (var logs = Files.walk(logOrdner, 1)) {
			logs.forEach(logdatei -> {
				try {
					var bearbeitet = Files.getLastModifiedTime(logdatei).toInstant();
					var alter = Duration.between(bearbeitet, Instant.now());
					if (alter.compareTo(aufbewahrungsfrist) > 0 && logdatei.toString().endsWith(".log")
							&& logdatei.toString().contains("classifier")) {
						boolean geloescht = Files.deleteIfExists(logdatei);
						if (geloescht) {
							log.fine(() -> logdatei.toAbsolutePath().toString() + " geloescht");
						} else {
							log.fine(() -> logdatei.toAbsolutePath().toString() + " konnte nicht geloscht werden");
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Fehler beim loeschen alter Logdateien in " + logOrdner);
		}
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
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
	
}