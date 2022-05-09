/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.main;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;

import io.github.aid_labor.classifier.basis.DateiUtil;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.Ressourcen;
import io.github.aid_labor.classifier.gui.HauptAnsicht;
import io.github.aid_labor.classifier.gui.View;
import io.github.aid_labor.classifier.gui.util.FensterUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Hauptfenster extends Application {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final Logger log = Logger.getLogger(Hauptfenster.class.getName());
	private static final ProgrammDetails programm = new ProgrammDetails("0.0.1", "Classifier",
			null, null, "https://github.com/AID-Labor/classifier");
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static void main(String[] args) {
		LoggingEinstellung.initialisiereLogging(programm);
		CommandLine aufruf = new KommandozeilenAuswertung(args, programm).werteArgumenteAus();
		log.log(Level.SEVERE, () -> "%s gestartet  -  OS: %s_%s_%s  -  Java: %s %s".formatted(
				programm.getVersionName(),
				System.getProperty("os.name"),
				System.getProperty("os.version"),
				System.getProperty("os.arch"),
				System.getProperty("java.vm.name"),
				System.getProperty("java.vm.version")));
		log.finest(() -> "Args: " + Arrays.toString(aufruf.getArgs()));
		
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
		View hauptansicht = new HauptAnsicht();
		
		Scene szene = new Scene(hauptansicht.getWurzelknoten());
		FensterUtil.installiereFensterwiederherstellung(hauptFenster, 300, 500,
				Ressourcen.get().KONFIGURATIONSORDNER.alsPath());
		
		hauptFenster.setScene(szene);
		hauptFenster.setTitle(programm.name());
		
		try (var iconStream = Ressourcen.get().CLASSIFIER_ICON_L.oeffneStream()) {
			hauptFenster.getIcons().add(new Image(iconStream));
		} catch (IOException | IllegalStateException e) {
			log.log(Level.WARNING, e, () -> "Icon konnte nicht gesetzt werden");
		}
		
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
