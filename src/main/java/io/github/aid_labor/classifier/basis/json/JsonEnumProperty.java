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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;


// @formatter:off
@JsonSerialize(
		contentAs = String.class,
		converter = JsonEnumProperty.PropertyZuTypKonverter.class
)
@JsonDeserialize(converter = JsonEnumProperty.EnumZuPropertyKonverter.class)
// @formatter:on
public class JsonEnumProperty<T extends Enum<T>> extends SimpleObjectProperty<T> {
	
	public static class PropertyZuTypKonverter<T extends Enum<T>>
			extends StdConverter<ObjectProperty<T>, String> {
		@Override
		public String convert(ObjectProperty<T> value) {
			String klasse = value.getValue().getClass().getName();
			String wert = value.get().name();
			return klasse + "." + wert;
		}
	}
	
	public static class EnumZuPropertyKonverter<T extends Enum<T>>
			extends StdConverter<String, ObjectProperty<T>> {
		@SuppressWarnings("unchecked")
		@Override
		public ObjectProperty<T> convert(String value) {
			int klassenEnde = value.lastIndexOf(".");
			String klasse = value.substring(0, klassenEnde);
			String wert = value.substring(klassenEnde + 1, value.length());
			T enumeration = null;
			try {
				enumeration = Enum.valueOf((Class<T>) Class.forName(klasse), wert);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return new JsonEnumProperty<>(enumeration);
		}
	}
	
	/**
	 * Alternativer Konverter, der genutzt werden kann, um nur den Namen der Enumeration zu
	 * Serialisieren
	 * @author Tim Muehle
	 *
	 * @param <T> Enum-Typ
	 */
	public static class EnumPropertyZuStringKonverter<T extends Enum<T>>
			extends StdConverter<ObjectProperty<T>, String> {
		@Override
		public String convert(ObjectProperty<T> value) {
			return value.get().name();
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
	
	public JsonEnumProperty() {
		super();
	}
	
	public JsonEnumProperty(Object bean, String name, T initialValue) {
		super(bean, name, initialValue);
	}
	
	public JsonEnumProperty(Object bean, String name) {
		super(bean, name);
	}
	
	public JsonEnumProperty(T initialValue) {
		super(initialValue);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	@Override
	public int hashCode() {
		return Objects.hashCode(get());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectProperty<?> jip) {
			return Objects.equals(this.get(), jip.get())
					&& this.getName().equals(jip.getName());
		}
		return false;
	}
}