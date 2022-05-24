/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.main;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;

import fr.brouillard.oss.cssfx.CSSFX;
import fr.brouillard.oss.cssfx.api.URIToPathConverter;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.AusfuehrUmgebung;
import io.github.aid_labor.classifier.basis.io.DateiUtil;
import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.gui.HauptAnsicht;
import io.github.aid_labor.classifier.gui.util.FensterUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;


public class Hauptfenster extends Application {
	
	private static final Logger log = Logger.getLogger(Hauptfenster.class.getName());
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// @formatter:off
	private static final ProgrammDetails programm = new ProgrammDetails(
		"1.0.0-SNAPSHOT_001",
		"Classifier",
		null,
		null,
		"https://github.com/AID-Labor/classifier", Hauptfenster.class,
		new ExtensionFilter[] {
			new ExtensionFilter("Classifier Projektdatei", "*.classifile")
		}
	);
	// @formatter:on
	
	private static CommandLine kommandoZeile;
	private static KommandozeilenAuswertung auswertung;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static void main(String[] args) {
		LoggingEinstellung.initialisiereLogging(programm);
		auswertung = new KommandozeilenAuswertung(args, programm);
		kommandoZeile = auswertung.werteArgumenteAus();
		if (!kommandoZeile.hasOption(auswertung.debug)
				&& !kommandoZeile.hasOption(auswertung.loglevel)) {
			// TODO Debug-Einstellung, entfernen in finaler Version!
			LoggingEinstellung.setzeLogLevel(Level.FINE, Level.FINER);
		}
		log.log(Level.SEVERE, () -> "%s gestartet  -  OS: %s_%s_%s  -  Java: %s %s".formatted(
				programm.getVersionName(),
				System.getProperty("os.name"),
				System.getProperty("os.version"),
				System.getProperty("os.arch"),
				System.getProperty("java.vm.name"),
				System.getProperty("java.vm.version")));
		log.fine(() -> "Args [main]: " + Arrays.toString(args));
		log.fine(() -> "Args [getArgs()]: " + Arrays.toString(kommandoZeile.getArgs()));
		
		Ressourcen.setProgrammDetails(programm);
		DateiUtil.loescheVeralteteLogs(Duration.ofDays(14), programm);
		
		launch(args);
		Einstellungen.speicherBenutzerdefiniert();
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
	public void start(Stage hauptFenster) {
		HauptAnsicht hauptansicht = new HauptAnsicht(programm);
		
		Scene szene = new Scene(hauptansicht.getWurzelknoten());
		FensterUtil.installiereFensterwiederherstellung(hauptFenster, 720, 1120,
				Ressourcen.get().KONFIGURATIONSORDNER.alsPath());
		
		szene.getStylesheets().add(Ressourcen.get().BASIS_CSS.externeForm());
		
		if (kommandoZeile.hasOption(auswertung.debugCSS)) {
			if (AusfuehrUmgebung.getFuerKlasse(this.getClass()).equals(AusfuehrUmgebung.IDE)) {
				CSSFX.addConverter(new URIToPathConverter() {
					@Override
					public Path convert(String uri) {
						if (uri.contains(OS.getDefault().getKonfigurationsOrdner(programm))) {
							var datei = Path.of(URI.create(uri));
							Path codePfad = Path
									.of(Ressourcen.class.getProtectionDomain().getCodeSource()
											.getLocation().getPath())
									.getParent().getParent()
									.resolve("src/main/resources/ressourcen/css")
									.resolve(datei.getFileName());
							return codePfad;
						}
						return null;
					}
				}).start();
			} else {
				log.severe(() -> "CSS-Auto-update nur bei Ausf%chrung in der IDE erlaubt"
						.formatted(ue));
			}
		}
		
		hauptFenster.setScene(szene);
		hauptFenster.setTitle(programm.name());
		
		try (var iconStream = Ressourcen.get().CLASSIFIER_ICON_L.oeffneStream()) {
			hauptFenster.getIcons().add(new Image(iconStream));
		} catch (IOException | IllegalStateException e) {
			log.log(Level.WARNING, e, () -> "Icon konnte nicht gesetzt werden");
		}
		
		hauptFenster.setOnShown(e -> {
			var dateien = Arrays.stream(kommandoZeile.getArgs())
					.map(dateiname -> new File(dateiname))
					.filter(datei -> datei.exists() && datei.isFile()).toList();
			hauptansicht.oeffneDateien(dateien);
		});
		
		hauptFenster.show();
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
