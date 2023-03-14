/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.validierung;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.tobiasdiez.easybind.EasyBind;
import com.tobiasdiez.easybind.Subscription;

import io.github.aid_labor.classifier.basis.projekt.Schliessbar;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ValidierungCollection implements Validierung, Schliessbar {

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Klassenattribute                                                                    *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public   ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// protected    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// package  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// private  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Klassenmethoden                                                                     *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public   ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// protected    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// package  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// private  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                                          #
// #    Instanzen                                                                             #
// #                                                                                          #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Attribute                                                                           *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public   ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// protected    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// package  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// private  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

    private final ReadOnlyBooleanWrapper valid;
    private boolean darfGeschlossenWerden;
    private final ObservableList<Validierung> validierungen;
    private final Map<Validierung, Subscription> validierungsSubscriptions;

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Konstruktoren                                                                       *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public   ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// protected    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// package  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// private  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

    public ValidierungCollection() {
        this.validierungen = FXCollections.observableList(new LinkedList<>(),
                v -> new Observable[] { v.isValidProperty() });
        this.valid = new ReadOnlyBooleanWrapper(this, "valid_property");
        this.darfGeschlossenWerden = true;
        this.validierungsSubscriptions = new HashMap<>();
    }

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Getter und Setter                                                                   *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public   ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

    @Override
    public ReadOnlyBooleanProperty isValidProperty() {
        return this.valid.getReadOnlyProperty();
    }

    public void add(Validierung v) {
        this.validierungen.add(v);
        subscribeValidierung(v);
    }

    public void addAll(Collection<Validierung> v) {
        this.validierungen.addAll(v);
        for (Validierung validierung : v) {
            subscribeValidierung(validierung);
        }
    }

    public void addAll(Validierung... v) {
        this.validierungen.addAll(v);
        for (Validierung validierung : v) {
            subscribeValidierung(validierung);
        }
    }

    public void remove(Validierung v) {
        this.validierungen.remove(v);
        unsubscribeValidierung(v);
    }

    @Override
    public void schliesse() throws Exception {
        this.valid.unbind();
    }

    @Override
    public boolean darfGeschlossenWerden() {
        return this.darfGeschlossenWerden;
    }

    @Override
    public void setDarfGeschlossenWerden(boolean darfGeschlossenWerden) {
        this.darfGeschlossenWerden = darfGeschlossenWerden;
    }

// protected    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// package  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// private  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *   Methoden                                                                            *
//  * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public   ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// protected    ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// package  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##

// private  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ##
    
    private void subscribeValidierung(Validierung v) {
        var sub = EasyBind.subscribe(v.isValidProperty(), valid -> {
            boolean newValid = true;
            for (var val : validierungen) {
                if (!val.isValidProperty().get()) {
                    newValid = false;
                    break;
                }
            }
            this.valid.set(newValid);
        });
        if (this.validierungsSubscriptions.containsKey(v)) {
            this.validierungsSubscriptions.put(v, this.validierungsSubscriptions.get(v).and(sub));
        } else {
            this.validierungsSubscriptions.put(v, sub);
        }
    }
    
    private void unsubscribeValidierung(Validierung v) {
        if (this.validierungsSubscriptions.containsKey(v)) {
            var sub = this.validierungsSubscriptions.remove(v);
            sub.unsubscribe();
        }
    }
    
}