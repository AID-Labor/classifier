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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.carbonicons.CarbonIcons;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.remixicon.RemixiconMZ;
import org.kordamp.ikonli.typicons.Typicons;
import org.kordamp.ikonli.whhg.WhhgAL;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.Column;
import com.pixelduke.control.ribbon.QuickAccessBar;
import com.pixelduke.control.ribbon.RibbonGroup;
import com.pixelduke.control.ribbon.RibbonTab;

import io.github.aid_labor.classifier.basis.Ressourcen;
import io.github.aid_labor.classifier.basis.SprachUtil;
import io.github.aid_labor.classifier.basis.Sprache;
import io.github.aid_labor.classifier.gui.elemente.ElementIcon;
import io.github.aid_labor.classifier.gui.util.ButtonTyp;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Labeled;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	private final TabPane projektAnsicht;
	
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
		
		this.projektAnsicht = new TabPane();
		wurzel.setCenter(projektAnsicht);
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
	
	// =====================================================================================
	// Beginn Menue
	
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
//		menuebar.setUseSystemMenuBar(true);
		
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
				"nachVorne", "Eine Ebene nach vorne");
		MenuItem anordnenNachGanzVorne = SprachUtil.bindText(new MenuItem(), sprache,
				"nachGanzVorne", "In den Vordergrund");
		MenuItem anordnenNachHinten = SprachUtil.bindText(new MenuItem(), sprache,
				"nachHinten", "Eine Ebene nach vorne");
		MenuItem anordnenNachGanzHinten = SprachUtil.bindText(new MenuItem(), sprache,
				"nachGanzHinten", "In den Hintergrund");
		
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
	
	// Ende Menue
	// =====================================================================================
	// Beginn Ribbon
	
	private Ribbon erstelleRibbon() {
		RibbonTab startTab = erstelleStartTab();
		RibbonTab diagrammTab = erstelleDiagrammTab();
		
		QuickAccessBar schnellzugriff = new QuickAccessBar();
		Button speichernSchnellzugriff = neuerButton(ButtonTyp.SPEICHERN);
		Button rueckgaengigSchnellzugriff = neuerButton(ButtonTyp.RUECKGAENGIG);
		Button wiederholenSchnellzugriff = neuerButton(ButtonTyp.WIEDERHOLEN);
		((FontIcon) speichernSchnellzugriff.getGraphic()).setIconSize(18);
		((FontIcon) rueckgaengigSchnellzugriff.getGraphic()).setIconSize(18);
		((FontIcon) wiederholenSchnellzugriff.getGraphic()).setIconSize(18);
		NodeUtil.macheUnfokussierbar(speichernSchnellzugriff, rueckgaengigSchnellzugriff,
				wiederholenSchnellzugriff);
		
		schnellzugriff.getButtons().addAll(speichernSchnellzugriff, rueckgaengigSchnellzugriff,
				wiederholenSchnellzugriff);
		
		Ribbon ribbon = new Ribbon();
		
		ribbon.setQuickAccessBar(schnellzugriff);
		ribbon.getTabs().addAll(startTab, diagrammTab);
		
		return ribbon;
	}
	
	private Button neuerButton(ButtonTyp typ) {
		return switch (typ) {
			case RUECKGAENGIG: {
				Button rueckgaengig = SprachUtil.bindText(new Button(), sprache,
						"rueckgaengig", "R%cckg%cngig".formatted(ue, ae));
				NodeUtil.erzeugeIconNode(rueckgaengig, CarbonIcons.UNDO);
				yield rueckgaengig;
			}
			case SPEICHERN: {
				Button speichern = SprachUtil.bindText(new Button(), sprache,
						"speichern", "Speichern");
				NodeUtil.fuegeIconHinzu(speichern, RemixiconMZ.SAVE_3_FILL, 20);
				yield speichern;
			}
			case WIEDERHOLEN: {
				Button wiederholen = SprachUtil.bindText(new Button(), sprache,
						"wiederholen", "Wiederholen");
				NodeUtil.erzeugeIconNode(wiederholen, CarbonIcons.REDO);
				yield wiederholen;
			}
		};
	}
	
	private RibbonTab erstelleStartTab() {
		RibbonTab startTab = SprachUtil.bindText(new RibbonTab(), sprache, "startTab",
				"START");
		
		RibbonGroup projektGruppe = erstelleProjektGruppe();
		RibbonGroup bearbeitenGruppe = erstelleBearbeitenGruppe();
		RibbonGroup anordnenGruppe = erstelleAnordnenGruppe();
		
		startTab.getRibbonGroups().addAll(projektGruppe, bearbeitenGruppe, anordnenGruppe);
		fuegeLogoHinzu(startTab);
		
		return startTab;
	}
	
	private RibbonGroup erstelleProjektGruppe() {
		Button oeffnen = SprachUtil.bindText(new Button(), sprache, "oeffnen",
				"%cffnen".formatted(OE));
		Button neu = SprachUtil.bindText(new Button(), sprache, "neu",
				"Neu...");
		Button importieren = SprachUtil.bindText(new Button(), sprache, "importieren",
				"Importieren");
		NodeUtil.fuegeIconHinzu(oeffnen, Typicons.FOLDER_OPEN);
		NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS);
		NodeUtil.fuegeIconHinzu(importieren, CarbonIcons.DOWNLOAD);
		Column ersteSpalte = new Column();
		ersteSpalte.getChildren().addAll(oeffnen, neu, importieren);
		
		Button speichern = neuerButton(ButtonTyp.SPEICHERN);
		Button screenshot = SprachUtil.bindText(new Button(), sprache, "screenshot",
				"Screenshot...");
		Button exportieren = SprachUtil.bindText(new Button(), sprache, "exportieren",
				"Exportieren...");
		NodeUtil.fuegeIconHinzu(screenshot, Typicons.IMAGE);
		NodeUtil.fuegeIconHinzu(exportieren, CarbonIcons.SCRIPT_REFERENCE);
		Column zweiteSpalte = new Column();
		zweiteSpalte.getChildren().addAll(speichern, screenshot, exportieren);
		
		RibbonGroup projekt = new RibbonGroup();
		projekt.titleProperty().bind(sprache.getTextProperty("projekt", "Projekt"));
		projekt.getNodes().addAll(ersteSpalte, zweiteSpalte);
		
		NodeUtil.macheUnfokussierbar(projekt.getNodes());
		NodeUtil.macheUnfokussierbar(ersteSpalte.getChildren());
		NodeUtil.macheUnfokussierbar(zweiteSpalte.getChildren());
		
		return projekt;
	}
	
	private RibbonGroup erstelleBearbeitenGruppe() {
		Button kopieren = SprachUtil.bindText(new Button(), sprache, "kopieren",
				"Kopieren");
		Button einfuegen = SprachUtil.bindText(new Button(), sprache, "einfuegen",
				"Einf%cgen".formatted(ue));
		Button loeschen = SprachUtil.bindText(new Button(), sprache, "loeschen",
				"L%cschen".formatted(oe));
		NodeUtil.fuegeIconHinzu(kopieren, CarbonIcons.COPY_FILE);
		NodeUtil.fuegeIconHinzu(einfuegen, CarbonIcons.PASTE);
		NodeUtil.fuegeIconHinzu(loeschen, CarbonIcons.DELETE);
		Column spalte = new Column();
		spalte.getChildren().addAll(kopieren, einfuegen, loeschen);
		
		Button rueckgaengig = neuerButton(ButtonTyp.RUECKGAENGIG);
		Button wiederholen = neuerButton(ButtonTyp.WIEDERHOLEN);
		
		RibbonGroup bearbeiten = new RibbonGroup();
		bearbeiten.titleProperty().bind(sprache.getTextProperty("bearbeiten", "Bearbeiten"));
		bearbeiten.getNodes().addAll(spalte, rueckgaengig, wiederholen);
		
		NodeUtil.macheUnfokussierbar(bearbeiten.getNodes());
		NodeUtil.macheUnfokussierbar(spalte.getChildren());
		
		return bearbeiten;
	}
	
	private RibbonGroup erstelleAnordnenGruppe() {
		Button anordnenNachVorne = SprachUtil.bindText(new Button(), sprache,
				"nachVorne", "Eine Ebene nach vorne");
		Button anordnenNachGanzVorne = SprachUtil.bindText(new Button(), sprache,
				"nachGanzVorne", "In den Vordergrund");
		NodeUtil.fuegeIconHinzu(anordnenNachVorne, BootstrapIcons.LAYER_FORWARD);
		NodeUtil.fuegeIconHinzu(anordnenNachGanzVorne, WhhgAL.LAYERORDERUP);
		Column ersteSpalte = new Column();
		ersteSpalte.getChildren().addAll(anordnenNachVorne, anordnenNachGanzVorne);
		
		Button anordnenNachHinten = SprachUtil.bindText(new Button(), sprache,
				"nachHinten", "Eine Ebene nach vorne");
		Button anordnenNachGanzHinten = SprachUtil.bindText(new Button(), sprache,
				"nachGanzHinten", "In den Hintergrund");
		NodeUtil.fuegeIconHinzu(anordnenNachHinten, BootstrapIcons.LAYER_BACKWARD);
		NodeUtil.fuegeIconHinzu(anordnenNachGanzHinten, WhhgAL.LAYERORDERDOWN);
		Column zweiteSpalte = new Column();
		zweiteSpalte.getChildren().addAll(anordnenNachHinten, anordnenNachGanzHinten);
		
		RibbonGroup anordnen = new RibbonGroup();
		anordnen.titleProperty().bind(sprache.getTextProperty("anordnen", "Anordnen"));
		anordnen.getNodes().addAll(ersteSpalte, zweiteSpalte);
		
		NodeUtil.macheUnfokussierbar(anordnen.getNodes());
		NodeUtil.macheUnfokussierbar(ersteSpalte.getChildren());
		NodeUtil.macheUnfokussierbar(zweiteSpalte.getChildren());
		
		return anordnen;
	}
	
	private RibbonTab erstelleDiagrammTab() {
		RibbonGroup diagrammElemente = erstelleDiagrammElementeGruppe();
		RibbonGroup verbindungen = erstelleVerbindungenGruppe();
		RibbonGroup sonstiges = erstelleSonstigeGruppe();
		RibbonGroup zoom = erstelleZoomGruppe();
		
		RibbonTab diagrammTab = SprachUtil.bindText(new RibbonTab(), sprache, "diagrammTab",
				"DIAGRAMM");
		diagrammTab.getRibbonGroups().addAll(diagrammElemente, verbindungen, sonstiges, zoom);
		
		fuegeLogoHinzu(diagrammTab);
		
		return diagrammTab;
	}
	
	private RibbonGroup erstelleDiagrammElementeGruppe() {
		Button neueKlasse = new Button();
		setzeElementGrafik(neueKlasse, "klassenBezeichnung", "Klasse");
		Button neuesInterface = new Button();
		setzeElementGrafik(neuesInterface, "interfaceBezeichnung", "Interface",
				"interfaceStereotyp", "<<interface>>");
		Button neueEnumeration = new Button();
		setzeElementGrafik(neueEnumeration, "enumerationBezeichnung", "Enumeration",
				"enumerationStereotyp", "<<enumeration>>");
		RibbonGroup diagrammElemente = new RibbonGroup();
		diagrammElemente.titleProperty().bind(sprache.getTextProperty("diagrammElemente",
				"Diagramm Elemente"));
		diagrammElemente.getNodes().addAll(neueKlasse, neuesInterface, neueEnumeration);
		
		NodeUtil.macheUnfokussierbar(diagrammElemente.getNodes());
		
		return diagrammElemente;
	}
	
	private void setzeElementGrafik(Labeled node, String bezeichnungSchluessel,
			String alternativBezeichnung) {
		this.setzeElementGrafik(node, bezeichnungSchluessel, alternativBezeichnung, null,
				null);
	}
	
	private void setzeElementGrafik(Labeled node, String bezeichnungSchluessel,
			String alternativBezeichnung, String stereotypSchluessel,
			String alternativStereotyp) {
		var icon = new ElementIcon(sprache, bezeichnungSchluessel, alternativBezeichnung,
				stereotypSchluessel, alternativStereotyp);
		Node container = NodeUtil.plusIconHinzufuegen(icon);
		node.setGraphic(container);
	}
	
	private RibbonGroup erstelleVerbindungenGruppe() {
		Button vererbung = SprachUtil.bindText(new Button(), sprache, "vererbung",
				"Vererbung");
		NodeUtil.fuegeGrafikHinzu(vererbung, Ressourcen.get().UML_VERERBUNGS_PFEIL, 70);
		Button assoziation = SprachUtil.bindText(new Button(), sprache, "assoziation",
				"Assoziation");
		assoziation.setId("assoziationButton");
		NodeUtil.fuegeGrafikHinzu(assoziation,
				Ressourcen.get().UML_ASSOZIATIONS_PFEIL, 25);
		RibbonGroup verbindungen = new RibbonGroup();
		verbindungen.titleProperty().bind(sprache.getTextProperty("verbindungen",
				"Verbindungen"));
		verbindungen.getNodes().addAll(vererbung, assoziation);
		
		NodeUtil.macheUnfokussierbar(verbindungen.getNodes());
		
		return verbindungen;
	}
	
	private RibbonGroup erstelleSonstigeGruppe() {
		Button kommentar = new Button();
		var kommentarGrafik = NodeUtil.fuegeGrafikHinzu(kommentar, Ressourcen.get().UML_KOMMENTAR, 90);
		var kommentarContainer = NodeUtil.plusIconHinzufuegen(kommentarGrafik);
		kommentar.setGraphic(kommentarContainer);
		kommentar.setGraphicTextGap(-30);
		RibbonGroup sonstiges = new RibbonGroup();
		sonstiges.titleProperty().bind(sprache.getTextProperty("kommentar", "Kommentar"));
		sonstiges.getNodes().add(kommentar);
		
		NodeUtil.macheUnfokussierbar(sonstiges.getNodes());
		
		return sonstiges;
	}
	
	private RibbonGroup erstelleZoomGruppe() {
		Button zoomGroesser = SprachUtil.bindText(new Button(), sprache, "vergroessern",
				"Vergr%c%cern".formatted(oe, sz));
		Button zoomKleiner = SprachUtil.bindText(new Button(), sprache, "verkleinern",
				"Verkleinern");
		Button zoomOriginalgroesse = SprachUtil.bindText(new Button(), sprache,
				"originalgroesse", "Originalgr%c%ce".formatted(oe, sz));
		NodeUtil.fuegeIconHinzu(zoomGroesser, CarbonIcons.ZOOM_IN);
		NodeUtil.fuegeIconHinzu(zoomKleiner, CarbonIcons.ZOOM_OUT);
		NodeUtil.fuegeIconHinzu(zoomOriginalgroesse, CarbonIcons.ICA_2D);
		
		Column spalte = new Column();
		spalte.getChildren().addAll(zoomGroesser, zoomKleiner, zoomOriginalgroesse);
		
		RibbonGroup zoom = new RibbonGroup();
		zoom.titleProperty().bind(sprache.getTextProperty("zoom",
				"Zoom"));
		zoom.getNodes().addAll(spalte);
		
		NodeUtil.macheUnfokussierbar(zoom.getNodes());
		NodeUtil.macheUnfokussierbar(spalte.getChildren());
		
		return zoom;
	}
	
	private void fuegeLogoHinzu(RibbonTab tab) {
		try {
			ImageView classifierLogo = new ImageView(
					new Image(Ressourcen.get().CLASSIFIER_LOGO_M.oeffneStream()));
			classifierLogo.setPreserveRatio(true);
			classifierLogo.setSmooth(true);
			classifierLogo.setCache(true);
			classifierLogo.setFitHeight(128);
			
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
	
	// Ende Ribbon
	// =====================================================================================
	// Beginn Projektansicht
	
	
	/**
	 * eindeutige ID, mit der Projekte in einer Map fuer spaeteren Zugriff gespeichert werden
	 */
	private int naechsteProjektID = 0;
	
	private void erzeugeProjekt() {
		Tab tab = new Tab();
		tab.setUserData(naechsteProjektID);	// Tab merkt sich die ID des zugehoerigen Projektes
		naechsteProjektID++;
	}
	
}