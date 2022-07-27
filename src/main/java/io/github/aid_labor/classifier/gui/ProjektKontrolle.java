/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.AE;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.sz;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.controlsfx.dialog.ExceptionDialog;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import com.dlsc.gemsfx.DialogPane.Type;

import io.github.aid_labor.classifier.basis.DatumWrapper;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.ProjekteAnsicht.ExportErgebnis;
import io.github.aid_labor.classifier.gui.util.FensterUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import io.github.aid_labor.classifier.uml.programmierung.ExportImportVerarbeitung.ImportErgebnis;
import io.github.aid_labor.classifier.uml.programmierung.ImportException;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Labeled;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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
		boolean veraendert = true;
		try {
			if (ansicht.get().getProjekt().getSpeicherort() != null) {
				UMLProjekt dateiInhalt = UMLProjekt.ausDateiOeffnen(ansicht.get().getProjekt().getSpeicherort());
				veraendert = dateiInhalt.hashCode() != ansicht.get().getProjekt().getGespeicherterHash();
			}
		} catch (Exception e) {
			log.log(Level.WARNING, e, () -> "Fehler beim lesen der Datei '%s' fuer Vergleich"
					.formatted(ansicht.get().getProjekt().getSpeicherort()));
		}
		if (!ansicht.get().getProjekt().istGespeichertProperty().get() || veraendert) {
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
						boolean erfolg = projektSpeichern(this.ansicht.get().getProjekt());
						if (erfolg) {
							this.ansicht.get().schliesse();
						} else {
							event.consume();
						}
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
		
		boolean veraendert = true;
		try {
			UMLProjekt dateiInhalt = UMLProjekt.ausDateiOeffnen(projekt.getSpeicherort());
			veraendert = dateiInhalt.hashCode() != projekt.getGespeicherterHash();
		} catch (IOException e) {
			log.log(Level.WARNING, e,
					() -> "Fehler beim lesen der Datei '%s' fuer Vergleich".formatted(projekt.getSpeicherort()));
		}
		
		if (veraendert) {
			String dateiname = projekt.getSpeicherort() == null ? projekt.getName()
					: projekt.getSpeicherort().getFileName().toString();
			boolean ueberschreiben = zeigeSpeichernUeberschreibenDialog(dateiname);
			if (!ueberschreiben) {
				return false;
			}
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
		
		String format = sprache.getText("speichernDialogTitel", "Projekt \"{0}\" speichern als...");
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
	
	ExportErgebnis exportiereAlsQuellcode(List<UMLKlassifizierer> elemente, UMLProjekt projekt) throws Exception {
		
		DirectoryChooser dateiDialog = new DirectoryChooser();
		
		if (projekt.getSpeicherort() != null) {
			dateiDialog.setInitialDirectory(projekt.getSpeicherort().getParent().toFile());
		} else {
			dateiDialog.setInitialDirectory(
					new File(Einstellungen.getBenutzerdefiniert().letzterQuellcodeSpeicherortProperty().get()));
		}
		
		String titel = sprache.getText("speichernQuellcodeDialogTitel", "Quellcode speichern unter...");
		dateiDialog.setTitle(titel);
		
		File speicherOrt = dateiDialog.showDialog(ansicht.get().getTabPane().getScene().getWindow());
		if (speicherOrt == null) {
			// @formatter:off
			log.info(() -> 
				"'Speichern Unter' f%cr Projekt %s fehlgeschlagen, da der Speicherort null war"
					.formatted(Umlaute.ue, projekt));
			// @formatter:on
			return ExportErgebnis.keinErfolg;
		}
		
		Einstellungen.getBenutzerdefiniert().letzterQuellcodeSpeicherortProperty().set(speicherOrt.getAbsolutePath());
		
		var exportVerarbeitung = projekt.getProgrammiersprache().getVerarbeitung();
		exportVerarbeitung.exportiere(elemente, speicherOrt);
		
		return ExportErgebnis.mitOrdner(speicherOrt.toPath());
	}
	
	void importiereAusDatei() {
		UMLProjekt projekt = ansicht.get().getProjekt();
		var importVerwaltung = projekt.getProgrammiersprache().getVerarbeitung();
		FileChooser dateiDialog = new FileChooser();
		
		dateiDialog.setInitialDirectory(
				new File(Einstellungen.getBenutzerdefiniert().letzterQuellcodeSpeicherortProperty().get()));
		
		if (!OS.getDefault().istLinux()) {	// Workaraound, da in Linux die Dateierweitung nicht erkannt wird
			dateiDialog.getExtensionFilters().addAll(importVerwaltung.getImportDateierweiterungen());
		}
		
		String titel = sprache.getText("oeffnenDialogTitel", "Projekt %cffnen".formatted(Umlaute.OE));
		dateiDialog.setTitle(titel);
		
		List<File> dateien = dateiDialog.showOpenMultipleDialog(ansicht.get().getTabPane().getScene().getWindow());
		
		if (dateien != null && !dateien.isEmpty()) {
			Einstellungen.getBenutzerdefiniert().letzterSpeicherortProperty()
					.set(dateien.get(0).getParentFile().getAbsolutePath());
			
			importiereAusDateien(dateien);
		}
	}
	
	private void importiereAusDateien(List<File> dateien) {
		var wartedialog = ansicht.get().getOverlayDialog().showBusyIndicator();
		wartedialog.setShowCloseButton(false);
		
		Thread t = new Thread(() -> {
			ExecutorService ausfuehrService = Executors
					.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
			CompletionService<ImportErgebnis> fertigstellungService = new ExecutorCompletionService<>(ausfuehrService);
			
			dateien.forEach(datei -> {
				fertigstellungService.submit(() -> {
					List<UMLKlassifizierer> neueKlassifizierer = new LinkedList<>();
					List<UMLVerbindung> neueAssoziationen = new LinkedList<>();
					ImportErgebnis ergebnis = null;
					
					UMLProjekt projekt = ansicht.get().getProjekt();
					var importVerwaltung = projekt.getProgrammiersprache().getVerarbeitung();
					
					try {
						ergebnis = importVerwaltung.importiere(datei);
						if (ergebnis == null) {
							throw new NullPointerException();
						} else if (ergebnis.getNeueKlassifizierer().isEmpty()) {
							throw ergebnis.getFehler();
						}
						neueKlassifizierer.addAll(ergebnis.getNeueKlassifizierer());
						ergebnis.getNeueAssoziationen().stream()
								.filter(v -> UMLVerbindungstyp.ASSOZIATION.equals(v.getTyp())).forEach(assoziation -> {
									assoziation.setzeAutomatisch(false);
									neueAssoziationen.add(assoziation);
								});
					} catch (ImportException e) {
						log.log(Level.WARNING, e, () -> "Importfehler");
						if (!ergebnis.istFehlerBehandelt()) {
							
							String format = sprache.getText("importParseFehler", """
									Die Datei \"{0}\" konnte nicht interpretiert werden.
									M%cglicherweise ist die Datei besch%cdigt oder der Quellcode ist ung%cltig"""
									.formatted(Umlaute.oe, Umlaute.ae, Umlaute.ue));
							String beschreibung = MessageFormat.format(format, datei.getName());
							ergebnis.setIstFehlerBehandelt(true);
							Platform.runLater(() -> zeigeImportFehlerDialog(beschreibung, e));
						}
					} catch (Exception e) {
						log.log(Level.WARNING, e, () -> "Importfehler");
						if (!ergebnis.istFehlerBehandelt()) {
							String format = sprache.getText("importIOFehler",
									"Die Datei \"{0}\" konnte nicht gelesen werden.");
							String beschreibung = MessageFormat.format(format, datei.getName());
							Platform.runLater(() -> zeigeImportFehlerDialog(beschreibung, e));
							ergebnis.setIstFehlerBehandelt(true);
						}
					}
					
					return ergebnis;
				});
			});
			
			List<UMLKlassifizierer> neueKlassifizierer = new LinkedList<>();
			List<UMLVerbindung> neueAssoziationen = new LinkedList<>();
			for (int i = 0; i < dateien.size(); i++) {
				try {
					ImportErgebnis teilergebnis = fertigstellungService.take().get();
					
					if (teilergebnis.getNeueKlassifizierer().isEmpty() && !teilergebnis.istFehlerBehandelt()) {
						log.log(Level.WARNING, teilergebnis.getFehler(), () -> "Fehler beim parallelisiertem Import");
						String beschreibung = sprache.getText("importThreadFehler",
								"Es ist ein Fehler beim Import aufgetreten.");
						teilergebnis.setIstFehlerBehandelt(true);
						Platform.runLater(() -> zeigeImportFehlerDialog(beschreibung, teilergebnis.getFehler()));
					}
					
					neueKlassifizierer.addAll(teilergebnis.getNeueKlassifizierer());
					neueAssoziationen.addAll(teilergebnis.getNeueAssoziationen());
				} catch (Exception e) {
					log.log(Level.WARNING, e, () -> "Fehler beim parallelisiertem Import");
					String beschreibung = sprache.getText("importThreadFehler",
							"Es ist ein Fehler beim Import aufgetreten.");
					Platform.runLater(() -> zeigeImportFehlerDialog(beschreibung, e));
				}
			}
//			List<UMLKlassifizierer> neueKlassifizierer = new LinkedList<>();
//			List<UMLVerbindung> neueAssoziationen = new LinkedList<>();
//			for (File datei : dateien) {
//				
//				UMLProjekt projekt = ansicht.get().getProjekt();
//				var importVerwaltung = projekt.getProgrammiersprache().getVerarbeitung();
//				
//				try {
//					List<UMLVerbindung> assoziationen = new LinkedList<>();
//					List<UMLKlassifizierer> klassifizierer = importVerwaltung.importiere(datei, assoziationen);
//					if (klassifizierer == null) {
//						throw new NullPointerException();
//					}
//					neueKlassifizierer.addAll(klassifizierer);
//					assoziationen.stream().filter(v -> UMLVerbindungstyp.ASSOZIATION.equals(v.getTyp()))
//							.forEach(assoziation -> {
//								assoziation.setzeAutomatisch(false);
//								neueAssoziationen.add(assoziation);
//							});
//				} catch (ImportException e) {
//					log.log(Level.WARNING, e, () -> "Importfehler");
//					String format = sprache.getText("importParseFehler", """
//							Die Datei \"{0}\" konnte nicht interpretiert werden.
//							M%cglicherweise ist die Datei besch%cdigt oder der Quellcode ist ung%cltig"""
//							.formatted(Umlaute.oe, Umlaute.ae, Umlaute.ue));
//					String beschreibung = MessageFormat.format(format, datei.getName());
//					Platform.runLater(() -> zeigeImportFehlerDialog(beschreibung, e));
//				} catch (Exception e) {
//					log.log(Level.WARNING, e, () -> "Importfehler");
//					String format = sprache.getText("importIOFehler", "Die Datei \"{0}\" konnte nicht gelesen werden.");
//					String beschreibung = MessageFormat.format(format, datei.getName());
//					Platform.runLater(() -> zeigeImportFehlerDialog(beschreibung, e));
//				}
//			}
			
			Platform.runLater(() -> {
				wartedialog.cancel();
				ansicht.get().getOverlayDialog().hideDialog(wartedialog);
				UMLProjekt projekt = ansicht.get().getProjekt();
				var status = projekt.getUeberwachungsStatus();
				projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
				projekt.getDiagrammElemente().addAll(neueKlassifizierer);
				projekt.getAssoziationen().addAll(neueAssoziationen);
				projekt.setUeberwachungsStatus(status);
			});
		});
		t.start();
	}
	
	void importiereAusDatei(DragEvent event) {
		log.finest(() -> "DragEvent ausgeloest: " + event.getEventType().getName());
		if (event.isConsumed()) {
			log.finest(() -> "DragEvent bereits konsumiert");
			return;
		}
		if (event.getEventType().equals(DragEvent.DRAG_OVER)) {
			UMLProjekt projekt = ansicht.get().getProjekt();
			var importVerwaltung = projekt.getProgrammiersprache().getVerarbeitung();
			Dragboard db = event.getDragboard();
			boolean akzeptieren = false;
			if (db.hasFiles()) {
				akzeptieren = pruefeDateiErweiterungen(db.getFiles(), importVerwaltung.getImportDateierweiterungen());
				String akzeptiert = String.valueOf(akzeptieren);
				log.finer(() -> """
						DragOver-Dateien: [%s]
						         Akzeptieren: %s""".formatted(
						db.getFiles().stream().map(datei -> datei.getAbsolutePath()).collect(Collectors.joining("   ")),
						akzeptiert));
			}
			if (akzeptieren) {
				event.acceptTransferModes(TransferMode.COPY);
				event.consume();
			}
		}
		if (event.getEventType().equals(DragEvent.DRAG_DROPPED)) {
			UMLProjekt projekt = ansicht.get().getProjekt();
			var importVerwaltung = projekt.getProgrammiersprache().getVerarbeitung();
			Dragboard db = event.getDragboard();
			if (db.hasFiles()
					&& pruefeDateiErweiterungen(db.getFiles(), importVerwaltung.getImportDateierweiterungen())) {
				event.consume();
				importiereAusDateien(db.getFiles());
			}
		}
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private boolean pruefeDateiErweiterungen(Collection<File> dateien, Iterable<ExtensionFilter> erweiterungenListe) {
		boolean akzeptieren = false;
		for (ExtensionFilter erweiterungen : erweiterungenListe) {
			for (String erweiterung : erweiterungen.getExtensions()) {
				akzeptieren |= !dateien.stream().filter(datei -> datei.getName().endsWith(erweiterung.replace("*", "")))
						.toList().isEmpty();
			}
		}
		return akzeptieren;
	}
	
	private void zeigeImportFehlerDialog(String beschreibung, Throwable fehler) {
		String titel = sprache.getText("importFehlerTitel", "Fehler beim Importieren");
		var text = new TextFlow(new Text(beschreibung));
		text.getStyleClass().add("dialog-text-warnung");
		text.setTextAlignment(TextAlignment.CENTER);
		if (fehler == null) {
			this.ansicht.get().getOverlayDialog().showNode(Type.ERROR, titel, text);
		} else {
			this.ansicht.get().getOverlayDialog()
					.showNode(Type.ERROR, titel, text, List.of(ButtonType.OK, new ButtonType("", ButtonData.HELP)))
					.thenAccept(buttonTyp -> {
						if (buttonTyp.getButtonData().equals(ButtonData.HELP)) {
							var hilfeDialog = new ExceptionDialog(fehler);
							FensterUtil.initialisiereElternFenster(ansicht.get().getTabPane().getScene().getWindow(),
									hilfeDialog);
							hilfeDialog.setResizable(true);
							hilfeDialog.getDialogPane().setExpanded(true);
							hilfeDialog.getDialogPane().getChildren().forEach(n -> {
								if (n instanceof Labeled l) {
									l.setWrapText(false);
								}
							});
							int breite = 1000;
							int hoehe = 550;
							hilfeDialog.setWidth(breite);
							hilfeDialog.setHeight(hoehe);
							FontIcon symbol = new FontIcon(BootstrapIcons.EXCLAMATION_OCTAGON_FILL);
							symbol.setIconSize(30);
							symbol.getStyleClass().add("fehler-icon");
							hilfeDialog.getDialogPane().setGraphic(symbol);
							Platform.runLater(() -> {
								hilfeDialog.setWidth(breite);
								hilfeDialog.setHeight(hoehe);
							});
							hilfeDialog.showAndWait();
						}
					});
			
		}
	}
	
	private boolean zeigeSpeichernUeberschreibenDialog(String datei) {
		String titel = sprache.getText("dateiUeberschreibenTitel", "Datei %cberschreiben?".formatted(Umlaute.ue));
		MessageFormat nachricht = new MessageFormat(sprache.getText("dateiUeberschreiben", """
				Die Datei '{0}' wurde seit dem letzten Speichervorgang m%cglicherweise ver%cndert oder gel%cscht.
				
				Soll die Datei mit dem aktuellen Projekt %cberschrieben werden?
				""".formatted(Umlaute.oe, Umlaute.ae, Umlaute.oe, Umlaute.ue)));
		String beschreibung = nachricht.format(new Object[] { datei });
		var text = new TextFlow(new Text(beschreibung));
		text.getStyleClass().add("dialog-text-warnung");
		Alert abfrage = new Alert(AlertType.WARNING);
		abfrage.setTitle(titel);
		abfrage.getDialogPane().setContent(text);
		abfrage.getButtonTypes().clear();
		abfrage.getButtonTypes().addAll(ButtonType.CANCEL, ButtonType.YES);
		
		FensterUtil.initialisiereElternFenster(ansicht.get().getTabPane().getScene().getWindow(), abfrage);
		Optional<ButtonType> buttonWahl = abfrage.showAndWait();
		if (buttonWahl.isPresent()) {
			return ButtonType.YES.equals(buttonWahl.get());
		} else {
			return false;
		}
	}
	
}