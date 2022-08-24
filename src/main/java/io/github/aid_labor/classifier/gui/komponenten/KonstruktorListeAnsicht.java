/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Konstruktor;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class KonstruktorListeAnsicht extends ListenAnsicht<Konstruktor> {

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
	
	public KonstruktorListeAnsicht(ObservableList<Konstruktor> konstruktorListe) {
		super(konstruktorListe);
		fuelleListe();
		this.getStyleClass().add("konstruktor-liste");
		this.setVisible(!konstruktorListe.isEmpty() && Einstellungen.getBenutzerdefiniert().zeigeKonstruktorenProperty().get());
		NodeUtil.beobachteSchwach(this, Einstellungen.getBenutzerdefiniert().zeigeKonstruktorenProperty(), this::setVisible);
	}
	
	@Override
	protected Node[] erstelleZeile(Konstruktor konstruktor) {
		var sichtbarkeit = new EnhancedLabel();
		sichtbarkeit.textProperty().bind(konstruktor.sichtbarkeitProperty().get().kurzformProperty());
		konstruktor.sichtbarkeitProperty().addListener((p, alt, neu) -> {
			sichtbarkeit.textProperty().unbind();
			sichtbarkeit.textProperty().bind(new StringBinding() {
				
				Observable x;
				
				{
					super.bind(konstruktor.sichtbarkeitProperty());
				}
				
				@Override
				protected String computeValue() {
					if (x != null) {
						super.unbind(x);
					}
					var prop = konstruktor.sichtbarkeitProperty().get();
					
					if (prop == null) {
						x = null;
						return "";
					} else {
						x = prop.kurzformProperty();
						super.bind(prop.kurzformProperty());
						return prop.kurzformProperty().get();
					}
				}
				
			});
		});
		
		var beschreibung = new EnhancedLabel();
		beschreibung.textProperty().bind(konstruktor.nameProperty().concat("(").concat(new StringBinding() {
			{
				super.bind(konstruktor.parameterListeProperty());
			}
			StringExpression stringExpr = null;
			
			@Override
			protected String computeValue() {
				if (stringExpr != null) {
					super.unbind(stringExpr);
				}
				var params = konstruktor.parameterListeProperty().stream().map(param -> {
					return Bindings.concat(
							new When(Einstellungen.getBenutzerdefiniert().zeigeParameterNamenProperty())
									.then(param.nameProperty().concat(": ")).otherwise(""),
							param.getDatentyp().typNameProperty());
				}).toArray();
				
				if (params.length < 1) {
					return "";
				}
				
				stringExpr = Bindings.concat(params[0]);
				for (int i = 1; i < params.length; i++) {
					stringExpr = stringExpr.concat(", ");
					stringExpr = stringExpr.concat(params[i]);
				}
				
				super.bind(stringExpr);
				return stringExpr.get();
			}
		}).concat(")"));
		
		sichtbarkeit.getStyleClass().addAll("sichtbarkeit-label");
		
		return new Node[] { sichtbarkeit, beschreibung };
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