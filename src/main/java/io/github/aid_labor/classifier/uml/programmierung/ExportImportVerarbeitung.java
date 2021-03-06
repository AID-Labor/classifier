/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.programmierung;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import javafx.stage.FileChooser.ExtensionFilter;


public interface ExportImportVerarbeitung {
	
	public static class ImportErgebnis {
		private final List<UMLKlassifizierer> neueKlassifizierer;
		private final List<UMLVerbindung> neueAssoziationen;
		private final Exception fehler;
		private boolean istFehlerBehandelt;
		
		public ImportErgebnis(List<UMLKlassifizierer> neueKlassifizierer, List<UMLVerbindung> neueAssoziationen,
				Exception fehler) {
			super();
			this.neueKlassifizierer = Collections.unmodifiableList(neueKlassifizierer);
			this.neueAssoziationen = Collections.unmodifiableList(neueAssoziationen);
			this.fehler = fehler;
			this.istFehlerBehandelt = fehler == null;
		}
		
		public List<UMLVerbindung> getNeueAssoziationen() {
			return neueAssoziationen;
		}
		
		public List<UMLKlassifizierer> getNeueKlassifizierer() {
			return neueKlassifizierer;
		}
		
		public Exception getFehler() {
			return fehler;
		}
		
		public boolean istFehlerBehandelt() {
			return istFehlerBehandelt;
		}
		
		public void setIstFehlerBehandelt(boolean istFehlerBehandelt) {
			this.istFehlerBehandelt = istFehlerBehandelt;
		}
	}
	
	/**
	 * Erzeugt aus einem Klassifizierer Quellcode und Schreibt diesen in den Writer.
	 * 
	 * @param klassifizierer   Klassifizierer, der implementiert wird
	 * @param ausgabe          Stream, in den der Quellcode geschrieben wird
	 * @throws Exception Exception, die möglicherweise beim Export auftritt.
	 */
	public void exportiere(UMLKlassifizierer klassifizierer, Writer ausgabe) throws Exception;
	
	/**
	 * Erzeugt aus einem oder mehreren Klassifizierern Quellcode und speichert diesen als Dateien im übergebenen
	 * Ordner
	 * 
	 * @param klassifiziererSammlung Klassifizierer, die implementiert werden
	 * @param ordner                 Ordner, in dem die Quellcode-Dateien gespeichert werden
	 * @throws Exception Exception mit allen aufgetretenen Fehlern. Es können mehrere Exceptions auftreten, da bei einem
	 *                   Fehler weiterhin versucht wird den nächsten Klassifizierer zu exportieren. Die erste
	 *                   aufgetretene Exception wird in {@link Exception#getCause()} festgehalten, alle weiteren
	 *                   Exceptions sind mit {@link Exception#getSuppressed()} abrufbar.
	 */
	public default void exportiere(Iterable<UMLKlassifizierer> klassifiziererSammlung, File ordner) throws Exception {
		Queue<Exception> exceptions = new LinkedList<>();
		for (var klassifzierer : klassifiziererSammlung) {
			File datei = new File(ordner, erzeugeDateiName(klassifzierer));
			datei.getParentFile().mkdirs();
			try (var ausgabe = new FileWriter(datei)) {
				exportiere(klassifzierer, ausgabe);
			} catch (Exception e) {
				exceptions.add(e);
			}
		}
		
		if (!exceptions.isEmpty()) {
			var ex = new Exception("Beim Export sind " + exceptions.size() + " Fehler aufgetreten", exceptions.poll());
			while (!exceptions.isEmpty()) {
				ex.addSuppressed(exceptions.poll());
			}
			throw ex;
		}
	}
	
	/**
	 * Importiert Quellcode und erzeugt daraus einen UMLKlassifizierer.
	 * Optional können zugehörige Verbindungen erzeugt und in die übergebene Liste gelegt werden.
	 * <b>
	 * Vererbungen werden automatisch durch die Eigenschaften {@link UMLKlassifizierer#superklasseProperty()} und
	 * {@link UMLKlassifizierer#getInterfaces()} hinzugefügt. Die Liste {@code verbindungen} wird nur für weitere
	 * Assoziationen bereitgestellt.
	 * </b>
	 * 
	 * @param quelle       Quelldatei, die importiert wird
	 * @return importierter UMLKlassifizierer
	 * @throws ImportException Wenn die Datei keinen gültigen Programmcode enthält oder aus einem anderen Grund nicht
	 *                         interpretiert werden kann
	 * @throws IOException     Wenn beim Lesen der Datei ein Fehler auftritt
	 */
	public ImportErgebnis importiere(File quelle) throws ImportException, IOException;
	
	/**
	 * Erzeugt einen Dateinamen für den Export auf Basis der Konventionen für die jeweilige Programmiersprache
	 * 
	 * @param klassifizierer Klassifizierer, für den der Dateiname ermittelt wird
	 * @return Dateinamen für den Export
	 */
	public String erzeugeDateiName(UMLKlassifizierer klassifizierer);
	
	/**
	 * Unterstützte Dateierweiterungen für Import
	 * 
	 * @return unterstützte Dateierweiterungen für Import
	 */
	public Collection<ExtensionFilter> getImportDateierweiterungen();
	
}