/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.elemente.UMLElementBasisAnsicht;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import javafx.beans.binding.When;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;


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
	private final Pane zeichenflaeche;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjektAnsicht(UMLProjekt projekt, Sprache sprache, DialogPane overlayDialog,
			ProgrammDetails programm) {
		this.projekt = projekt;
		this.overlayDialog = overlayDialog;
		this.programm = programm;
		this.controller = new ProjektKontrolle(this, sprache);
		this.zeichenflaeche = new Pane();
		
		var inhalt = new ScrollPane(zeichenflaeche);
		this.setContent(inhalt);
		
		this.textProperty().bind(new When(projekt.istGespeichertProperty()).then("")
				.otherwise("*").concat(projekt.nameProperty()));
		
		if (!projekt.istGespeichertProperty().get()) {
			this.getStyleClass().add(UNGESPEICHERT_CSS_CLASS);
		}
		
		projekt.istGespeichertProperty().addListener((property, alterWert, istGespeichert) -> {
			if (!istGespeichert) {
				this.getStyleClass().add(UNGESPEICHERT_CSS_CLASS);
			} else {
				this.getStyleClass().remove(UNGESPEICHERT_CSS_CLASS);
			}
		});
		
		this.setOnCloseRequest(this.controller::checkSchliessen);
		
		projekt.getDiagrammElemente()
				.addListener((Change<? extends UMLDiagrammElement> aenderung) -> {
					this.ueberwacheDiagrammElemente(aenderung);
				});
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
	
	void zeigeUMLElement(UMLElementBasisAnsicht<?> umlElement) {
		throw new UnsupportedOperationException("Noch nicht implementiert");
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private <E extends UMLDiagrammElement> void ueberwacheDiagrammElemente(
			Change<E> aenderung) {
		while (aenderung.next()) {
			if (aenderung.wasPermutated()) {
				
			}
			if (aenderung.wasAdded()) {
				
			}
			if (aenderung.wasRemoved()) {
				
			}
		}
	}
	
}