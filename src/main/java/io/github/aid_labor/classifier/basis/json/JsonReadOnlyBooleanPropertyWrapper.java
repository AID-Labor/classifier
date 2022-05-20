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
import javafx.beans.property.ReadOnlyBooleanWrapper;

// @formatter:off
@JsonSerialize(
		contentAs = Boolean.class,
		converter = JsonReadOnlyBooleanPropertyWrapper.PropertyZuBooleanKonverter.class
)
@JsonDeserialize(
		converter = JsonReadOnlyBooleanPropertyWrapper.BooleanZuPropertyKonverter.class
)
// @formatter:on
public class JsonReadOnlyBooleanPropertyWrapper extends ReadOnlyBooleanWrapper {
	
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
			return new JsonReadOnlyBooleanPropertyWrapper(value);
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
	
	public JsonReadOnlyBooleanPropertyWrapper() {
		super();
	}
	
	public JsonReadOnlyBooleanPropertyWrapper(boolean initialValue) {
		super(initialValue);
	}
	
	public JsonReadOnlyBooleanPropertyWrapper(Object bean, String name, boolean initialValue) {
		super(bean, name, initialValue);
	}
	
	public JsonReadOnlyBooleanPropertyWrapper(Object bean, String name) {
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
