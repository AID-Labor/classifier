/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
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
	
	private static final Logger log = Logger.getLogger(HauptAnsicht.class.getName());
	
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
		
		this.projektAnsicht.getAngezeigtesProjektProperty()
				.addListener((property, altesProjekt, gezeigtesProjekt) -> {
					// TODO update speichersymbol Bindung
					if (gezeigtesProjekt != null) {
						ribbonAnsicht.getSpeichern().setDisable(false);
						NodeUtil.setzeHervorhebung(gezeigtesProjekt.istGespeichertProperty(), ribbonAnsicht.getSpeichern());
						gezeigtesProjekt.istGespeichertProperty().addListener(
								(gespeichertProperty, alterWert, istGespeichert) -> {
									NodeUtil.setzeHervorhebung(spracheGesetzt, ribbonAnsicht.getSpeichern());
								});
					} else {
						ribbonAnsicht.getSpeichern().setDisable(true);
					}
				});
		if(projektAnsicht.getAngezeigtesProjektProperty().get() == null) {
			NodeUtil.disable(ribbonAnsicht.getSpeichern());
		}
		
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
		
		NodeUtil.disable(menue.getDateiOeffnen(), menue.getDateiLetzeOeffnen(),
				menue.getDateiSchliessen(), menue.getDateiSpeichern(),
				menue.getDateiAlleSpeichern(), menue.getDateiSpeichernUnter(),
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
		
		NodeUtil.disable(ribbon.getOeffnen(), ribbon.getImportieren(), ribbon.getScreenshot(),
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
		
		ribbon.getNeu().setOnAction(controller::neuesProjektErzeugen);
		ribbon.getSpeichern().setOnAction(controller::projektSpeichern);
	}
	
	// Ende Ribbon
	// =====================================================================================
	// Beginn Projektansicht
	
	void zeigeProjekt(UMLProjekt projekt) {
		this.projektAnsicht.zeigeProjekt(projekt);
	}
	
}