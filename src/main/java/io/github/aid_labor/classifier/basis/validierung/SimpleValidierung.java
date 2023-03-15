/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.validierung;

import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tobiasdiez.easybind.EasyBind;
import com.tobiasdiez.easybind.Subscription;

import io.github.aid_labor.classifier.basis.projekt.Schliessbar;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SimpleValidierung implements FehlerValidierung, Schliessbar {

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
    
    public static ValidierungBuilder of(BooleanBinding valid, ObservableValue<String> errorMessage) {
        return new ValidierungBuilder(valid, errorMessage);
    }
    
    public static ValidierungCollection merge(Validierung a, Validierung b) {
        var vc = new ValidierungCollection();
        vc.addAll(a, b);
        return vc;
    }
    
    public static ValidierungCollection merge(Validierung a, Validierung b, Validierung... more) {
        var vc = new ValidierungCollection();
        vc.addAll(a, b);
        vc.addAll(more);
        return vc;
    }
    
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
    
    public static final class ValidierungBuilder {
        
        private final ObservableList<ObservableValue<String>> errorMessages;
        private BooleanBinding validierung;
        private Subscription subscriptions = Subscription.EMPTY;
        
        private ValidierungBuilder(BooleanBinding valid, ObservableValue<String> errorMessage) {
            errorMessages = FXCollections.observableList(new LinkedList<>());
            this.validierung = valid;
            var sub = EasyBind.includeWhen(errorMessages, errorMessage, valid.not());
            subscriptions = subscriptions.and(sub);
        }
        
        public ValidierungBuilder and(BooleanBinding valid, ObservableValue<String> errorMessage) {
            this.validierung = this.validierung.and(valid);
            var sub = EasyBind.includeWhen(errorMessages, errorMessage, valid.not());
            subscriptions = subscriptions.and(sub);
            return this;
        }
        
        public ValidierungBuilder or(BooleanBinding valid, ObservableValue<String> errorMessage) {
            this.validierung = this.validierung.or(valid);
            var sub = EasyBind.includeWhen(errorMessages, errorMessage, valid.not());
            subscriptions = subscriptions.and(sub);
            return this;
        }
        
        public SimpleValidierung build() {
            return new SimpleValidierung(validierung, errorMessages, subscriptions);
        }
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
	
	private final ReadOnlyBooleanWrapper valid;
	private final ObservableList<ObservableValue<String>> errorMessages;
	private boolean darfGeschlossenWerden;
	private Subscription subscriptions;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private SimpleValidierung(BooleanBinding valid, ObservableList<ObservableValue<String>> errorMessages, 
	        Subscription subscriptions) {
	    this.valid = new ReadOnlyBooleanWrapper(this, "valid_property");
	    this.valid.bind(valid);
        this.errorMessages = FXCollections.unmodifiableObservableList(errorMessages);
        this.darfGeschlossenWerden = true;
        this.subscriptions = subscriptions;
    }
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
    public ReadOnlyBooleanProperty isValidProperty () {
	    return this.valid.getReadOnlyProperty();
	}
	
	@Override
    public ObservableList<ObservableValue<String>> getErrorMessages() {
        return errorMessages;
    }
	
	public Optional<String> getHighestErrorMessage() {
        if (errorMessages.isEmpty()) {
            return Optional.empty();
        }
        
        return Optional.ofNullable(errorMessages.get(errorMessages.size()-1).getValue());
    }
	
	@Override
    public void schliesse() throws Exception {
        this.errorMessages.clear();
        this.valid.unbind();
        this.subscriptions.unsubscribe();
    }


    @Override
    public boolean darfGeschlossenWerden() {
        return this.darfGeschlossenWerden;
    }


    @Override
    public void setDarfGeschlossenWerden(boolean darfGeschlossenWerden) {
        this.darfGeschlossenWerden = darfGeschlossenWerden;
    }
	
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