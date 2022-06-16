/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.function.Supplier;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import javafx.beans.binding.When;
import javafx.scene.control.Alert;


class ProjekteKontrolle {
	private static final Logger log = Logger.getLogger(ProjekteKontrolle.class.getName());
	
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
	
	private final ProjekteAnsicht ansicht;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	ProjekteKontrolle(ProjekteAnsicht ansicht, Sprache sprache) {
		this.ansicht = ansicht;
		this.sprache = sprache;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	void legeNeuenKlassifiziererAn(KlassifiziererTyp typ) {
		var projekt = this.ansicht.getAngezeigtesProjektProperty().get();
		legeNeuenKlassifiziererAn(
				new UMLKlassifizierer(typ, projekt.getProgrammiersprache(), ""));
	}
	
	void legeNeuenKlassifiziererAn(UMLKlassifizierer klassifizierer) {
		var projekt = this.ansicht.getAngezeigtesProjektProperty().get();
		
		legeDiagrammElementAn(klassifizierer, () -> {
			var dialog = new UMLKlassifiziererBearbeitenDialog(klassifizierer, projekt);
			dialog.titleProperty().bind(
					projekt.nameProperty().concat(" > ")
							.concat(new When(klassifizierer.nameProperty().isEmpty())
									.then(sprache.getText("unbenannt", "Unbenannt"))
									.otherwise(klassifizierer.nameProperty())));
			return dialog;
		});
	}
	
	void legeKommentarAn() {
		legeKommentarAn(new UMLKommentar());
	}
	
	void legeKommentarAn(UMLKommentar kommentar) {
		var projekt = this.ansicht.getAngezeigtesProjektProperty().get();
		
		legeDiagrammElementAn(kommentar, () -> {
			var dialog = new UMLKommentarBearbeitenDialog(kommentar);
			dialog.titleProperty().bind(projekt.nameProperty().concat(" > ")
					.concat(sprache.getTextProperty("kommentarBearbeitenTitel",
							"Kommentar bearbeiten")));
			return dialog;
		});
	}
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void legeDiagrammElementAn(UMLDiagrammElement element,
			Supplier<Alert> dialogKonstruktor) {
		var projekt = this.ansicht.getAngezeigtesProjektProperty().get();
		var alterStatus = projekt.getUeberwachungsStatus();
		projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
		
		projekt.getDiagrammElemente().add(element);
		
		var dialog = dialogKonstruktor.get();
		dialog.initOwner(this.ansicht.getAnsicht().getScene().getWindow());
		dialog.showAndWait().ifPresent(button -> {
			switch (button.getButtonData()) {
				case BACK_PREVIOUS -> {
					log.fine(() -> "Entferne " + element);
					projekt.verwerfeEditierungen();
				}
				case FINISH -> {
					log.fine(() -> "Aenderungen an " + element + " uebernommen");
					projekt.uebernehmeEditierungen();
				}
				default -> {
					log.severe(() -> "Unbekannter Buttontyp: " + button);
				}
			}
			projekt.setUeberwachungsStatus(alterStatus);
		});
	}
}