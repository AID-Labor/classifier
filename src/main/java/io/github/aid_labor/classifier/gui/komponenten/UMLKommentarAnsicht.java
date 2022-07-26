/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.web.WebView;


public class UMLKommentarAnsicht extends UMLElementBasisAnsicht<UMLKommentar> {
//	private static final Logger log = Logger.getLogger(UMLKommentarAnsicht.class.getName());

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

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKommentarAnsicht(UMLKommentar kommentar) {
		super(kommentar);
		this.setMinSize(100, 100);
		if (this.getPrefHeight() < 100) {
			this.setPrefHeight(100);
		}
		if (this.getPrefWidth() < 100) {
			this.setPrefWidth(100);
		}
		this.getStyleClass().add("kommentar-hintergrund");
		
		final int eckenAbstand = 25;
		
		setzeRand(eckenAbstand);
		
		var htmlAnsicht = new WebView();
		
		var engine = htmlAnsicht.getEngine();
		engine.setJavaScriptEnabled(false);
		
		engine.loadContent(kommentar.getInhalt());
		kommentar.inhaltProperty().addListener((p, alt, neuerInhalt) -> engine.loadContent(neuerInhalt));
		
		htmlAnsicht.setContextMenuEnabled(false);
		StackPane.setMargin(htmlAnsicht, new Insets(5, eckenAbstand + 5.0, 5, 5));
		
		var text = new StackPane(htmlAnsicht);
		text.getStyleClass().add("kommentar");
		setzeTextRand(text, eckenAbstand);
		
		htmlAnsicht.addEventFilter(EventType.ROOT, e -> {
			if (e.getTarget().equals(htmlAnsicht) && !e.isConsumed()) {
				var event = e.copyFor(e.getSource(), htmlAnsicht.getParent());
				htmlAnsicht.getParent().fireEvent(event);
				e.consume();
			}
		});
		htmlAnsicht.addEventHandler(EventType.ROOT, e -> {
			if (e.getTarget().equals(htmlAnsicht) && !e.isConsumed()) {
				var event = e.copyFor(e.getSource(), htmlAnsicht.getParent());
				htmlAnsicht.getParent().fireEvent(event);
				e.consume();
			}
		});
		
		this.getChildren().add(text);
	}
	
	private void setzeRand(int eckenAbstand) {
		VLineTo links = new VLineTo();
		links.yProperty().bind(this.heightProperty());
		HLineTo unten = new HLineTo();
		unten.xProperty().bind(this.widthProperty());
		VLineTo rechts = new VLineTo(eckenAbstand);
		LineTo schraege = new LineTo();
		schraege.xProperty().bind(this.widthProperty().subtract(eckenAbstand));
		schraege.setY(0);
		HLineTo oben = new HLineTo(0);
		
		Path rand = new Path(new MoveTo(0, 0), links, unten, rechts, schraege, oben);
		this.setShape(rand);
	}
	
	private void setzeTextRand(Region text, int eckenAbstand) {
		VLineTo links = new VLineTo();
		links.yProperty().bind(this.heightProperty());
		HLineTo unten = new HLineTo();
		unten.xProperty().bind(this.widthProperty());
		VLineTo rechts = new VLineTo(eckenAbstand);
		HLineTo oben = new HLineTo(0);
		HLineTo falteUnten = new HLineTo();
		falteUnten.xProperty().bind(this.widthProperty().subtract(eckenAbstand));
		VLineTo falteLinks = new VLineTo(0);
		
		MoveTo zuSchraege = new MoveTo();
		zuSchraege.xProperty().bind(this.widthProperty().subtract(eckenAbstand));
		LineTo schraege = new LineTo();
		schraege.xProperty().bind(unten.xProperty());
		schraege.setY(eckenAbstand + 5.0);
		Path rand = new Path(new MoveTo(0, 0), links, unten, rechts, falteUnten, falteLinks, oben);
		text.setShape(rand);
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
	
}