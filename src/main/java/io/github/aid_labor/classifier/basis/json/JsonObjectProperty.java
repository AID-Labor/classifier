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


@JsonSerialize(converter = JsonObjectProperty.PropertyZuTypKonverter.class)
@JsonDeserialize(converter = JsonObjectProperty.TypZuPropertyKonverter.class)
public class JsonObjectProperty<T> extends SimpleObjectProperty<T> {
	
	private static final String NULL_OBJECT = "::nullobjekt::";
	public static class PropertyZuTypKonverter extends StdConverter<ObjectProperty<Object>, Object> {
		@Override
		public Object convert(ObjectProperty<Object> value) {
			return Objects.requireNonNullElse(value.get(), NULL_OBJECT);
		}
	}
	
	public static class TypZuPropertyKonverter<T> extends StdConverter<T, ObjectProperty<T>> {
		@Override
		public ObjectProperty<T> convert(T value) {
			if(value.equals(NULL_OBJECT)) {
				return new JsonObjectProperty<>(null);
			} else {
				return new JsonObjectProperty<>(value);
			}
		}
	}
	
	public JsonObjectProperty() {
		super();
	}
	
	public JsonObjectProperty(Object bean, String name, T initialValue) {
		super(bean, name, initialValue);
	}
	
	public JsonObjectProperty(Object bean, String name) {
		super(bean, name);
	}
	
	public JsonObjectProperty(T initialValue) {
		super(initialValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectProperty<?> jip) {
			return Objects.deepEquals(this.getValue(), jip.getValue())
					&& this.getName().equals(jip.getName());
		}
		return false;
	}
}