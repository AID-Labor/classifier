/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.elemente;

import java.util.Objects;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.uml.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.eigenschaften.Programmiersprache;
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
				.bind(methode.getSichtbarkeitProperty().get().getKurzform());
		methode.getSichtbarkeitProperty().addListener((p, alt, neu) -> {
			sichtbarkeit.textProperty().unbind();
			sichtbarkeit.textProperty()
					.bind(methode.getSichtbarkeitProperty().get().getKurzform());
		});
		
		var beschreibung = new EnhancedLabel();
		beschreibung.textProperty().bind(
				methode.getNameProperty()
						.concat("(")
						.concat(new StringBinding() {
							{
								super.bind(methode.getParameterListe());
							}
							StringExpression stringExpr = null;
							@Override
							protected String computeValue() {
//								super.unbind(stringExpr);
								var params = methode.getParameterListe().stream().map(param -> {
									return Bindings.concat(new When(Einstellungen
													.getBenutzerdefiniert().zeigeParameterNamen)
													.then(param.getNameProperty().concat(": "))
													.otherwise(""), 
													param.getDatentyp().getTypNameProperty());
								}).toArray();
								
								if(params.length < 1) {
									return "";
								}
								
								stringExpr = Bindings.concat(params[0]);
								for (int i = 1; i < params.length; i++) {
									stringExpr = stringExpr.concat(", ");
									stringExpr = stringExpr.concat(params[i]);
								}
								
								// TODO ?
//								super.bind(stringExpr);
								return stringExpr.get();
							}
						})
						.concat(")")
						.concat(new StringBinding() {
							{
								super.bind(methode.getRueckgabeTyp().getTypNameProperty(),
										Einstellungen.getBenutzerdefiniert().zeigeVoid);
							}
							
							@Override
							protected String computeValue() {
								if (programmiersprache.getEigenschaften()
										.istVoid(methode.getRueckgabeTyp())
										&& !Einstellungen.getBenutzerdefiniert().zeigeVoid
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