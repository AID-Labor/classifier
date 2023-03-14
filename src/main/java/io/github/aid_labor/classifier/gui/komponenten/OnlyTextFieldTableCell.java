/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.komponenten;

import java.util.function.BiConsumer;
import java.util.function.Function;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

public class OnlyTextFieldTableCell<S,T> extends TableCell<S,T> {
	
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
	
    private final TextField textField;
    private StringConverter<T> converter;
    private boolean keyPressed = false;
    private final Function<S, BooleanProperty> disableBinding;
    private BiConsumer<TextField, S> onUpdateAction;
    
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
    public OnlyTextFieldTableCell(StringConverter<T> converter) {
        this(converter, null);
    }
    
	public OnlyTextFieldTableCell(StringConverter<T> converter, Function<S, BooleanProperty> disableBinding) {
	    this.converter = converter;
	    this.textField = new TextField();
	    this.disableBinding = disableBinding;
	    
	    textField.setOnAction(event -> {
	        if (getConverter() == null) {
                throw new IllegalStateException(
                        "Attempting to convert text input into Object, but provided "
                                + "StringConverter is null. Be sure to set a StringConverter "
                                + "in your cell factory.");
            }
            this.commitEdit(converter.fromString(textField.getText()));
            event.consume();
        });
	    
	    textField.addEventHandler(KeyEvent.KEY_PRESSED, e -> keyPressed = true);
	    textField.addEventHandler(KeyEvent.KEY_RELEASED, e -> keyPressed = false);
	    
	    // by default the graphic is null until the cell stops being empty
        setGraphic(null);
    }
	
	public OnlyTextFieldTableCell() {
        this((Function<S, BooleanProperty>)null);
    }
	
	public OnlyTextFieldTableCell(Function<S, BooleanProperty> disableBinding) {
	    this(new StringConverter<>() {
	        
	        @Override
	        public String toString(T object) {
	            return String.valueOf(object);
	        }
	        
	        @Override
	        @SuppressWarnings("unchecked")
	        public T fromString(String string) {
	            T value;
	            try {
	                value = (T) string;
	            } catch (Exception e) {
	                throw new UnsupportedOperationException("Value cannot be converted from String! Please provide a custom StringConverter.", e);
	            }
	            return value;
	        }
	    }, disableBinding);
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	

    /**
     * Sets the {@link StringConverter} to be used in this cell.
     * @param value the {@link StringConverter} to be used in this cell
     */
    public final void setConverter(StringConverter<T> value) {
        this.converter = value;
    }

    /**
     * Returns the {@link StringConverter} used in this cell.
     * @return the {@link StringConverter} used in this cell
     */
    public final StringConverter<T> getConverter() {
        return converter;
    }
    
    public void setOnUpdateAction(BiConsumer<TextField, S> onUpdateAction) {
        this.onUpdateAction = onUpdateAction;
    }
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
    /** {@inheritDoc} */
    @Override
    public void updateItem(T item, boolean empty) {
        if (keyPressed) {
            // nicht updaten, wenn gerade Eingabe in Textfeld erfolgt ist
            // ansonsten springt der Cursor an den Anfang des Textfelds
            return;
        }
        super.updateItem(item, empty);
        if (boundProp != null) {
            textField.textProperty().unbindBidirectional(boundProp);
        }
        
        if (empty || getIndex() < 0) {
            setGraphic(null);
        } else {
            ObservableValue<T> ov = getTableColumn().getCellObservableValue(getIndex());
            
            if (ov instanceof StringProperty) {
                boundProp = (Property<T>) ov;
                textField.textProperty().bindBidirectional((StringProperty)boundProp);
            } else if (ov instanceof Property) {
                boundProp = (Property<T>) ov;
                textField.textProperty().bindBidirectional(boundProp, converter);
            }
            setGraphic(textField);
            if (disableBinding != null) {
                textField.disableProperty().bind(disableBinding.apply(getTableView().getItems().get(getIndex())));
            }
        }
        
        if (onUpdateAction != null) {
            S rowItem = getIndex() < this.getTableView().getItems().size() && getIndex() > 0 ? 
                    this.getTableView().getItems().get(getIndex()) : this.getTableRow() != null ? 
                            this.getTableRow().getItem() : null;
            onUpdateAction.accept(textField, rowItem);
        }
    }
	
	private Property<T> boundProp;
	
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
}