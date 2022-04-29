
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public final class JsonUtil {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	private static JsonFactory fabrik;

	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	public static JsonFactory getJsonFabrik() {
		if (fabrik == null) {
			var mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			fabrik = new JsonFactory(mapper);
		}
		
		return fabrik;
	}
	
	public static JsonGenerator getUTF8JsonGenerator(Path zielDateipfad) throws IOException {
		BufferedWriter speicherziel = Files.newBufferedWriter(zielDateipfad,
				StandardCharsets.UTF_8);
		return getJsonFabrik().createGenerator(speicherziel).useDefaultPrettyPrinter();
	}
	
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
		// statische Hilfsklasse, keine Instanzen erlaubt!
	}
}
