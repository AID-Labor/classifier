/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ae;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ue;

import java.util.List;
import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane.Type;
import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.uml.Programmiersprache;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


class HauptKontrolle {
	private static final Logger log = Logger.getLogger(HauptKontrolle.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private HauptAnsicht ansicht;
	private Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	HauptKontrolle(HauptAnsicht ansicht, Sprache sprache) {
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
	
	void neuesProjektErzeugen(Event event) {
		GridPane dialog = new GridPane();
		TextField eingabeName = new TextField();
		dialog.add(new EnhancedLabel(sprache.getText("name", "Name") + ":"), 0, 0);
		dialog.add(eingabeName, 1, 0);
		
		
		ChoiceBox<Programmiersprache> wahlProgrammiersprache = new ChoiceBox<>();
		wahlProgrammiersprache.getItems().addAll(Programmiersprache.values());
		wahlProgrammiersprache.getSelectionModel().select(Programmiersprache.Java);
		dialog.add(new EnhancedLabel(sprache.getText("programmiersprache",
				"Programmiersprache") + ":"), 0, 1);
		dialog.add(wahlProgrammiersprache, 1, 1);
		dialog.setHgap(10);
		dialog.setVgap(15);
		dialog.setPadding(new Insets(20));
		
		List<ButtonType> buttons = List.of(
				new ButtonType(sprache.getText("abbrechen", "Abbrechen"),
						ButtonData.CANCEL_CLOSE),
				new ButtonType(sprache.getText("ok", "Ok"), ButtonData.OK_DONE)
		);
		
		Platform.runLater(() -> eingabeName.requestFocus());
		ansicht.getOverlayDialog()
				.showNode(Type.INPUT, sprache.getText("neuesProjekt", "Neues Projekt"), dialog,
						false, buttons, true)
				.thenAccept(button -> {
					if (button.getButtonData().equals(ButtonData.OK_DONE)) {
						log.finest(() -> "neues Projekt anlegen");
						
						String name = eingabeName.textProperty().get();
						Programmiersprache programmiersprache = wahlProgrammiersprache
								.getSelectionModel().getSelectedItem();
						
						if (name.isBlank()) {
							String titel = sprache.getText("anlegenFehlerTitel",
									"Fehler beim Anlegen eines Projektes");
							String beschreibung = sprache.getText("anlegenFehlerName",
									"Es muss ein g%cltiger Projektname angegeben werden"
											.formatted(ue));
							ansicht.getOverlayDialog().showNode(Type.ERROR, titel,
									new StackPane(new EnhancedLabel(beschreibung)));
						} else if (programmiersprache == null) {
							String titel = sprache.getText("anlegenFehlerTitel",
									"Fehler beim Anlegen eines Projektes");
							String beschreibung = sprache.getText(
									"anlegenFehlerProgrammiersprache",
									"Es muss eine Programmiersprache ausgew%chlt werden"
											.formatted(ae));
							ansicht.getOverlayDialog().showError(titel, beschreibung);
						} else {
							UMLProjekt projekt = new UMLProjekt(name, programmiersprache,
									false);
							ansicht.zeigeProjekt(projekt);
						}
					} else if (button.getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
						log.finest(() -> "Projekt erstellen abgebrochen.");
					} else {
						log.severe(() -> "Unbehandelter Buttontyp: "
								+ button.getButtonData().toString());
					}
				});
	}
	
	void projektSpeichern(Event event) {
		UMLProjekt projekt = ansicht.getProjektAnsicht().getAngezeigtesProjektProperty().get();
		
		if (projekt.getSpeicherort() == null) {
			projektSpeichernUnter(event);
		}
		
		ansicht.getProjektAnsicht().angezeigtesProjektSpeichern();
	}
	
	void projektSpeichernUnter(Event event) {
		ansicht.getProjektAnsicht().angezeigtesProjektSpeicherUnter();
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}