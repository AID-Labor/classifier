/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.Ressourcen;
import io.github.aid_labor.classifier.basis.SprachUtil;
import io.github.aid_labor.classifier.basis.Sprache;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


public class ProjekteAnsicht {
	private static final Logger log = Logger.getLogger(ProjekteAnsicht.class.getName());
	
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
	
	private final ReadOnlyObjectWrapper<UMLProjekt> angezeigtesProjekt;
	private final ObservableMap<Object, UMLProjekt> projekte;
	private final TabPane tabAnsicht;
	private final DialogPane overlayDialog;
	private final Sprache sprache;
	private final ProgrammDetails programm;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjekteAnsicht(DialogPane overlayDialog, ProgrammDetails programm) {
		this.projekte = FXCollections.observableHashMap();
		this.overlayDialog = overlayDialog;
		this.sprache = new Sprache();
		this.programm = programm;
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "ProjektAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.angezeigtesProjekt = new ReadOnlyObjectWrapper<>();
		this.tabAnsicht = new TabPane();
		
		this.tabAnsicht.getSelectionModel().selectedItemProperty()
				.addListener((itemProperty, alteWahl, neueWahl) -> {
					log.finest(() -> "neuer Tab ausgewaehlt: %s"
							.formatted(neueWahl == null ? "null" : neueWahl.getText()));
					if (neueWahl instanceof ProjektAnsicht tab) {
						angezeigtesProjekt.set(tab.getProjekt());
					}
				});
		
		angezeigtesProjekt.addListener((property, alt, neu) -> {
			String nameAlt = alt == null ? "null" : alt.getName();
			String nameNeu = neu == null ? "null" : neu.getName();
			log.finest(() -> "aktuelles Projekt geaendert -> alt: %s - neu: %s"
					.formatted(nameAlt, nameNeu));
		});
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public ReadOnlyObjectProperty<UMLProjekt> getAngezeigtesProjektProperty() {
		return angezeigtesProjekt.getReadOnlyProperty();
	}
	
	public Parent getAnsicht() {
		return this.tabAnsicht;
	}
	
	public void zeigeProjekt(UMLProjekt projekt) {
		ProjektAnsicht tab = new ProjektAnsicht(projekt, sprache, overlayDialog, programm);
		
		this.tabAnsicht.getTabs().add(tab);
		this.tabAnsicht.getSelectionModel().select(tab);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	protected DialogPane getOverlayDialog() {
		return this.overlayDialog;
	}
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public boolean angezeigtesProjektSpeichern() {
		var tab = this.tabAnsicht.getSelectionModel().getSelectedItem();
		if(tab instanceof ProjektAnsicht pa) {
			return pa.projektSpeichern();
		} else {
			return false;
		}
	}
	
	public boolean angezeigtesProjektSpeicherUnter() {
		var tab = this.tabAnsicht.getSelectionModel().getSelectedItem();
		if(tab instanceof ProjektAnsicht pa) {
			return pa.projektSpeichernUnter();
		} else {
			return false;
		}
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}