/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.json;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// @formatter:off
@JsonSerialize(
		contentAs = String.class,
		converter = JsonStringProperty.PropertyZuStringConverter.class
)
@JsonDeserialize(converter = JsonStringProperty.StringZuPropertyConverter.class)
// @formatter:on
public class JsonStringProperty extends SimpleStringProperty {
	
	private static final String NULL_STRING = "::nullstring::";
	public static class PropertyZuStringConverter
			extends StdConverter<StringProperty, String> {
		@Override
		public String convert(StringProperty value) {
			return Objects.requireNonNullElse(value.get(), NULL_STRING);
		}
	}
	
	public static class StringZuPropertyConverter
			extends StdConverter<String, StringProperty> {
		@Override
		public StringProperty convert(String value) {
			if(value.equals(NULL_STRING)) {
				return new JsonStringProperty(null);
			} else {
				return new JsonStringProperty(value);
			}
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
		
	public JsonStringProperty() {
		super();
	}
	
	public JsonStringProperty(Object bean, String name, String initialValue) {
		super(bean, name, initialValue);
	}
	
	public JsonStringProperty(Object bean, String name) {
		super(bean, name);
	}
	
	public JsonStringProperty(String initialValue) {
		super(initialValue);
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StringProperty jip) {
			return Objects.equals(this.get(), jip.get())
					&& this.getName().equals(jip.getName());
		}
		return false;
	}
}
