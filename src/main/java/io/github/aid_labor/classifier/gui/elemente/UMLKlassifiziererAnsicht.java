/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.elemente;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.beans.Observable;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


public class UMLKlassifiziererAnsicht extends UMLElementBasisAnsicht<UMLKlassifizierer> {
//	private static final Logger log = Logger
//			.getLogger(UMLKlassifiziererAnsicht.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final AttributListeAnsicht attribute;
	private final VBox methoden;
	private final Label stereotyp;
	private final Label name;
	private final Separator attributeTrenner;
	private final Separator methodenTrenner;
	private final VBox inhalt;
	private final VBox oben;
	private final VBox eigenschaften;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKlassifiziererAnsicht(UMLKlassifizierer klassifizierer) {
		super(klassifizierer);
		
		this.getStyleClass().clear();
		this.getStyleClass().add("UML-Klassifizierer-Ansicht");
		
		this.stereotyp = new Label(klassifizierer.getTyp().getStereotyp());
		this.name = new Label(klassifizierer.getName());
		
		this.attribute = new AttributListeAnsicht(klassifizierer.getAttribute());
		this.methoden = new VBox();
		this.attributeTrenner = new Separator();
		this.methodenTrenner = new Separator();
		
		formatiere();
		erstelleBindungen();
		beobachte();
		
		eigenschaften = new VBox(attributeTrenner, attribute, methodenTrenner, methoden);
		oben = new VBox(name);
		oben.getStyleClass().add("klassifizierung");
		inhalt = new VBox(oben, eigenschaften);
		inhalt.setAlignment(Pos.TOP_CENTER);
		StackPane.setMargin(inhalt, new Insets(0));
		this.getChildren().add(inhalt);
		checkeTrenner(null);
		updateStereotyp(klassifizierer.getTyp());
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
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void formatiere() {
		this.name.getStyleClass().add("name-label");
		this.attribute.setMinHeight(20);
		this.methoden.setMinHeight(20);
		this.methoden.setPadding(new Insets(0, 10, 0, 10));
		this.attributeTrenner.getStyleClass().clear();
		this.attributeTrenner.getStyleClass().add("UML-Klassifizierer-Trennlinie");
		this.methodenTrenner.getStyleClass().clear();
		this.methodenTrenner.getStyleClass().add("UML-Klassifizierer-Trennlinie");
	}
	
	private void erstelleBindungen() {
		this.stereotyp.textProperty().bind(new StringBinding() {
			
			{
				super.bind(umlElementModel.getTypProperty());
			}
			
			@Override
			protected String computeValue() {
				var typ = umlElementModel.getTypProperty().get().getStereotyp();
				return typ == null || typ.isBlank() ? "" : "\u00AB" + typ + "\u00BB";
			}
		});
		var paket = new When(umlElementModel.getPaketProperty().isNotEmpty())
				.then(umlElementModel.getPaketProperty().concat("::")).otherwise("");
		this.name.textProperty()
				.bind(paket.concat(umlElementModel.nameProperty()));
		umlElementModel.getAttribute().addListener(this::checkeTrenner);
		methoden.getChildren().addListener(this::checkeTrenner);
	}
	
	private void beobachte() {
		umlElementModel.getTypProperty().addListener((property, alterTyp, neuerTyp) -> {
			updateStereotyp(neuerTyp);
		});
	}
	
	private void updateStereotyp(KlassifiziererTyp neuerTyp) {
		if (neuerTyp.getStereotyp() == null || neuerTyp.getStereotyp().isBlank()) {
			oben.getChildren().remove(stereotyp);
		} else {
			oben.getChildren().add(0, stereotyp);
		}
		if (neuerTyp.istAbstrakt()) {
			name.getStyleClass().add("abstrakt");
		} else {
			name.getStyleClass().remove("abstrakt");
		}
	}
	
	private void checkeTrenner(Observable o) {
		boolean hatAttributOderMethode = umlElementModel.getAttribute().size() > 0
				|| umlElementModel.getMethoden().size() > 0;
		if (hatAttributOderMethode) {
			if (!this.inhalt.getChildren().contains(this.eigenschaften)) {
				this.inhalt.getChildren().add(eigenschaften);
			}
		} else {
			this.inhalt.getChildren().remove(eigenschaften);
		}
	}
	
}