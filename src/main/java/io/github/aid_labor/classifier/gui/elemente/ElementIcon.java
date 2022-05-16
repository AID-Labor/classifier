/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.elemente;

import io.github.aid_labor.classifier.basis.SprachUtil;
import io.github.aid_labor.classifier.basis.Sprache;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/**
 * Icon-Ansicht fuer UML-Diagramm-Elemente (speziell: UML Classifier aus dem Klassendiagramm)
 * 
 * @author Tim Muehle
 *
 */
public class ElementIcon extends StackPane {
	
//	private static final Logger log = Logger.getLogger(ElementIcon.class.getName());

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final DoubleProperty hoeheProperty;
	private final DoubleProperty breiteProperty;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ElementIcon(Sprache sprache, String bezeichnungSchluessel,
			String alternativBezeichnung) {
		this(sprache, bezeichnungSchluessel, alternativBezeichnung, null, null);
	}
	
	public ElementIcon(Sprache sprache, String bezeichnungSchluessel,
			String alternativBezeichnung,
			String stereotypSchluessel, String alternativStereotyp) {
		super();
		
		this.hoeheProperty = new SimpleDoubleProperty(90);
		this.breiteProperty = new SimpleDoubleProperty(130);
		this.getStyleClass().add("element-icon");
		
		var linieOben = new Line();
		var linieUnten = new Line();
		
		linieOben.startXProperty().bind(this.layoutXProperty());
		linieOben.endXProperty().bind(this.layoutXProperty().add(this.breiteProperty));
		linieUnten.startXProperty().bind(this.layoutXProperty());
		linieUnten.endXProperty().bind(this.layoutXProperty().add(this.breiteProperty));
		
		linieOben.boundsInParentProperty().addListener((obj, alt, neu) -> {
			DoubleBinding xOben = hoeheProperty.divide(2).negate().add(neu.getCenterY() + 7);
			linieUnten.translateYProperty()
					.bind(xOben.add(hoeheProperty.subtract(neu.getCenterY() + 14).divide(2)));
		});
		
		linieOben.getStyleClass().add("element-icon-linie");
		linieUnten.getStyleClass().add("element-icon-linie");
		
		VBox text = new VBox();
		text.setAlignment(Pos.TOP_CENTER);
		
		if (stereotypSchluessel != null) {
			Label stereotyp = SprachUtil.bindText(new Label(), sprache, stereotypSchluessel,
					alternativStereotyp);
			text.getChildren().add(stereotyp);
		}
		
		Label bezeichnung = SprachUtil.bindText(new Label(), sprache, bezeichnungSchluessel,
				alternativBezeichnung);
		bezeichnung.setStyle("-fx-font: bold 16 sans-serif;");
		text.getChildren().addAll(bezeichnung, linieOben);
		StackPane.setAlignment(text, Pos.TOP_CENTER);
		StackPane.setMargin(text, new Insets(2, 0, 2, 0));
		
		this.getChildren().addAll(text, linieUnten);
		setzeHoehe(90);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	public void setzeHoehe(double hoehe) {
		this.hoeheProperty.set(hoehe);
		this.setPrefHeight(hoehe);
		this.setMaxHeight(hoehe);
		this.setMinHeight(hoehe);
	}
	
	public void setzeBreite(double breite) {
		this.breiteProperty.set(breite);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
}