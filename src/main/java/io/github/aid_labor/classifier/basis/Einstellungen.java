/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.github.aid_labor.classifier.basis.json.JsonEnumProperty;
import io.github.aid_labor.classifier.basis.json.JsonObjectProperty;
import io.github.aid_labor.classifier.basis.json.JsonUtil;
import javafx.beans.property.ObjectProperty;


@JsonAutoDetect(getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE, creatorVisibility = Visibility.NONE, fieldVisibility = Visibility.ANY)
public class Einstellungen {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

	private static Logger log = Logger.getLogger(Einstellungen.class.getName());
	
	private static Einstellungen defaultInstanz;
	private static Einstellungen benutzerInstanz;
	
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public static Einstellungen getDefault() {
		if (defaultInstanz == null) {
			log.config(() -> "erzeuge Default-Einstellungen");
			defaultInstanz = new Einstellungen();
		}
		
		return defaultInstanz;
	}
	
	public static Einstellungen getBenutzerdefiniert() {
		if (benutzerInstanz == null) {
			log.config(() -> "erzeuge benutzerdefinierte Einstellungen");
			benutzerInstanz = laden(Ressourcen.get().NUTZER_EINSTELLUNGEN.alsPath())
					.orElse(new Einstellungen());
		}
		
		return benutzerInstanz;
	}
	
	public static boolean speicherBenutzerdefiniert() {
		return speichern(benutzerInstanz, Ressourcen.get().NUTZER_EINSTELLUNGEN.alsPath());
	}
	
	public static boolean resetBenutzerdefiniert() {
		log.config(() -> "Benutzereinstellungen zuruecksetzen");
		var benutzerInstanzNeu = laden(Ressourcen.get().NUTZER_EINSTELLUNGEN.alsPath());
		if (benutzerInstanzNeu.isPresent()) {
			benutzerInstanz = benutzerInstanzNeu.get();
			log.config(() -> "Reset erfolgreich");
			return true;
		} else {
			log.warning(() -> "Reset nicht erfolgreich");
			return false;
		}
	}
	
	// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	private static boolean speichern(Einstellungen e, Path ziel) {
		try (JsonGenerator generator = JsonUtil.getUTF8JsonGenerator(ziel)) {
			generator.writePOJO(e);
			generator.flush();
		} catch (Exception exc) {
			log.log(Level.WARNING, exc, () -> "Speichern der Einstellungen nicht erfolgreich");
			return false;
		}
		
		return true;
	}
	
	private static Optional<Einstellungen> laden(Path quelle) {
		Einstellungen e;
		try (JsonParser parser = JsonUtil.getUTF8JsonParser(quelle)) {
			e = parser.readValueAs(Einstellungen.class);
		} catch (Exception exc) {
			log.log(Level.WARNING, exc, () -> "Laden der Einstellungen nicht erfolgreich");
			return Optional.empty();
		}
		
		return Optional.of(e);
	}
	
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                                    #
// #	Instanzen																		#
// #                                                                                    #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public final ObjectProperty<Level> logLevelDatei;
	public final ObjectProperty<Level> logLevelKonsole;
	public final ObjectProperty<Theme> themeEinstellung;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// Singleton bzw. Multiton-Muster
	private Einstellungen() {
		this.logLevelDatei = new JsonObjectProperty<>(Level.INFO);
		this.logLevelKonsole = new JsonObjectProperty<>(Level.WARNING);
		this.themeEinstellung = new JsonEnumProperty<Theme>(Theme.SYSTEM);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
}
