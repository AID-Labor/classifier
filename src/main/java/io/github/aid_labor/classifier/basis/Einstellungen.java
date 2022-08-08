/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.io.Theme;
import io.github.aid_labor.classifier.basis.io.system.OS;
import io.github.aid_labor.classifier.basis.json.JsonBooleanProperty;
import io.github.aid_labor.classifier.basis.json.JsonDoubleProperty;
import io.github.aid_labor.classifier.basis.json.JsonEnumProperty;
import io.github.aid_labor.classifier.basis.json.JsonIntegerProperty;
import io.github.aid_labor.classifier.basis.json.JsonLocaleProperty;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.json.JsonUtil;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;


/**
 * Speicherung aller Einstellungen, die von der:dem Nutzer:in personalisiert werden kann.
 * Diese Klasse ist als 'Multition-Pattern' ausgelegt. Es gibt eine Instanz für die personalisierten Einstellungen
 * und eien Instanz für die Default-Einstellungen, die jeweils über eine Klassenmethode erreicht werden können.
 */
// @formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		creatorVisibility = Visibility.NON_PRIVATE,
		fieldVisibility = Visibility.ANY
)
// @formatter:on
public class Einstellungen {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private static Logger log = Logger.getLogger(Einstellungen.class.getName());
	
	private static Einstellungen benutzerInstanz;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	/**
	 * Default-Einstellungen. Diese Methode erzeugt bei jedem Aufruf ein neues Objekt, um sicherzustellen, dass
	 * keine Änderungen stattgefunden haben.
	 * 
	 * @return Ausgangs-Einstellungen
	 */
	public static Einstellungen getStandard() {
		return new Einstellungen();
	}
	
	/**
	 * Personalisierte Einstellungen als geteilte Singleton-Instanz.
	 * 
	 * @return benutzerdefinierte Einstellungen
	 */
	public static Einstellungen getBenutzerdefiniert() {
		if (benutzerInstanz == null) {
			log.config(() -> "erzeuge benutzerdefinierte Einstellungen");
			benutzerInstanz = laden(Ressourcen.get().NUTZER_EINSTELLUNGEN.alsPath()).orElse(new Einstellungen());
		}
		
		return benutzerInstanz;
	}
	
	/**
	 * Speichert die benutzerdefinierte Einstellungen als JSON-Datei. Der Speicherort ist in
	 * {@link Ressourcen#NUTZER_EINSTELLUNGEN} festgelegt
	 * 
	 * @return {@code true}, wenn das Speichern erfolgreich war, sonst {@code false}
	 */
	public static boolean speicherBenutzerdefiniert() {
		return speichern(benutzerInstanz, Ressourcen.get().NUTZER_EINSTELLUNGEN.alsPath());
	}
	
