/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.Umlaute.OE;
import static io.github.aid_labor.classifier.basis.Umlaute.ae;
import static io.github.aid_labor.classifier.basis.Umlaute.oe;
import static io.github.aid_labor.classifier.basis.Umlaute.sz;
import static io.github.aid_labor.classifier.basis.Umlaute.ue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.Column;
import com.pixelduke.control.ribbon.QuickAccessBar;
import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;

import io.github.aid_labor.classifier.basis.Ressourcen;
import io.github.aid_labor.classifier.basis.SprachUtil;
import io.github.aid_labor.classifier.basis.Sprache;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class HauptAnsicht implements View {
	
	private static final Logger log = Logger.getLogger(HauptAnsicht.class.getName());
	
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
	
	private final BorderPane wurzel;
	private final HauptController controller;
	private final Sprache sprache;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public HauptAnsicht() {
		this.wurzel = new BorderPane();
		this.controller = new HauptController(this);
		this.sprache = new Sprache();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "HauptAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		var menue = erstelleMenueLeiste();
		var ribbon = erstelleRibbon();
		wurzel.setTop(new VBox(menue, ribbon));
		erstelleProjektAnsicht();
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	@Override
	public BorderPane getWurzelknoten() {
		return wurzel;
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
	
	private MenuBar erstelleMenueLeiste() {
		Menu dateiMenue = erstelleDateiMenue();
		Menu bearbeitenMenue = erstelleBearbeitenMenue();
		Menu einfuegenMenue = erstelleEinfuegenMenue();
		Menu anordnenMenue = erstelleAnordnenMenue();
		Menu darstellungMenue = erstelleDarstellungMenue();
		Menu fensterMenue = erstelleFensterMenue();
		Menu einstellungenMenue = erstelleEinstellungenMenue();
		
		MenuBar menuebar = new MenuBar(dateiMenue, bearbeitenMenue, einfuegenMenue,
				anordnenMenue, darstellungMenue, fensterMenue, einstellungenMenue);
		
		return menuebar;
	}
	
	private Menu erstelleDateiMenue() {
		Menu dateiMenue = SprachUtil.bindText(new Menu(), sprache, "dateiMenue", "Datei");
		MenuItem dateiNeu = SprachUtil.bindText(new MenuItem(), sprache, "neu", "Neu...");
		MenuItem dateiOeffnen = SprachUtil.bindText(new MenuItem(), sprache, "oeffnen",
				"%cffnen...".formatted(OE));
		Menu dateiLetzeOeffnen = SprachUtil.bindText(new Menu(), sprache, "letzteOeffnen",
				"Letzte Dateien");
		MenuItem dateiSchliessen = SprachUtil.bindText(new MenuItem(), sprache, "schliessen",
				"Schlie%cen".formatted(sz));
		MenuItem dateiSpeichern = SprachUtil.bindText(new MenuItem(), sprache, "speichern",
				"Speichern");
		MenuItem dateiAlleSpeichern = SprachUtil.bindText(new MenuItem(), sprache,
				"allesSpeichern", "Alles Speichern");
		MenuItem dateiSpeichernUnter = SprachUtil.bindText(new MenuItem(), sprache,
				"speichernUnter", "Speichern unter...");
		MenuItem dateiUmbenennen = SprachUtil.bindText(new MenuItem(), sprache, "umbenennen",
				"Umbenennen...");
		MenuItem dateiImportieren = SprachUtil.bindText(new MenuItem(), sprache, "importieren",
				"Importieren...");
		Menu dateiExportieren = SprachUtil.bindText(new Menu(), sprache, "exportieren",
				"Exportieren");
		
		MenuItem exportierenBild = SprachUtil.bindText(new MenuItem(), sprache,
				"exportierenBild", "als Bild...");
		MenuItem exportierenQuellcode = SprachUtil.bindText(new MenuItem(), sprache,
				"exportierenQuellcode", "als Quellcode...");
		dateiExportieren.getItems().addAll(exportierenBild, exportierenQuellcode);
		
		dateiMenue.getItems().addAll(dateiNeu, dateiOeffnen, dateiLetzeOeffnen,
				new SeparatorMenuItem(), dateiSchliessen, dateiSpeichern, dateiAlleSpeichern,
				dateiSpeichernUnter, dateiUmbenennen, new SeparatorMenuItem(),
				dateiImportieren, dateiExportieren);
		
		return dateiMenue;
	}
	
	private Menu erstelleBearbeitenMenue() {
		Menu bearbeitenMenue = SprachUtil.bindText(new Menu(), sprache, "bearbeitenMenue",
				"Bearbeiten");
		
		MenuItem rueckgaengig = SprachUtil.bindText(new MenuItem(), sprache, "rueckgaengig",
				"R%ckg%cngig".formatted(ue, ae));
		MenuItem wiederholen = SprachUtil.bindText(new MenuItem(), sprache, "wiederholen",
				"Wiederholen");
		MenuItem kopieren = SprachUtil.bindText(new MenuItem(), sprache, "kopieren",
				"Kopieren");
		MenuItem einfuegen = SprachUtil.bindText(new MenuItem(), sprache, "einfuegen",
				"Einfuegen");
		MenuItem loeschen = SprachUtil.bindText(new MenuItem(), sprache, "loeschen",
				"L%cschen".formatted(oe));
		
		bearbeitenMenue.getItems().addAll(rueckgaengig, wiederholen, new SeparatorMenuItem(),
				kopieren, einfuegen, loeschen);
		
		return bearbeitenMenue;
	}
	
	private Menu erstelleEinfuegenMenue() {
		Menu einfuegenMenue = SprachUtil.bindText(new Menu(), sprache, "einfuegenMenue",
				"Einf%cgen".formatted(ue));
		MenuItem klasseEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuKlasse",
				"Neue Klasse...");
		MenuItem interfaceEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuInterface", "Neues Interface...");
		MenuItem enumEinfuegen = SprachUtil.bindText(new MenuItem(), sprache, "neuEnum",
				"Neue Enumeration...");
		MenuItem vererbungEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuVererbung", "Vererbung hinzuf%cgen".formatted(ue));
		MenuItem assoziationEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuAssoziation", "Assoziation hinzuf%cgen".formatted(ue));
		MenuItem kommentarEinfuegen = SprachUtil.bindText(new MenuItem(), sprache,
				"neuKommentar", "Neuer Kommentar...");
		
		einfuegenMenue.getItems().addAll(klasseEinfuegen, interfaceEinfuegen, enumEinfuegen,
				new SeparatorMenuItem(), vererbungEinfuegen, assoziationEinfuegen,
				new SeparatorMenuItem(), kommentarEinfuegen);
		
		return einfuegenMenue;
	}
	
	private Menu erstelleAnordnenMenue() {
		Menu anordnenMenue = SprachUtil.bindText(new Menu(), sprache, "anordnenMenue",
				"Anordnen");
		MenuItem anordnenNachVorne = SprachUtil.bindText(new MenuItem(), sprache,
				"nachVorne", "Nach vorne");
		MenuItem anordnenNachGanzVorne = SprachUtil.bindText(new MenuItem(), sprache,
				"nachGanzVorne", "Nach ganz vorne");
		MenuItem anordnenNachHinten = SprachUtil.bindText(new MenuItem(), sprache,
				"nachHinten", "Nach hinten");
		MenuItem anordnenNachGanzHinten = SprachUtil.bindText(new MenuItem(), sprache,
				"nachGanzHinten", "Nach ganz hinten");
		
		anordnenMenue.getItems().addAll(anordnenNachVorne, anordnenNachGanzVorne,
				anordnenNachHinten, anordnenNachGanzHinten);
		
		return anordnenMenue;
	}
	
	private Menu erstelleDarstellungMenue() {
		Menu darstellungMenue = SprachUtil.bindText(new Menu(), sprache, "darstellungMenue",
				"Darstellung");
		Menu zoomen = SprachUtil.bindText(new Menu(), sprache, "zoomen", "Zoomen");
		MenuItem darstellungGroesser = SprachUtil.bindText(new MenuItem(), sprache,
				"vergroessern", "Vergr%c%cern".formatted(oe, sz));
		MenuItem darstellungKleiner = SprachUtil.bindText(new MenuItem(), sprache,
				"verkleinern", "Verkleinern");
		MenuItem darstellungOriginalgroesse = SprachUtil.bindText(new MenuItem(), sprache,
				"originalgroesse", "Originalgr%c%ce".formatted(oe, sz));
		zoomen.getItems().addAll(darstellungGroesser, darstellungKleiner,
				darstellungOriginalgroesse);
		
		MenuItem vollbild = SprachUtil.bindText(new CheckMenuItem(), sprache,
				"vollbild", "Vollbild");
		MenuItem symbolleisteAusblenden = SprachUtil.bindText(new MenuItem(), sprache,
				"symbolleisteAusblenden", "Symbolleiste ausblenden");
		
		darstellungMenue.getItems().addAll(zoomen, new SeparatorMenuItem(), vollbild,
				new SeparatorMenuItem(), symbolleisteAusblenden);
		
		return darstellungMenue;
	}
	
	private Menu erstelleFensterMenue() {
		Menu fensterMenue = SprachUtil.bindText(new Menu(), sprache, "fensterMenue",
				"Fenster");
		MenuItem minimieren = SprachUtil.bindText(new MenuItem(), sprache,
				"minimieren", "Minimieren");
		MenuItem maximieren = SprachUtil.bindText(new MenuItem(), sprache,
				"maximieren", "Maximieren");
		MenuItem vorherigerTab = SprachUtil.bindText(new MenuItem(), sprache,
				"vorherigerTab", "vorheriger Tab");
		MenuItem naechsterTab = SprachUtil.bindText(new MenuItem(), sprache,
				"naechsterTab", "n%cchster Tab".formatted(ae));
		
		fensterMenue.getItems().addAll(minimieren, maximieren, new SeparatorMenuItem(),
				vorherigerTab, naechsterTab);
		
		return fensterMenue;
	}
	
	private Menu erstelleEinstellungenMenue() {
		Menu einstellungenMenue = SprachUtil.bindText(new Menu(), sprache,
				"einstellungenMenue", "Einstellungen");
		MenuItem voidAnzeigen = SprachUtil.bindText(new MenuItem(), sprache, "voidAnzeigen",
				"void Anzeigen");
		Menu theme = SprachUtil.bindText(new Menu(), sprache, "theme",
				"Farbschema");
		MenuItem info = SprachUtil.bindText(new MenuItem(), sprache, "info",
				"Info");
		
		einstellungenMenue.getItems().addAll(voidAnzeigen, new SeparatorMenuItem(), theme,
				new SeparatorMenuItem(), info);
		
		return einstellungenMenue;
	}
	
	private Node erstelleRibbon() {
		RibbonTab startTab = erstelleStartTab();
		RibbonTab diagrammTab = erstelleDiagrammTab();
		
		QuickAccessBar schnellzugriff = new QuickAccessBar();
		Button speichernSchnellzugriff = SprachUtil.bindText(new Button(), sprache,
				"speichern", "Speichern");
		Button rueckgaengigSchnellzugriff = SprachUtil.bindText(new Button(), sprache,
				"rueckgaengig", "R%cckg%cngig".formatted(ue, ae));
		Button wiederholenSchnellzugriff = SprachUtil.bindText(new Button(), sprache,
				"wiederholen", "Wiederholen");
		schnellzugriff.getButtons().addAll(speichernSchnellzugriff, rueckgaengigSchnellzugriff,
				wiederholenSchnellzugriff);
		
		Ribbon ribbon = new Ribbon();
		
		ribbon.setQuickAccessBar(schnellzugriff);
		ribbon.getTabs().addAll(startTab, diagrammTab);
		
		return ribbon;
	}
	
	private RibbonTab erstelleStartTab() {
		Button oeffnen = SprachUtil.bindText(new Button(), sprache, "oeffnen",
				"%cffnen".formatted(OE));
		Button neu = SprachUtil.bindText(new Button(), sprache, "neu",
				"Neu...");
		Button importieren = SprachUtil.bindText(new Button(), sprache, "importieren",
				"Importieren");
		Column ersteSpalte = new Column();
		ersteSpalte.getChildren().addAll(oeffnen, neu, importieren);
		
		Button speichern = SprachUtil.bindText(new Button(), sprache, "speichern",
				"Speichern");
		Button screenshot = SprachUtil.bindText(new Button(), sprache, "screenshot",
				"Screenshot...");
		Button exportieren = SprachUtil.bindText(new Button(), sprache, "exportieren",
				"Exportieren...");
		Column zweiteSpalte = new Column();
		zweiteSpalte.getChildren().addAll(speichern, screenshot, exportieren);
		
		RibbonGroup erstellen = new RibbonGroup();
		erstellen.titleProperty().bind(sprache.getTextProperty("projekt", "Projekt"));
		erstellen.getNodes().addAll(ersteSpalte, zweiteSpalte);
		
		RibbonTab startTab = SprachUtil.bindText(new RibbonTab(), sprache, "startTab",
				"Start");
		startTab.getRibbonGroups().add(erstellen);
		
		fuegeLogoHinzu(startTab);
		
		return startTab;
	}
	
	private RibbonTab erstelleDiagrammTab() {
		Button neueKlasse = SprachUtil.bindText(new Button(), sprache, "klasse", "Klasse +");
		Button neuesInterface = SprachUtil.bindText(new Button(), sprache, "interface",
				"Interface");
		Button neueEnumeration = SprachUtil.bindText(new Button(), sprache, "enumeration",
				"Enumeration +");
		RibbonGroup diagrammElemente = new RibbonGroup();
		diagrammElemente.titleProperty().bind(sprache.getTextProperty("diagrammElemente",
				"Diagramm Elemente"));
		diagrammElemente.getNodes().addAll(neueKlasse, neuesInterface, neueEnumeration);
		
		Button vererbung = SprachUtil.bindText(new Button(), sprache, "vererbung",
				"Vererbung +");
		Button assoziation = SprachUtil.bindText(new Button(), sprache, "assoziation",
				"Assoziation +");
		RibbonGroup verbindungen = new RibbonGroup();
		verbindungen.titleProperty().bind(sprache.getTextProperty("verbindungen",
				"Verbindungen"));
		verbindungen.getNodes().addAll(vererbung, assoziation);
		
		Button kommentar = SprachUtil.bindText(new Button(), sprache, "kommentar",
				"Kommentar +");
		RibbonGroup sonstiges = new RibbonGroup();
		sonstiges.getNodes().add(kommentar);
		
		RibbonTab diagrammTab = SprachUtil.bindText(new RibbonTab(), sprache, "diagrammTab",
				"Diagramm");
		diagrammTab.getRibbonGroups().addAll(diagrammElemente, verbindungen, sonstiges);
		
		fuegeLogoHinzu(diagrammTab);
		
		return diagrammTab;
	}
	
	private void fuegeLogoHinzu(RibbonTab tab) {
		try {
			ImageView classifierLogo = new ImageView(
					new Image(Ressourcen.get().CLASSIFIER_LOGO_M.oeffneStream()));
			classifierLogo.setPreserveRatio(true);
			classifierLogo.setSmooth(true);
			classifierLogo.setCache(true);
			classifierLogo.setFitHeight(134);
			
			var logoContainer = new HBox(classifierLogo);
			logoContainer.setMaxHeight(134);
			logoContainer.setAlignment(Pos.CENTER_RIGHT);
			HBox.setHgrow(logoContainer, Priority.ALWAYS);
			HBox.setMargin(logoContainer, new Insets(5));
			((HBox) tab.getContent()).getChildren().add(logoContainer);
		} catch (IllegalStateException | IOException e) {
			log.log(Level.WARNING, e, () -> "Logo konnte nicht geladen werden");
		}
	}
	
	private void erstelleProjektAnsicht() {
		try (BufferedReader ein = new BufferedReader(
				new InputStreamReader(Ressourcen.get().LIZENZ_DATEI.oeffneStream()))) {
			String lizenz = ein.lines().collect(Collectors.joining("\n"));
			this.wurzel.setCenter(new Label(lizenz));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}