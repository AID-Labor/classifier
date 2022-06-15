/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKommentar;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;


public class UMLKommentarBearbeitenDialog extends Alert {
	private static final Logger log = Logger
			.getLogger(UMLKommentarBearbeitenDialog.class.getName());

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
	
	private final Sprache sprache;
	private final UMLKommentar kommentar;
	private final HTMLEditor inhalt;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public UMLKommentarBearbeitenDialog(UMLKommentar kommentar) {
		super(AlertType.NONE);
		this.sprache = new Sprache();
		this.kommentar = kommentar;
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"UMLKommentarBearbeitenDialog");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		inhalt = new HTMLEditor();
		inhalt.setHtmlText(kommentar.getInhalt());
		
		inhalt.addEventFilter(KeyEvent.ANY, event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
				event.consume();
			}
		});
		
		ueberwacheAenderungen(inhalt);
		
		WebView web = (WebView) inhalt.lookup("WebView");
		web.addEventFilter(Event.ANY, this::updateKommentar);
		
		BorderPane wurzel = new BorderPane();
		wurzel.setCenter(inhalt);
		wurzel.setPadding(new Insets(20, 20, 0, 10));
		
		getDialogPane().setContent(wurzel);
		getDialogPane().setPadding(new Insets(0));
		initialisiereButtons();
		this.setResizable(true);
		this.getDialogPane().setPrefSize(700, 400);
		this.setOnHidden(this::updateKommentar);
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
	
	private void updateKommentar(Event event) {
		kommentar.setInhalt(inhalt.getHtmlText().replace(" contenteditable=\"true\"", ""));
		if (event != null) {
			log.finest(() -> """
					potentielle Aenderung festgestellt
					    Ausloeser: %s
					    inhalt: %s""".formatted(event, inhalt.getHtmlText()));
		}
	}
	
	private void ueberwacheAenderungen(HTMLEditor editor) {
		String[] selektoren = {
			".html-editor-cut",
			".html-editor-copy",
			".html-editor-paste",
			".html-editor-align-left",
			".html-editor-align-right",
			".html-editor-align-center",
			".html-editor-align-justify",
			".html-editor-outdent",
			".html-editor-indent",
			".html-editor-bullets",
			".html-editor-numbers",
			".format-menu-button",
			".font-family-menu-button",
			".font-size-menu-button",
			".html-editor-bold",
			".html-editor-italic",
			".html-editor-underline",
			".html-editor-strike",
			".html-editor-hr",
			".html-editor-foreground",
			".html-editor-background",
		};
		for (String selektor : selektoren) {
			Platform.runLater(() -> {
				var node = editor.lookup(selektor);
				if (node instanceof ButtonBase b) {
					var action = b.getOnAction();
					b.setOnAction(e -> {
						if (e != null && action!= null) {
							action.handle(e);
						}
						this.updateKommentar(e);
					});
				} else if (node instanceof ComboBoxBase<?> cb) {
					var action = cb.getOnAction();
					cb.setOnAction(e -> {
						if (e != null && action!= null) {
							action.handle(e);
						}
						this.updateKommentar(e);
					});
				}
			});
		}
	}
	
	private void initialisiereButtons() {
		ButtonType[] buttons = {
			new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.FINISH),
			new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"),
					ButtonData.BACK_PREVIOUS)
		};
		this.getDialogPane().getButtonTypes().addAll(buttons);
	}
}