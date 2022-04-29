/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


@JsonSerialize(contentAs = Boolean.class, converter = JsonBooleanProperty.PropertyZuBooleanKonverter.class)
@JsonDeserialize(converter = JsonBooleanProperty.BooleanZuPropertyKonverter.class)
public class JsonBooleanProperty extends SimpleBooleanProperty {
	
	public static class PropertyZuBooleanKonverter
			extends StdConverter<BooleanProperty, Boolean> {
		@Override
		public Boolean convert(BooleanProperty value) {
			return value.get();
		}
	}
	
	public static class BooleanZuPropertyKonverter
			extends StdConverter<Boolean, BooleanProperty> {
		@Override
		public BooleanProperty convert(Boolean value) {
			return new JsonBooleanProperty(value);
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
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public JsonBooleanProperty() {
		super();
	}
	
	public JsonBooleanProperty(boolean initialValue) {
		super(initialValue);
	}
	
	public JsonBooleanProperty(Object bean, String name, boolean initialValue) {
		super(bean, name, initialValue);
	}
	
	public JsonBooleanProperty(Object bean, String name) {
		super(bean, name);
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BooleanProperty jip) {
			return this.get() == jip.get() && this.getName().equals(jip.getName());
		}
		return false;
	}
}
