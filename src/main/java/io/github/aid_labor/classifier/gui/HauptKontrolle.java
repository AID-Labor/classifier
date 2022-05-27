/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.AE;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ae;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.oe;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.dlsc.gemsfx.DialogPane.Dialog;
import com.dlsc.gemsfx.DialogPane.Type;
import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.DatumWrapper;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.DateiUtil;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


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
	
	void konigurationsordnerOeffnen(Event event) {
		Path ordner = OS.getDefault().getKonfigurationsOrdnerPath(ansicht.getProgrammDetails());
		try {
			ansicht.getRechnerService().showDocument(ordner.toUri().toString());
		} catch (Exception e) {
			log.log(Level.WARNING, e,
					() -> "Ordner %s konnte nicht angezeigt werden".formatted(ordner));
		}
	}
	
	void konigurationsordnerBereinigen(Event event) {
		String dialogTitel = sprache.getText("neustartWarnungTitel", "Achtung:");
		EnhancedLabel beschreibung = new EnhancedLabel(sprache.getText("neustartWarnung", """
				Es werden alle Programm-Ressourcen inklusive Log-Dateien gel%cscht! \
				Nur die Einstellungen bleiben erhalten.
				
				Das Programm wird automatisch beendet und muss wieder manuell ge%cffnet werden.
				Nicht gespeicherte %cnderungen gehen ohne Nachfrage verloren!"""
				.formatted(oe, oe, AE)));
		beschreibung.setWrapText(true);
		beschreibung.setId("NEUSTART_WARNUNG");
		this.ansicht.getOverlayDialog().showNode(Type.WARNING, dialogTitel,
				new StackPane(beschreibung), false, List.of(ButtonType.OK, ButtonType.CANCEL))
				.thenAccept(buttonTyp -> {
					if (buttonTyp.equals(ButtonType.OK)) {
						bereinigeKonfigurationsOrdner();
						Platform.exit();
					}
				});
	}
	
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
				new ButtonType(sprache.getText("ok", "Ok"), ButtonData.OK_DONE));
		
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
							ansicht.zeigeAnlegenFehlerDialog(sprache.getText(
									"anlegenFehlerName",
									"Es muss ein g%cltiger Projektname angegeben werden"
											.formatted(ue)));
						} else if (programmiersprache == null) {
							ansicht.zeigeAnlegenFehlerDialog(sprache.getText(
									"anlegenFehlerProgrammiersprache",
									"Es muss eine Programmiersprache ausgew%chlt werden"
											.formatted(ae)));
						} else {
							UMLProjekt projekt = new UMLProjekt(name, programmiersprache,
									false);
							try {
								ansicht.zeigeProjekt(projekt);
							} catch (Exception e) {
								log.log(Level.SEVERE, e,
										() -> "Fehler beim Anlegen eins Projektes");
							}
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
	
	void projektOeffnen(Event event) {
		FileChooser dateiDialog = new FileChooser();
		
		dateiDialog.setInitialDirectory(new File(
				Einstellungen.getBenutzerdefiniert().letzterSpeicherortEinstellung.get()));
		
		dateiDialog.getExtensionFilters()
				.addAll(ansicht.getProgrammDetails().dateiZuordnung());
		
		String titel = sprache.getText("oeffnenDialogTitel",
				"Projekt %cffnen".formatted(Umlaute.OE));
		dateiDialog.setTitle(titel);
		
		List<File> dateien = dateiDialog
				.showOpenMultipleDialog(ansicht.getWurzelknoten().getScene().getWindow());
		
		if (dateien != null && !dateien.isEmpty()) {
			Einstellungen.getBenutzerdefiniert().letzterSpeicherortEinstellung
					.set(dateien.get(0).getParentFile().getAbsolutePath());
			
			dateien.forEach(datei -> {
				projektOeffnen(datei);
			});
		}
	}
	
	void projektOeffnen(DragEvent event) {
		log.finest(() -> "DragEvent ausgeloest: " + event.getEventType().getName());
		if (event.getEventType().equals(DragEvent.DRAG_OVER)) {
			Dragboard db = event.getDragboard();
			boolean akzeptieren = false;
			if (db.hasFiles()) {
				for (ExtensionFilter erweiterungen : this.ansicht.getProgrammDetails()
						.dateiZuordnung()) {
					for (String erweiterung : erweiterungen.getExtensions()) {
						akzeptieren |= db.getFiles().stream()
								.filter(datei -> datei.getName().endsWith(erweiterung))
								.toList()
								.isEmpty();
					}
				}
				String akzeptiert = String.valueOf(akzeptieren);
				log.finer(() -> """
						DragOver-Dateien: [%s]
						         Akzeptieren: %s"""
						.formatted(db.getFiles().stream().map(datei -> datei.getAbsolutePath())
								.collect(Collectors.joining("   ")), akzeptiert));
			}
			if (akzeptieren) {
				event.acceptTransferModes(TransferMode.ANY);
			}
		}
		if (event.getEventType().equals(DragEvent.DRAG_DROPPED)) {
			Dragboard db = event.getDragboard();
			if (db.hasFiles()) {
				for (File datei : db.getFiles()) {
					projektOeffnen(datei);
				}
			}
		}
		event.consume();
	}
	
	void projektOeffnen(File datei) {
		UMLProjekt projekt = null;
		try {
			projekt = UMLProjekt.ausDateiOeffnen(datei.toPath());
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Datei %s konnte nicht gelesen werden"
					.formatted(datei.getAbsolutePath()));
		} catch (Exception e) {
			log.log(Level.WARNING, e,
					() -> "Beim Lesen der Datei %s ist ein Fehler aufgetreten"
							.formatted(datei.getAbsolutePath()));
		}
		
		if (projekt == null) {
			ansicht.zeigeOeffnenFehlerDialog(datei);
		} else {
			String projektName = projekt.getName();
			try {
				this.ansicht.zeigeProjekt(projekt);
				DatumWrapper<Path> dateiEintrag = new DatumWrapper<Path>(datei.toPath());
				if (!Einstellungen.getBenutzerdefiniert().letzteDateien.add(dateiEintrag)) {
					Einstellungen.getBenutzerdefiniert().letzteDateien.remove(dateiEintrag);
					Einstellungen.getBenutzerdefiniert().letzteDateien.add(dateiEintrag);
				}
			} catch (Exception e) {
				log.log(Level.INFO, e,
						() -> "Projekt %s wurde nicht geoeffnet".formatted(projektName));
				ansicht.zeigeOffnenWarnungDialog(datei);
			}
		}
	}
	
	void projektUmbenennen(Event event) {
		UMLProjekt projekt = ansicht.getProjektAnsicht().getAngezeigtesProjektProperty().get();
		
		MessageFormat format = new MessageFormat(sprache.getText("umbenennenTitel",
				"Neuen Namen f%cr Projekt {0} festlegen".formatted(ue)));
		String titel = format.format(new Object[] { projekt.getName() });
		
		Dialog<String> eingabeDialog = this.ansicht.getOverlayDialog().showTextInput(titel,
				projekt.getName());
		
		TextField eingabe = findeElement(eingabeDialog.getContent(), TextField.class);
		if (eingabe != null) {
			eingabeDialog.validProperty().bind(eingabe.textProperty().isNotEmpty());
		}
		
		eingabeDialog.thenAccept(buttonTyp -> {
			if (buttonTyp.getButtonData().equals(ButtonData.OK_DONE)) {
				projekt.setName(eingabeDialog.getValue());
			}
		});
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void bereinigeKonfigurationsOrdner() {
		var konfigurationsordner = OS.getDefault()
				.getKonfigurationsOrdnerPath(ansicht.getProgrammDetails());
		var ausgeschlossen = Ressourcen.get().KONFIGURATIONSORDNER.alsPath();
		
		log.info(() -> "Bereinige Konfigurationsordner " + konfigurationsordner);
		
		try (var konfigurationsordnerInhalt = Files.newDirectoryStream(konfigurationsordner,
				eintrag -> !eintrag.toAbsolutePath().startsWith(ausgeschlossen))) {
			for (Path inhalt : konfigurationsordnerInhalt) {
				DateiUtil.loescheDateiOderOrdner(inhalt);
			}
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Fehler beim Bereinigen des Konfigurationsordners");
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T> T findeElement(Node wurzel, Class<T> klasse) {
		if (klasse.isAssignableFrom(wurzel.getClass())) {
			return (T) wurzel;
		} else if (wurzel instanceof Parent p) {
			for (Node kind : p.getChildrenUnmodifiable()) {
				T element = findeElement(kind, klasse);
				if (element != null) {
					return element;
				}
			}
		}
		
		return null;
	}
	
}