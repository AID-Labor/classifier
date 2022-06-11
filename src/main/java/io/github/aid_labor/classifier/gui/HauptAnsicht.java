/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.OE;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ae;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.oe;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ue;

import java.io.File;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.DialogPane.Type;
import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.DatumWrapper;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.projekt.Projekt;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.elemente.MenueLeisteKomponente;
import io.github.aid_labor.classifier.gui.elemente.RibbonKomponente;
import io.github.aid_labor.classifier.gui.util.FensterUtil;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import javafx.application.HostServices;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.SetChangeListener;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;


public class HauptAnsicht {
	
//	private static final Logger log = Logger.getLogger(HauptAnsicht.class.getName());

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
	
	private final Pane wurzel;
	private final HauptKontrolle controller;
	private final Sprache sprache;
	private final ProjekteAnsicht projektAnsicht;
	private final DialogPane overlayDialog;
	private final ProgrammDetails programm;
	private final RibbonKomponente ribbonKomponente;
	private final HostServices rechnerService;
	private final BooleanBinding hatKeinProjekt;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public HauptAnsicht(ProgrammDetails programm, HostServices rechnerService) {
		this.wurzel = new StackPane();
		this.sprache = new Sprache();
		this.overlayDialog = new DialogPane();
		this.programm = programm;
		this.controller = new HauptKontrolle(this, sprache);
		this.projektAnsicht = new ProjekteAnsicht(overlayDialog, programm);
		this.rechnerService = rechnerService;
		this.hatKeinProjekt = projektAnsicht.getAngezeigtesProjektProperty().isNull();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "HauptAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.overlayDialog.setConverter(new StringConverter<ButtonType>() {
			@Override
			public String toString(ButtonType buttonTyp) {
				return switch (buttonTyp.getButtonData()) {
					case APPLY -> sprache.getText("APPLY", "Anwenden");
					case BACK_PREVIOUS ->
						sprache.getText("BACK_PREVIOUS", "zur%cck".formatted(ue));
					case CANCEL_CLOSE -> sprache.getText("CANCEL_CLOSE", "Abbrechen");
					case FINISH -> sprache.getText("FINISH", "Beenden");
					case HELP -> sprache.getText("HELP", "Hilfe");
					case HELP_2 -> sprache.getText("HELP_2", "Hilfe");
					case NEXT_FORWARD -> sprache.getText("NEXT_FORWARD", "Weiter");
					case NO -> sprache.getText("NO", "Nein");
					case OK_DONE -> sprache.getText("OK_DONE", "Ok");
					case YES -> sprache.getText("YES", "Ja");
					case BIG_GAP -> "";
					case LEFT -> "";
					case OTHER -> "";
					case RIGHT -> "";
					case SMALL_GAP -> "";
					default -> null;
				};
			}
			
			@Override
			public ButtonType fromString(String string) {
				return null;
			}
		});
		
		var menueAnsicht = new MenueLeisteKomponente();
		setzeMenueAktionen(menueAnsicht);
		
		ribbonKomponente = new RibbonKomponente();
		setzeRibbonAktionen(ribbonKomponente);
		
		var hauptInhalt = new BorderPane();
		hauptInhalt
				.setTop(new VBox(menueAnsicht.getMenueleiste(), ribbonKomponente.getRibbon()));
		hauptInhalt.setCenter(projektAnsicht.getAnsicht());
		
		// TODO Datei(en) oeffnen!!!
		hauptInhalt.setOnDragDetected(this.controller::projektOeffnen);
		hauptInhalt.setOnDragDone(this.controller::projektOeffnen);
		hauptInhalt.setOnDragEntered(this.controller::projektOeffnen);
		hauptInhalt.setOnDragExited(this.controller::projektOeffnen);
		hauptInhalt.setOnDragOver(this.controller::projektOeffnen);
		hauptInhalt.setOnDragDropped(this.controller::projektOeffnen);
		
		wurzel.getChildren().add(hauptInhalt);
		wurzel.getChildren().add(overlayDialog);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public Parent getWurzelknoten() {
		return wurzel;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	protected DialogPane getOverlayDialog() {
		return this.overlayDialog;
	}
	
	public HostServices getRechnerService() {
		return rechnerService;
	}
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	ProjekteAnsicht getProjektAnsicht() {
		return projektAnsicht;
	}
	
	public ProgrammDetails getProgrammDetails() {
		return programm;
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public void oeffneDateien(Iterable<File> dateien) {
		for (File datei : dateien) {
			this.controller.projektOeffnen(datei);
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	// =====================================================================================
	// Beginn Menue
	
	private void setzeMenueAktionen(MenueLeisteKomponente menue) {
		// Menue Datei
		NodeUtil.deaktivieren(menue.getDateiImportieren(),
				menue.getExportierenBild(), menue.getExportierenQuellcode());
		
		menue.getDateiNeu().setOnAction(this.controller::neuesProjektErzeugen);
		menue.getDateiOeffnen().setOnAction(this.controller::projektOeffnen);
		
		menue.getDateiSchliessen()
				.setOnAction(e -> this.projektAnsicht.angezeigtesProjektSchliessen());
		menue.getDateiSpeichern().setOnAction(this.controller::projektSpeichern);
		menue.getDateiAlleSpeichern().setOnAction(e -> this.projektAnsicht.allesSpeichern());
		menue.getDateiSpeichernUnter().setOnAction(this.controller::projektSpeichernUnter);
		menue.getDateiUmbenennen().setOnAction(this.controller::projektUmbenennen);
		
		// Menue Bearbeiten
		var aktuellesProjekt = this.projektAnsicht.getAngezeigtesProjektProperty().get();
		updateRueckgaengigWiederholen(menue, aktuellesProjekt);
		NodeUtil.deaktivieren(menue.getKopieren(), menue.getEinfuegen(), menue.getLoeschen());
		
		// Menue Einfuegen
		menue.getKlasseEinfuegen()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Klasse));
		menue.getAbstrakteKlasseEinfuegen()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.AbstrakteKlasse));
		menue.getInterfaceEinfuegen()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Interface));
		menue.getEnumEinfuegen()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Enumeration));
		
		NodeUtil.deaktivieren(menue.getKlasseEinfuegen(), menue.getInterfaceEinfuegen(),
				menue.getEnumEinfuegen(), menue.getVererbungEinfuegen(),
				menue.getAssoziationEinfuegen(), menue.getKommentarEinfuegen());
		
		// Menue Anordnen
		NodeUtil.deaktivieren(menue.getAnordnenNachVorne(), menue.getAnordnenNachGanzVorne(),
				menue.getAnordnenNachHinten(), menue.getAnordnenNachGanzHinten());
		
		// Menue Darstellung
		menue.getDarstellungGroesser().setOnAction(this.controller::zoomeGroesser);
		menue.getDarstellungKleiner().setOnAction(this.controller::zoomeKleiner);
		menue.getDarstellungOriginalgroesse().setOnAction(this.controller::resetZoom);
		
		menue.getVollbild().setOnAction(e -> {
			if (wurzel.getScene().getWindow() instanceof Stage fenster) {
				fenster.setFullScreen(!fenster.isFullScreen());
				menue.getVollbild().setSelected(fenster.isFullScreen());
			}
		});
		
		menue.getSymbolleisteAusblenden().setOnAction(e -> {
			ribbonKomponente.getRibbon().setVisible(!ribbonKomponente.getRibbon().isVisible());
			if (!ribbonKomponente.getRibbon().isVisible()) {
				ribbonKomponente.getRibbon().setMaxHeight(0);
			} else {
				ribbonKomponente.getRibbon().setMaxHeight(Double.MAX_VALUE);
			}
			menue.getSymbolleisteAusblenden()
					.setSelected(!ribbonKomponente.getRibbon().isVisible());
		});
		
		// Menue Fenster
		menue.getVorherigerTab().setOnAction(e -> this.projektAnsicht.vorherigerTab());
		menue.getNaechsterTab().setOnAction(e -> this.projektAnsicht.naechsterTab());
		
		// Menue Einstellungen
		NodeUtil.deaktivieren(menue.getVoidAnzeigen(), menue.getTheme());
		menue.getInfo().setOnAction(e -> {
			var info = new InfoAnsicht(programm, rechnerService);
			FensterUtil.initialisiereElternFenster(wurzel.getScene().getWindow(), info);
			info.show();
		});
		
		menue.getKonfigurationsordnerOeffnen()
				.setOnAction(this.controller::konigurationsordnerOeffnen);
		menue.getKonfigurationsordnerBereinigen()
				.setOnAction(this.controller::konigurationsordnerBereinigen);
		
		setzeMenueBindungen(menue);
	}
	
	private void setzeMenueBindungen(MenueLeisteKomponente menue) {
		// Letzte Dateien Updaten
		updateLetzteDateien(menue.getDateiLetzeOeffnen());
		Einstellungen.getBenutzerdefiniert().letzteDateien
				.addListener((SetChangeListener<? super DatumWrapper<Path>>) aenderung -> {
					updateLetzteDateien(menue.getDateiLetzeOeffnen());
				});
		
		// Dateimenue updaten
		menue.getDateiSpeichern().disableProperty().bind(hatKeinProjekt);
		menue.getDateiSpeichernUnter().disableProperty().bind(hatKeinProjekt);
		menue.getDateiSchliessen().disableProperty().bind(hatKeinProjekt);
		menue.getDateiAlleSpeichern().disableProperty().bind(hatKeinProjekt);
		menue.getDateiUmbenennen().disableProperty().bind(hatKeinProjekt);
		
		// Menue Bearbeiten
		this.projektAnsicht.getAngezeigtesProjektProperty().addListener((p, alt, projekt) -> {
			updateRueckgaengigWiederholen(menue, projekt);
		});
		
		// Menue Einfuegen
		menue.getKlasseEinfuegen().disableProperty().bind(hatKeinProjekt);
		menue.getAbstrakteKlasseEinfuegen().disableProperty().bind(hatKeinProjekt);
		menue.getInterfaceEinfuegen().disableProperty().bind(hatKeinProjekt);
		menue.getEnumEinfuegen().disableProperty().bind(hatKeinProjekt);
		
		// Menue Anordnen
		// TODO
		
		// Menue Darstellung
		menue.getDarstellungGroesser().disableProperty().bind(hatKeinProjekt);
		menue.getDarstellungKleiner().disableProperty().bind(hatKeinProjekt);
		menue.getDarstellungOriginalgroesse().disableProperty().bind(hatKeinProjekt);
		
		updateZoomKleinerButton(menue, this.projektAnsicht.getAnzeige().get());
		this.projektAnsicht.getAnzeige().addListener((property, alteAnzeige, neueAnzeige) -> {
			updateZoomKleinerButton(menue, neueAnzeige);
		});
		
		// Szene fuer Vollbild ueberwachen
		wurzel.sceneProperty().addListener((property, alteSzene, neueSzene) -> {
			menue.getVollbild().selectedProperty().unbind();
			if (neueSzene.getWindow() instanceof Stage fenster) {
				menue.getVollbild().selectedProperty().bind(fenster.fullScreenProperty());
			}
		});
		
		// Menue Fenster
		menue.getVorherigerTab().disableProperty().bind(hatKeinProjekt);
		menue.getNaechsterTab().disableProperty().bind(hatKeinProjekt);
	}
	
	private void updateLetzteDateien(Menu menueLetzteDateien) {
		menueLetzteDateien.getItems().clear();
		for (DatumWrapper<Path> datei : Einstellungen
				.getBenutzerdefiniert().letzteDateien) {
			MenuItem menueEintrag = new MenuItem(datei.getElement().toString());
			menueEintrag.setOnAction(e -> {
				this.controller.projektOeffnen(datei.getElement().toFile());
			});
			menueLetzteDateien.getItems().add(0, menueEintrag);
		}
	}
	
	private void updateRueckgaengigWiederholen(MenueLeisteKomponente menue, Projekt projekt) {
		menue.getRueckgaengig().disableProperty().unbind();
		menue.getWiederholen().disableProperty().unbind();
		
		if (projekt != null) {
			menue.getRueckgaengig().setOnAction(e -> projekt.macheRueckgaengig());
			menue.getWiederholen().setOnAction(e -> projekt.wiederhole());
			menue.getRueckgaengig().disableProperty()
					.bind(projekt.kannRueckgaengigGemachtWerdenProperty().not());
			menue.getWiederholen().disableProperty()
					.bind(projekt.kannWiederholenProperty().not());
		} else {
			menue.getRueckgaengig().setOnAction(null);
			menue.getWiederholen().setOnAction(null);
			menue.getRueckgaengig().disableProperty().set(true);
			menue.getWiederholen().disableProperty().set(true);
		}
	}
	
	private void updateZoomKleinerButton(MenueLeisteKomponente menue,
			ProjektAnsicht projektAnsicht) {
		menue.getDarstellungKleiner().disableProperty().unbind();
		if (projektAnsicht != null) {
			menue.getDarstellungKleiner().disableProperty()
					.bind(projektAnsicht.kannKleinerZoomenProperty().not());
		} else {
			menue.getDarstellungKleiner().setDisable(true);
		}
	}
	
	// Ende Menue
	// =====================================================================================
	// Beginn Ribbon
	
	private void setzeRibbonAktionen(RibbonKomponente ribbon) {
		ribbon.getNeu().setOnAction(controller::neuesProjektErzeugen);
		ribbon.getSpeichern().setOnAction(controller::projektSpeichern);
		ribbon.getSpeichernSchnellzugriff().setOnAction(controller::projektSpeichern);
		ribbon.getOeffnen().setOnAction(this.controller::projektOeffnen);
		
		ribbon.getNeueKlasse()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Klasse));
		ribbon.getNeueAbstrakteKlasse()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.AbstrakteKlasse));
		ribbon.getNeuesInterface()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Interface));
		ribbon.getNeueEnumeration()
				.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Enumeration));
		
		NodeUtil.deaktivieren(ribbon.getImportieren(), ribbon.getScreenshot(),
				ribbon.getExportieren());
