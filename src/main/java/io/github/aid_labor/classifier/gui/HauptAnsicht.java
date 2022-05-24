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
import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.elemente.MenueLeisteKomponente;
import io.github.aid_labor.classifier.gui.elemente.RibbonKomponente;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.collections.SetChangeListener;
import javafx.scene.Parent;
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
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public HauptAnsicht(ProgrammDetails programm) {
		this.wurzel = new StackPane();
		this.sprache = new Sprache();
		this.overlayDialog = new DialogPane();
		this.programm = programm;
		this.controller = new HauptKontrolle(this, sprache);
		this.projektAnsicht = new ProjekteAnsicht(overlayDialog, programm);
		
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
				// TODO Auto-generated method stub
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
		NodeUtil.disable(menue.getDateiImportieren(),
				menue.getExportierenBild(), menue.getExportierenQuellcode());
		
		menue.getDateiNeu().setOnAction(this.controller::neuesProjektErzeugen);
		menue.getDateiOeffnen().setOnAction(this.controller::projektOeffnen);
		
		updateLetzteDateien(menue.getDateiLetzeOeffnen());
		
		// Letzte Dateien Updaten
		Einstellungen.getBenutzerdefiniert().letzteDateien
				.addListener((SetChangeListener<? super DatumWrapper<Path>>) aenderung -> {
					updateLetzteDateien(menue.getDateiLetzeOeffnen());
				});
		
		menue.getDateiSchliessen()
				.setOnAction(e -> this.projektAnsicht.angezeigtesProjektSchliessen());
		menue.getDateiSpeichern().setOnAction(this.controller::projektSpeichern);
		menue.getDateiAlleSpeichern().setOnAction(e -> this.projektAnsicht.allesSpeichern());
		menue.getDateiSpeichernUnter().setOnAction(this.controller::projektSpeichernUnter);
		menue.getDateiUmbenennen().setOnAction(this.controller::projektUmbenennen);
		
		// Dateimenue updaten
		this.projektAnsicht.getAngezeigtesProjektProperty()
				.addListener((property, altesProjekt, gezeigtesProjekt) -> {
					boolean inaktiv = gezeigtesProjekt == null;
					menue.getDateiSpeichern().setDisable(inaktiv);
					menue.getDateiSpeichernUnter().setDisable(inaktiv);
					menue.getDateiSchliessen().setDisable(inaktiv);
					menue.getDateiAlleSpeichern().setDisable(inaktiv);
					menue.getDateiUmbenennen().setDisable(inaktiv);
				});
		if (projektAnsicht.getAngezeigtesProjektProperty().get() == null) {
			NodeUtil.disable(menue.getDateiSpeichern(), menue.getDateiSpeichernUnter(),
					menue.getDateiAlleSpeichern(), menue.getDateiSchliessen(),
					menue.getDateiUmbenennen());
		}
		
		// Menue Bearbeiten
		NodeUtil.disable(menue.getRueckgaengig(), menue.getWiederholen(), menue.getKopieren(),
				menue.getEinfuegen(), menue.getLoeschen());
		
		// Menue Einfuegen
		NodeUtil.disable(menue.getKlasseEinfuegen(), menue.getInterfaceEinfuegen(),
				menue.getEnumEinfuegen(), menue.getVererbungEinfuegen(),
				menue.getAssoziationEinfuegen(), menue.getKommentarEinfuegen());
		
		// Menue Anordnen
		NodeUtil.disable(menue.getAnordnenNachVorne(), menue.getAnordnenNachGanzVorne(),
				menue.getAnordnenNachHinten(), menue.getAnordnenNachGanzHinten());
		
		// Menue Darstellung
		NodeUtil.disable(menue.getDarstellungGroesser(), menue.getDarstellungKleiner(),
				menue.getDarstellungOriginalgroesse());
		
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
		NodeUtil.disable(menue.getMinimieren(), menue.getMaximieren(),
				menue.getVorherigerTab(), menue.getNaechsterTab());
		
		// Menue Einstellungen
		NodeUtil.disable(menue.getVoidAnzeigen(), menue.getTheme(), menue.getInfo());
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
	
	// Ende Menue
	// =====================================================================================
	// Beginn Ribbon
	
	private void setzeRibbonAktionen(RibbonKomponente ribbon) {
		ribbon.getNeu().setOnAction(controller::neuesProjektErzeugen);
		ribbon.getSpeichern().setOnAction(controller::projektSpeichern);
		ribbon.getSpeichernSchnellzugriff().setOnAction(controller::projektSpeichern);
		
		// Speichersymbol updaten
		this.projektAnsicht.getAngezeigtesProjektProperty()
				.addListener((property, altesProjekt, gezeigtesProjekt) -> {
					if (gezeigtesProjekt != null) {
						ribbon.getSpeichern().setDisable(false);
						ribbon.getSpeichernSchnellzugriff().setDisable(false);
						NodeUtil.setzeHervorhebung(
								gezeigtesProjekt.istGespeichertProperty().not().get(),
								ribbon.getSpeichern(), ribbon.getSpeichernSchnellzugriff());
						gezeigtesProjekt.istGespeichertProperty().addListener(
								(gespeichertProperty, alterWert, istGespeichert) -> {
									NodeUtil.setzeHervorhebung(!istGespeichert,
											ribbon.getSpeichern(),
											ribbon.getSpeichernSchnellzugriff());
								});
					} else {
						NodeUtil.setzeHervorhebung(false, ribbon.getSpeichern(),
								ribbon.getSpeichernSchnellzugriff());
						ribbon.getSpeichern().setDisable(true);
						ribbon.getSpeichernSchnellzugriff().setDisable(true);
					}
				});
		if (projektAnsicht.getAngezeigtesProjektProperty().get() == null) {
			NodeUtil.disable(ribbon.getSpeichern(), ribbon.getSpeichernSchnellzugriff());
		}
		
		ribbon.getOeffnen().setOnAction(this.controller::projektOeffnen);
		
		NodeUtil.disable(ribbon.getImportieren(), ribbon.getScreenshot(),
				ribbon.getExportieren());
		NodeUtil.disable(ribbon.getKopieren(), ribbon.getEinfuegen(), ribbon.getLoeschen(),
				ribbon.getRueckgaengig(), ribbon.getWiederholen());
		NodeUtil.disable(ribbon.getAnordnenNachVorne(), ribbon.getAnordnenNachGanzVorne(),
				ribbon.getAnordnenNachHinten(),
				ribbon.getAnordnenNachGanzHinten());
		NodeUtil.disable(ribbon.getNeueKlasse(), ribbon.getNeuesInterface(),
				ribbon.getNeueEnumeration());
		NodeUtil.disable(ribbon.getVererbung(), ribbon.getAssoziation());
		NodeUtil.disable(ribbon.getKommentar());
		NodeUtil.disable(ribbon.getZoomGroesser(), ribbon.getZoomKleiner(),
				ribbon.getZoomOriginalgroesse());
		NodeUtil.disable(ribbon.getSpeichernSchnellzugriff(),
				ribbon.getRueckgaengigSchnellzugriff(),
				ribbon.getWiederholenSchnellzugriff());
		
	}
	
	// Ende Ribbon
	// =====================================================================================
	// Beginn Projektansicht
	
	void zeigeProjekt(UMLProjekt projekt) {
		this.projektAnsicht.zeigeProjekt(projekt);
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
	
	void zeigeOffnenWarnungDialog(File datei) {
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