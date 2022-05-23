/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.AE;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.sz;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.DatumWrapper;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.event.Event;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
	
	private final ProjektAnsicht ansicht;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	ProjektKontrolle(ProjektAnsicht ansicht, Sprache sprache) {
		this.ansicht = ansicht;
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
		if (!ansicht.getProjekt().istGespeichertProperty().get()) {
			MessageFormat nachricht = new MessageFormat(sprache.getText("schliessenAbfrage",
					"Projekt \"{0}\" vor dem Schlie%cen speichern?".formatted(sz)));
			String abfrage = nachricht.format(new Object[] { ansicht.getProjekt().getName() });
			ButtonType abbrechenButton = new ButtonType(sprache.getText("abbrechen",
					"Abbrechen"),
					ButtonData.CANCEL_CLOSE);
			ButtonType verwerfenButton = new ButtonType(sprache.getText("verwerfen",
					"%cnderungen verwerfen".formatted(AE)),
					ButtonData.NO);
			ButtonType speichernSchliessenButton = new ButtonType(sprache.getText(
					"speichernSchliessen", "Speichern und Schlie%cen".formatted(sz)),
					ButtonData.APPLY);
			
			// @formatter:off
			Alert dialog = new Alert(AlertType.CONFIRMATION, "", new ButtonType[] 
					{abbrechenButton, verwerfenButton, speichernSchliessenButton});
			dialog.setHeaderText(abfrage);
			Window hauptfenster = ansicht.getTabPane().getScene().getWindow();
			Bounds position = new BoundingBox(hauptfenster.getX(), hauptfenster.getY(), 
					hauptfenster.getWidth(), hauptfenster.getHeight());
			
			dialog.widthProperty().addListener((breite, alt, neueBreite) -> {
				dialog.setX(position.getCenterX() - neueBreite.doubleValue()/2);
			});
			dialog.heightProperty().addListener((hoehe, alt, neueHoehe) -> {
				dialog.setY(position.getCenterY() - neueHoehe.doubleValue()/2);
			});
			
			dialog.initOwner(hauptfenster);
			dialog.initModality(Modality.WINDOW_MODAL);
			
			dialog.setX(position.getCenterX() - dialog.getWidth()/2);
			dialog.setY(position.getCenterY() - dialog.getHeight()/2);
			
			dialog.setOnShowing(e -> {
				dialog.getDialogPane().lookupButton(speichernSchliessenButton).requestFocus();
			});
			
			dialog.showAndWait().ifPresent(button -> {
				switch (button.getButtonData()) {
					case NO -> {
						log.info(() -> "Verwerfe %cnderungen am Projekt [%s] und Schlie%ce"
						.formatted(AE, ansicht.getProjekt(), sz));
					}
					case APPLY -> {
						log.info(() -> "Speichere %cnderungen am Projekt [%s] und Schlie%ce"
								.formatted(AE, ansicht.getProjekt(), sz));
						projektSpeichern(this.ansicht.getProjekt());
					}
					default -> {
						log.info(() -> "Schlie%cen von Projekt [%s] abgebrochen"
							.formatted(sz, ansicht.getProjekt()));
						event.consume();
					}
				}
			});
			// @formatter:on
		}
	}
	
	boolean projektSpeichern(UMLProjekt projekt) {
		if (projekt.getSpeicherort() == null) {
			return projektSpeichernUnter(projekt);
		}
		
		return ansicht.getProjekt().speichern();
	}
	
	boolean projektSpeichernUnter(UMLProjekt projekt) {
		FileChooser dateiDialog = new FileChooser();
		
		if (projekt.getSpeicherort() != null) {
			dateiDialog.setInitialDirectory(projekt.getSpeicherort().getParent().toFile());
			dateiDialog.setInitialFileName(projekt.getSpeicherort().getFileName().toString());
		} else {
			dateiDialog.setInitialDirectory(new File(
					Einstellungen.getBenutzerdefiniert().letzterSpeicherortEinstellung.get()));
			dateiDialog.setInitialFileName(projekt.getName());
		}
		
		dateiDialog.getExtensionFilters()
				.addAll(ansicht.getProgrammDetails().dateiZuordnung());
		
		String format = sprache.getText("speichernDialogTitel",
				"Projekt \"{1}\" speichern als...");
		String titel = MessageFormat.format(format, projekt.getName());
		dateiDialog.setTitle(titel);
		
		File speicherOrt = dateiDialog
				.showSaveDialog(ansicht.getTabPane().getScene().getWindow());
		if (speicherOrt == null) {
			// @formatter:off
			log.info(() -> 
				"'Speichern Unter' f%cr Projekt %s fehlgeschlagen, da der Speicherort null war"
					.formatted(Umlaute.ue, projekt));
			// @formatter:on
			return false;
		}
		
		Einstellungen.getBenutzerdefiniert().letzterSpeicherortEinstellung
				.set(speicherOrt.getParentFile().getAbsolutePath());
		
		boolean gespeichert = ansicht.getProjekt().speichern(speicherOrt.toPath());
		
		if (gespeichert) {
			DatumWrapper<Path> dateiEintrag = new DatumWrapper<Path>(speicherOrt.toPath());
			if (!Einstellungen.getBenutzerdefiniert().letzteDateien.add(dateiEintrag)) {
				Einstellungen.getBenutzerdefiniert().letzteDateien.remove(dateiEintrag);
				Einstellungen.getBenutzerdefiniert().letzteDateien.add(dateiEintrag);
			}
		}
		
		return gespeichert;
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}