//		NodeUtil.deaktivieren(ribbon.getKopieren(), ribbon.getEinfuegen());
		
		ribbon.getLoeschen()
				.setOnAction(e -> projektAnsicht.getAnzeige().get().entferneAuswahl());
		
		NodeUtil.deaktivieren(ribbon.getAnordnenNachVorne(), ribbon.getAnordnenNachGanzVorne(),
				ribbon.getAnordnenNachHinten(), ribbon.getAnordnenNachGanzHinten());
		
		NodeUtil.deaktivieren(ribbon.getVererbung(), ribbon.getAssoziation());
		NodeUtil.deaktivieren(ribbon.getKommentar());
		
		ribbon.getZoomGroesser().setOnAction(this.controller::zoomeGroesser);
		ribbon.getZoomKleiner().setOnAction(this.controller::zoomeKleiner);
		ribbon.getZoomOriginalgroesse().setOnAction(this.controller::resetZoom);
		updateZoomKleinerButton(ribbon, projektAnsicht.getAnzeige().get());
		
		this.projektAnsicht.getAnzeige().addListener((property, alteAnzeige, neueAnzeige) -> {
			updateZoomKleinerButton(ribbon, neueAnzeige);
		});
		
		setzeRibbonBindungen(ribbon);
	}
	
	private void setzeRibbonBindungen(RibbonKomponente ribbon) {
		// Buttons updaten
		ribbon.getSpeichern().disableProperty().bind(hatKeinProjekt);
		ribbon.getSpeichernSchnellzugriff().disableProperty().bind(hatKeinProjekt);
		ribbon.getNeueKlasse().disableProperty().bind(hatKeinProjekt);
		ribbon.getNeueAbstrakteKlasse().disableProperty().bind(hatKeinProjekt);
		ribbon.getNeuesInterface().disableProperty().bind(hatKeinProjekt);
		ribbon.getNeueEnumeration().disableProperty().bind(hatKeinProjekt);
		
		updateRueckgaengigWiederholen(ribbon,
				projektAnsicht.getAngezeigtesProjektProperty().get());
		this.projektAnsicht.getAngezeigtesProjektProperty().addListener((p, alt, projekt) -> {
			updateSpeichernHervorhebeung(ribbon, projekt);
			updateRueckgaengigWiederholen(ribbon, projekt);
		});
		
		updateSelektionButtons(ribbon, projektAnsicht.getAnzeige().get());
		this.projektAnsicht.getAnzeige().addListener((__, alt, neueAnzeige) -> {
			updateSelektionButtons(ribbon, neueAnzeige);
		});
		
		ribbon.getZoomGroesser().disableProperty().bind(hatKeinProjekt);
		ribbon.getZoomKleiner().disableProperty().bind(hatKeinProjekt);
		ribbon.getZoomOriginalgroesse().disableProperty().bind(hatKeinProjekt);
	}
	
	private void updateSpeichernHervorhebeung(RibbonKomponente ribbon, UMLProjekt projekt) {
		if (projekt != null) {
			NodeUtil.setzeHervorhebung(
					projekt.istGespeichertProperty().not().get(),
					ribbon.getSpeichern(), ribbon.getSpeichernSchnellzugriff());
			projekt.istGespeichertProperty().addListener(
					(gespeichertProperty, alterWert, istGespeichert) -> {
						NodeUtil.setzeHervorhebung(!istGespeichert,
								ribbon.getSpeichern(),
								ribbon.getSpeichernSchnellzugriff());
					});
		} else {
			NodeUtil.setzeHervorhebung(false, ribbon.getSpeichern(),
					ribbon.getSpeichernSchnellzugriff());
		}
	}
	
	private void updateRueckgaengigWiederholen(RibbonKomponente ribbon, Projekt projekt) {
		ribbon.getRueckgaengig().disableProperty().unbind();
		ribbon.getRueckgaengigSchnellzugriff().disableProperty().unbind();
		ribbon.getWiederholen().disableProperty().unbind();
		ribbon.getWiederholenSchnellzugriff().disableProperty().unbind();
		
		if (projekt != null) {
			ribbon.getRueckgaengig().setOnAction(e -> projekt.macheRueckgaengig());
			ribbon.getRueckgaengigSchnellzugriff()
					.setOnAction(e -> projekt.macheRueckgaengig());
			ribbon.getWiederholen().setOnAction(e -> projekt.wiederhole());
			ribbon.getWiederholenSchnellzugriff().setOnAction(e -> projekt.wiederhole());
			ribbon.getRueckgaengig().disableProperty()
					.bind(projekt.kannRueckgaengigGemachtWerdenProperty().not());
			ribbon.getRueckgaengigSchnellzugriff().disableProperty().bind(
					projekt.kannRueckgaengigGemachtWerdenProperty().not());
			ribbon.getWiederholen().disableProperty()
					.bind(projekt.kannWiederholenProperty().not());
			ribbon.getWiederholenSchnellzugriff().disableProperty()
					.bind(projekt.kannWiederholenProperty().not());
		} else {
			ribbon.getRueckgaengig().setOnAction(null);
			ribbon.getRueckgaengigSchnellzugriff().setOnAction(null);
			ribbon.getWiederholen().setOnAction(null);
			ribbon.getWiederholenSchnellzugriff().setOnAction(null);
			ribbon.getRueckgaengig().disableProperty().set(true);
			ribbon.getRueckgaengigSchnellzugriff().disableProperty().set(true);
			ribbon.getWiederholen().disableProperty().set(true);
			ribbon.getWiederholenSchnellzugriff().disableProperty().set(true);
		}
	}
	
	private void updateZoomKleinerButton(RibbonKomponente ribbon,
			ProjektAnsicht projektAnsicht) {
		ribbon.getZoomKleiner().disableProperty().unbind();
		if (projektAnsicht != null) {
			ribbon.getZoomKleiner().disableProperty()
					.bind(projektAnsicht.kannKleinerZoomenProperty().not());
		} else {
			ribbon.getZoomKleiner().setDisable(true);
		}
	}
	
	private void updateSelektionButtons(RibbonKomponente ribbon, ProjektAnsicht neueAnzeige) {
		Button[] buttons = {
			ribbon.getKopieren(),
			ribbon.getEinfuegen(),
			ribbon.getLoeschen(),
//			ribbon.getAnordnenNachVorne(),
//			ribbon.getAnordnenNachGanzVorne(),
//			ribbon.getAnordnenNachHinten(),
//			ribbon.getAnordnenNachGanzHinten()
		};
		for (var b : buttons) {
			b.disableProperty().unbind();
			if (neueAnzeige == null) {
				b.setDisable(true);
			} else {
				b.disableProperty().bind(neueAnzeige.hatSelektionProperty());
			}
		}
	}
	
	// Ende Ribbon
	// =====================================================================================
	// Beginn Projektansicht
	
	void zeigeProjekt(UMLProjekt projekt) {
		this.projektAnsicht.zeigeProjekt(projekt);
	}
	
	private void erzeugeKlassifizierer(KlassifiziererTyp typ) {
		this.projektAnsicht.legeNeuenKlassifiziererAn(typ);
	}
	
	// =====================================================================================
	// Dialog
	
	void zeigeAnlegenFehlerDialog(String beschreibung) {
		String titel = sprache.getText("anlegenFehlerTitel",
				"Fehler beim Anlegen eines Projektes");
		this.overlayDialog.showNode(Type.ERROR, titel,
				new StackPane(new EnhancedLabel(beschreibung)));
	}
	
	void zeigeOeffnenFehlerDialog(File datei) {
		String dialogTitel = sprache.getText("oeffnenFehlerTitel",
				"Fehler beim %cffnen".formatted(Umlaute.OE));
		MessageFormat nachricht = new MessageFormat(sprache.getText("oeffnenFehler", """
				Die Datei {0} konnte nicht gelesen werden. M%cglicherweise ist die \
				Datei besch%cdigt oder keine g%cltige Projektdatei."""
				.formatted(oe, ae, ue)));
		String beschreibung = nachricht.format(new Object[] { datei.getName() });
		
		this.overlayDialog.showNode(Type.ERROR, dialogTitel,
				new StackPane(new EnhancedLabel(beschreibung)));
	}
	
	void zeigeOeffnenWarnungDialog(File datei) {
		MessageFormat titelformat = new MessageFormat(sprache.getText(
				"oeffnenWarnungTitel", "Die Datei {0} wurde nicht ge%cffnet"
						.formatted(oe)));
		String dialogTitel = titelformat.format(new Object[] { datei.getName() });
		String beschreibung = sprache.getText("oeffnenWarnung",
				"""
						Die Datei ist bereits ge%cffnet. Das mehrfache %cffnen einer Datei \
						wird nicht unterst%ctzt."""
						.formatted(oe, OE, ue));
		this.overlayDialog.showNode(Type.WARNING, dialogTitel,
				new StackPane(new EnhancedLabel(beschreibung)), false, List.of(ButtonType.OK));
	}
	
}