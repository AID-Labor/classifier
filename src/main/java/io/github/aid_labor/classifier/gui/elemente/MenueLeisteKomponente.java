/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui.elemente;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.OE;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ae;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.oe;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.sz;
import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ue;

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;


/**
 * Erstellung der Menueleiste mit allen Menue-Elementen. Ereignisaktionen sind nicht
 * implmentiert und koennen ueber die Getter hinzugefuegt werden.
 * 
 * @author Tim Muehle
 *
 */
public class MenueLeisteKomponente {
//	private static final Logger log = Logger.getLogger(MenueLeisteAnsicht.class.getName());

// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private final MenuBar menueleiste;
	private final Sprache sprache;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Datei
	private MenuItem dateiNeu;
	private MenuItem dateiOeffnen;
	private Menu dateiLetzeOeffnen;
	private MenuItem dateiSchliessen;
	private MenuItem dateiSpeichern;
	private MenuItem dateiAlleSpeichern;
	private MenuItem dateiSpeichernUnter;
	private MenuItem dateiUmbenennen;
	private MenuItem dateiImportieren;
	private MenuItem exportierenBild;
	private MenuItem exportierenQuellcode;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Bearbeiten
	private MenuItem rueckgaengig;
	private MenuItem wiederholen;
	private MenuItem kopieren;
	private MenuItem einfuegen;
	private MenuItem loeschen;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Einfuegen
	private MenuItem klasseEinfuegen;
	private MenuItem interfaceEinfuegen;
	private MenuItem enumEinfuegen;
	private MenuItem vererbungEinfuegen;
	private MenuItem assoziationEinfuegen;
	private MenuItem kommentarEinfuegen;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Anordnen
	private MenuItem anordnenNachVorne;
	private MenuItem anordnenNachGanzVorne;
	private MenuItem anordnenNachHinten;
	private MenuItem anordnenNachGanzHinten;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Darstellung
	private MenuItem darstellungGroesser;
	private MenuItem darstellungKleiner;
	private MenuItem darstellungOriginalgroesse;
	private CheckMenuItem vollbild;
	private CheckMenuItem symbolleisteAusblenden;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Fenster
	private MenuItem vorherigerTab;
	private MenuItem naechsterTab;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Menue Einstellungen
	private MenuItem voidAnzeigen;
	private Menu theme;
	private MenuItem info;
	private MenuItem konfigurationsordnerOeffnen;
	private MenuItem konfigurationsordnerBereinigen;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public MenueLeisteKomponente() {
		this.sprache = new Sprache();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "MenueLeisteAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		Menu dateiMenue = erstelleDateiMenue();
		Menu bearbeitenMenue = erstelleBearbeitenMenue();
		Menu einfuegenMenue = erstelleEinfuegenMenue();
		Menu anordnenMenue = erstelleAnordnenMenue();
		Menu darstellungMenue = erstelleDarstellungMenue();
		Menu fensterMenue = erstelleFensterMenue();
		Menu einstellungenMenue = erstelleEinstellungenMenue();
		
		MenuBar menueleiste = new MenuBar(dateiMenue, bearbeitenMenue, einfuegenMenue,
				anordnenMenue, darstellungMenue, fensterMenue, einstellungenMenue);
		this.menueleiste = menueleiste;
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public MenuBar getMenueleiste() {
		return menueleiste;
	}
	
	public MenuItem getDateiNeu() {
		return dateiNeu;
	}
	
	public MenuItem getDateiOeffnen() {
		return dateiOeffnen;
	}
	
	public Menu getDateiLetzeOeffnen() {
		return dateiLetzeOeffnen;
	}
	
	public MenuItem getDateiSchliessen() {
		return dateiSchliessen;
	}
	
	public MenuItem getDateiSpeichern() {
		return dateiSpeichern;
	}
	
	public MenuItem getDateiAlleSpeichern() {
		return dateiAlleSpeichern;
	}
	
	public MenuItem getDateiSpeichernUnter() {
		return dateiSpeichernUnter;
	}
	
	public MenuItem getDateiUmbenennen() {
		return dateiUmbenennen;
	}
	
	public MenuItem getDateiImportieren() {
		return dateiImportieren;
	}
	
	public MenuItem getExportierenBild() {
		return exportierenBild;
	}
	
	public MenuItem getExportierenQuellcode() {
		return exportierenQuellcode;
	}
	
	public MenuItem getRueckgaengig() {
		return rueckgaengig;
	}
	
	public MenuItem getWiederholen() {
		return wiederholen;
	}
	
	public MenuItem getKopieren() {
		return kopieren;
	}
	
	public MenuItem getEinfuegen() {
		return einfuegen;
	}
	
	public MenuItem getLoeschen() {
		return loeschen;
	}
	
	public MenuItem getKlasseEinfuegen() {
		return klasseEinfuegen;
	}
	
	public MenuItem getInterfaceEinfuegen() {
		return interfaceEinfuegen;
	}
	
	public MenuItem getEnumEinfuegen() {
		return enumEinfuegen;
	}
	
	public MenuItem getVererbungEinfuegen() {
		return vererbungEinfuegen;
	}
	
	public MenuItem getAssoziationEinfuegen() {
		return assoziationEinfuegen;
	}
	
	public MenuItem getKommentarEinfuegen() {
		return kommentarEinfuegen;
	}
	
	public MenuItem getAnordnenNachVorne() {
		return anordnenNachVorne;
	}
	
	public MenuItem getAnordnenNachGanzVorne() {
		return anordnenNachGanzVorne;
	}
	
	public MenuItem getAnordnenNachHinten() {
		return anordnenNachHinten;
	}
	
	public MenuItem getAnordnenNachGanzHinten() {
		return anordnenNachGanzHinten;
	}
	
	public MenuItem getDarstellungGroesser() {
		return darstellungGroesser;
	}
	
	public MenuItem getDarstellungKleiner() {
		return darstellungKleiner;
	}
	
	public MenuItem getDarstellungOriginalgroesse() {
		return darstellungOriginalgroesse;
	}
	
	public CheckMenuItem getVollbild() {
		return vollbild;
	}
	
	public CheckMenuItem getSymbolleisteAusblenden() {
		return symbolleisteAusblenden;
	}
	
	public MenuItem getVorherigerTab() {
		return vorherigerTab;
	}
	
	public MenuItem getNaechsterTab() {
		return naechsterTab;
	}
	
	public MenuItem getVoidAnzeigen() {
		return voidAnzeigen;
	}
	
	public Menu getTheme() {
		return theme;
	}
	
	public MenuItem getInfo() {
		return info;
	}
	
	public MenuItem getKonfigurationsordnerOeffnen() {
		return konfigurationsordnerOeffnen;
	}
	
	public MenuItem getKonfigurationsordnerBereinigen() {
		return konfigurationsordnerBereinigen;
	}
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private Menu erstelleDateiMenue() {
		Menu dateiMenue = SprachUtil.bindText(new Menu(), sprache, "dateiMenue", "Datei");
		dateiNeu = SprachUtil.bindText(new MenuItem(), sprache, "neu", "Neu...");
		dateiOeffnen = SprachUtil.bindText(new MenuItem(), sprache, "oeffnen",
				"%cffnen...".formatted(OE));
		dateiLetzeOeffnen = SprachUtil.bindText(new Menu(), sprache, "letzteOeffnen",
				"Letzte Dateien");
		dateiSchliessen = SprachUtil.bindText(new MenuItem(), sprache, "schliessen",
				"Schlie%cen".formatted(sz));
		dateiSpeichern = SprachUtil.bindText(new MenuItem(), sprache, "speichern",
				"Speichern");
		dateiAlleSpeichern = SprachUtil.bindText(new MenuItem(), sprache,
				"allesSpeichern", "Alles Speichern");
		dateiSpeichernUnter = SprachUtil.bindText(new MenuItem(), sprache,
				"speichernUnter", "Speichern unter...");
		dateiUmbenennen = SprachUtil.bindText(new MenuItem(), sprache, "umbenennen",
				"Umbenennen...");
		dateiImportieren = SprachUtil.bindText(new MenuItem(), sprache, "importieren",
				"Importieren...");
		Menu dateiExportieren = SprachUtil.bindText(new Menu(), sprache, "exportieren",
				"Exportieren");
		
		exportierenBild = SprachUtil.bindText(new MenuItem(), sprache,
				"exportierenBild", "als Bild...");
		exportierenQuellcode = SprachUtil.bindText(new MenuItem(), sprache,
				"exportierenQuellcode", "als Quellcode...");
		dateiExportieren.getItems().addAll(exportierenBild, exportierenQuellcode);
		
		dateiMenue.getItems().addAll(dateiNeu, dateiOeffnen, dateiLetzeOeffnen,
				new SeparatorMenuItem(), dateiSchliessen, dateiSpeichern, dateiAlleSpeichern,
				dateiSpeichernUnter, dateiUmbenennen, new SeparatorMenuItem(),
				dateiImportieren, dateiExportieren);
		
		return dateiMenue;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Menu erstelleBearbeitenMenue() {
		Menu bearbeitenMenue = SprachUtil.bindText(new Menu(), sprache, "bearbeitenMenue",
				"Bearbeiten");
		
		rueckgaengig = SprachUtil.bindText(new MenuItem(), sprache, "rueckgaengig",
				"R%ckg%cngig".formatted(ue, ae));
		wiederholen = SprachUtil.bindText(new MenuItem(), sprache, "wiederholen",
				"Wiederholen");
		kopieren = SprachUtil.bindText(new MenuItem(), sprache, "kopieren",
				"Kopieren");
		einfuegen = SprachUtil.bindText(new MenuItem(), sprache, "einfuegen",
				"Einfuegen");
		loeschen = SprachUtil.bindText(new MenuItem(), sprache, "loeschen",
				"L%cschen".formatted(oe));
		
		bearbeitenMenue.getItems().addAll(rueckgaengig, wiederholen, new SeparatorMenuItem(),
				kopieren, einfuegen, loeschen);
		
		return bearbeitenMenue;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Menu erstelleEinfuegenMenue() {
		Menu einfuegenMenue = SprachUtil.bindText(new Menu(), sprache, "einfuegenMenue",
				"Einf%cgen".formatted(ue));
		klasseEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuKlasse",
				"Neue Klasse...");
		interfaceEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuInterface", "Neues Interface...");
		enumEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuEnum",
				"Neue Enumeration...");
		vererbungEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuVererbung", "Vererbung hinzuf%cgen".formatted(ue));
		assoziationEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuAssoziation", "Assoziation hinzuf%cgen".formatted(ue));
		kommentarEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuKommentar", "Neuer Kommentar...");
		
		einfuegenMenue.getItems().addAll(klasseEinfuegen, interfaceEinfuegen, enumEinfuegen,
				new SeparatorMenuItem(), vererbungEinfuegen, assoziationEinfuegen,
				new SeparatorMenuItem(), kommentarEinfuegen);
		
		return einfuegenMenue;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Menu erstelleAnordnenMenue() {
		Menu anordnenMenue = SprachUtil.bindText(new Menu(), sprache, "anordnenMenue",
				"Anordnen");
		anordnenNachVorne = SprachUtil.bindText(new MenuItem(), sprache,
				"nachVorne", "Eine Ebene nach vorne");
		anordnenNachGanzVorne = SprachUtil.bindText(new MenuItem(), sprache,
				"nachGanzVorne", "In den Vordergrund");
		anordnenNachHinten = SprachUtil.bindText(new MenuItem(), sprache,
				"nachHinten", "Eine Ebene nach vorne");
		anordnenNachGanzHinten = SprachUtil.bindText(new MenuItem(), sprache,
				"nachGanzHinten", "In den Hintergrund");
		
		anordnenMenue.getItems().addAll(anordnenNachVorne, anordnenNachGanzVorne,
				anordnenNachHinten, anordnenNachGanzHinten);
		
		return anordnenMenue;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Menu erstelleDarstellungMenue() {
		Menu darstellungMenue = SprachUtil.bindText(new Menu(), sprache, "darstellungMenue",
				"Darstellung");
		Menu zoomen = SprachUtil.bindText(new Menu(), sprache, "zoomen", "Zoomen");
		darstellungGroesser = SprachUtil.bindText(new MenuItem(), sprache,
				"vergroessern", "Vergr%c%cern".formatted(oe, sz));
		darstellungKleiner = SprachUtil.bindText(new MenuItem(), sprache,
				"verkleinern", "Verkleinern");
		darstellungOriginalgroesse = SprachUtil.bindText(new MenuItem(), sprache,
				"originalgroesse", "Originalgr%c%ce".formatted(oe, sz));
		zoomen.getItems().addAll(darstellungGroesser, darstellungKleiner,
				darstellungOriginalgroesse);
		
		vollbild = SprachUtil.bindText(new CheckMenuItem(), sprache,
				"vollbild", "Vollbild");
		symbolleisteAusblenden = SprachUtil.bindText(new CheckMenuItem(), sprache,
				"symbolleisteAusblenden", "Symbolleiste ausblenden");
		
		darstellungMenue.getItems().addAll(zoomen, new SeparatorMenuItem(), vollbild,
				new SeparatorMenuItem(), symbolleisteAusblenden);
		
		return darstellungMenue;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Menu erstelleFensterMenue() {
		Menu fensterMenue = SprachUtil.bindText(new Menu(), sprache, "fensterMenue",
				"Fenster");
		vorherigerTab = SprachUtil.bindText(new MenuItem(), sprache,
				"vorherigerTab", "vorheriger Tab");
		naechsterTab = SprachUtil.bindText(new MenuItem(), sprache,
				"naechsterTab", "n%cchster Tab".formatted(ae));
		
		fensterMenue.getItems().addAll(vorherigerTab, naechsterTab);
		
		return fensterMenue;
	}
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private Menu erstelleEinstellungenMenue() {
		Menu einstellungenMenue = SprachUtil.bindText(new Menu(), sprache,
				"einstellungenMenue", "Einstellungen");
		voidAnzeigen = SprachUtil.bindText(new MenuItem(), sprache, "voidAnzeigen",
				"void Anzeigen");
		theme = SprachUtil.bindText(new Menu(), sprache, "theme",
				"Farbschema");
		info = SprachUtil.bindText(new MenuItem(), sprache, "info",
				"Info");
		konfigurationsordnerOeffnen = SprachUtil.bindText(new MenuItem(), sprache,
				"konfigOeffnen", "Konfigurationsordner %cffnen".formatted(oe));
		konfigurationsordnerBereinigen = SprachUtil.bindText(new MenuItem(), sprache,
				"konfigBereinigen", "Konfigurationsordner bereinigen".formatted(oe));
		
		einstellungenMenue.getItems().addAll(voidAnzeigen, new SeparatorMenuItem(), theme,
				new SeparatorMenuItem(), info, konfigurationsordnerOeffnen,
				konfigurationsordnerBereinigen);
		
		return einstellungenMenue;
	}
	
}