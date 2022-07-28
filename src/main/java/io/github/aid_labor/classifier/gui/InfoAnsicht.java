/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import static io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute.ue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.controlsfx.control.HyperlinkLabel;

import com.dlsc.gemsfx.EnhancedLabel;

import io.github.aid_labor.classifier.basis.BibliothekInfo;
import io.github.aid_labor.classifier.basis.ProgrammDetails;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;


public class InfoAnsicht extends Alert {
	private static final Logger log = Logger.getLogger(InfoAnsicht.class.getName());
	
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
// #                                                                              		      #
// #	Instanzen																			  #
// #																						  #
// ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ## ##
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Attribute																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	private final ProgrammDetails programmDetails;
	private final Sprache sprache;
	private final BorderPane wurzel;
	private final VBox infobox;
	private final VBox scrollbox;
	private final HostServices rechnerService;
	private final Runnable easterEgg;
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
	public InfoAnsicht(ProgrammDetails programmDetails, HostServices rechnerService, Runnable easterEgg) {
		super(AlertType.NONE);
		this.programmDetails = programmDetails;
		this.rechnerService = rechnerService;
		this.sprache = new Sprache();
		this.wurzel = new BorderPane();
		this.infobox = new VBox();
		this.scrollbox = new VBox();
		this.easterEgg = easterEgg;
		
		boolean spracheGesetzt = SprachUtil.setUpSprache(sprache,
				Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(), "InfoAnsicht");
		if (!spracheGesetzt) {
			sprache.ignoriereSprachen();
		}
		
		SprachUtil.bindText(this.titleProperty(), sprache, "infoTitel", "%cber".formatted(ue));
		
		erstelleProgrammInfo();
		erstelleUeber();
		scrollbox.getChildren().add(erstelleLibInfo());
		formatierung();
		
		ScrollPane scrollPane = new ScrollPane(scrollbox);
		scrollPane.setFitToWidth(true);
		scrollPane.getStyleClass().add("edge-to-edge");
		BorderPane.setMargin(scrollPane, new Insets(10, 10, 0, 10));
		
		wurzel.setTop(infobox);
		wurzel.setCenter(scrollPane);
		
		this.getDialogPane().setContent(wurzel);
		this.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
		this.getDialogPane().setMinSize(945, 540);
		this.getDialogPane().setPrefSize(945, 540);
		this.setResizable(true);
	}
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	
// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##
	
	private void formatierung() {
		infobox.setPadding(new Insets(10, 20, 10, 20));
		infobox.setSpacing(10);
		infobox.setAlignment(Pos.TOP_CENTER);
		scrollbox.setSpacing(10);
		scrollbox.setAlignment(Pos.TOP_CENTER);
	}
	
	private void erstelleProgrammInfo() {
		try {
			ImageView classifierLogo = new ImageView(
					new Image(Ressourcen.get().CLASSIFIER_LOGO_M.oeffneStream()));
			classifierLogo.setPreserveRatio(true);
			classifierLogo.setSmooth(true);
			classifierLogo.setCache(true);
			classifierLogo.setFitHeight(128);
			infobox.getChildren().add(classifierLogo);
		} catch (IllegalStateException | IOException e) {
			log.log(Level.WARNING, e, () -> "Logo konnte nicht geladen werden");
		}
		
		var programmName = new EnhancedLabel(programmDetails.name());
		programmName.getStyleClass().add("titel");
		
		var programmVersion = new EnhancedLabel(
				sprache.getText("version", "Version") + " " + programmDetails.version());
		var programmEntwickler = new EnhancedLabel(
				sprache.getText("entwickler", "Entwickler") + ": "
						+ programmDetails.entwickler());
		
		infobox.getChildren().addAll(programmName, programmVersion, programmEntwickler);
	}
	
