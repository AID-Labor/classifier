/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

@JsonSerialize(
		contentAs=Integer.class,
		converter=JsonIntegerProperty.PropertyZuIntegerKonverter.class
)
@JsonDeserialize (
		converter=JsonIntegerProperty.IntegerZuPropertyKonverter.class
)
public class JsonIntegerProperty extends SimpleIntegerProperty {
	public static class PropertyZuIntegerKonverter extends StdConverter<IntegerProperty, Integer> {
		@Override
		public Integer convert(IntegerProperty value) {
			return value.get();
		}
	}
	
	public static class IntegerZuPropertyKonverter extends StdConverter<Integer, IntegerProperty> {
		@Override
		public IntegerProperty convert(Integer value) {
			return new JsonIntegerProperty(value);
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
		
	public JsonIntegerProperty() {
		super();
	}

	public JsonIntegerProperty(int initialValue) {
		super(initialValue);
	}

	public JsonIntegerProperty(Object bean, String name, int initialValue) {
		super(bean, name, initialValue);
	}

	public JsonIntegerProperty(Object bean, String name) {
		super(bean, name);
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof IntegerProperty jip) {
			return this.get() == jip.get() && this.getName().equals(jip.getName());
		}
		return false;
	}
}
