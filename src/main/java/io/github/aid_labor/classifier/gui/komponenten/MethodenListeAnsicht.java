/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Objects;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.collections.ObservableList;
import javafx.scene.Node;


public class MethodenListeAnsicht extends ListenAnsicht<Methode> {
//	private static final Logger log = Logger.getLogger(AttributListeAnsicht.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * CSS-Klasse {@code statisch} fuer statische Methoden
	 */
	public static String CSS_STATISCH_KLASSE = "statisch";
	
	/**
	 * CSS-Klasse {@code abstrakt} fuer abstrakte Methoden
	 */
	public static String CSS_ABSTRAKT_KLASSE = "abstrakt";
	
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
	
	private final Programmiersprache programmiersprache;
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public MethodenListeAnsicht(ObservableList<Methode> methodenListe,
			Programmiersprache programmiersprache) {
		super(methodenListe);
		this.programmiersprache = Objects.requireNonNull(programmiersprache);
		fuelleListe();
	}
	
	@Override
	protected Node[] erstelleZeile(Methode methode) {
		var sichtbarkeit = new EnhancedLabel();
		sichtbarkeit.textProperty()
				.bind(methode.sichtbarkeitProperty().get().kurzformProperty());
		methode.sichtbarkeitProperty().addListener((p, alt, neu) -> {
			sichtbarkeit.textProperty().unbind();
			sichtbarkeit.textProperty()
					.bind(new StringBinding() {
						
						Observable x;
						
						{
							super.bind(methode.sichtbarkeitProperty());
						}
						
						@Override
						protected String computeValue() {
							if (x != null) {
								super.unbind(x);
							}
							var prop = methode.sichtbarkeitProperty().get();
							
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
		beschreibung.textProperty().bind(
				methode.nameProperty()
						.concat("(")
						.concat(new StringBinding() {
							{
								super.bind(methode.parameterListeProperty());
							}
							StringExpression stringExpr = null;
							@Override
							protected String computeValue() {
								if (stringExpr != null) {
									super.unbind(stringExpr);
								}
								var params = methode.parameterListeProperty().stream().map(param -> {
									return Bindings.concat(new When(Einstellungen
													.getBenutzerdefiniert().zeigeParameterNamenProperty())
													.then(param.nameProperty().concat(": "))
													.otherwise(""), 
													param.getDatentyp().typNameProperty());
								}).toArray();
								
								if(params.length < 1) {
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
						})
						.concat(")")
						.concat(new StringBinding() {
							{
								super.bind(methode.getRueckgabeTyp().typNameProperty(),
										Einstellungen.getBenutzerdefiniert().zeigeVoidProperty());
							}
							
							@Override
							protected String computeValue() {
								if (programmiersprache.getEigenschaften()
										.istVoid(methode.getRueckgabeTyp())
										&& !Einstellungen.getBenutzerdefiniert().zeigeVoidProperty()
												.get()) {
									return "";
								}
								String typ = methode.getRueckgabeTyp().getTypName();
								if (typ != null && !typ.isBlank()) {
									return ": " + typ;
								}
								return "";
							}
						}));
		
		sichtbarkeit.getStyleClass().addAll("sichtbarkeit-label");
		
		if (methode.istStatisch()) {
			beschreibung.getStyleClass().add(CSS_STATISCH_KLASSE);
		} else {
			beschreibung.getStyleClass().remove(CSS_STATISCH_KLASSE);
		}
		
		methode.istStatischProperty().addListener((property, alt, istStatisch) -> {
			if (istStatisch) {
				beschreibung.getStyleClass().add(CSS_STATISCH_KLASSE);
			} else {
				beschreibung.getStyleClass().remove(CSS_STATISCH_KLASSE);
			}
		});
		
		if (methode.istAbstrakt()) {
			beschreibung.getStyleClass().add(CSS_ABSTRAKT_KLASSE);
		} else {
			beschreibung.getStyleClass().remove(CSS_ABSTRAKT_KLASSE);
		}
		
		methode.istAbstraktProperty().addListener((property, alt, istAbstrakt) -> {
			if (istAbstrakt) {
				beschreibung.getStyleClass().add(CSS_ABSTRAKT_KLASSE);
			} else {
				beschreibung.getStyleClass().remove(CSS_ABSTRAKT_KLASSE);
			}
		});
		
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