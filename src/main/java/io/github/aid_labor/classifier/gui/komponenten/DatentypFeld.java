/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import org.controlsfx.validation.ValidationSupport;
import org.kordamp.ikonli.remixicon.RemixiconAL;

import com.dlsc.gemsfx.SearchField;

import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.programmierung.Programmiersprache;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.util.StringConverter;


public class DatentypFeld extends SearchField<String> {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static final Map<String, SortedSet<String>> programmiersprachenDatentypen = new HashMap<>();
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static List<String> setzePaketnamenNachHinten(SortedSet<String> klassenliste) {
		return klassenliste.stream().map(klasse -> {
			String paket = "";
			if (klasse.contains(":")) {
				int index = klasse.lastIndexOf(":");
				paket = klasse.substring(0, index);
				klasse = klasse.substring(index + 1);
				return klasse + ": " + paket;
			} else {
				return klasse;
			}
		}).toList();
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final SortedSet<String> bekannteDatentypen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	
	public DatentypFeld(String startText, boolean voidErlaubt, Programmiersprache programmiersprache,
	        ValidationSupport validierung) {
	    this(startText, voidErlaubt, programmiersprache);
	    System.err.println("DatentypFeld: Validierung nicht mehr unterstützt!");
	}
	
	public DatentypFeld(String startText, boolean voidErlaubt, Programmiersprache programmiersprache) {
		setSelectedItem(startText);
		setText(startText);
		Button loeschen = new Button();
		loeschen.setOnMouseClicked(e -> select(""));
		NodeUtil.fuegeIconHinzu(loeschen, RemixiconAL.DELETE_BACK_2_LINE, 18, ContentDisplay.GRAPHIC_ONLY, "loeschen-button-font-icon");
		loeschen.setPadding(new Insets(0, 5, 0, 5));
		loeschen.prefHeightProperty().bind(getEditor().heightProperty());
		setRight(loeschen);
		
		SortedSet<String> datentypen = programmiersprachenDatentypen.get(programmiersprache.getName());
		if (datentypen == null) {
			datentypen = new TreeSet<>();
			datentypen.addAll(programmiersprache.getEigenschaften().getPrimitiveDatentypen());
			datentypen.addAll(
					setzePaketnamenNachHinten(programmiersprache.getEigenschaften().getBekannteKlassen()));
			datentypen.addAll(
					setzePaketnamenNachHinten(programmiersprache.getEigenschaften().getBekannteInterfaces()));
			datentypen.addAll(setzePaketnamenNachHinten(
					programmiersprache.getEigenschaften().getBekannteEnumerationen()));
			programmiersprachenDatentypen.put(programmiersprache.getName(), datentypen);
		}
		
		this.bekannteDatentypen = datentypen;
		
		setSuggestionProvider(request -> {
			var vorschlaege = new ArrayList<String>();
			vorschlaege.add(request.getUserText());
			if (voidErlaubt) {
				vorschlaege.add(programmiersprache.getEigenschaften().getVoid());
			}
			vorschlaege.addAll(bekannteDatentypen);
			return vorschlaege.stream()
					.filter(datentyp -> datentyp.toLowerCase().contains(request.getUserText().toLowerCase())).toList();
		});
		
		setNewItemProducer(eingabe -> {
			eingabe = eingabe == null ? "" : eingabe;
			if (eingabe.contains(":")) {
				eingabe = eingabe.substring(0, eingabe.lastIndexOf(":"));
			}
			eingabe = eingabe.strip();
			return eingabe;
		});
		
		setConverter(new StringConverter<String>() {
			@Override
			public String toString(String object) {
				return Objects.requireNonNullElse(object, "");
			}
			
			@Override
			public String fromString(String string) {
				return Objects.requireNonNullElse(string, "");
			}
		});
		
		setShowSearchIcon(false);
		setHidePopupWithSingleChoice(false);
		
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	protected void update(Collection<String> newSuggestions) {
		if (wirdUpgedatet) {
			return;
		}
		super.update(newSuggestions);
	}
	
	private boolean wirdUpgedatet = false;
	
	@Override
	public void commit() {
		if (wirdUpgedatet) {
			return;
		}
		
		wirdUpgedatet = true;
		String eingabe = getSelectedItem();
		if (eingabe == null) {
			eingabe = "";
		}
		if (eingabe.contains(":")) {
			eingabe = eingabe.substring(0, eingabe.lastIndexOf(":"));
		}
		eingabe = eingabe.strip();
		
		setSelectedItem(eingabe);
		super.commit();
		wirdUpgedatet = false;
	}
	
}