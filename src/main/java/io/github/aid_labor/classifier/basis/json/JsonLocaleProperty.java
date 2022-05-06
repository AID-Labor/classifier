/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.json;

import java.util.Locale;
import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


@JsonSerialize(contentAs = String.class, converter = JsonLocaleProperty.PropertyZuStringKonverter.class)
@JsonDeserialize(converter = JsonLocaleProperty.StringZuPropertyKonverter.class)
public class JsonLocaleProperty extends SimpleObjectProperty<Locale> {
	
	public static class PropertyZuStringKonverter extends StdConverter<ObjectProperty<Locale>, String> {
		@Override
		public String convert(ObjectProperty<Locale> value) {
			return value.get().toLanguageTag();
		}
	}
	
	public static class StringZuPropertyKonverter extends StdConverter<String, ObjectProperty<Locale>> {
		@Override
		public ObjectProperty<Locale> convert(String value) {
			Locale l = Locale.forLanguageTag(value);
			return new JsonLocaleProperty(l);
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
	
	public JsonLocaleProperty() {
		super();
	}
	
	public JsonLocaleProperty(Object bean, String name, Locale initialValue) {
		super(bean, name, initialValue);
	}
	
	public JsonLocaleProperty(Object bean, String name) {
		super(bean, name);
	}
	
	public JsonLocaleProperty(Locale initialValue) {
		super(initialValue);
	}
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectProperty<?> jip) {
			return Objects.equals(this.get(), jip.get())
					&& this.getName().equals(jip.getName());
		}
		return false;
	}
}