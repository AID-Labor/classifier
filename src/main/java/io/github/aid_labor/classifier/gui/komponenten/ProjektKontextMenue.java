/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.komponenten;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ae;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.oe;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.sz;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ue;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.ProjektAnsicht;
import io.github.aid_labor.classifier.gui.ProjekteAnsicht;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


public class ProjektKontextMenue extends ContextMenu {
	
	private static final Logger log = Logger.getLogger(ProjektKontextMenue.class.getName());
	
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
	// Menue Datei
	private MenuItem dateiSpeichern;
	private MenuItem dateiImportieren;
	private MenuItem exportierenBild;
	private MenuItem exportierenQuellcode;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Bearbeiten
	private MenuItem rueckgaengig;
	private MenuItem wiederholen;
	private MenuItem einfuegen;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Einfuegen
	private MenuItem klasseEinfuegen;
	private MenuItem abstraktelasseEinfuegen;
	private MenuItem interfaceEinfuegen;
	private MenuItem kommentarEinfuegen;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Darstellung
	private MenuItem darstellungGroesser;
	private MenuItem darstellungKleiner;
	private MenuItem darstellungOriginalgroesse;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public ProjektKontextMenue(ProjektAnsicht projektAnsicht) {
		this.sprache = new Sprache();
		this.projektAnsicht = projektAnsicht;
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
				"ProjektKontextMenue");
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
		dateiSpeichern = SprachUtil.bindText(new MenuItem(), sprache, "speichern", "Speichern");
		rueckgaengig = SprachUtil.bindText(new MenuItem(), sprache, "rueckgaengig", "R%ckg%cngig".formatted(ue, ae));
		wiederholen = SprachUtil.bindText(new MenuItem(), sprache, "wiederholen", "Wiederholen");
		einfuegen = SprachUtil.bindText(new MenuItem(), sprache, "einfuegen", "Einfuegen");
		
		klasseEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuKlasse", "Neue Klasse...");
		abstraktelasseEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuAbstakteKlasse",
				"Neue abstrakte Klasse...");
		interfaceEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuInterface", "Neues Interface...");
		kommentarEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuKommentar", "Neuer Kommentar...");
		
		darstellungGroesser = SprachUtil.bindText(new MenuItem(), sprache, "vergroessern",
				"Vergr%c%cern".formatted(oe, sz));
		darstellungKleiner = SprachUtil.bindText(new MenuItem(), sprache, "verkleinern", "Verkleinern");
		darstellungOriginalgroesse = SprachUtil.bindText(new MenuItem(), sprache, "originalgroesse",
				"Originalgr%c%ce".formatted(oe, sz));
		
		dateiImportieren = SprachUtil.bindText(new MenuItem(), sprache, "importieren", "Importieren...");
		Menu dateiExportieren = SprachUtil.bindText(new Menu(), sprache, "exportieren", "Exportieren");
		exportierenBild = SprachUtil.bindText(new MenuItem(), sprache, "exportierenBild", "als Bild...");
		exportierenQuellcode = SprachUtil.bindText(new MenuItem(), sprache, "exportierenQuellcode", "als Quellcode...");
		dateiExportieren.getItems().addAll(exportierenBild, exportierenQuellcode);
		
		return List.of(dateiSpeichern, rueckgaengig, wiederholen, einfuegen, new SeparatorMenuItem(), klasseEinfuegen,
				abstraktelasseEinfuegen, interfaceEinfuegen, kommentarEinfuegen, new SeparatorMenuItem(),
				darstellungGroesser, darstellungKleiner, darstellungOriginalgroesse, new SeparatorMenuItem(),
				dateiImportieren, dateiExportieren);
	}
	
	private void setzeActions() {
		// Menue Datei
		dateiSpeichern.setOnAction(e -> projektAnsicht.projektSpeichern());
		updateRueckgaengigWiederholen();
		
		// Menue Bearbeiten
		einfuegen.setOnAction(e -> projektAnsicht.getProjekteAnsicht().auswahlEinfuegen());
		einfuegen.disableProperty().bind(Bindings.isEmpty(ProjekteAnsicht.getKopiepuffer()));
		
		// Neues Element Einfuegen
		klasseEinfuegen.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Klasse));
		abstraktelasseEinfuegen.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.AbstrakteKlasse));
		interfaceEinfuegen.setOnAction(e -> erzeugeKlassifizierer(KlassifiziererTyp.Interface));
		kommentarEinfuegen.setOnAction(e -> erzeugeKommentar());
		
		// Darstellung
		darstellungGroesser.setOnAction(this::zoomeGroesser);
		darstellungKleiner.setOnAction(this::zoomeKleiner);
		darstellungOriginalgroesse.setOnAction(this::resetZoom);
		
		// Importieren/Exportieren
		dateiImportieren.setOnAction(e -> {
			try {
				projektAnsicht.importiereAusDatei();
			} catch (Exception exc) {
				log.log(Level.WARNING, exc, () -> "Importfehler");
			}
		});
		exportierenBild.setOnAction(this.projektAnsicht::exportiereAlsBild);
		exportierenQuellcode.setOnAction(this.projektAnsicht::exportiereAlsQuellcode);
	}
	
	private void updateRueckgaengigWiederholen() {
		rueckgaengig.disableProperty().unbind();
		wiederholen.disableProperty().unbind();
		
		rueckgaengig.setOnAction(e -> projektAnsicht.getProjekt().macheRueckgaengig());
		wiederholen.setOnAction(e -> projektAnsicht.getProjekt().wiederhole());
		rueckgaengig.disableProperty().bind(projektAnsicht.getProjekt().kannRueckgaengigGemachtWerdenProperty().not());
		wiederholen.disableProperty().bind(projektAnsicht.getProjekt().kannWiederholenProperty().not());
	}
	
	private void erzeugeKlassifizierer(KlassifiziererTyp typ) {
		this.projektAnsicht.getProjekteAnsicht().legeNeuenKlassifiziererAn(typ);
	}
	
	private void erzeugeKommentar() {
		this.projektAnsicht.getProjekteAnsicht().legeKommentarAn();
	}
	
	void zoomeGroesser(Event event) {
		projektAnsicht.skaliere(projektAnsicht.getSkalierung() + 0.1);
	}
	
	void zoomeKleiner(Event event) {
		projektAnsicht.skaliere(projektAnsicht.getSkalierung() - 0.1);
	}
	
	void resetZoom(Event event) {
		projektAnsicht.skaliere(projektAnsicht.getStandardSkalierung());
	}
}