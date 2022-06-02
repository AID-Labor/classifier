/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.dlsc.gemsfx.DialogPane;

import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.elemente.UMLElementBasisAnsicht;
import io.github.aid_labor.classifier.gui.elemente.UMLKlassifiziererAnsicht;
import io.github.aid_labor.classifier.gui.elemente.UMLKommentarAnsicht;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import javafx.beans.binding.When;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


public class ProjektAnsicht extends Tab {
	private static final Logger log = Logger.getLogger(ProjektAnsicht.class.getName());
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
	private final Map<Integer, Node> ansichten;
	private final ObservableList<Node> selektion;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjektAnsicht(UMLProjekt projekt, Sprache sprache, DialogPane overlayDialog,
			ProgrammDetails programm) {
		this.projekt = projekt;
		this.sprache = sprache;
		this.overlayDialog = overlayDialog;
		this.programm = programm;
		this.controller = new ProjektKontrolle(this, sprache);
		this.zeichenflaeche = new StackPane();
		this.ansichten = new HashMap<>();
		this.selektion = FXCollections.observableArrayList();
		
		selektion.addListener((Change<? extends Node> aenderung) -> {
			log.finer(() -> "Selektion geandert: ");
			while (aenderung.next()) {
				if (aenderung.wasRemoved()) {
					log.finer(() -> "deselektiert: "
							+ Arrays.toString(aenderung.getRemoved().toArray()));
					for (Node n : aenderung.getRemoved()) {
						n.setId("");
					}
				}
				if (aenderung.wasAdded()) {
					log.finer(() -> "selektiert: "
							+ Arrays.toString(aenderung.getAddedSubList().toArray()));
					for (Node n : aenderung.getAddedSubList()) {
						n.setId("selektiert");
					}
				}
			}
		});
		
		var inhalt = new ScrollPane(zeichenflaeche);
//		inhalt.setFitToHeight(true);
//		inhalt.setFitToWidth(true);
//		inhalt.setPannable(true);
		inhalt.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		inhalt.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		zeichenflaeche.setId("zeichenflaeche");
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
		
		fuegeAlleHinzu(projekt.getDiagrammElemente());
		
		this.getContent().addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getTarget().equals(inhalt) || e.getTarget().equals(zeichenflaeche)) {
				selektion.clear();
				e.consume();
			}
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
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private int idZaehler = 0;
	
	private <E extends UMLDiagrammElement> void ueberwacheDiagrammElemente(
			Change<E> aenderung) {
		while (aenderung.next()) {
			if (aenderung.wasPermutated()) {
				var kopie = zeichenflaeche.getChildren()
						.toArray(new Node[zeichenflaeche.getChildren().size()]);
				for (int i = aenderung.getFrom(); i < aenderung.getTo(); i++) {
					zeichenflaeche.getChildren().set(aenderung.getPermutation(i), kopie[i]);
				}
			}
			if (aenderung.wasAdded()) {
				fuegeAlleHinzu(aenderung.getAddedSubList());
			}
			if (aenderung.wasRemoved()) {
				for (UMLDiagrammElement entfernt : aenderung.getRemoved()) {
					var ansicht = this.ansichten.remove(entfernt.getId());
					this.zeichenflaeche.getChildren().remove(ansicht);
				}
			}
		}
	}
	
	private <E extends UMLDiagrammElement> void fuegeAlleHinzu(List<E> elemente) {
		for (var neu : elemente) {
			UMLElementBasisAnsicht<? extends UMLDiagrammElement> ansicht = null;
			if (neu instanceof UMLKlassifizierer klassifizierer) {
				ansicht = new UMLKlassifiziererAnsicht(klassifizierer);
			} else if (neu instanceof UMLKommentar kommentar) {
				ansicht = new UMLKommentarAnsicht(kommentar);
			} else {
				log.severe(() -> "unbekannter Typ: " + neu.getClass().getName());
				continue;
			}
			
			fuegeHinzu(ansicht);
		}
	}
	
	private void fuegeHinzu(UMLElementBasisAnsicht<? extends UMLDiagrammElement> ansicht) {
		this.zeichenflaeche.getChildren().add(ansicht);
		ansicht.getUmlElement().setId(idZaehler);
		this.ansichten.put(idZaehler, ansicht);
		idZaehler++;
		
		ansicht.addEventFilter(MouseEvent.MOUSE_RELEASED, e -> {
			boolean multiSelektion = OS.getDefault().istMacOS() && e.isMetaDown()
					|| !OS.getDefault().istMacOS() && e.isControlDown();
			if (!multiSelektion) {
				selektion.clear();
			}
			selektion.add(ansicht);
			e.consume();
		});
		
		if (ansicht.getUmlElement() instanceof UMLKlassifizierer klassifizierer) {
			ansicht.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
				if (event.getClickCount() == 2) {
					var dialog = new UMLKlassifiziererBearbeitenDialog(klassifizierer);
					dialog.initOwner(ansicht.getScene().getWindow());
					dialog.titleProperty().bind(
							projekt.nameProperty().concat(" > ")
									.concat(new When(klassifizierer.nameProperty().isEmpty())
											.then(sprache.getText("unbenannt", "Unbenannt"))
											.otherwise(klassifizierer.nameProperty())));
					dialog.showAndWait().ifPresent(button -> {
						switch (button.getButtonData()) {
							case BACK_PREVIOUS -> {
								log.fine(() -> "Setze zurueck " + klassifizierer);
								int i = projekt.getDiagrammElemente().indexOf(klassifizierer);
								projekt.getDiagrammElemente().set(i,
										dialog.getSicherungskopie());
							}
							case FINISH -> {
								log.fine(() -> "Aenderungen an " + klassifizierer
										+ " uebernommen");
							}
							default -> {
								log.severe(() -> "Unbekannter Buttontyp: " + button);
							}
						}
					});
				}
			});
		}
		
		NodeUtil.macheBeweglich(ansicht);
	}
	
}