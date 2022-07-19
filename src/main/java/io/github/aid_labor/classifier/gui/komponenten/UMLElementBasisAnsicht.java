/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.lang.ref.WeakReference;

import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class UMLElementBasisAnsicht<E extends UMLDiagrammElement> extends StackPane {
//	private static final Logger log = Logger.getLogger(UMLElementBasisAnsicht.class.getName());

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
	
	protected final WeakReference<E> umlElementModel;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLElementBasisAnsicht(E umlElementModel) {
		this.umlElementModel = new WeakReference<>(umlElementModel);
		this.translateXProperty()
				.bindBidirectional(umlElementModel.getPosition().xProperty());
		this.translateYProperty()
				.bindBidirectional(umlElementModel.getPosition().yProperty());
		if (umlElementModel.getPosition().getBreite() > 0) {
			this.setPrefWidth(umlElementModel.getPosition().getBreite());
		}
		if (umlElementModel.getPosition().getHoehe() > 0) {
			this.setPrefHeight(umlElementModel.getPosition().getHoehe());
		}
		
		umlElementModel.getPosition().breiteProperty()
				.bindBidirectional(this.prefWidthProperty());
		umlElementModel.getPosition().hoeheProperty()
				.bindBidirectional(this.prefHeightProperty());
		
		NodeUtil.beobachteSchwach(this, this.widthProperty(), b -> {
			double breite = b.doubleValue();
			if (breite != getPrefWidth() && breite > getMinWidth()) {
				setPrefWidth(breite);
			}
		});
		NodeUtil.beobachteSchwach(this, this.heightProperty(), h -> {
			double hoehe = h.doubleValue();
			if (hoehe != getPrefHeight() && hoehe > getMinHeight()) {
				setPrefHeight(hoehe);
			}
		});
		
		this.setMinSize(80, 30);
		this.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public E getUmlElement() {
		return umlElementModel.get();
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
	
}