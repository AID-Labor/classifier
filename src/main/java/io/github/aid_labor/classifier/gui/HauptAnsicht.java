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
import java.text.MessageFormat;

import com.dlsc.gemsfx.DialogPane;
import com.dlsc.gemsfx.DialogPane.Type;
import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.gui.elemente.MenueLeisteKomponente;
import io.github.aid_labor.classifier.gui.elemente.RibbonKomponente;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


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
		
		var menueAnsicht = new MenueLeisteKomponente();
		setzeMenueAktionen(menueAnsicht);
		
		var ribbonAnsicht = new RibbonKomponente();
		setzeRibbonAktionen(ribbonAnsicht);
		
		var hauptInhalt = new BorderPane();
		hauptInhalt.setTop(new VBox(menueAnsicht.getMenueleiste(), ribbonAnsicht.getRibbon()));
		hauptInhalt.setCenter(projektAnsicht.getAnsicht());
		
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
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	// =====================================================================================
	// Beginn Menue
	
	private void setzeMenueAktionen(MenueLeisteKomponente menue) {
		// Menue Datei
		menue.getDateiNeu().setOnAction(this.controller::neuesProjektErzeugen);
		menue.getDateiSpeichern().setOnAction(this.controller::projektSpeichern);
		menue.getDateiSpeichernUnter().setOnAction(this.controller::projektSpeichernUnter);
		menue.getDateiOeffnen().setOnAction(this.controller::projektOeffnen);
		
		// Speichern updaten
		this.projektAnsicht.getAngezeigtesProjektProperty()
				.addListener((property, altesProjekt, gezeigtesProjekt) -> {
					if (gezeigtesProjekt != null) {
						menue.getDateiSpeichern().setDisable(false);
						menue.getDateiSpeichernUnter().setDisable(false);
					} else {
						menue.getDateiSpeichern().setDisable(true);
						menue.getDateiSpeichernUnter().setDisable(true);
					}
				});
		if (projektAnsicht.getAngezeigtesProjektProperty().get() == null) {
			NodeUtil.disable(menue.getDateiSpeichern(), menue.getDateiSpeichernUnter());
		}
		
		NodeUtil.disable(menue.getDateiLetzeOeffnen(),
				menue.getDateiSchliessen(), menue.getDateiAlleSpeichern(),
				menue.getDateiUmbenennen(), menue.getDateiImportieren());
		
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
		NodeUtil.disable(menue.getVollbild(), menue.getSymbolleisteAusblenden());
		
		// Menue Fenster
		NodeUtil.disable(menue.getMinimieren(), menue.getMaximieren(),
				menue.getVorherigerTab(), menue.getNaechsterTab());
		
		// Menue Einstellungen
		NodeUtil.disable(menue.getVoidAnzeigen(), menue.getTheme(), menue.getInfo());
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
				new StackPane(new EnhancedLabel(beschreibung)));
	}
}