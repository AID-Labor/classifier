/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.AE;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.sz;

import java.io.File;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.DatumWrapper;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.util.FensterUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Window;


class ProjektKontrolle {
	private static final Logger log = Logger.getLogger(ProjektKontrolle.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final WeakReference<ProjektAnsicht> ansicht;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	ProjektKontrolle(ProjektAnsicht ansicht, Sprache sprache) {
		this.ansicht = new WeakReference<ProjektAnsicht>(ansicht);
		this.sprache = sprache;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	void checkSchliessen(Event event) {
		if (!ansicht.get().getProjekt().istGespeichertProperty().get()) {
			MessageFormat nachricht = new MessageFormat(sprache.getText("schliessenAbfrage",
					"Projekt \"{0}\" vor dem Schlie%cen speichern?".formatted(sz)));
			String abfrage = nachricht.format(new Object[] { ansicht.get().getProjekt().getName() });
			ButtonType abbrechenButton = new ButtonType(sprache.getText("abbrechen", "Abbrechen"),
					ButtonData.CANCEL_CLOSE);
			ButtonType verwerfenButton = new ButtonType(
					sprache.getText("verwerfen", "%cnderungen verwerfen".formatted(AE)), ButtonData.NO);
			ButtonType speichernSchliessenButton = new ButtonType(
					sprache.getText("speichernSchliessen", "Speichern und Schlie%cen".formatted(sz)), ButtonData.APPLY);
			
			// @formatter:off
			Alert dialog = new Alert(AlertType.CONFIRMATION, "", new ButtonType[] 
					{abbrechenButton, verwerfenButton, speichernSchliessenButton});
			dialog.setHeaderText(abfrage);
			Window hauptfenster = ansicht.get().getTabPane().getScene().getWindow();
			FensterUtil.initialisiereElternFenster(hauptfenster, dialog);
			
			dialog.setOnShowing(e -> {
				dialog.getDialogPane().lookupButton(speichernSchliessenButton).requestFocus();
			});
			
			dialog.showAndWait().ifPresent(button -> {
				switch (button.getButtonData()) {
					case NO -> {
						log.info(() -> "Verwerfe %cnderungen am Projekt [%s] und Schlie%ce"
							.formatted(AE, ansicht.get().getProjekt(), sz));
						this.ansicht.get().schliesse();
					}
					case APPLY -> {
						log.info(() -> "Speichere %cnderungen am Projekt [%s] und Schlie%ce"
							.formatted(AE, ansicht.get().getProjekt(), sz));
						projektSpeichern(this.ansicht.get().getProjekt());
						this.ansicht.get().schliesse();
					}
					default -> {
						log.info(() -> "Schlie%cen von Projekt [%s] abgebrochen"
							.formatted(sz, ansicht.get().getProjekt()));
						event.consume();
					}
				}
			});
			// @formatter:on
		} else {
			this.ansicht.get().schliesse();
		}
	}
	
	boolean projektSpeichern(UMLProjekt projekt) {
		if (projekt.getSpeicherort() == null) {
			return projektSpeichernUnter(projekt);
		}
		
		return ansicht.get().getProjekt().speichern();
	}
	
	boolean projektSpeichernUnter(UMLProjekt projekt) {
		FileChooser dateiDialog = new FileChooser();
		
		if (projekt.getSpeicherort() != null) {
			dateiDialog.setInitialDirectory(projekt.getSpeicherort().getParent().toFile());
			dateiDialog.setInitialFileName(projekt.getSpeicherort().getFileName().toString());
		} else {
			dateiDialog.setInitialDirectory(
					new File(Einstellungen.getBenutzerdefiniert().letzterSpeicherortProperty().get()));
			dateiDialog.setInitialFileName(projekt.getName());
		}
		
		dateiDialog.getExtensionFilters().addAll(ansicht.get().getProgrammDetails().dateiZuordnung());
		
		String format = sprache.getText("speichernDialogTitel", "Projekt \"{1}\" speichern als...");
		String titel = MessageFormat.format(format, projekt.getName());
		dateiDialog.setTitle(titel);
		
		File speicherOrt = dateiDialog.showSaveDialog(ansicht.get().getTabPane().getScene().getWindow());
		if (speicherOrt == null) {
			// @formatter:off
			log.info(() -> 
				"'Speichern Unter' f%cr Projekt %s fehlgeschlagen, da der Speicherort null war"
					.formatted(Umlaute.ue, projekt));
			// @formatter:on
			return false;
		}
		
		if (OS.getDefault().istLinux()) { // Workaround fuer Dateierweiterung
			var erweiterungen = Arrays.stream(ansicht.get().getProgrammDetails().dateiZuordnung())
					.flatMap(e -> e.getExtensions().stream()).toList();
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
		
		Einstellungen.getBenutzerdefiniert().letzterSpeicherortProperty()
				.set(speicherOrt.getParentFile().getAbsolutePath());
		
		boolean gespeichert = ansicht.get().getProjekt().speichern(speicherOrt.toPath());
		
		if (gespeichert) {
			DatumWrapper<Path> dateiEintrag = new DatumWrapper<Path>(speicherOrt.toPath());
			if (!Einstellungen.getBenutzerdefiniert().letzteDateienProperty().add(dateiEintrag)) {
				Einstellungen.getBenutzerdefiniert().letzteDateienProperty().remove(dateiEintrag);
				Einstellungen.getBenutzerdefiniert().letzteDateienProperty().add(dateiEintrag);
			}
		}
		
		return gespeichert;
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}