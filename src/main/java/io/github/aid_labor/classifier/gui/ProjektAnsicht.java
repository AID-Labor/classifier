/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.io.ProgrammDetails;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import javafx.beans.binding.When;
import javafx.scene.control.Tab;


public class ProjektAnsicht extends Tab {
//	private static final Logger log = Logger.getLogger(ProjektAnsicht.class.getName());
	private static String UNGESPEICHERT_CSS_CLASS = "tab-ungespeichert";
	
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
	
	private final UMLProjekt projekt;
	private final ProjektKontrolle controller;
	private final DialogPane overlayDialog;
	private final ProgrammDetails programm;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjektAnsicht(UMLProjekt projekt, Sprache sprache, DialogPane overlayDialog, 
			ProgrammDetails programm) {
		this.projekt = projekt;
		this.overlayDialog = overlayDialog;
		this.programm = programm;
		this.controller = new ProjektKontrolle(this, sprache);
		
		this.textProperty().bind(new When(projekt.istGespeichertProperty()).then("")
				.otherwise("*").concat(projekt.nameProperty()));
		
		if(!projekt.istGespeichertProperty().get()) {
			this.getStyleClass().add(UNGESPEICHERT_CSS_CLASS);
		}
		
		projekt.istGespeichertProperty().addListener((property, alterWert, istGespeichert) -> {
			if(!istGespeichert) {
				this.getStyleClass().add(UNGESPEICHERT_CSS_CLASS);
			} else {
				this.getStyleClass().remove(UNGESPEICHERT_CSS_CLASS);
			}
		});
		
		this.setOnCloseRequest(this.controller::checkSchliessen);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public UMLProjekt getProjekt() {
		return projekt;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	protected DialogPane getOverlayDialog() {
		return this.overlayDialog;
	}
	
	protected ProgrammDetails getProgrammDetails() {
		return this.programm;
	}
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public boolean projektSpeichern() {
		return this.controller.projektSpeichern(this.projekt);
	}
	
	public boolean projektSpeichernUnter() {
		return this.controller.projektSpeichernUnter(this.projekt);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}