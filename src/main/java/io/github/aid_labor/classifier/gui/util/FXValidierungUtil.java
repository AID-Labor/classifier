/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.dlsc.gemsfx.SearchField;
import com.tobiasdiez.easybind.EasyBind;
import com.tobiasdiez.easybind.Subscription;

import io.github.aid_labor.classifier.basis.validierung.SimpleValidierung;
import io.github.aid_labor.classifier.basis.validierung.Validierung;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class FXValidierungUtil {

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
    public static final String CSS_EINGABE_FEHLER = "eingabefehler";
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
    
    private static final String popupKey = "popUpEventHandler";
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
    private static enum EventTarget{ HANDLER, FILTER; }
    private static record EventDefinition<T extends Event>(EventHandler<? super T> handler, EventType<T> type, EventTarget target) {}
    
    public static Subscription setzeValidierungStyle(Node node, Validierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            removeAllEventDefinitions(node);
            if (valid.booleanValue()) {
                if (node.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    node.getStyleClass().remove(CSS_EINGABE_FEHLER);
                }
            } else {
                if (!node.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    node.getStyleClass().add(CSS_EINGABE_FEHLER);
                }
            }
        });
    }
    
    public static Subscription setzeValidierungMeldungen(Node node, Validierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            removeAllEventDefinitions(node);
            if (!valid.booleanValue()) {
                EventHandler<? super MouseEvent> showPopUp = e -> {
                    TextFlow errorsTF = new TextFlow(validierung.getErrorMessages().stream()
                            .map(err -> {
                                Text t = new Text();
                                t.textProperty().bind(Bindings.concat(" – ", err, "\n"));
                                return t;
                            })
                            .toArray(i -> new Node[i]));
                    Pane p = new Pane(errorsTF);
                    errorsTF.setPadding(new Insets(15));
                    PopOver popOver = new PopOver(p);
                    popOver.setArrowLocation(ArrowLocation.TOP_CENTER);
                    popOver.getRoot().getStylesheets().addAll(node.getScene().getStylesheets());
                    EventHandler<? super MouseEvent> hidePopup = me -> popOver.hide();
                    node.addEventHandler(MouseEvent.MOUSE_EXITED, hidePopup);
                    addEventDefinition(node, hidePopup, MouseEvent.MOUSE_EXITED, EventTarget.HANDLER);
                    node.addEventFilter(MouseEvent.MOUSE_PRESSED, hidePopup);
                    addEventDefinition(node, hidePopup, MouseEvent.MOUSE_PRESSED, EventTarget.FILTER);
                    popOver.setMaxWidth(100);
                    popOver.show(node);
                };
                node.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, showPopUp);
                addEventDefinition(node, showPopUp, MouseEvent.MOUSE_ENTERED_TARGET, EventTarget.HANDLER);
            }
        });
    }
    
    private static void addEventDefinition(Node node, EventHandler<? extends Event> handler, EventType<?> type, EventTarget target) {
        List<EventDefinition> definitions;
        if (node.getProperties().containsKey(popupKey)) {
            definitions = (List<EventDefinition>) node.getProperties().get(popupKey);
        } else {
            definitions = new LinkedList<>();
            node.getProperties().put(popupKey, definitions);
        }
        definitions.add(new EventDefinition(handler, type, target));
    }
    
    private static void removeAllEventDefinitions(Node node) {
        if (node.getProperties().containsKey(popupKey)) {
            List<EventDefinition> definitions = (List<EventDefinition>) node.getProperties().get(popupKey);
            for (var definition : definitions) {
                switch (definition.target) {
                    case FILTER -> node.removeEventFilter(definition.type, definition.handler);
                    case HANDLER -> node.removeEventHandler(definition.type, definition.handler);
                }
            }
            definitions.clear();
        }
    }
    
    public static Subscription setzePlatzhalter(TextInputControl eingabefeld, SimpleValidierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            if (valid.booleanValue()) {
                eingabefeld.setPromptText(null);
                eingabefeld.setTooltip(null);
            } else {
                Optional<String> fehler = validierung.getHighestErrorMessage();
                if (fehler.isPresent()) {
                    eingabefeld.setPromptText(fehler.get());
                    eingabefeld.setTooltip(new Tooltip(fehler.get()));
                } else {
                    eingabefeld.setPromptText(null);
                    eingabefeld.setTooltip(null);
                }
            }
        }).and(setzeValidierungStyle(eingabefeld, validierung));
    }
    
    public static Subscription setzePlatzhalter(SearchField<?> eingabefeld, SimpleValidierung validierung) {
        return setzePlatzhalter(eingabefeld.getEditor(), validierung);
    }
    
    public static Subscription setzePlatzhalter(CheckBox eingabefeld, SimpleValidierung validierung) {
        return EasyBind.subscribe(validierung.isValidProperty(), valid -> {
            if (valid.booleanValue()) {
                eingabefeld.setTooltip(null);
            } else {
                Optional<String> fehler = validierung.getHighestErrorMessage();
                if (fehler.isPresent()) {
                    eingabefeld.setTooltip(new Tooltip(fehler.get()));
                } else {
                    eingabefeld.setTooltip(null);
                }
            }
        }).and(setzeValidierungStyle(eingabefeld, validierung));
    }
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private FXValidierungUtil() {
        // Hilfsklasse, nicht instanziierbar!
        throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
    }
	
}