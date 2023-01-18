/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.gui.komponenten;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.carbonicons.CarbonIcons;

import com.tobiasdiez.easybind.EasyBind;
import com.tobiasdiez.easybind.EasyBinding;

import io.github.aid_labor.classifier.gui.util.NodeUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class ListControlsTableCell<S> extends TableCell<S, S> {

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Klassenattribute                                                                    *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Klassenmethoden                                                                     *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

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

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    private final HBox controlElements;
    private final Label hoch;
    private final Label runter;
    private final Label loeschen;
    private final EasyBinding<Number> listSize;
    private final BooleanBinding sizeBinding;
    private Optional<S> item;

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    public ListControlsTableCell() {
        this.item = Optional.empty();
        
        hoch = new Label();
        NodeUtil.erzeugeIconNode(hoch, BootstrapIcons.CARET_UP_FILL, 15, "tabelle-steuerung-font-icon");
        hoch.setOnMouseClicked(e -> {
            if (item.isPresent()) {
                tausche(getTableView().itemsProperty().get(), indexProperty().get(), indexProperty().get() - 1);
            }
        });
        hoch.getStyleClass().add("tabelle-steuerung-button");
        hoch.disableProperty().bind(indexProperty().isEqualTo(0));

        runter = new Label();
        NodeUtil.erzeugeIconNode(runter, BootstrapIcons.CARET_DOWN_FILL, 15, "tabelle-steuerung-font-icon");
        runter.setOnMouseClicked(e -> {
            if (item.isPresent()) {
                tausche(getTableView().itemsProperty().get(), indexProperty().get(), indexProperty().get() + 1);
            }
        });
        runter.getStyleClass().add("tabelle-steuerung-button");
        
        listSize = EasyBind.wrapNullable(tableViewProperty())
            .mapObservable(table -> EasyBind.wrapNullable(table.itemsProperty()))
            .mapObservable(list -> Bindings.size(list.orElse(FXCollections.<S>emptyObservableList())))
            .orElse(-1);
        sizeBinding = Bindings.createBooleanBinding(() -> indexProperty().get() >= listSize.get().intValue() - 1, indexProperty(), listSize);
        runter.disableProperty().bind(sizeBinding);

        loeschen = new Label();
        NodeUtil.erzeugeIconNode(loeschen, CarbonIcons.DELETE, 15, "tabelle-steuerung-font-icon");
        loeschen.setOnMouseClicked(e -> {
            if (item.isPresent()) {
                getTableView().itemsProperty().get().remove(item.get());
            }
        });
        loeschen.getStyleClass().add("tabelle-steuerung-button");
        
        this.controlElements = new HBox(hoch, runter, loeschen);
        controlElements.setSpacing(10);
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
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
            this.item = Optional.empty();
        } else {
            this.item = Optional.of(item);
            setGraphic(controlElements);
            controlElements.disableProperty().bind(Bindings.not(
                    getTableView().editableProperty().and(
                            getTableColumn().editableProperty()).and(
                                    editableProperty())));

        }
    }

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

}