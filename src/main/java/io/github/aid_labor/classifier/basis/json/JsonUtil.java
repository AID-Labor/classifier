
package io.github.aid_labor.classifier.basis.json;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;


public final class JsonUtil {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static JsonFactory fabrik;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Gibt eine wiederverwendbare Singleton-Instanz von JsonFactory zurueck.
	 * Es sind folgende zusaetzliche JsonFeatures aktiviert:
	 * <ul>
	 * <li>{@link JsonReadFeature#ALLOW_MISSING_VALUES}</li>
	 * <li>{@link SerializationFeature#INDENT_OUTPUT}</li>
	 * </ul>
	 * 
	 * @return wiederverwendbare Singleton-Instanz von JsonFactory
	 */
	public static JsonFactory getJsonFabrik() {
		if (fabrik == null) {
			var mapper = JsonMapper.builder()
					.enable(JsonReadFeature.ALLOW_MISSING_VALUES)
					.enable(SerializationFeature.INDENT_OUTPUT)
					.build();
			mapper.findAndRegisterModules();
			fabrik = new JsonFactory(mapper);
		}
		
		return fabrik;
	}
	
	/**
	 * Erzeugt einen neuen UTF-8 codierten JsonGenerator, mit dem semantische json-Dateien
	 * generiert werden koennen. Zum Erzeugen wird eine Singleton Instanz von JsonFactory
	 * genutzt (siehe {@link #getJsonFabrik()}.
	 * 
	 * @param zielDateipfad Speicherort der json-Datei
	 * @return neuer UTF-8 codierter JsonGenerator mit dem uebergebenen Speicherort
	 * @throws IOException Falls kein Stream fuer den uebergebenen Pfad geoeffnet werden kann
	 */
	public static JsonGenerator getUTF8JsonGenerator(Path zielDateipfad) throws IOException {
		Files.createDirectories(zielDateipfad.getParent());
		BufferedWriter speicherziel = Files.newBufferedWriter(zielDateipfad,
				StandardCharsets.UTF_8);
		return getJsonFabrik().createGenerator(speicherziel).useDefaultPrettyPrinter();
	}
	
	/**
	 * Erzeugt einen neuen UTF-8 codierten JsonParser, mit dem json-Dateien ausgelesen werden
	 * koennen. Zum Erzeugen wird eine Singleton Instanz von JsonFactory
	 * genutzt (siehe {@link #getJsonFabrik()}.
	 * 
	 * @param quellDateipfad json-Datei, die eingelesen wird
	 * @return neuer UTF-8 codierter JsonParser fuer den uebegebenen Pfad
	 * @throws IOException Falls kein Stream fuer den uebergebenen Pfad geoeffnet werden kann
	 */
	public static JsonParser getUTF8JsonParser(Path quellDateipfad) throws IOException {
		BufferedReader quelle = Files.newBufferedReader(quellDateipfad,
				StandardCharsets.UTF_8);
		return getJsonFabrik().createParser(quelle);
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
	private JsonUtil() {
		// Hilfsklasse, nicht instanziierbar!
		throw new UnsupportedOperationException("Hilfsklasse darf nicht instanziiert werden!");
	}
}
