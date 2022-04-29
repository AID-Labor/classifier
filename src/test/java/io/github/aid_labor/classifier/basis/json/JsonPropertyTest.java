/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonGenerator;

import javafx.beans.property.Property;


class JsonPropertyTest {
	
	@Test
	void testJsonBooleanProperty1() {
		var testObjekt = new JsonBooleanProperty(true);
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonBooleanProperty2() {
		var testObjekt = new JsonBooleanProperty(false);
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonIntegerProperty() {
		var testObjekt = new JsonIntegerProperty(5);
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonDoubleProperty() {
		var testObjekt = new JsonDoubleProperty(1.23);
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonStringProperty() {
		var testObjekt = new JsonStringProperty("JsonTest");
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonStringPropertyEmpty() {
		var testObjekt = new JsonStringProperty("");
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonStringPropertyNull() {
		var testObjekt = new JsonStringProperty(null);
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonObjectPropertyString() {
		var testObjekt = new JsonObjectProperty<String>("ObjektTest");
		testPropertySerialisation(testObjekt);
	}
	
	@Test
	void testJsonObjectPropertyNull() {
		var testObjekt = new JsonObjectProperty<String>(null);
		testPropertySerialisation(testObjekt);
	}
	
	public enum TestEnum {
		ENUM_TEST;
	}
	
	@Test
	void testJsonObjectPropertyEnum() {
		JsonEnumProperty<TestEnum> testEnum = new JsonEnumProperty<>(TestEnum.ENUM_TEST);
		testPropertySerialisation(testEnum);
	}
	
	@Test
	void testJsonObjectPropertyList() {
		var liste = new LinkedList<Integer>();
		liste.add(5);
		liste.add(6);
		liste.add(7);
		var testObjekt = new JsonObjectProperty<LinkedList<Integer>>(liste);
		testPropertySerialisation(testObjekt);
	}
	
	<T> void testPropertySerialisation(Property<T> testObjekt) {
		try {
			var datei = Files.createTempFile("JsonIntegerProperty", "json");
			try (JsonGenerator generator = JsonUtil.getUTF8JsonGenerator(datei)) {
				generator.writeObject(testObjekt);
				generator.close();
			}
			
			try (BufferedReader in = new BufferedReader(new FileReader(datei.toFile()))) {
				String zeile;
				while ((zeile = in.readLine()) != null) {
					System.out.println();
					System.out.print(zeile);
				}
				if (testObjekt.getValue() != null) {
					System.out.print(" (" + testObjekt.getValue().getClass() + ")");
				}
				System.out.println();
			}
			
			try (var parser = JsonUtil.getUTF8JsonParser(datei)) {
				var rekonstruiert = parser.readValueAs(testObjekt.getClass());
				Files.deleteIfExists(datei);
				System.out.print("  -> " + rekonstruiert.getValue());
				if (rekonstruiert.getValue() != null) {
					System.out.print(" (" + rekonstruiert.getValue().getClass() + ")");
				}
				System.out.println("\n");
				if (testObjekt.getValue() instanceof Iterable<?> it1
						&& rekonstruiert.getValue() instanceof Iterable<?> it2) {
					assertIterableEquals(it1, it2);
				} else if (Objects.nonNull(testObjekt.getValue())
						&& Objects.nonNull(rekonstruiert.getValue())
						&& testObjekt.getValue().getClass().isArray()
						&& rekonstruiert.getValue().getClass().isArray()) {
					assertTrue(
							Objects.deepEquals(testObjekt.getValue(), testObjekt.getValue()));
				} else {
					assertEquals(testObjekt.getValue(), rekonstruiert.getValue());
				}
			}
			
		} catch (IOException e) {
			fail(e);
		}
	}
	
}