/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.Umlaute.AE;
import static io.github.aid_labor.classifier.basis.Umlaute.sz;

import java.io.File;
import java.text.MessageFormat;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.Sprache;
import io.github.aid_labor.classifier.main.Hauptfenster;
import javafx.event.Event;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
					"Projekt \"{0}\" ohne zu Speichern schlie%cen?".formatted(sz)));
			String abfrage = nachricht.format(new Object[] { ansicht.getProjekt().getName() });
			ButtonType[] buttons = {
				new ButtonType(sprache.getText("abbrechen", "Abbrechen"),
						ButtonData.CANCEL_CLOSE),
				new ButtonType(sprache.getText("verwerfen",
						"%cnderungen verwerfen".formatted(AE)), ButtonData.YES),
				new ButtonType(sprache.getText("speichernSchliessen",
						"Speichern und Schlie%cen".formatted(sz)), ButtonData.NO)
			};
			
			// @formatter:off
			Alert dialog = new Alert(AlertType.CONFIRMATION, "", buttons);
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
			
			dialog.showAndWait().ifPresent(button -> {
				switch (button.getButtonData()) {
					case CANCEL_CLOSE -> {
						log.info(() -> "Schlie%cen von Projekt [%s] abgebrochen"
								.formatted(sz, ansicht.getProjekt().getName()));
						event.consume();
					}
					case NO -> {
						log.info(() -> "Speichere %cnderungen am Projekt [%s] und Schlie%ce"
								.formatted(AE, ansicht.getProjekt().getName(), sz));
						// TODO
					}
					default ->
						log.info(() -> "Verwerfe %cnderungen am Projekt [%s] und Schlie%ce"
								.formatted(AE, ansicht.getProjekt().getName(), sz));
				}
			});
			// @formatter:on
		}
	}
	
	boolean projektSpeichern() {
		var projekt = ansicht.getProjekt();
		
		if (projekt.getSpeicherort() == null) {
			return projektSpeichernUnter();
		}
		
		return ansicht.getProjekt().speichern();
	}
	
	boolean projektSpeichernUnter() {
		var projekt = ansicht.getProjekt();
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
		Einstellungen.getBenutzerdefiniert().letzterSpeicherortEinstellung
				.set(speicherOrt.getParentFile().getAbsolutePath());
		
		return ansicht.getProjekt().speichern(speicherOrt.toPath());
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}