	private void erstelleUeber() {
		var programmLink = new HyperlinkLabel(sprache.getText("homepage", "Homepage")
				+ ": [%s]".formatted(programmDetails.homepage()));
		programmLink.setOnAction(event -> {
			try {
				rechnerService.showDocument(programmDetails.homepage());
			} catch (Exception e) {
				log.log(Level.WARNING, e, () -> "Link konnte nicht geoeffnet werden");
			}
		});
		
		var programmInfo = new EnhancedLabel(sprache.getText("info", "[Programm-Info]"));
		programmInfo.setWrapText(true);
		programmInfo.setTextAlignment(TextAlignment.JUSTIFY);
		
		var programmDank = new EnhancedLabel(sprache.getText("danksagung", "[Programm-Info]"));
		programmDank.setWrapText(true);
		programmDank.setTextAlignment(TextAlignment.JUSTIFY);
		programmDank.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
			if (e.getButton().equals(MouseButton.PRIMARY) && e.getClickCount() == 9) {
				easterEgg.run();
			}
		});
		
		var lizenzText = new EnhancedLabel();
		try (var lizenzdatei = new BufferedReader(
				new InputStreamReader(Ressourcen.get().LIZENZ_DATEI.oeffneStream()))) {
			lizenzText.setText(lizenzdatei.lines().collect(Collectors.joining("\n")));
		} catch (IllegalStateException | IOException e) {
			lizenzText.setText(sprache.getText("lizenzPlatzhalter",
					"[Lizenzdatei konnte nicht gelesen werden]"));
			log.log(Level.WARNING, e, () -> "Lizenzdatei %s konnte nicht gelesen werden"
					.formatted(Ressourcen.get().LIZENZ_DATEI.toString()));
		}
		
		var lizenzContainer = new TitledPane(sprache.getText("lizenz", "Lizenz"), lizenzText);
		lizenzContainer.setExpanded(false);
		
		this.scrollbox.getChildren().addAll(programmInfo, programmDank, programmLink,
				lizenzContainer);
	}
	
	private Node erstelleLibInfo() {
		var libInfo = new EnhancedLabel(sprache.getText("libInfo", """
				Die aufgelisteten Bibliotheken wurden bei der Entwicklung verwendet. \
				Es gilt die jeweils angegebene Lizenz. \
				Vielen Dank an alle beteiligten Herausgeber und Entwickler:innen."""));
		libInfo.setWrapText(true);
		libInfo.setTextAlignment(TextAlignment.JUSTIFY);
		VBox.setMargin(libInfo, new Insets(10, 5, 10, 5));
		
		Collection<TitledPane> drittanbieter = erzeugeDrittanbieter();
		var drittanbieterContainer = new Accordion();
		drittanbieterContainer.getPanes().addAll(drittanbieter);
		
		VBox libbox = new VBox(libInfo);
		libbox.getChildren().addAll(drittanbieterContainer);
		
		var libContainer = new TitledPane(sprache.getText("libTitel",
				"Informationen zu verwendeten Open-Source Bibliotheken"), libbox);
		libContainer.setExpanded(false);
		
		return libContainer;
	}
	
	private Collection<TitledPane> erzeugeDrittanbieter() {
		Collection<TitledPane> drittanbieterListe = new LinkedList<>();
		try (var stream = Files
				.newDirectoryStream(Ressourcen.get().LIZENZ_INFO_ORDNER.alsPath())) {
			var eintraege = new TreeSet<Path>(
					(pfad1, pfad2) -> pfad1.getFileName().toString().toLowerCase()
							.compareTo(pfad2.getFileName().toString().toLowerCase()));
			for (Path unterordner : stream) {
				eintraege.add(unterordner);
				
			}
			eintraege.forEach(eintrag -> {
				TitledPane drittanbieterDetails = erzeugeDrittanbieterDetails(eintrag);
				drittanbieterListe.add(drittanbieterDetails);
			});
		} catch (Exception e) {
			log.log(Level.WARNING, e,
					() -> "Fehler beim Lesen der Drittanbieter Lizenz-Infos in %s"
							.formatted(Ressourcen.get().LIZENZ_INFO_ORDNER));
		}
		
		return drittanbieterListe;
	}
	
	private TitledPane erzeugeDrittanbieterDetails(Path unterordner) {
		var detailsContainer = new TitledPane();
		detailsContainer.setExpanded(false);
		
		var details = new VBox(10);
		detailsContainer.setContent(details);
		
		var lizenzDetails = new VBox(10);
		
		var bib = BibliothekInfo.ladeAusJson(unterordner.resolve("ueber.json"));
		if (bib == null) {
			log.warning(() -> "BibliothekInfo in %s konnte nicht gelesen werden"
					.formatted(unterordner));
		} else {
			detailsContainer.setText(bib.name());
			var beschreibung = new EnhancedLabel(bib.beschreibung());
			beschreibung.setWrapText(true);
			var version = new EnhancedLabel(
					sprache.getText("version", "Version") + ": " + bib.version());
			var github = new HyperlinkLabel("GitHub: [%s]".formatted(bib.github()));
			var link = new HyperlinkLabel("Website: [%s]".formatted(bib.link()));
			
			github.setOnAction(event -> {
				try {
					rechnerService.showDocument(bib.github());
				} catch (Exception e) {
					log.log(Level.WARNING, e, () -> "Link %s kann nicht geoeffnet werden"
							.formatted(bib.github()));
				}
			});
			link.setOnAction(event -> {
				try {
					rechnerService.showDocument(bib.link());
				} catch (Exception e) {
					log.log(Level.WARNING, e, () -> "Link %s kann nicht geoeffnet werden"
							.formatted(bib.link()));
				}
			});
			details.getChildren().addAll(beschreibung, version, github, link);
			
			var lizenzName = new EnhancedLabel(bib.license().name());
			var lizenzLink = new HyperlinkLabel("Link: [%s]".formatted(bib.license().link()));
			lizenzLink.setOnAction(event -> {
				try {
					rechnerService.showDocument(bib.license().link());
				} catch (Exception e) {
					log.log(Level.WARNING, e, () -> "Link %s kann nicht geoeffnet werden"
							.formatted(bib.license().link()));
				}
			});
			
			lizenzDetails.getChildren().addAll(lizenzName, lizenzLink, new Separator());
		}
		
		var lizenztextLabel = new EnhancedLabel(sprache.getText("lizenzPlatzhalter",
				"[Lizenzdatei konnte nicht gelesen werden]"));
		try {
			String lizenztext = Files.lines(unterordner.resolve("LICENSE.txt"))
					.collect(Collectors.joining("\n"));
			lizenztextLabel.setText(lizenztext);
		} catch (Exception e) {
			log.log(Level.WARNING, e, () -> "Lizenztext in %s konnte nicht gelesen werden"
					.formatted(unterordner));
		}
		
		lizenzDetails.getChildren().add(lizenztextLabel);
		
		var lizenzContainer = new TitledPane(sprache.getText("lizenz", "Lizenz"),
				lizenzDetails);
		lizenzContainer.setExpanded(false);
		details.getChildren().add(lizenzContainer);
		
		return detailsContainer;
	}
	
}