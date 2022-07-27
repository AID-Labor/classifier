/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Collections;
import java.util.List;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.carbonicons.CarbonIcons;

import io.github.aid_labor.classifier.gui.util.NodeUtil;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

public class KontrollElemente<T> {

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static <E> void tausche(List<E> liste, int indexA, int indexB) {
		Collections.swap(liste, indexA, indexB);
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final Label hoch;
	private final Label runter;
	private final Label loeschen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public KontrollElemente(ObservableList<T> liste, T element, int zeile) {
		hoch = new Label();
		NodeUtil.erzeugeIconNode(hoch, BootstrapIcons.CARET_UP_FILL, 15, "tabelle-steuerung-font-icon");
		hoch.setOnMouseClicked(e -> tausche(liste, zeile, zeile - 1));
		hoch.getStyleClass().add("tabelle-steuerung-button");
		
		if (zeile == 0) {
			hoch.setDisable(true);
		}
		
		runter = new Label();
		NodeUtil.erzeugeIconNode(runter, BootstrapIcons.CARET_DOWN_FILL, 15, "tabelle-steuerung-font-icon");
		runter.setOnMouseClicked(e -> tausche(liste, zeile, zeile + 1));
		runter.getStyleClass().add("tabelle-steuerung-button");
		
		if (zeile == liste.size() - 1) {
			runter.setDisable(true);
		}
		
		loeschen = new Label();
		NodeUtil.erzeugeIconNode(loeschen, CarbonIcons.DELETE, 15, "tabelle-steuerung-font-icon");
		loeschen.setOnMouseClicked(e -> liste.remove(element));
		loeschen.getStyleClass().add("tabelle-steuerung-button");
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public Label getHoch() {
		return hoch;
	}
	
	public Label getRunter() {
		return runter;
	}
	
	public Label getLoeschen() {
		return loeschen;
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