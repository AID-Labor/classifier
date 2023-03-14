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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TableCell;

public class CustomNodeTableCell<S, T, N extends Node> extends TableCell<S,T> {
    
    public static class CustomNodeTableCellBuilder<S, T, N extends Node> {
        private Supplier<N> nodeFactory;
        private Function<S, BooleanProperty> disableBinding;
        private Function<N, Property<T>> getProperty;
        private Function<N, T> getValue;
        private BiConsumer<N, T> setValue;
        private UpdateCallback<T, N> onUpdateCallback;
        private BiConsumer<N, S> onUpdateAction;
        
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
        
        public CustomNodeTableCellBuilder<S, T, N> disableBinding(Function<S, BooleanProperty> disableBinding) {
            this.disableBinding = disableBinding;
            return this;
        }
        
        public CustomNodeTableCellBuilder<S, T, N> updateCallback(UpdateCallback<T, N> onUpdateCallback) {
            this.onUpdateCallback = onUpdateCallback;
            return this;
        }
        
        public CustomNodeTableCellBuilder<S, T, N> onUpdateAction(BiConsumer<N, S> onUpdateAction) {
            this.onUpdateAction = onUpdateAction;
            return this;
        }
        
        public CustomNodeTableCell<S, T, N> build() {
            Objects.requireNonNull(nodeFactory);
            
            N node = nodeFactory.get();
            
            return new CustomNodeTableCell<>(node, getProperty, getValue, setValue, disableBinding, onUpdateCallback,
                    onUpdateAction);
        }
    }
    
    public static <S, T, N extends Node> CustomNodeTableCellBuilder<S, T, N> builder(Class<S> s, Class<T> t, Class<N> n) {
        return new CustomNodeTableCellBuilder<>();
    }
	
    public static interface UpdateCallback<T, N extends Node> {
        void onUpdate(N node, T item);
        void onEmpty(N node, T item);
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
    private final BiConsumer<N, T> setValue;
    private final Function<S, BooleanProperty> disableBinding;
    private final UpdateCallback<T, N> onUpdateCallback;
    private final BiConsumer<N, S> onUpdateAction;
            
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public CustomNodeTableCell(N node, Function<N, Property<T>> getProperty, Function<N, T> getValue,
	        BiConsumer<N, T> setValue, Function<S, BooleanProperty> disableBinding, 
	        UpdateCallback<T, N> onUpdateCallback, BiConsumer<N, S> onUpdateAction) {
        this.node = node;
        this.getProperty = getProperty;
        this.setValue = setValue;
        this.disableBinding = disableBinding;
        this.onUpdateCallback = onUpdateCallback;
        this.onUpdateAction = onUpdateAction;
        
        node.addEventHandler(ActionEvent.ACTION, event -> {
            if (getValue != null) {
                this.commitEdit(getValue.apply(node));
            }
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
	
	public N getNode() {
        return node;
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
        super.updateItem(item, empty);
        if (boundProp != null) {
            getProperty.apply(node).unbindBidirectional(boundProp);
        }
        
        if (empty || getIndex() < 0) {
            if (onUpdateCallback != null) {
                onUpdateCallback.onEmpty(node, item);
            }
            setGraphic(null);
        } else {
            ObservableValue<T> ov = getTableColumn().getCellObservableValue(getIndex());
            setGraphic(node);
            if (setValue != null) {
                setValue.accept(node, item);
            }
            if (disableBinding != null) {
                node.disableProperty().bind(disableBinding.apply(getTableView().getItems().get(getIndex())));
            }
            
            if (getProperty != null) {
                if (ov instanceof Property) {
                    boundProp = (Property<T>) ov;
                    getProperty.apply(node).bindBidirectional(boundProp);
                } else if (ov != null) {
                    throw new IllegalStateException("ObservableValue<T> " + ov + " must be instance of Property<T>");
                }
            }
            
            if (onUpdateCallback != null) {
                onUpdateCallback.onUpdate(node, item);
            }
        }
        
        if (onUpdateAction != null) {
            S rowItem = getIndex() < this.getTableView().getItems().size() && getIndex() > 0 ? 
                    this.getTableView().getItems().get(getIndex()) : this.getTableRow() != null ? 
                            this.getTableRow().getItem() : null;
            onUpdateAction.accept(node, rowItem);
        }
    }
	
	private Property<T> boundProp;
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
	
	
}