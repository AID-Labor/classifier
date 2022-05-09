/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.Umlaute.OE;
import static io.github.aid_labor.classifier.basis.Umlaute.sz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import io.github.aid_labor.classifier.basis.Ressourcen;
import io.github.aid_labor.classifier.basis.SprachUtil;
import io.github.aid_labor.classifier.basis.Sprache;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;


public class HauptAnsicht implements View {
	
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
	
	private final BorderPane wurzel;
	private final HauptController controller;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public HauptAnsicht() {
		this.wurzel = new BorderPane();
		this.controller = new HauptController(this);
		this.sprache = new Sprache();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "HauptAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		var menue = erstelleMenueLeiste();
		wurzel.setTop(menue);
		erstelleRibbon();
		erstelleProjektAnsicht();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public BorderPane getWurzelknoten() {
		return wurzel;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	Locale l;
	
	private MenuBar erstelleMenueLeiste() {
		Menu dateiMenue = erstelleDateiMenue();
		
		MenuBar menuebar = new MenuBar(dateiMenue);
		
		return menuebar;
	}
	
	private Menu erstelleDateiMenue() {
		Menu dateiMenue = SprachUtil.bindText(new Menu(), sprache, "dateiMenue", "Datei");
		MenuItem dateiNeu = SprachUtil.bindText(new MenuItem(), sprache, "neu", "Neu...");
		MenuItem dateiOeffnen = SprachUtil.bindText(new MenuItem(), sprache, "oeffnen",
				"%cffnen...".formatted(OE));
		Menu dateiLetzeOeffnen = SprachUtil.bindText(new Menu(), sprache, "letzteOeffnen",
				"Letzte Dateien");
		MenuItem dateiSchliessen = SprachUtil.bindText(new MenuItem(), sprache, "schliessen",
				"Schlie%cen".formatted(sz));
		MenuItem dateiSpeichern = SprachUtil.bindText(new MenuItem(), sprache, "speichern",
				"Speichern");
		MenuItem dateiAlleSpeichern = SprachUtil.bindText(new MenuItem(), sprache,
				"allesSpeichern", "Alles Speichern");
		MenuItem dateiSpeichernUnter = SprachUtil.bindText(new MenuItem(), sprache,
				"speichernUnter", "Speichern unter...");
		MenuItem dateiUmbenennen = SprachUtil.bindText(new MenuItem(), sprache, "umbenennen",
				"Umbenennen...");
		MenuItem dateiImportieren = SprachUtil.bindText(new MenuItem(), sprache, "importieren",
				"Importieren...");
		Menu dateiExportieren = SprachUtil.bindText(new Menu(), sprache, "exportieren",
				"Exportieren");
		
		dateiMenue.getItems().addAll(dateiNeu, dateiOeffnen, dateiLetzeOeffnen,
				dateiSchliessen, dateiSpeichern, dateiAlleSpeichern, dateiSpeichernUnter,
				dateiUmbenennen, dateiImportieren, dateiExportieren);
		
		return dateiMenue;
	}
	
	private void erstelleRibbon() {
		// TODO Auto-generated method stub
		
	}
	
	private void erstelleProjektAnsicht() {
		Button b = new Button("English");
		l = Locale.ENGLISH;
		b.setOnAction(e -> {
			try {
				SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "HauptAnsicht", l);
				l = l.equals(Locale.ENGLISH) ? Locale.GERMAN : Locale.ENGLISH;
				b.setText(l.getDisplayLanguage(l));
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		});
		this.wurzel.setBottom(b);
		try (BufferedReader ein = new BufferedReader(
				new InputStreamReader(Ressourcen.get().LIZENZ_DATEI.oeffneStream()))) {
			String lizenz = ein.lines().collect(Collectors.joining("\n"));
			this.wurzel.setCenter(new Label(lizenz));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}