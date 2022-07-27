/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.oe;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.BiConsumer;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.projekt.UeberwachungsStatus;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.ProjektAnsicht;
import io.github.aid_labor.classifier.gui.ProjekteAnsicht;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import javafx.beans.binding.Bindings;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class KlassifiziererKontextMenue extends ContextMenu {
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenmethoden																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final Sprache sprache;
	private final ProjektAnsicht projektAnsicht;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Bearbeiten
	private MenuItem kopieren;
	private MenuItem einfuegen;
	private MenuItem loeschen;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Anordnen
	private MenuItem anordnenNachVorne;
	private MenuItem anordnenNachGanzVorne;
	private MenuItem anordnenNachHinten;
	private MenuItem anordnenNachGanzHinten;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Export
	private MenuItem exportierenBild;
	private MenuItem exportierenQuellcode;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public KlassifiziererKontextMenue(ProjektAnsicht projektAnsicht) {
		this.sprache = new Sprache();
		this.projektAnsicht = projektAnsicht;
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"KlassifiziererKontextMenue");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		this.getItems().addAll(erstelleItems());
		setzeActions();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private List<MenuItem> erstelleItems() {
		kopieren = SprachUtil.bindText(new MenuItem(), sprache, "kopieren", "Kopieren");
		einfuegen = SprachUtil.bindText(new MenuItem(), sprache, "einfuegen", "Einfuegen");
		loeschen = SprachUtil.bindText(new MenuItem(), sprache, "loeschen", "L%cschen".formatted(oe));
		
		anordnenNachVorne = SprachUtil.bindText(new MenuItem(), sprache, "nachVorne", "Eine Ebene nach vorne");
		anordnenNachGanzVorne = SprachUtil.bindText(new MenuItem(), sprache, "nachGanzVorne", "In den Vordergrund");
		anordnenNachHinten = SprachUtil.bindText(new MenuItem(), sprache, "nachHinten", "Eine Ebene nach hinten");
		anordnenNachGanzHinten = SprachUtil.bindText(new MenuItem(), sprache, "nachGanzHinten", "In den Hintergrund");
		
		Menu dateiExportieren = SprachUtil.bindText(new Menu(), sprache, "exportieren", "Exportieren");
		exportierenBild = SprachUtil.bindText(new MenuItem(), sprache, "exportierenBild", "als Bild...");
		exportierenQuellcode = SprachUtil.bindText(new MenuItem(), sprache, "exportierenQuellcode", "als Quellcode...");
		dateiExportieren.getItems().addAll(exportierenBild, exportierenQuellcode);
		
		return List.of(kopieren, einfuegen, loeschen, new SeparatorMenuItem(), anordnenNachVorne, anordnenNachGanzVorne,
				anordnenNachHinten, anordnenNachGanzHinten, new SeparatorMenuItem(), dateiExportieren);
	}
	
	private void setzeActions() {
		// Menue Bearbeiten
		kopieren.setOnAction(e -> projektAnsicht.getProjekteAnsicht().auswahlKopieren());
		einfuegen.setOnAction(e -> projektAnsicht.getProjekteAnsicht().auswahlEinfuegen());
		loeschen.setOnAction(e -> projektAnsicht.getProjekteAnsicht().auswahlLoeschen());
		einfuegen.disableProperty().bind(Bindings.isEmpty(ProjekteAnsicht.getKopiepuffer()));
		
		// Menue Anordnen
		anordnenNachVorne.setOnAction(e -> nachVorne());
		anordnenNachHinten.setOnAction(e -> nachHinten());
		anordnenNachGanzVorne.setOnAction(e -> nachGanzVorne());
		anordnenNachGanzHinten.setOnAction(e -> nachGanzHinten());
		
		// Exportieren
		exportierenBild.setOnAction(this.projektAnsicht::exportiereAlsBild);
		exportierenQuellcode.setOnAction(this.projektAnsicht::exportiereAlsQuellcode);
	}
	
	private void nachVorne() {
		SortedSet<Integer> indizesRueckwaerts = getIndizes(Comparator.reverseOrder());
		
		var projekt = projektAnsicht.getProjekt();
		var diagrammElemente = projekt.getDiagrammElemente();
		var status = projekt.getUeberwachungsStatus();
		projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
		
		for (int index : indizesRueckwaerts) {
			if (index < diagrammElemente.size() - 1) {
				var a = diagrammElemente.remove(index);
				diagrammElemente.add(index + 1, a);
			}
		}
		projekt.setUeberwachungsStatus(status);
	}
	
	private void nachHinten() {
		SortedSet<Integer> indizes = getIndizes();
		
		var projekt = projektAnsicht.getProjekt();
		var diagrammElemente = projekt.getDiagrammElemente();
		var status = projekt.getUeberwachungsStatus();
		projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
		
		for (int index : indizes) {
			if (index > 0) {
				var a = diagrammElemente.remove(index);
				diagrammElemente.add(index - 1, a);
			}
		}
		projekt.setUeberwachungsStatus(status);
	}
	
	private void nachGanzVorne() {
		verschiebeAuswahl((auswahl, projekt) -> projekt.getDiagrammElemente().addAll(auswahl));
	}
	
	private void nachGanzHinten() {
		verschiebeAuswahl((auswahl, projekt) -> projekt.getDiagrammElemente().addAll(0, auswahl));
	}
	
	private SortedSet<Integer> getIndizes() {
		return this.getIndizes(Comparator.naturalOrder());
	}
	
	private SortedSet<Integer> getIndizes(Comparator<Integer> sortierVergleich) {
		SortedSet<Integer> indizes = new TreeSet<>(sortierVergleich);
		var ids = projektAnsicht.getSelektion().stream().map(el -> el.getId()).toList();
		var elemente = this.projektAnsicht.getProjekt().getDiagrammElemente();
		int i = 0;
		int anzahlGefunden = 0;
		for (var element : elemente) {
			if (ids.contains(element.getId())) {
				indizes.add(i);
				anzahlGefunden++;
			}
			
			if (anzahlGefunden == ids.size()) {
				break;
			}
			i++;
		}
		return indizes;
	}
	
	private void verschiebeAuswahl(BiConsumer<List<UMLDiagrammElement>, UMLProjekt> wiedereinfuegenAktion) {
		SortedSet<Integer> indizesRueckwaerts = getIndizes(Comparator.reverseOrder());
		
		var projekt = projektAnsicht.getProjekt();
		var diagrammElemente = projekt.getDiagrammElemente();
		var status = projekt.getUeberwachungsStatus();
		projekt.setUeberwachungsStatus(UeberwachungsStatus.ZUSAMMENFASSEN);
		
		List<UMLDiagrammElement> neuAnordnen = new LinkedList<>();
		for (int index : indizesRueckwaerts) {
			neuAnordnen.add(diagrammElemente.remove(index));
		}
		Collections.reverse(neuAnordnen);
		wiedereinfuegenAktion.accept(neuAnordnen, projekt);
		projekt.setUeberwachungsStatus(status);
	}
}