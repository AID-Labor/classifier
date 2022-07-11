/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.When;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
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
	private final MethodenListeAnsicht methoden;
	private final Label stereotyp;
	private final Label name;
	private final Separator attributeTrenner;
	private final Separator methodenTrenner;
	private final VBox inhalt;
	private final VBox oben;
	private final VBox eigenschaften;
	private InvalidationListener trennerBeobachter;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKlassifiziererAnsicht(UMLKlassifizierer klassifizierer) {
		super(klassifizierer);
		
		this.getStyleClass().clear();
		this.getStyleClass().add("UML-Klassifizierer-Ansicht");
		
		this.stereotyp = new Label(klassifizierer.getTyp().getStereotyp());
		this.name = new Label(klassifizierer.getName());
		
		this.attribute = new AttributListeAnsicht(klassifizierer.attributeProperty());
		this.methoden = new MethodenListeAnsicht(klassifizierer.methodenProperty(),
				klassifizierer.getProgrammiersprache());
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
		oben.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		eigenschaften.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		StackPane.setMargin(inhalt, new Insets(0));
		this.getChildren().add(inhalt);
		this.minHeightProperty().bind(inhalt.prefHeightProperty());
		this.minWidthProperty().bind(inhalt.prefWidthProperty());
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
				super.bind(umlElementModel.get().typProperty());
			}
			
			@Override
			protected String computeValue() {
				var typ = umlElementModel.get().typProperty().get().getStereotyp();
				return typ == null || typ.isBlank() ? "" : "\u00AB" + typ + "\u00BB";
			}
		});
		var paket = new When(umlElementModel.get().paketProperty().isNotEmpty())
				.then(umlElementModel.get().paketProperty().concat("::")).otherwise("");
		this.name.textProperty()
				.bind(paket.concat(umlElementModel.get().nameProperty()));
		trennerBeobachter = this::checkeTrenner;
		umlElementModel.get().attributeProperty().addListener(new WeakInvalidationListener(trennerBeobachter));
		methoden.getChildren().addListener(new WeakInvalidationListener(trennerBeobachter));
	}
	
	private void beobachte() {
		umlElementModel.get().typProperty().addListener((property, alterTyp, neuerTyp) -> {
			updateStereotyp(neuerTyp);
		});
	}
	
	private void updateStereotyp(KlassifiziererTyp neuerTyp) {
		if (neuerTyp.getStereotyp() == null || neuerTyp.getStereotyp().isBlank()) {
			oben.getChildren().remove(stereotyp);
		} else if (!oben.getChildren().contains(stereotyp)) {
			oben.getChildren().add(0, stereotyp);
		}
		if (neuerTyp.istAbstrakt()) {
			name.getStyleClass().add("abstrakt");
		} else {
			name.getStyleClass().remove("abstrakt");
		}
	}
	
	private void checkeTrenner(Observable o) {
		boolean hatAttributOderMethode = !umlElementModel.get().attributeProperty().isEmpty()
				|| !umlElementModel.get().methodenProperty().isEmpty();
		if (hatAttributOderMethode) {
			if (!this.inhalt.getChildren().contains(this.eigenschaften)) {
				this.inhalt.getChildren().add(eigenschaften);
			}
		} else {
			this.inhalt.getChildren().remove(eigenschaften);
		}
	}
	
}