	/**
	 * Setzt die benutzerdefinierten Einstellungen auf den Ausgangszustand zurück.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void resetBenutzerdefiniert() {
		log.config(() -> "Benutzereinstellungen zuruecksetzen");
		Einstellungen standard = getStandard();
		
		for (Field attribut : Einstellungen.class.getDeclaredFields()) {
			if (Modifier.isStatic(attribut.getModifiers())) {
				continue;
			}
			
			if (attribut.trySetAccessible()) {
				try {
					if (attribut.get(benutzerInstanz) instanceof Property eigenschaft
							&& attribut.get(standard) instanceof Property eigenschaftStandard) {
						// beide Eigenschaften haben zwingend den gleichen Generic-Typ!
						eigenschaft.setValue(eigenschaftStandard.getValue());
					}
				} catch (Exception e) {
					log.log(Level.WARNING, e, () -> "Fehler beim zuruecksetzen von " + attribut.getName());
				}
			}
			
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
	
	private final JsonEnumProperty<Theme> themeProperty;
	private final JsonLocaleProperty sprachProperty;
	private final JsonStringProperty letzterSpeicherortProperty;
	private final ObservableSet<DatumWrapper<Path>> letzteDateienProperty;
	private final JsonIntegerProperty verlaufAnzahlProperty;
	private final JsonBooleanProperty zeigePackageModifierProperty;
	private final JsonBooleanProperty zeigeVoidProperty;
	private final JsonBooleanProperty zeigeParameterNamenProperty;
	private final JsonBooleanProperty erweiterteValidierungAktivierenProperty;
	private final JsonEnumProperty<Theme> exportThemeProperty;
	private final JsonDoubleProperty exportSkalierungProperty;
	private final JsonBooleanProperty exportTransparentProperty;
	private final JsonStringProperty letzterBildSpeicherortProperty;
	private final JsonStringProperty letzterQuellcodeSpeicherortProperty;
	private final JsonBooleanProperty linienRasterungAktivierenProperty;
	private final JsonBooleanProperty positionRasterungAktivierenProperty;
	private final JsonBooleanProperty groesseRasterungAktivierenProperty;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	// Singleton bzw. Multiton-Muster
	private Einstellungen() {
		this.themeProperty = new JsonEnumProperty<Theme>(Theme.SYSTEM);
		this.sprachProperty = new JsonLocaleProperty(Locale.GERMAN);
		this.letzterSpeicherortProperty = new JsonStringProperty(OS.getDefault().getDokumenteOrdner());
		this.letzteDateienProperty = FXCollections
				.observableSet(new AutomatischEntfernendesSet<DatumWrapper<Path>>(15) {
					private static final long serialVersionUID = -7312452108634427547L;
					
					@Override
					public boolean add(DatumWrapper<Path> e) {
						if (this.contains(e)) {
							return false;
						}
						return super.add(e);
					}
					
					@Override
					public boolean contains(Object o) {
						var<DatumWrapper<Path>> elemente = this.iterator();
						while (elemente.hasNext()) {
							if (elemente.next().equals(o)) {
								return true;
							}
						}
						return false;
					}
					
					@Override
					public boolean remove(Object o) {
						var<DatumWrapper<Path>> elemente = this.iterator();
						while (elemente.hasNext()) {
							var element = elemente.next();
							if (element.equals(o)) {
								return super.remove(element);
							}
						}
						return false;
					}
				});
		this.verlaufAnzahlProperty = new JsonIntegerProperty(100);
		this.zeigePackageModifierProperty = new JsonBooleanProperty(true);
		this.zeigeVoidProperty = new JsonBooleanProperty(false);
		this.zeigeParameterNamenProperty = new JsonBooleanProperty(true);
		this.erweiterteValidierungAktivierenProperty = new JsonBooleanProperty(true);
		this.exportThemeProperty = new JsonEnumProperty<Theme>(Theme.LIGHT);
		this.exportSkalierungProperty = new JsonDoubleProperty(1);
		this.exportTransparentProperty = new JsonBooleanProperty(false);
		this.letzterBildSpeicherortProperty = new JsonStringProperty(null);
		this.letzterQuellcodeSpeicherortProperty = new JsonStringProperty(null);
		this.linienRasterungAktivierenProperty = new JsonBooleanProperty(true);
		this.positionRasterungAktivierenProperty = new JsonBooleanProperty(true);
		this.groesseRasterungAktivierenProperty = new JsonBooleanProperty(true);
	}
	
	/**
	 * Konstruktor fuer Jackson, zum Lesen aus json-Datei
	 * 
	 * @param letzteDateien
	 */
	@JsonCreator
	Einstellungen(@JsonProperty("letzteDateienProperty") List<DatumWrapper<Path>> letzteDateienProperty) {
		this();
		this.letzteDateienProperty
				.addAll(letzteDateienProperty.stream().filter(eintrag -> Files.exists(eintrag.getElement())).toList());
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	/**
	 * Verwendetes Layout-Theme
	 * 
	 * @return themeProperty
	 */
	public final JsonEnumProperty<Theme> themeProperty() {
		return themeProperty;
	}
	
	/**
	 * Verwendete Programmsprache
	 * 
	 * @return sprachProperty
	 */
	public final JsonLocaleProperty sprachProperty() {
		return sprachProperty;
	}
	
	/**
	 * letzter verwendeter Speicherort vom Oeffnen oder Speichern eines Projektes
	 * 
	 * @return letzterSpeicherortProperty
	 */
	public final JsonStringProperty letzterSpeicherortProperty() {
		return letzterSpeicherortProperty;
	}
	
	/**
	 * Letzte verwendete Datei sortiert nach dem Zeitpunkt des Hinzufuegens. Es werden nur die
	 * letzten 10 Dateien behalten. Gelöschte Dateien werden beim Laden der Einstellungen ignoriert.
	 * 
	 * @return letzteDateienProperty
	 */
	public final ObservableSet<DatumWrapper<Path>> letzteDateienProperty() {
		return letzteDateienProperty;
	}
	
	/**
	 * Anzahl der Elemente, die im Verlauf zum Rueckgaengig machen bzw. Wiederholen
	 * gespeichert werden
	 * 
	 * @return verlaufAnzahlProperty
	 */
	public final JsonIntegerProperty verlaufAnzahlProperty() {
		return verlaufAnzahlProperty;
	}
	
	/**
	 * {@code true}, wenn der Modifizierer 'package' im UML-Diagramm angezeigt werden soll
	 * 
	 * @return zeigePackageModifierProperty
	 */
	public final JsonBooleanProperty zeigePackageModifierProperty() {
		return zeigePackageModifierProperty;
	}
	
	/**
	 * {@code true}, wenn 'void' im UML-Diagramm angezeigt werden soll
	 * 
	 * @return zeigeVoidProperty
	 */
	public final JsonBooleanProperty zeigeVoidProperty() {
		return zeigeVoidProperty;
	}
	
	/**
	 * {@code true}, wenn Parameternamen im UML-Diagramm angezeigt werden sollen
	 * 
	 * @return zeigeParameterNamenProperty
	 */
	public final JsonBooleanProperty zeigeParameterNamenProperty() {
		return zeigeParameterNamenProperty;
	}
	
	/**
	 * {@code true}, wenn die erweiterte Eingabevalidierung im UML-Diagramm aktiviert ist
	 * 
	 * @return erweiterteValidierungAktivierenProperty
	 */
	public final JsonBooleanProperty erweiterteValidierungAktivierenProperty() {
		return erweiterteValidierungAktivierenProperty;
	}
	
	public final JsonBooleanProperty linienRasterungAktivierenProperty() {
		return linienRasterungAktivierenProperty;
	}
	
	public final JsonBooleanProperty positionRasterungAktivierenProperty() {
		return positionRasterungAktivierenProperty;
	}
	
	public final JsonBooleanProperty groesseRasterungAktivierenProperty() {
		return groesseRasterungAktivierenProperty;
	}
	
	public final JsonEnumProperty<Theme> exportThemeProperty() {
		return exportThemeProperty;
	}
	
	public final JsonBooleanProperty exportTransparentProperty() {
		return exportTransparentProperty;
	}
	
	public final JsonDoubleProperty exportSkalierungProperty() {
		return exportSkalierungProperty;
	}
	
	public final JsonStringProperty letzterBildSpeicherortProperty() {
		return letzterBildSpeicherortProperty;
	}
	
	public final JsonStringProperty letzterQuellcodeSpeicherortProperty() {
		return letzterQuellcodeSpeicherortProperty;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
}
