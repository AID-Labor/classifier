/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

@JsonSerialize(
		contentAs=Double.class,
		converter=JsonDoubleProperty.PropertyZuDoubleKonverter.class
)
@JsonDeserialize (
		converter=JsonDoubleProperty.DoubleZuPropertyKonverter.class
)
public class JsonDoubleProperty extends SimpleDoubleProperty {
	public static class PropertyZuDoubleKonverter extends StdConverter<DoubleProperty, Double> {
		@Override
		public Double convert(DoubleProperty value) {
			return value.get();
		}
	}
	
	public static class DoubleZuPropertyKonverter extends StdConverter<Double, DoubleProperty> {
		@Override
		public DoubleProperty convert(Double value) {
			return new JsonDoubleProperty(value);
		}
	}

	public JsonDoubleProperty() {
		super();
	}

	public JsonDoubleProperty(double initialValue) {
		super(initialValue);
	}

	public JsonDoubleProperty(Object bean, String name, double initialValue) {
		super(bean, name, initialValue);
	}

	public JsonDoubleProperty(Object bean, String name) {
		super(bean, name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( obj instanceof DoubleProperty jip) {
			return this.get() == jip.get() && this.getName().equals(jip.getName());
		}
		return false;
	}
}
