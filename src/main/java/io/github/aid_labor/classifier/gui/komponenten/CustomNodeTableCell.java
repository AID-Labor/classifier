/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

public class CustomNodeTableCell<S, T, N extends Node> extends TableCell<S,T> {
    
    public static class CustomNodeTableCellBuilder<S, T, N extends Node> {
        private Supplier<N> nodeFactory;
        private Function<N, Property<T>> getProperty;
        private Function<N, T> getValue;
        private BiConsumer<N, T> setValue;
        
        private CustomNodeTableCellBuilder() {
            
        }
        
        public CustomNodeTableCellBuilder<S, T, N> nodeFactory(Supplier<N> nodeFactory) {
            this.nodeFactory = nodeFactory;
            return this;
        }
        
        public CustomNodeTableCellBuilder<S, T, N> getProperty(Function<N, Property<T>> getProperty) {
            this.getProperty = getProperty;
            return this;
        }
        
        public CustomNodeTableCellBuilder<S, T, N> getValue(Function<N, T> getValue) {
            this.getValue = getValue;
            return this;
        }
        
        public CustomNodeTableCellBuilder<S, T, N> setValue(BiConsumer<N, T> setValue) {
            this.setValue = setValue;
            return this;
        }
        
        public CustomNodeTableCell<S, T, N> build() {
            Objects.requireNonNull(nodeFactory);
            Objects.requireNonNull(getValue);
            Objects.requireNonNull(setValue);
            Objects.requireNonNull(getProperty);
            
            N node = nodeFactory.get();
            
            return new CustomNodeTableCell<>(node, getProperty, getValue, setValue);
        }
    }
    
    public static <S, T, N extends Node> CustomNodeTableCellBuilder<S, T, N> builder(Class<S> s, Class<T> t, Class<N> n) {
        return new CustomNodeTableCellBuilder<>();
    }
	
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
	
    private final N node;
    private final Function<N, Property<T>> getProperty;
    private final Function<N, T> getValue;
    private final BiConsumer<N, T> setValue;
            
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public CustomNodeTableCell(N node, Function<N, Property<T>> getProperty, Function<N, T> getValue,
	        BiConsumer<N, T> setValue) {
        this.node = node;
        this.getProperty = getProperty;
        this.getValue = getValue;
        this.setValue = setValue;
        
        node.addEventHandler(ActionEvent.ACTION, event -> {
            this.commitEdit(getValue.apply(node));
            event.consume();
        });
        
        // by default the graphic is null until the cell stops being empty
        setGraphic(null);
    }
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
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
	
    /** {@inheritDoc} */
    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (boundProp != null) {
            getProperty.apply(node).unbindBidirectional(boundProp);
        }
        
        if (empty || getIndex() < 0) {
            setGraphic(null);
        } else {
            ObservableValue<T> ov = getTableColumn().getCellObservableValue(getIndex());
            setGraphic(node);
            setValue.accept(node, item);
            node.disableProperty().bind(Bindings.not(
                    getTableView().editableProperty().and(
                    getTableColumn().editableProperty()).and(
                    editableProperty())
                ));
            
            if (ov instanceof Property) {
                boundProp = (Property<T>) ov;
                getProperty.apply(node).bindBidirectional(boundProp);
            } else if (ov != null) {
                throw new IllegalStateException("ObservableValue<T> " + ov + " must be instance of Property<T>");
            }
        }
    }
	
	private Property<T> boundProp;
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
}