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

import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.gui.util.ButtonTyp;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;


public class RibbonKomponente {
	private static final Logger log = Logger.getLogger(RibbonKomponente.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private final Sprache sprache;
	private final Ribbon ribbon;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Tab Start
	private Button oeffnen;
	private Button neu;
	private Button importieren;
	private Button speichern;
	private Button screenshot;
	private Button exportieren;
	
	private Button kopieren;
	private Button einfuegen;
	private Button loeschen;
	private Button rueckgaengig;
	private Button wiederholen;
	
	private Button anordnenNachVorne;
	private Button anordnenNachGanzVorne;
	private Button anordnenNachHinten;
	private Button anordnenNachGanzHinten;
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	// Tab Diagramm
	private Button neueKlasse;
	private Button neuesInterface;
	private Button neueEnumeration;
	
	private Button vererbung;
	private Button assoziation;
	
	private Button kommentar;
	
	private Button zoomGroesser;
	private Button zoomKleiner;
	private Button zoomOriginalgroesse;
	
	private Button speichernSchnellzugriff;
	private Button rueckgaengigSchnellzugriff;
	private Button wiederholenSchnellzugriff;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public RibbonKomponente() {
		this.sprache = new Sprache();
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "RibbonAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		RibbonTab startTab = erstelleStartTab();
		RibbonTab diagrammTab = erstelleDiagrammTab();
		QuickAccessBar schnellzugriff = erstelleSchnellzugriff();
		
		this.ribbon = new Ribbon();
		
		ribbon.setQuickAccessBar(schnellzugriff);
		ribbon.getTabs().addAll(startTab, diagrammTab);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	public Ribbon getRibbon() {
		return ribbon;
	}
	
	public Button getOeffnen() {
		return oeffnen;
	}
	
	public Button getNeu() {
		return neu;
	}
	
	public Button getImportieren() {
		return importieren;
	}
	
	public Button getSpeichern() {
		return speichern;
	}
	
	public Button getScreenshot() {
		return screenshot;
	}
	
	public Button getExportieren() {
		return exportieren;
	}
	
	public Button getKopieren() {
		return kopieren;
	}
	
	public Button getEinfuegen() {
		return einfuegen;
	}
	
	public Button getLoeschen() {
		return loeschen;
	}
	
	public Button getRueckgaengig() {
		return rueckgaengig;
	}
	
	public Button getWiederholen() {
		return wiederholen;
	}
	
	public Button getAnordnenNachVorne() {
		return anordnenNachVorne;
	}
	
	public Button getAnordnenNachGanzVorne() {
		return anordnenNachGanzVorne;
	}
	
	public Button getAnordnenNachHinten() {
		return anordnenNachHinten;
	}
	
	public Button getAnordnenNachGanzHinten() {
		return anordnenNachGanzHinten;
	}
	
	public Button getNeueKlasse() {
		return neueKlasse;
	}
	
	public Button getNeuesInterface() {
		return neuesInterface;
	}
	
	public Button getNeueEnumeration() {
		return neueEnumeration;
	}
	
	public Button getVererbung() {
		return vererbung;
	}
	
	public Button getAssoziation() {
		return assoziation;
	}
	
	public Button getKommentar() {
		return kommentar;
	}
	
	public Button getZoomGroesser() {
		return zoomGroesser;
	}
	
	public Button getZoomKleiner() {
		return zoomKleiner;
	}
	
	public Button getZoomOriginalgroesse() {
		return zoomOriginalgroesse;
	}
	
	public Button getSpeichernSchnellzugriff() {
		return speichernSchnellzugriff;
	}
	
	public Button getRueckgaengigSchnellzugriff() {
		return rueckgaengigSchnellzugriff;
	}
	
	public Button getWiederholenSchnellzugriff() {
		return wiederholenSchnellzugriff;
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
		oeffnen = SprachUtil.bindText(new Button(), sprache, "oeffnen",
				"%cffnen".formatted(OE));
		neu = SprachUtil.bindText(new Button(), sprache, "neu",
				"Neu...");
		importieren = SprachUtil.bindText(new Button(), sprache, "importieren",
				"Importieren");
		NodeUtil.fuegeIconHinzu(oeffnen, Typicons.FOLDER_OPEN);
		NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS);
		NodeUtil.fuegeIconHinzu(importieren, CarbonIcons.DOWNLOAD);
		Column ersteSpalte = new Column();
		ersteSpalte.getChildren().addAll(oeffnen, neu, importieren);
		
		speichern = neuerButton(ButtonTyp.SPEICHERN);
		screenshot = SprachUtil.bindText(new Button(), sprache, "screenshot",
				"Screenshot...");
		exportieren = SprachUtil.bindText(new Button(), sprache, "exportieren",
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
		kopieren = SprachUtil.bindText(new Button(), sprache, "kopieren",
				"Kopieren");
		einfuegen = SprachUtil.bindText(new Button(), sprache, "einfuegen",
				"Einf%cgen".formatted(ue));
		loeschen = SprachUtil.bindText(new Button(), sprache, "loeschen",
				"L%cschen".formatted(oe));
		NodeUtil.fuegeIconHinzu(kopieren, CarbonIcons.COPY_FILE);
		NodeUtil.fuegeIconHinzu(einfuegen, CarbonIcons.PASTE);
		NodeUtil.fuegeIconHinzu(loeschen, CarbonIcons.DELETE);
		Column spalte = new Column();
		spalte.getChildren().addAll(kopieren, einfuegen, loeschen);
		
		rueckgaengig = neuerButton(ButtonTyp.RUECKGAENGIG);
		wiederholen = neuerButton(ButtonTyp.WIEDERHOLEN);
		
		RibbonGroup bearbeiten = new RibbonGroup();
		bearbeiten.titleProperty().bind(sprache.getTextProperty("bearbeiten", "Bearbeiten"));
		bearbeiten.getNodes().addAll(spalte, rueckgaengig, wiederholen);
		
		NodeUtil.macheUnfokussierbar(bearbeiten.getNodes());
		NodeUtil.macheUnfokussierbar(spalte.getChildren());
		
		return bearbeiten;
	}
	
	private RibbonGroup erstelleAnordnenGruppe() {
		anordnenNachVorne = SprachUtil.bindText(new Button(), sprache,
				"nachVorne", "Eine Ebene nach vorne");
		anordnenNachGanzVorne = SprachUtil.bindText(new Button(), sprache,
				"nachGanzVorne", "In den Vordergrund");
		NodeUtil.fuegeIconHinzu(anordnenNachVorne, BootstrapIcons.LAYER_FORWARD);
		NodeUtil.fuegeIconHinzu(anordnenNachGanzVorne, WhhgAL.LAYERORDERUP);
		Column ersteSpalte = new Column();
		ersteSpalte.getChildren().addAll(anordnenNachVorne, anordnenNachGanzVorne);
		
		anordnenNachHinten = SprachUtil.bindText(new Button(), sprache,
				"nachHinten", "Eine Ebene nach vorne");
		anordnenNachGanzHinten = SprachUtil.bindText(new Button(), sprache,
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
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
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
		neueKlasse = new Button();
		setzeElementGrafik(neueKlasse, "klassenBezeichnung", "Klasse");
		neuesInterface = new Button();
		setzeElementGrafik(neuesInterface, "interfaceBezeichnung", "Interface",
				"interfaceStereotyp", "<<interface>>");
		neueEnumeration = new Button();
		setzeElementGrafik(neueEnumeration, "enumerationBezeichnung", "Enumeration",
				"enumerationStereotyp", "<<enumeration>>");
		RibbonGroup diagrammElemente = new RibbonGroup();
		diagrammElemente.titleProperty().bind(sprache.getTextProperty("diagrammElemente",
				"Diagramm Elemente"));
		diagrammElemente.getNodes().addAll(neueKlasse, neuesInterface, neueEnumeration);
		
		NodeUtil.macheUnfokussierbar(diagrammElemente.getNodes());
		
		return diagrammElemente;
	}
	
	private RibbonGroup erstelleVerbindungenGruppe() {
		vererbung = SprachUtil.bindText(new Button(), sprache, "vererbung",
				"Vererbung");
		NodeUtil.fuegeGrafikHinzu(vererbung, Ressourcen.get().UML_VERERBUNGS_PFEIL, 70);
		assoziation = SprachUtil.bindText(new Button(), sprache, "assoziation",
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
		kommentar = new Button();
		var kommentarGrafik = NodeUtil.fuegeGrafikHinzu(kommentar,
				Ressourcen.get().UML_KOMMENTAR, 90);
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
		zoomGroesser = SprachUtil.bindText(new Button(), sprache, "vergroessern",
				"Vergr%c%cern".formatted(oe, sz));
		zoomKleiner = SprachUtil.bindText(new Button(), sprache, "verkleinern",
				"Verkleinern");
		zoomOriginalgroesse = SprachUtil.bindText(new Button(), sprache,
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
	
	// -------------------------------------------------------------------------------------
	
	private QuickAccessBar erstelleSchnellzugriff() {
		QuickAccessBar schnellzugriff = new QuickAccessBar();
		
		speichernSchnellzugriff = neuerButton(ButtonTyp.SPEICHERN);
		rueckgaengigSchnellzugriff = neuerButton(ButtonTyp.RUECKGAENGIG);
		wiederholenSchnellzugriff = neuerButton(ButtonTyp.WIEDERHOLEN);
		((FontIcon) speichernSchnellzugriff.getGraphic()).setIconSize(18);
		((FontIcon) rueckgaengigSchnellzugriff.getGraphic()).setIconSize(18);
		((FontIcon) wiederholenSchnellzugriff.getGraphic()).setIconSize(18);
		NodeUtil.macheUnfokussierbar(speichernSchnellzugriff, rueckgaengigSchnellzugriff,
				wiederholenSchnellzugriff);
		
		schnellzugriff.getButtons().addAll(speichernSchnellzugriff, rueckgaengigSchnellzugriff,
				wiederholenSchnellzugriff);
		
		return schnellzugriff;
	}
	
	// =====================================================================================
	
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
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
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
	
	// * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
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
	
}