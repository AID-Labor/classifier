/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.io.Theme;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty;
import io.github.aid_labor.classifier.basis.json.JsonLocaleProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.json.JsonUtil;

// @formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		creatorVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
// @formatter:on
public class Einstellungen {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static Logger log = Logger.getLogger(Einstellungen.class.getName());
	
	private static Einstellungen defaultInstanz;
	private static Einstellungen benutzerInstanz;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
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
		Einstellungen einstellungen;
		try (JsonParser parser = JsonUtil.getUTF8JsonParser(quelle)) {
			einstellungen = parser.readValueAs(Einstellungen.class);
			return Optional.of(einstellungen);
		} catch (Exception exc) {
			log.log(Level.INFO, exc, () -> "Laden der Einstellungen nicht erfolgreich");
			return Optional.empty();
		}
		
	}
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                                    #
// #	Instanzen																		#
// #                                                                                    #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public final JsonEnumProperty<Theme> themeEinstellung;
	public final JsonLocaleProperty sprachEinstellung;
	public final JsonStringProperty letzterSpeicherortEinstellung;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// Singleton bzw. Multiton-Muster
	private Einstellungen() {
		this.themeEinstellung = new JsonEnumProperty<Theme>(Theme.SYSTEM);
		this.sprachEinstellung = new JsonLocaleProperty(Locale.GERMAN);
		this.letzterSpeicherortEinstellung = new JsonStringProperty(
				OS.getDefault().getNutzerOrdner());
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
}
