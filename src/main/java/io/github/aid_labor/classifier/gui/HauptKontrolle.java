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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

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
import io.github.aid_labor.classifier.gui.komponenten.UMLKlassifiziererAnsicht;
import io.github.aid_labor.classifier.gui.komponenten.UMLKommentarAnsicht;
import io.github.aid_labor.classifier.gui.util.FensterUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import io.github.aid_labor.classifier.uml.programmierung.ProgrammiersprachenVerwaltung;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotResult;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
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
	
	private WeakReference<HauptAnsicht> ansicht;
	private Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	HauptKontrolle(HauptAnsicht ansicht, Sprache sprache) {
		this.ansicht = new WeakReference<HauptAnsicht>(ansicht);
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
	
	void exportiereAlsBild(Event event) {
		List<? extends UMLDiagrammElement> elemente;
		if (ansicht.get().getProjektAnsicht().getProjektAnsicht().hatSelektion()) {
			elemente = ansicht.get().getProjektAnsicht().getProjektAnsicht().getSelektion();
		} else {
			elemente = ansicht.get().getProjektAnsicht().getAngezeigtesProjekt().getDiagrammElemente();
		}
		if (elemente.isEmpty()) {
			ansicht.get().zeigeExportFehlerDialog(sprache.getText("exportierenFehlerLeer",
					"Das Projekt enth%clt keine exportierbaren Elemente.".formatted(ae)));
			return;
		}
		
		// TODO:
		// neue GUI-Objekte erzeugen, in Scene legen und exportieren
		Group gruppe = new Group();
//		Group vorschau = new Group();
		for (var element : elemente) {
			Node node;
//			Node nodeVorschau;
			if (element instanceof UMLKlassifizierer k) {
				node = new UMLKlassifiziererAnsicht(k);
//				nodeVorschau = new UMLKlassifiziererAnsicht(k);
			} else if (element instanceof UMLKommentar k) {
				node = new UMLKommentarAnsicht(k);
//				nodeVorschau = new UMLKommentarAnsicht(k);
			} else {
				log.warning("unbekannter Elementtyp: " + element.getClass().getName());
				continue;
			}
			
			gruppe.getChildren().add(node);
//			vorschau.getChildren().add(nodeVorschau);
		}
		
//		BildexportDialog dialog = new BildexportDialog(vorschau);
		BildexportDialog dialog = new BildexportDialog(gruppe);
		FensterUtil.initialisiereElternFenster(ansicht.get().getWurzelknoten().getScene().getWindow(), dialog);
		dialog.showAndWait().ifPresent(parameter -> {
//			var snapshotScene = new Scene(gruppe);
			var snapshotScene = parameter.getSnapshotScene();
			snapshotScene.getStylesheets().addAll(ansicht.get().getWurzelknoten().getScene().getStylesheets());
			double skalierung = parameter.getSkalierung();
			gruppe.setScaleX(skalierung);
			gruppe.setScaleY(skalierung);
			
			// Workaround fuer Webview
			// Quelle: https://stackoverflow.com/a/60746994/1534698
			// without this runlater, the first capture is missed and all following captures are offset
//			var kontrolle = this;
//			Platform.runLater(new Runnable() {
//			    @Override
//				public void run() {
//			        // start a new animation timer which waits for exactly two pulses
//			        new AnimationTimer() {
//			            int frames = 0;
//
//			            @Override
//			            public void handle(long l) {
//			                // capture at exactly two frames
//			                if (++frames == 2) {
////			                    System.out.println("Attempting image capture");
//			                	vorschau.snapshot(kontrolle::snapshotSpeichern, null, null);
//
//			                    //stop timer after snapshot
//			                    stop();
//			                }
//			            }
//			        }.start();
//			    }
//			});
			
			// Achtung: WebView muss mindestens zwei Pulse in einem Fenster angezeigt worden sein!
			gruppe.snapshot(this::snapshotSpeichern, null, null);
		});
	}
	
	Void snapshotSpeichern(SnapshotResult ergebnis) {
		FileChooser dateiDialog = new FileChooser();
		
		var projekt = ansicht.get().getProjektAnsicht().getAngezeigtesProjekt();
		if (projekt.getSpeicherort() != null) {
			dateiDialog.setInitialDirectory(projekt.getSpeicherort().getParent().toFile());
			dateiDialog.setInitialFileName(projekt.getSpeicherort().getFileName().toString());
		} else {
			dateiDialog.setInitialDirectory(
					new File(Einstellungen.getBenutzerdefiniert().letzterSpeicherortProperty().get()));
			dateiDialog.setInitialFileName(projekt.getName());
		}
		
		dateiDialog.getExtensionFilters().addAll(new ExtensionFilter("Bild", "*.png"));
		
		String format = sprache.getText("exportierenDialogTitel", "Projekt \"{1}\" exportieren als...");
		String titel = MessageFormat.format(format, projekt.getName());
		dateiDialog.setTitle(titel);
		
		File speicherOrt = dateiDialog.showSaveDialog(ansicht.get().getWurzelknoten().getScene().getWindow());
		if (speicherOrt == null) {
			log.info(
					"'Speichern' f%cr Bild fehlgeschlagen, da der Speicherort null war".formatted(Umlaute.ue, projekt));
			return null;
		}
		
		BufferedImage bild = SwingFXUtils.fromFXImage(ergebnis.getImage(), null);
		try (var out = new FileOutputStream(speicherOrt)) {
			ImageIO.write(bild, "png", out);
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Datei %s konnte nicht geschrieben werden".formatted(speicherOrt.getPath()));
		}
		
		return null;
	}
	
	void konigurationsordnerOeffnen(Event event) {
		Path ordner = OS.getDefault().getKonfigurationsOrdnerPath(ansicht.get().getProgrammDetails());
		try {
			ansicht.get().getRechnerService().showDocument(ordner.toUri().toString());
		} catch (Exception e) {
			log.log(Level.WARNING, e, () -> "Ordner %s konnte nicht angezeigt werden".formatted(ordner));
		}
	}
	
	void konigurationsordnerBereinigen(Event event) {
		String dialogTitel = sprache.getText("neustartWarnungTitel", "Achtung:");
		EnhancedLabel beschreibung = new EnhancedLabel(sprache.getText("neustartWarnung", """
				Es werden alle Programm-Ressourcen inklusive Log-Dateien gel%cscht! \
				Nur die Einstellungen bleiben erhalten.
				
				Das Programm wird automatisch beendet und muss wieder manuell ge%cffnet werden.
				Nicht gespeicherte %cnderungen gehen ohne Nachfrage verloren!""".formatted(oe, oe, AE)));
		beschreibung.setWrapText(true);
		beschreibung.setPrefWidth(850);
		beschreibung.getStyleClass().add("dialog-text-warnung");
		this.ansicht.get().getOverlayDialog()
				.showNode(Type.WARNING, dialogTitel, beschreibung, false, List.of(ButtonType.OK, ButtonType.CANCEL))
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
		wahlProgrammiersprache.getItems().addAll(ProgrammiersprachenVerwaltung.getProgrammiersprachen());
		wahlProgrammiersprache.getSelectionModel()
				.select(ProgrammiersprachenVerwaltung.getStandardProgrammiersprache());
		dialog.add(new EnhancedLabel(sprache.getText("programmiersprache", "Programmiersprache") + ":"), 0, 1);
		dialog.add(wahlProgrammiersprache, 1, 1);
		dialog.setHgap(10);
		dialog.setVgap(15);
		dialog.setPadding(new Insets(20));
		
		List<ButtonType> buttons = List.of(
				new ButtonType(sprache.getText("abbrechen", "Abbrechen"), ButtonData.CANCEL_CLOSE),
				new ButtonType(sprache.getText("ok", "Ok"), ButtonData.OK_DONE));
		
		Platform.runLater(eingabeName::requestFocus);
		ansicht.get().getOverlayDialog()
				.showNode(Type.INPUT, sprache.getText("neuesProjekt", "Neues Projekt"), dialog, false, buttons, true)
				.thenAccept(button -> {
					if (button.getButtonData().equals(ButtonData.OK_DONE)) {
						log.finest(() -> "neues Projekt anlegen");
						
						String name = eingabeName.textProperty().get();
						Programmiersprache programmiersprache = wahlProgrammiersprache.getSelectionModel()
								.getSelectedItem();
						
						if (name.isBlank()) {
							ansicht.get().zeigeAnlegenFehlerDialog(sprache.getText("anlegenFehlerName",
									"Es muss ein g%cltiger Projektname angegeben werden".formatted(ue)));
						} else if (programmiersprache == null) {
							ansicht.get().zeigeAnlegenFehlerDialog(sprache.getText("anlegenFehlerProgrammiersprache",
									"Es muss eine Programmiersprache ausgew%chlt werden".formatted(ae)));
						} else {
							UMLProjekt projekt = new UMLProjekt(name, programmiersprache, false);
							try {
								ansicht.get().zeigeProjekt(projekt);
							} catch (Exception e) {
								log.log(Level.SEVERE, e, () -> "Fehler beim Anlegen eins Projektes");
							}
						}
					} else if (button.getButtonData().equals(ButtonData.CANCEL_CLOSE)) {
						log.finest(() -> "Projekt erstellen abgebrochen.");
					} else {
						log.severe(() -> "Unbehandelter Buttontyp: " + button.getButtonData().toString());
					}
				});
	}
	
	void projektSpeichern(Event event) {
		UMLProjekt projekt = ansicht.get().getProjektAnsicht().angezeigtesProjektProperty().get();
		
		if (projekt.getSpeicherort() == null) {
			projektSpeichernUnter(event);
		}
		
		ansicht.get().getProjektAnsicht().angezeigtesProjektSpeichern();
	}
	
	void projektSpeichernUnter(Event event) {
		ansicht.get().getProjektAnsicht().angezeigtesProjektSpeicherUnter();
	}
	
	void projektOeffnen(Event event) {
		FileChooser dateiDialog = new FileChooser();
		
		dateiDialog
				.setInitialDirectory(new File(Einstellungen.getBenutzerdefiniert().letzterSpeicherortProperty().get()));
		
		if (!OS.getDefault().istLinux()) {	// Workaraound, da in Linux die Dateierweitung nicht erkannt wird
			dateiDialog.getExtensionFilters().addAll(ansicht.get().getProgrammDetails().dateiZuordnung());
		}
		
		String titel = sprache.getText("oeffnenDialogTitel", "Projekt %cffnen".formatted(Umlaute.OE));
		dateiDialog.setTitle(titel);
		
		List<File> dateien = dateiDialog.showOpenMultipleDialog(ansicht.get().getWurzelknoten().getScene().getWindow());
		
		if (dateien != null && !dateien.isEmpty()) {
			Einstellungen.getBenutzerdefiniert().letzterSpeicherortProperty()
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
				for (ExtensionFilter erweiterungen : this.ansicht.get().getProgrammDetails().dateiZuordnung()) {
					for (String erweiterung : erweiterungen.getExtensions()) {
						akzeptieren |= db.getFiles().stream().filter(datei -> datei.getName().endsWith(erweiterung))
								.toList().isEmpty();
					}
				}
				String akzeptiert = String.valueOf(akzeptieren);
				log.finer(() -> """
						DragOver-Dateien: [%s]
						         Akzeptieren: %s""".formatted(
						db.getFiles().stream().map(datei -> datei.getAbsolutePath()).collect(Collectors.joining("   ")),
						akzeptiert));
			}
			if (akzeptieren) {
				event.acceptTransferModes(TransferMode.COPY);
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
			log.log(Level.WARNING, e, () -> "Datei %s konnte nicht gelesen werden".formatted(datei.getAbsolutePath()));
		} catch (Exception e) {
			log.log(Level.WARNING, e,
					() -> "Beim Lesen der Datei %s ist ein Fehler aufgetreten".formatted(datei.getAbsolutePath()));
		}
		
		if (projekt == null) {
			ansicht.get().zeigeOeffnenFehlerDialog(datei);
		} else {
			String projektName = projekt.getName();
			try {
				this.ansicht.get().zeigeProjekt(projekt);
				DatumWrapper<Path> dateiEintrag = new DatumWrapper<Path>(datei.toPath());
				if (!Einstellungen.getBenutzerdefiniert().letzteDateienProperty().add(dateiEintrag)) {
					Einstellungen.getBenutzerdefiniert().letzteDateienProperty().remove(dateiEintrag);
					Einstellungen.getBenutzerdefiniert().letzteDateienProperty().add(dateiEintrag);
				}
			} catch (Exception e) {
				log.log(Level.INFO, e, () -> "Projekt %s wurde nicht geoeffnet".formatted(projektName));
				ansicht.get().zeigeOeffnenWarnungDialog(datei);
			}
		}
	}
	
	void projektUmbenennen(Event event) {
		UMLProjekt projekt = ansicht.get().getProjektAnsicht().angezeigtesProjektProperty().get();
		
		MessageFormat format = new MessageFormat(
				sprache.getText("umbenennenTitel", "Neuen Namen f%cr Projekt {0} festlegen".formatted(ue)));
		String titel = format.format(new Object[] { projekt.getName() });
		
		Dialog<String> eingabeDialog = this.ansicht.get().getOverlayDialog().showTextInput(titel, projekt.getName());
		
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
	
	void zoomeGroesser(Event event) {
		var projekt = ansicht.get().getProjektAnsicht().projektAnsichtProperty().get();
		projekt.skaliere(projekt.getSkalierung() + 0.1);
	}
	
	void zoomeKleiner(Event event) {
		var projekt = ansicht.get().getProjektAnsicht().projektAnsichtProperty().get();
		projekt.skaliere(projekt.getSkalierung() - 0.1);
	}
	
	void resetZoom(Event event) {
		var projekt = ansicht.get().getProjektAnsicht().projektAnsichtProperty().get();
		projekt.skaliere(projekt.getStandardSkalierung());
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void bereinigeKonfigurationsOrdner() {
		var konfigurationsordner = OS.getDefault().getKonfigurationsOrdnerPath(ansicht.get().getProgrammDetails());
		var ausgeschlossen = Ressourcen.get().KONFIGURATIONSORDNER.alsPath();
		
		log.info(() -> "Bereinige Konfigurationsordner " + konfigurationsordner);
		
		try (var konfigurationsordnerInhalt = Files.newDirectoryStream(konfigurationsordner,
				eintrag -> !eintrag.toAbsolutePath().startsWith(ausgeschlossen))) {
			for (Path inhalt : konfigurationsordnerInhalt) {
				DateiUtil.loescheDateiOderOrdner(inhalt);
			}
		} catch (IOException e) {
			log.log(Level.WARNING, e, () -> "Fehler beim Bereinigen des Konfigurationsordners");
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