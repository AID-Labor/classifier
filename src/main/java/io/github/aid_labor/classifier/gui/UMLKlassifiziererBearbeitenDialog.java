/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.gui;

import java.lang.ref.WeakReference;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;
import org.controlsfx.control.SegmentedButton;
import org.controlsfx.validation.ValidationResult;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.kordamp.ikonli.remixicon.RemixiconAL;
import org.kordamp.ikonli.typicons.Typicons;

import com.dlsc.gemsfx.SearchField;
import com.dlsc.gemsfx.TagsField;
import com.tobiasdiez.easybind.EasyBind;
import com.tobiasdiez.easybind.Subscription;

import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Umlaute;
import io.github.aid_labor.classifier.basis.validierung.SimpleValidierung;
import io.github.aid_labor.classifier.basis.validierung.ValidierungCollection;
import io.github.aid_labor.classifier.gui.komponenten.CustomNodeTableCell;
import io.github.aid_labor.classifier.gui.komponenten.CustomNodeTableCell.UpdateCallback;
import io.github.aid_labor.classifier.gui.komponenten.DatentypFeld;
import io.github.aid_labor.classifier.gui.komponenten.ListControlsTableCell;
import io.github.aid_labor.classifier.gui.komponenten.OnlyTextFieldTableCell;
import io.github.aid_labor.classifier.gui.komponenten.VerbindungBearbeitenListe;
import io.github.aid_labor.classifier.gui.komponenten.VerbindungBearbeitenListe.Typ;
import io.github.aid_labor.classifier.gui.util.FXValidierungUtil;
import io.github.aid_labor.classifier.gui.util.NodeUtil;
import io.github.aid_labor.classifier.uml.UMLProjekt;
import io.github.aid_labor.classifier.uml.klassendiagramm.KlassifiziererTyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLDiagrammElement;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLVerbindungstyp;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Attribut;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.HatParameterListe;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Konstruktor;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Methode;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Modifizierer;
import io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften.Parameter;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class UMLKlassifiziererBearbeitenDialog extends Alert {
    private static final Logger log = Logger.getLogger(UMLKlassifiziererBearbeitenDialog.class.getName());

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Klassenattribute																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public static final String CSS_EINGABE_FEHLER = "eingabefehler";

    private static final String validierungKey = "ValidierungSubscription";

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

    private final UMLKlassifizierer klassifizierer;
    private final WeakReference<UMLProjekt> umlProjektRef;
    private final Sprache sprache;
    private final SegmentedButton buttonBar;
    private final ToggleButton allgemein;
    private final ToggleButton attribute;
    private final ToggleButton konstruktoren;
    private final ToggleButton methoden;
    private final ToggleButton assoziation;
    private final ToggleButton vererbung;
    private final ValidationSupport eingabeValidierung;
    private final List<ChangeListener<KlassifiziererTyp>> typBeobachterListe;
    private final List<ChangeListener<ValidationResult>> validierungsBeobachter;
    private final List<Runnable> loeseBindungen;
    private final SortedMap<String, UMLKlassifizierer> klassenSuchBaum;
    private final List<String> vorhandeneKlassen;
    private final List<String> vorhandeneInterfaces;

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Konstruktoren																		*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    public UMLKlassifiziererBearbeitenDialog(UMLKlassifizierer klassifizierer, UMLProjekt projekt) {
        super(AlertType.NONE);
        this.klassifizierer = Objects.requireNonNull(klassifizierer);
        this.umlProjektRef = new WeakReference<>(projekt);
        this.sprache = new Sprache();
        this.eingabeValidierung = new ValidationSupport();
        this.typBeobachterListe = new LinkedList<>();
        this.validierungsBeobachter = new LinkedList<>();
        this.loeseBindungen = new LinkedList<>();
        List<UMLKlassifizierer> klassen = projekt.getDiagrammElemente().stream()
                .filter(UMLKlassifizierer.class::isInstance).map(UMLKlassifizierer.class::cast).toList();
        this.klassenSuchBaum = new TreeMap<>(
                klassen.parallelStream().collect(Collectors.toMap(UMLKlassifizierer::getName, Function.identity())));
        this.vorhandeneKlassen = projekt.getDiagrammElemente().parallelStream()
                .filter(e -> e instanceof UMLKlassifizierer k && k.getId() != klassifizierer.getId()
                        && !KlassifiziererTyp.Interface.equals(k.getTyp()))
                .map(UMLDiagrammElement::getName).sorted().toList();
        this.vorhandeneInterfaces = projekt.getDiagrammElemente().parallelStream()
                .filter(e -> e instanceof UMLKlassifizierer k && k.getId() != klassifizierer.getId()
                        && KlassifiziererTyp.Interface.equals(k.getTyp()))
                .map(UMLDiagrammElement::getName).sorted().toList();

        boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
                "UMLKlassifiziererBearbeitenDialog");
        if (!spracheGesetzt) {
            sprache.ignoriereSprachen();
        }

        this.allgemein = new ToggleButton(sprache.getText("allgemein", "Allgemein"));
        this.attribute = new ToggleButton(sprache.getText("attribute", "Attribute"));
        this.konstruktoren = new ToggleButton(sprache.getText("konstruktoren", "Konstruktoren"));
        this.methoden = new ToggleButton(sprache.getText("methoden", "Methoden"));
        this.assoziation = new ToggleButton(sprache.getText("assoziationen", "Assoziationen"));
        this.vererbung = new ToggleButton(sprache.getText("vererbungen", "Vererbungen"));
        this.buttonBar = new SegmentedButton(allgemein, attribute, konstruktoren, methoden, assoziation, vererbung);
        this.buttonBar.getToggleGroup().selectToggle(allgemein);
        HBox buttonContainer = initialisiereButtonBar();

        BorderPane wurzel = new BorderPane();
        wurzel.setTop(buttonContainer);

        erzeugeInhalt(wurzel);

        getDialogPane().setContent(wurzel);
        initialisiereButtons();
        this.setResizable(true);
        this.getDialogPane().setPrefSize(920, 512);

        NodeUtil.beobachteSchwach(wurzel, klassifizierer.nameProperty(), (alterName, neuerName) -> {
            if (this.klassenSuchBaum == null) {
                log.fine("klassenSuchBaum is null");
                return;
            }
            if (alterName != null && this.klassenSuchBaum.get(alterName) != null) {
                this.klassenSuchBaum.remove(alterName);
            }
            if (neuerName != null) {
                this.klassenSuchBaum.put(neuerName, getKlassifizierer());
            }
        });

        this.setOnHidden(e -> {
            log.config(() -> "räume UMLKlassifizeierBearbeitenDialog auf");
            for (var beobachter : typBeobachterListe) {
                klassifizierer.typProperty().removeListener(beobachter);
            }
            for (var beobachter : validierungsBeobachter) {
                eingabeValidierung.validationResultProperty().removeListener(beobachter);
            }

            for (Node n : this.getDialogPane().getChildren()) {
                entferneAlleBeobachter(n);
            }

            for (var c : eingabeValidierung.getRegisteredControls()) {
                eingabeValidierung.registerValidator(c, Validator.createPredicateValidator(x -> true, ""));
            }
            for (var prop : loeseBindungen) {
                prop.run();
            }
            loeseBindungen.clear();

            for (var attribut : this.getClass().getDeclaredFields()) {
                try {
                    if (!Modifier.isStatic(attribut.getModifiers()) && !attribut.getName().equals("istGeschlossen")) {
                        attribut.setAccessible(true);
                        attribut.set(this, null);
                    }
                } catch (Exception ex) {
                    // ignore
                    ex.printStackTrace();
                }
            }
            log.config(() -> "aufräumen abgeschlossen");
        });
    }

    private void entferneAlleBeobachter(Node n) {
        NodeUtil.entferneSchwacheBeobachtung(n);
        n.getProperties().remove(this);
        if (n instanceof Parent p) {
            for (Node kind : p.getChildrenUnmodifiable()) {
                entferneAlleBeobachter(kind);
            }
        }
        if (n instanceof AutoCloseable ac) {
            try {
                ac.close();
            } catch (Exception e1) {
                //
            }
        }
    }

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Getter und Setter																	*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    private UMLKlassifizierer getKlassifizierer() {
        return klassifizierer;
    }

//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
//  *	Methoden																			*
//	* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

// public	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// protected 	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// package	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

// private	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##	##

    private HBox initialisiereButtonBar() {
        allgemein.setSelected(true);
        for (var button : buttonBar.getButtons()) {
            button.setFocusTraversable(false);
        }
        NodeUtil.beobachteSchwach(buttonBar, buttonBar.getToggleGroup().selectedToggleProperty(),
                (alteWahl, neueWahl) -> {
                    if (neueWahl == null) {
                        buttonBar.getToggleGroup().selectToggle(alteWahl);
                    }
                });
        addValidierungCollection(this.attribute, getKlassifizierer(), () -> getKlassifizierer().getAttributeValid());
        addValidierungCollection(this.methoden, getKlassifizierer(), () -> getKlassifizierer().getMethodenValid());
        addValidierungCollection(this.konstruktoren, getKlassifizierer(),
                () -> getKlassifizierer().getKonstruktorenValid());
        HBox buttonContainer = new HBox(buttonBar);
        buttonContainer.setPadding(new Insets(5, 20, 40, 20));
        buttonContainer.setAlignment(Pos.CENTER);
        return buttonContainer;
    }

    private void erzeugeInhalt(BorderPane wurzel) {
        var allgemeinAnzeige = erzeugeAllgemeinAnzeige();
        var attributeAnzeige = erzeugeTabellenAnzeige(this.getKlassifizierer().attributeProperty(),
                this.getAttributSpalten(), event -> {
                    var programmierEigenschaften = getKlassifizierer().getProgrammiersprache().getEigenschaften();
                    this.getKlassifizierer().attributeProperty()
                            .add(new Attribut(
                                    programmierEigenschaften
                                            .getStandardAttributModifizierer(getKlassifizierer().getTyp()),
                                    programmierEigenschaften.getLetzerDatentyp()));
                });
        var konstruktorenAnzeige = erzeugeTabellenAnzeige(this.getKlassifizierer().konstruktorProperty(),
                this.getKonstruktorSpalten(), event -> {
                    if (!getKlassifizierer().getTyp().equals(KlassifiziererTyp.Interface)) {
                        var programmiersprache = getKlassifizierer().getProgrammiersprache();
                        var modifizierer = programmiersprache.getEigenschaften()
                                .getStandardKonstruktorModifizierer(getKlassifizierer().getTyp());
                        var neuerKonstruktor = new Konstruktor(modifizierer);
                        this.getKlassifizierer().konstruktorProperty().add(neuerKonstruktor);
                    }
                });
        var methodenAnzeige = erzeugeTabellenAnzeige(this.getKlassifizierer().methodenProperty(),
                this.getMethodeSpalten(), event -> {
                    var programmierEigenschaften = getKlassifizierer().getProgrammiersprache().getEigenschaften();
                    var methode = new Methode(
                            programmierEigenschaften.getStandardMethodenModifizierer(getKlassifizierer().getTyp()),
                            programmierEigenschaften.getLetzerDatentyp(), getKlassifizierer().getProgrammiersprache());
                    if (getKlassifizierer().getTyp().equals(KlassifiziererTyp.Interface)) {
                        methode.setzeAbstrakt(true);
                    }
                    this.getKlassifizierer().methodenProperty().add(methode);
                });
        var assoziationAnzeige = new VerbindungBearbeitenListe(
                new String[] { "Klasse/Interface", "Verwendet...", "Ausgeblendet" }, Typ.ASSOZIATION,
                eingabeValidierung, sprache, umlProjektRef.get(), false);
        assoziationAnzeige.setVerbindungenFilter(v -> {
            try {
                return v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION)
                        && getKlassifizierer().getName()
                                .equals(UMLKlassifizierer.nameOhnePaket(v.getVerbindungsStart()));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
        assoziationAnzeige.setVerbindungErzeuger(
                () -> new UMLVerbindung(UMLVerbindungstyp.ASSOZIATION, klassifizierer.getName(), ""));
        var vererbungAnzeige = new VerbindungBearbeitenListe(
                new String[] { "Klasse/Interface", "Superklasse/Interface", "Ausgeblendet" }, Typ.VERERBUNG,
                eingabeValidierung, sprache, umlProjektRef.get(), false);
        vererbungAnzeige.setVerbindungenFilter(v -> {
            try {
                return !v.getTyp().equals(UMLVerbindungstyp.ASSOZIATION)
                        && (klassifizierer.getName().equals(UMLKlassifizierer.nameOhnePaket(v.getVerbindungsStart()))
                                || klassifizierer.getName()
                                        .equals(UMLKlassifizierer.nameOhnePaket(v.getVerbindungsEnde())));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });

        StackPane container = new StackPane(allgemeinAnzeige, attributeAnzeige, konstruktorenAnzeige, methodenAnzeige,
                assoziationAnzeige, vererbungAnzeige);
        container.setPadding(new Insets(0, 20, 10, 20));
        container.setAlignment(Pos.TOP_CENTER);
        BorderPane.setAlignment(container, Pos.TOP_CENTER);
        wurzel.setCenter(container);

        ueberwacheSelektion(this.allgemein, allgemeinAnzeige);
        ueberwacheSelektion(this.attribute, attributeAnzeige);
        ueberwacheSelektion(this.konstruktoren, konstruktorenAnzeige);
        ueberwacheSelektion(this.methoden, methodenAnzeige);
        ueberwacheSelektion(this.assoziation, assoziationAnzeige);
        ueberwacheSelektion(this.vererbung, vererbungAnzeige);

        this.konstruktoren.disableProperty()
                .bind(Bindings.equal(KlassifiziererTyp.Interface, getKlassifizierer().typProperty()));
    }

    private void ueberwacheSelektion(ToggleButton button, Node anzeige) {
        anzeige.setVisible(button.isSelected());
        anzeige.visibleProperty().bind(button.selectedProperty().and(button.disabledProperty().not()));
    }

    private GridPane erzeugeAllgemeinAnzeige() {
        GridPane tabelle = new GridPane();

        String[] labelBezeichnungen = { "Typ", "Paket", "Name", "Sichtbarkeit", "Superklasse", "Interfaces" };
        Label interfaceLabel = null;
        for (int zeile = 0; zeile < labelBezeichnungen.length; zeile++) {
            String bezeichnung = labelBezeichnungen[zeile];
            var label = SprachUtil.bindText(new Label(), sprache, bezeichnung, bezeichnung + ":");
            if (zeile == labelBezeichnungen.length - 1) {
                // Label Interfaces oben
                GridPane.setValignment(label, VPos.TOP);
                interfaceLabel = label;
            }
            tabelle.add(label, 0, zeile);
        }

        ComboBox<KlassifiziererTyp> typ = new ComboBox<>();
        typ.getItems().addAll(KlassifiziererTyp.values());
        typ.getSelectionModel().select(this.getKlassifizierer().getTyp());
        tabelle.add(typ, 1, 0);

        TextField paket = new TextField(this.getKlassifizierer().getPaket());
        tabelle.add(paket, 1, 1);

        TextField name = new TextField(this.getKlassifizierer().getName());
        tabelle.add(name, 1, 2);

        var sichtbarkeit = erzeugeSichtbarkeit();
        tabelle.add(sichtbarkeit, 1, 3);

        SearchField<String> superklasse = erzeugeSuperklasseEingabe();
        tabelle.add(superklasse, 1, 4);

        TagsField<String> interfaces = erzeugeInterfacesEingabe();
        ScrollPane interfacesScroll = new ScrollPane(interfaces);
        interfacesScroll.setVbarPolicy(ScrollBarPolicy.NEVER);
        tabelle.add(interfaces, 1, 5);
        var interfaceL = interfaceLabel;
        interfaceLabel.paddingProperty()
                .bind(Bindings.createObjectBinding(
                        () -> new Insets((name.getHeight() - interfaceL.getHeight()) / 2, 0, 0, 0),
                        name.heightProperty(), interfaceLabel.heightProperty()));
        Platform.runLater(() -> {
            var tf = interfaces.getEditor();
            interfaces.setMaxSize(name.getWidth(), 800);
            NodeUtil.setzeBreite(name.getWidth(), tf);
            NodeUtil.setzeBreite(name.getWidth(), superklasse);
        });

        NodeUtil.beobachteSchwach(typ, typ.getSelectionModel().selectedItemProperty(),
                t -> getKlassifizierer().setTyp(t));
        bindeBidirektional(getKlassifizierer().paketProperty(), paket.textProperty());
        bindeBidirektional(getKlassifizierer().nameProperty(), name.textProperty());
        name.setOnKeyTyped(e -> eingabeValidierung.revalidate());

        tabelle.setHgap(5);
        tabelle.setVgap(15);
        tabelle.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        validiereAllgemein(name, superklasse, interfaces);

        updateSuperklasse(superklasse, getKlassifizierer().getTyp());
        ChangeListener<KlassifiziererTyp> typBeobachter = (p, alteWahl, neueWahl) -> updateSuperklasse(superklasse,
                neueWahl);
        this.typBeobachterListe.add(typBeobachter);
        typ.getSelectionModel().selectedItemProperty().addListener(typBeobachter);

        this.setOnShown(e -> Platform.runLater(name::requestFocus));

        return tabelle;
    }

    private TagsField<String> erzeugeInterfacesEingabe() {
        TagsField<String> interfaces = new TagsField<>() {
            @Override
            protected void update(Collection<String> newSuggestions) {
                super.update(newSuggestions);
                eingabeValidierung.revalidate();
            }
        };
        interfaces.getEditor().setOnKeyTyped(e -> eingabeValidierung.revalidate());
        interfaces.addTags(
                getKlassifizierer().getInterfaces().toArray(new String[getKlassifizierer().getInterfaces().size()]));
        Bindings.bindContentBidirectional(interfaces.getTags(), getKlassifizierer().getInterfaces());
        interfaces.setSuggestionProvider(request -> {
            var vorschlaege = new ArrayList<String>();
            vorschlaege.add(request.getUserText());
            vorschlaege.addAll(vorhandeneInterfaces.stream()
                    .filter(iName -> iName.toLowerCase().contains(request.getUserText().toLowerCase())).toList());
            vorschlaege.addAll(umlProjektRef.get().getProgrammiersprache().getEigenschaften().getBekannteInterfaces()
                    .stream().filter(iName -> iName.toLowerCase().contains(request.getUserText().toLowerCase()))
                    .toList());
            return vorschlaege;
        });
        interfaces.setTagViewFactory(text -> {
            Label tag = new Label(text);
            Label loeschen = new Label();
            loeschen.setOnMouseClicked(e -> {
                interfaces.removeTags(text);
                getKlassifizierer().getInterfaces().remove(text);
                eingabeValidierung.revalidate();
                interfaces.setSelectedItem(null);
                interfaces.getTagSelectionModel().clearSelection();
            });
            var icon = NodeUtil.fuegeIconHinzu(loeschen, RemixiconAL.CLOSE_FILL, 18, ContentDisplay.GRAPHIC_ONLY,
                    "tag-button-font-icon");
            icon.getStyleClass().add("tag");
            tag.setGraphic(loeschen);
            tag.setContentDisplay(ContentDisplay.RIGHT);
            tag.setGraphicTextGap(5);
            tag.getStyleClass().add("tag");
            return tag;
        });
        interfaces.setNewItemProducer(String::strip);
        interfaces.setShowSearchIcon(false);
        interfaces.setHidePopupWithSingleChoice(false);
        return interfaces;
    }

    private SearchField<String> erzeugeSuperklasseEingabe() {
        SearchField<String> superklasse = new SearchField<>() {
            @Override
            protected void update(Collection<String> newSuggestions) {
                super.update(newSuggestions);
                eingabeValidierung.revalidate();
            }
        };
        superklasse.setSelectedItem(getKlassifizierer().getSuperklasse());
        superklasse.setText(getKlassifizierer().getSuperklasse());
        superklasse.cancel();
        bindeBidirektional(superklasse.getEditor().textProperty(), getKlassifizierer().superklasseProperty());
        Button loeschen = new Button();
        loeschen.setOnMouseClicked(e -> superklasse.select(""));
        NodeUtil.fuegeIconHinzu(loeschen, RemixiconAL.DELETE_BACK_2_LINE, 18, ContentDisplay.GRAPHIC_ONLY,
                "loeschen-button-font-icon");
        loeschen.setPadding(new Insets(0, 5, 0, 5));
        loeschen.prefHeightProperty().bind(superklasse.getEditor().heightProperty());
        superklasse.setRight(loeschen);
        superklasse.setSuggestionProvider(request -> {
            var vorschlaege = new ArrayList<String>();
            vorschlaege.add(request.getUserText());
            vorschlaege.addAll(vorhandeneKlassen.stream()
                    .filter(iName -> iName.toLowerCase().contains(request.getUserText().toLowerCase())).toList());
            vorschlaege.addAll(umlProjektRef.get().getProgrammiersprache().getEigenschaften().getBekannteKlassen()
                    .stream().filter(iName -> iName.toLowerCase().contains(request.getUserText().toLowerCase()))
                    .toList());
            return vorschlaege;
        });
        superklasse.setNewItemProducer(String::strip);
        superklasse.setShowSearchIcon(false);
        superklasse.setHidePopupWithSingleChoice(false);
        return superklasse;
    }

    private void validiereAllgemein(TextField name, SearchField<?> superklasse, TagsField<String> interfaces) {
        Platform.runLater(() -> {
            /*
             * eingabeValidierung.registerValidator(name,
             * Validator.combine(
             * Validator.createEmptyValidator(
             * sprache.getText("klassennameValidierung",
             * "Der Klassenname muss angegeben werden")),
             * Validator.createPredicateValidator(tf ->
             * !vorhandeneElementNamen.contains(name.getText()),
             * sprache.getText("klassennameValidierung2",
             * "Ein Element mit diesem Namen ist bereits vorhanden"))));
             * setzePlatzhalter(name);
             */
            Subscription nameBeobachter = FXValidierungUtil.setzePlatzhalter(name,
                    this.getKlassifizierer().getNameValidierung());
            this.loeseBindungen.add(nameBeobachter::unsubscribe);
            if (umlProjektRef.get().getProgrammiersprache().getEigenschaften()
                    .erlaubtSuperklasse(getKlassifizierer().getTyp())) {
                eingabeValidierung.registerValidator(superklasse.getEditor(),
                        Validator.combine(Validator.createPredicateValidator(tf -> {
                            if (superklasse.getEditor().getText().isBlank()) {
                                return true;
                            }
                            String superName = superklasse.getEditor().getText();
                            if (superName.contains(":")) {
                                superName = superName.substring(superName.lastIndexOf(":") + 1);
                            }
                            boolean gleich = Objects.equals(name.getText(), superName);
                            return gleich ? name.getText().isBlank()
                                    : !istZirkular(getKlassifizierer(), klassenSuchBaum);
                        }, sprache.getText("superklasseValidierungZirkular",
                                "Die Vererbungshierarchie darf nicht zirkular sein!")),
                                Validator.createPredicateValidator(tf -> {
                                    String superName = getKlassifizierer().getSuperklasse() == null ? ""
                                            : getKlassifizierer().getSuperklasse();
                                    if (superName.contains(":")) {
                                        superName = superName.substring(superName.lastIndexOf(":") + 1);
                                    }
                                    var sk = klassenSuchBaum.get(superName);
                                    boolean istInterface = sk == null ? false
                                            : Objects.equals(sk.getTyp(), KlassifiziererTyp.Interface);
                                    return !istInterface;
                                }, sprache.getText("superklasseValidierungInterface",
                                        "Die Superklasse darf kein interface sein!"))));
                setzePlatzhalter(superklasse);
            }
            eingabeValidierung.registerValidator(interfaces.getEditor(),
                    Validator.combine(Validator.createPredicateValidator(tf -> {
                        for (String interfaceName : interfaces.getTags()) {
                            if (interfaceName.contains(":")) {
                                interfaceName = interfaceName.substring(interfaceName.lastIndexOf(":") + 1);
                            }
                            if (Objects.equals(interfaceName, name.getText())) {
                                return false;
                            }
                        }
                        return !istZirkularInterface(getKlassifizierer(), name.getText(), klassenSuchBaum);
                    }, sprache.getText("superklasseValidierungZirkular",
                            "Die Vererbungshierarchie darf nicht zirkular sein!")),
                            Validator.createPredicateValidator(tf -> {
                                for (String interfaceName : interfaces.getTags()) {
                                    if (interfaceName.contains(":")) {
                                        interfaceName = interfaceName.substring(interfaceName.lastIndexOf(":") + 1);
                                    }
                                    var interf = klassenSuchBaum.get(interfaceName);
                                    if (interf != null
                                            && !Objects.equals(interf.getTyp(), KlassifiziererTyp.Interface)) {
                                        return false;
                                    }
                                }
                                return true;
                            }, sprache.getText("interfaceValidierung", "Es dürfen nur Interfaces angegeben werden!"))));
            setzePlatzhalter(interfaces);
        });
    }

    private boolean istZirkular(UMLKlassifizierer startklasse, SortedMap<String, UMLKlassifizierer> klassenSuchBaum) {
        if (startklasse == null || startklasse.getSuperklasse() == null) {
            return false;
        }
        String superName = startklasse.getSuperklasse();
        if (superName.contains(":")) {
            superName = superName.substring(superName.lastIndexOf(":") + 1);
        }
        if (startklasse.getName() != null && !startklasse.getName().isBlank()
                && Objects.equals(startklasse.getName(), superName)) {
            return true;
        }
        var superklasse = klassenSuchBaum.get(superName);
        return pruefeZirkular(superklasse, startklasse.getName(), klassenSuchBaum);
    }

    private boolean pruefeZirkular(UMLKlassifizierer superklasse, String startname,
            SortedMap<String, UMLKlassifizierer> klassenSuchBaum) {
        if (superklasse == null) {
            return false;
        } else if (startname.equals(superklasse.getName())) {
            return !startname.isBlank();
        }

        String naechsteSuperklasse = superklasse.getSuperklasse();
        if (naechsteSuperklasse.contains(":")) {
            naechsteSuperklasse = naechsteSuperklasse.substring(naechsteSuperklasse.lastIndexOf(":") + 1);
        }

        if (naechsteSuperklasse == null || naechsteSuperklasse.isBlank()) {
            return false;
        }
        return pruefeZirkular(klassenSuchBaum.get(naechsteSuperklasse), startname, klassenSuchBaum);
    }

    private boolean istZirkularInterface(UMLKlassifizierer startklasse, String startname,
            SortedMap<String, UMLKlassifizierer> klassenSuchBaum) {
        if (startklasse == null || startklasse.getInterfaces().isEmpty()) {
            return false;
        }
        for (var interfaceName : startklasse.getInterfaces()) {
            if (interfaceName.contains(":")) {
                interfaceName = interfaceName.substring(interfaceName.lastIndexOf(":") + 1);
            }
            var interf = klassenSuchBaum.get(interfaceName);
            boolean istZirkular = pruefeZirkularInterface(interf, startname, klassenSuchBaum);
            if (istZirkular) {
                return true;
            }
        }
        return false;
    }

    private boolean pruefeZirkularInterface(UMLKlassifizierer interf, String startname,
            SortedMap<String, UMLKlassifizierer> klassenSuchBaum) {
        if (interf == null) {
            return false;
        } else if (startname.equals(interf.getName())) {
            return !startname.isBlank();
        }

        for (String interfaceName : interf.getInterfaces()) {
            if (interfaceName.contains(":")) {
                interfaceName = interfaceName.substring(interfaceName.lastIndexOf(":") + 1);
            }
            boolean zirkular = pruefeZirkularInterface(klassenSuchBaum.get(interfaceName), startname, klassenSuchBaum);
            if (zirkular) {
                return true;
            }
        }
        return false;
    }

    private <T> BorderPane erzeugeTabellenAnzeige(ObservableList<T> inhalt, Collection<TableColumn<T, ?>> spalten,
            EventHandler<ActionEvent> neuAktion) {
        TableView<T> tabelle = new TableView<>(inhalt);
        spalten.forEach(spalte -> spalte.setReorderable(false));
        tabelle.getColumns().addAll(spalten);
        tabelle.setEditable(true);

        Button neu = new Button();
        NodeUtil.fuegeIconHinzu(neu, Typicons.PLUS, 20, "neu-button-font-icon");
        neu.setOnAction(neuAktion);

        HBox tabellenButtons = new HBox(neu);
        tabellenButtons.setSpacing(5);
        tabellenButtons.setAlignment(Pos.CENTER_LEFT);
        tabellenButtons.setPadding(new Insets(5, 0, 0, 0));
        tabelle.setPadding(new Insets(0));

        BorderPane ansicht = new BorderPane();
        ansicht.setCenter(tabelle);
        ansicht.setBottom(tabellenButtons);

        return ansicht;
    }

    private Collection<TableColumn<Attribut, ?>> getAttributSpalten() {
        TableColumn<Attribut, Modifizierer> sichtbarkeitSpalte = new TableColumn<>();
        SprachUtil.bindText(sichtbarkeitSpalte.textProperty(), sprache, "sichtbarkeit", "Sichtbarkeit");
        sichtbarkeitSpalte.setCellValueFactory(param -> param.getValue().sichtbarkeitProperty());
        sichtbarkeitSpalte.setEditable(true);
        @SuppressWarnings("unchecked")
        var modifiziererCellBuilder = CustomNodeTableCell
                .builder(Attribut.class, Modifizierer.class, (Class<ComboBox<Modifizierer>>) (Class<?>) ComboBox.class)
                .nodeFactory(() -> {
                    ComboBox<Modifizierer> modifiziererCombo = new ComboBox<>();
                    modifiziererCombo.getItems().setAll(getKlassifizierer().getProgrammiersprache().getEigenschaften()
                            .getAttributModifizierer(getKlassifizierer().getTyp()));
                    updateOnKlassifiziererTyp(neuerTyp -> {
                        List<Modifizierer> neueModifizierer = getKlassifizierer().getProgrammiersprache()
                                .getEigenschaften().getAttributModifizierer(getKlassifizierer().getTyp());
                        modifiziererCombo.getItems().setAll(neueModifizierer);
                        var aktuellerModifizierer = modifiziererCombo.getSelectionModel().getSelectedItem();
                        if (aktuellerModifizierer != null && neueModifizierer.contains(aktuellerModifizierer)) {
                            modifiziererCombo.getSelectionModel().select(aktuellerModifizierer);
                        } else {
                            modifiziererCombo.getSelectionModel().selectFirst();
                        }
                    });
                    return modifiziererCombo;
                })
                .getProperty(combo -> combo.valueProperty())
                .getValue(combo -> combo.getSelectionModel().getSelectedItem())
                .setValue((combo, v) -> combo.getSelectionModel().select(v));
        sichtbarkeitSpalte.setCellFactory(spalte -> modifiziererCellBuilder.build());

        TableColumn<Attribut, String> nameSpalte = new TableColumn<>();
        SprachUtil.bindText(nameSpalte.textProperty(), sprache, "attributname", "Attributname");
        nameSpalte.setCellValueFactory(param -> param.getValue().nameProperty());
        nameSpalte.setEditable(true);
        nameSpalte.setCellFactory(col -> {
            var nameCell = new OnlyTextFieldTableCell<Attribut, String>();
            nameCell.setOnUpdateAction((tf, attribut) -> {
                if (tf.getProperties().containsKey(validierungKey)) {
                    var prop = tf.getProperties().get(validierungKey);
                    if (prop instanceof Subscription sub) {
                        sub.unsubscribe();
                    }
                }
                if (attribut != null) {
                    Subscription nameBeobachter = FXValidierungUtil.setzePlatzhalter(tf, attribut.getNameValidierung());
                    this.loeseBindungen.add(nameBeobachter::unsubscribe);
                    tf.getProperties().put(validierungKey, nameBeobachter);
                }
            });
            return nameCell;
        });

        TableColumn<Attribut, String> datentypSpalte = new TableColumn<>();
        SprachUtil.bindText(datentypSpalte.textProperty(), sprache, "datentyp", "Datentyp");
        datentypSpalte.setCellValueFactory(param -> param.getValue().getDatentyp().typNameProperty());
        datentypSpalte.setEditable(true);
        var datentypBuilder = CustomNodeTableCell.builder(Attribut.class, String.class, DatentypFeld.class)
                .nodeFactory(() -> new DatentypFeld(null, false, umlProjektRef.get().getProgrammiersprache()))
                .getProperty(DatentypFeld::textProperty)
                .getValue(DatentypFeld::getText)
                .setValue(DatentypFeld::select)
                .onUpdateAction((df, attribut) -> {
                    if (df.getProperties().containsKey(validierungKey)) {
                        var prop = df.getProperties().get(validierungKey);
                        if (prop instanceof Subscription sub) {
                            sub.unsubscribe();
                        }
                    }
                    if (attribut != null) {
                        Subscription nameBeobachter = FXValidierungUtil.setzePlatzhalter(df,
                                attribut.getDatentyp().getTypValidierung());
                        this.loeseBindungen.add(nameBeobachter::unsubscribe);
                        df.getProperties().put(validierungKey, nameBeobachter);
                    }
                });
        datentypSpalte.setCellFactory(col -> datentypBuilder.build());

        TableColumn<Attribut, String> initialwertSpalte = new TableColumn<>();
        SprachUtil.bindText(initialwertSpalte.textProperty(), sprache, "initialwert", "Initialwert");
        initialwertSpalte.setCellValueFactory(param -> param.getValue().initialwertProperty());
        initialwertSpalte.setEditable(true);
        initialwertSpalte.setCellFactory(col -> new OnlyTextFieldTableCell<>());

        var checkBoxCellBuilder = CustomNodeTableCell.builder(Attribut.class, Boolean.class, CheckBox.class)
                .nodeFactory(CheckBox::new)
                .getProperty(CheckBox::selectedProperty)
                .getValue(CheckBox::isSelected)
                .setValue(CheckBox::setSelected);

        TableColumn<Attribut, Boolean> getterSpalte = new TableColumn<>();
        SprachUtil.bindText(getterSpalte.textProperty(), sprache, "getter", "Getter");
        getterSpalte.setCellValueFactory(param -> param.getValue().hatGetterProperty());
        getterSpalte.setEditable(true);
        getterSpalte.setCellFactory(col -> checkBoxCellBuilder.build());

        TableColumn<Attribut, Boolean> setterSpalte = new TableColumn<>();
        SprachUtil.bindText(setterSpalte.textProperty(), sprache, "setter", "Setter");
        setterSpalte.setCellValueFactory(param -> param.getValue().hatSetterProperty());
        setterSpalte.setEditable(true);
        setterSpalte.setCellFactory(col -> checkBoxCellBuilder.build());

        TableColumn<Attribut, Boolean> staticSpalte = new TableColumn<>();
        SprachUtil.bindText(staticSpalte.textProperty(), sprache, "static", "static");
        staticSpalte.setCellValueFactory(param -> {
            return param.getValue().istStatischProperty();
        });
        staticSpalte.setEditable(true);
        var staticBuilder = CustomNodeTableCell.builder(Attribut.class, Boolean.class, CheckBox.class)
                .nodeFactory(CheckBox::new)
                .getProperty(CheckBox::selectedProperty)
                .getValue(CheckBox::isSelected)
                .setValue(CheckBox::setSelected)
                .onUpdateAction((cb, attribut) -> {
                    cb.setDisable(false);
                    if (attribut != null) {
                        boolean instanzAttributeErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                                .erlaubtInstanzAttribute(getKlassifizierer().getTyp());
                        cb.setDisable(!instanzAttributeErlaubt);
                    }
                });
        staticSpalte.setCellFactory(col -> {
            var staticCell = staticBuilder.build();
            updateOnKlassifiziererTyp(neuerTyp -> {
                var cb = staticCell.getNode();
                cb.setDisable(false);
                if (staticCell.getIndex() < 0
                        || staticCell.getIndex() >= col.getTableView().getItems().size()) {
                    return;
                }
                boolean instanzAttributeErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                        .erlaubtInstanzAttribute(neuerTyp);
                cb.setDisable(!instanzAttributeErlaubt);
            });
            return staticCell;
        });

        TableColumn<Attribut, Boolean> finalSpalte = new TableColumn<>();
        SprachUtil.bindText(finalSpalte.textProperty(), sprache, "final", "final");
        finalSpalte.setCellValueFactory(param -> param.getValue().istFinalProperty());
        finalSpalte.setEditable(true);
        finalSpalte.setCellFactory(col -> checkBoxCellBuilder.build());

        TableColumn<Attribut, Attribut> kontrollSpalte = new TableColumn<>();
        kontrollSpalte.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
        kontrollSpalte.setCellFactory(col -> new ListControlsTableCell<>());
        kontrollSpalte.setResizable(false);

        return List.of(sichtbarkeitSpalte, nameSpalte, datentypSpalte, initialwertSpalte, getterSpalte, setterSpalte,
                staticSpalte, finalSpalte, kontrollSpalte);
    }

    private HBox erzeugeSichtbarkeit() {
        HBox sichtbarkeit = new HBox();
        ToggleGroup sichtbarkeitGruppe = new ToggleGroup();
        Modifizierer[] modifizierer = { Modifizierer.PUBLIC, Modifizierer.PACKAGE, Modifizierer.PRIVATE,
                Modifizierer.PROTECTED };
        for (Modifizierer m : modifizierer) {
            RadioButton rb = new RadioButton(m.toString());
            rb.setUserData(m);
            rb.setDisable(!getKlassifizierer().getProgrammiersprache().getEigenschaften()
                    .istTypModifiziererErlaubt(getKlassifizierer().getTyp(), m));
            sichtbarkeitGruppe.getToggles().add(rb);
            sichtbarkeit.getChildren().add(rb);
            if (m.equals(getKlassifizierer().getSichtbarkeit())) {
                sichtbarkeitGruppe.selectToggle(rb);
            }
        }
        sichtbarkeit.setSpacing(10);

        ChangeListener<Toggle> beobachter = (property, alteWahl, neueWahl) -> {
            if (neueWahl.getUserData() instanceof Modifizierer m) {
                getKlassifizierer().setSichtbarkeit(m);
            }
        };
        sichtbarkeit.getProperties().put(this, beobachter);
        sichtbarkeitGruppe.selectedToggleProperty().addListener(new WeakChangeListener<>(beobachter));

        return sichtbarkeit;
    }

    private Collection<TableColumn<Methode, ?>> getMethodeSpalten() {
        TableColumn<Methode, Modifizierer> sichtbarkeitSpalte = new TableColumn<>();
        SprachUtil.bindText(sichtbarkeitSpalte.textProperty(), sprache, "sichtbarkeit", "Sichtbarkeit");
        sichtbarkeitSpalte.setCellValueFactory(param -> param.getValue().sichtbarkeitProperty());
        @SuppressWarnings("unchecked")
        var modifiziererCellBuilder = CustomNodeTableCell
                .builder(Methode.class, Modifizierer.class, (Class<ComboBox<Modifizierer>>) (Class<?>) ComboBox.class)
                .nodeFactory(() -> {
                    ComboBox<Modifizierer> modifiziererCombo = new ComboBox<>();
                    modifiziererCombo.getItems().setAll(getKlassifizierer().getProgrammiersprache().getEigenschaften()
                            .getMethodenModifizierer(getKlassifizierer().getTyp(), false, false));
                    return modifiziererCombo;
                })
                .getProperty(combo -> combo.valueProperty())
                .getValue(combo -> combo.getSelectionModel().getSelectedItem())
                .setValue((combo, v) -> combo.getSelectionModel().select(v))
                .onUpdateAction((combo, methode) -> {
                    final String subKey = "abstraktStatischSubscription";
                    var subscriptionProp = combo.getProperties().get(subKey);
                    if (subscriptionProp instanceof Subscription subscription) {
                        subscription.unsubscribe();
                    }
                    if (methode != null) {
                        var sub = EasyBind.subscribe(methode.istAbstraktProperty(), istAbstrakt -> {
                            updateMethodeSichtbarkeit(combo, methode.istStatisch(), istAbstrakt,
                                    methode.getSichtbarkeit(), getKlassifizierer().getTyp());
                        }).and(EasyBind.subscribe(methode.istStatischProperty(), istStatisch -> {
                            updateMethodeSichtbarkeit(combo, istStatisch, methode.istAbstrakt(),
                                    methode.getSichtbarkeit(), getKlassifizierer().getTyp());
                        }));
                        combo.getProperties().put(subKey, sub);
                    }
                });
        sichtbarkeitSpalte.setEditable(true);
        sichtbarkeitSpalte.setCellFactory(spalte -> {
            var modifiziererCell = modifiziererCellBuilder.build();
            updateOnKlassifiziererTyp(neuerTyp -> {
                if (modifiziererCell.getIndex() < 0
                        || modifiziererCell.getIndex() >= spalte.getTableView().getItems().size()) {
                    return;
                }
                Methode methode = spalte.getTableView().getItems().get(modifiziererCell.getIndex());
                var combo = modifiziererCell.getNode();
                updateMethodeSichtbarkeit(combo, methode.istStatisch(), methode.istAbstrakt(),
                        methode.getSichtbarkeit(), neuerTyp);
            });
            return modifiziererCell;
        });

        TableColumn<Methode, String> nameSpalte = new TableColumn<>();
        SprachUtil.bindText(nameSpalte.textProperty(), sprache, "methodenname", "Methodenname");
        nameSpalte.setCellValueFactory(param -> param.getValue().nameProperty());
        nameSpalte.setEditable(true);
        nameSpalte.setCellFactory(col -> {
            var nameCell = new OnlyTextFieldTableCell<Methode, String>();
            nameCell.setOnUpdateAction((tf, methode) -> {
                addValidierung(tf, methode, () -> methode.getNameValidierung());
                addValidierung(tf, methode, () -> methode.getSignaturValidierung(), "signaturValidierung");
            });
            return nameCell;
        });

        record MethodeParameter(Methode methode, StringProperty parameter) {
        }

        TableColumn<Methode, MethodeParameter> parameterlisteSpalte = new TableColumn<>();
        SprachUtil.bindText(parameterlisteSpalte.textProperty(), sprache, "parameterliste", "Parameterliste");
        parameterlisteSpalte.setCellValueFactory(param -> {
            StringProperty parameter = new SimpleStringProperty();
            parameter.bind(Bindings.concat("(").concat(new StringBinding() {
                {
                    super.bind(param.getValue().parameterListeProperty());
                }
                StringExpression stringExpr = null;

                @Override
                protected String computeValue() {
                    if (stringExpr != null) {
                        super.unbind(stringExpr);
                    }
                    var params = param.getValue().parameterListeProperty().stream()
                            .map(param -> Bindings.concat(
                                    new When(Einstellungen.getBenutzerdefiniert().zeigeParameterNamenProperty())
                                            .then(param.nameProperty().concat(": ")).otherwise(""),
                                    param.getDatentyp().typNameProperty()))
                            .toArray();

                    if (params.length < 1) {
                        return "";
                    }

                    stringExpr = Bindings.concat(params[0]);
                    for (int i = 1; i < params.length; i++) {
                        stringExpr = stringExpr.concat(", ");
                        stringExpr = stringExpr.concat(params[i]);
                    }

                    super.bind(stringExpr);
                    return stringExpr.get();
                }
            }).concat(")"));
            loeseBindungen.add(parameter::unbind);
            return new SimpleObjectProperty<>(new MethodeParameter(param.getValue(), parameter));
        });
        var parameterCellBuilder = CustomNodeTableCell.builder(Methode.class, MethodeParameter.class, TextField.class)
                .nodeFactory(() -> {
                    TextField tf = new TextField();
                    tf.setEditable(false);
                    tf.prefColumnCountProperty().bind(tf.textProperty().length());
                    return tf;
                })
                .updateCallback(new UpdateCallback<MethodeParameter, TextField>() {
                    @Override
                    public void onUpdate(TextField tf, MethodeParameter item) {
                        tf.textProperty().bind(item.parameter());
                        tf.setOnAction(e -> {
                            bearbeiteParameter(tf, item.methode);
                        });
                        tf.setOnMousePressed(e -> bearbeiteParameter(tf, item.methode));
                    }

                    @Override
                    public void onEmpty(TextField tf, MethodeParameter item) {
                        tf.textProperty().unbind();
                        tf.clear();
                        tf.setOnAction(null);
                        tf.setOnMousePressed(null);
                    }
                })
                .onUpdateAction((tf, methode) -> {
                    addValidierung(tf, methode, () -> methode.getParameterValidierung(), "parameterValidierung");
                    addValidierung(tf, methode, () -> methode.getSignaturValidierung(), "signaturValidierung");
                    addValidierungCollection(tf, methode, () -> methode.getParameterValid(),
                            "parameterNamenValidierung");
                    if (methode != null && (methode.istGetter() || methode.istSetter())) {
                        tf.setDisable(true);
                    } else {
                        tf.setDisable(false);
                    }
                });
        parameterlisteSpalte.setCellFactory(spalte -> parameterCellBuilder.build());

        TableColumn<Methode, String> rueckgabeSpalte = new TableColumn<>();
        SprachUtil.bindText(rueckgabeSpalte.textProperty(), sprache, "Rueckgabetyp",
                "R%cckgabetyp".formatted(Umlaute.ue));
        var datentypBuilder = CustomNodeTableCell.builder(Methode.class, String.class, DatentypFeld.class)
                .nodeFactory(() -> new DatentypFeld(null, false, umlProjektRef.get().getProgrammiersprache()))
                .getProperty(DatentypFeld::textProperty)
                .getValue(DatentypFeld::getText)
                .setValue(DatentypFeld::setText)
                .onUpdateAction((tf, methode) -> {
                    addValidierung(tf, methode, () -> methode.getRueckgabeTyp().getTypValidierung());
                    tf.setDisable(false);
                    if (methode != null && (methode.istGetter() || methode.istSetter())) {
                        tf.setDisable(true);
                    }
                });
        rueckgabeSpalte.setCellFactory(col -> datentypBuilder.build());

        TableColumn<Methode, Boolean> abstraktSpalte = new TableColumn<>();
        SprachUtil.bindText(abstraktSpalte.textProperty(), sprache, "abstrakt", "abstrakt");
        abstraktSpalte.setCellValueFactory(param -> param.getValue().istAbstraktProperty());
        abstraktSpalte.setEditable(true);
        var abstraktBuilder = CustomNodeTableCell.builder(Methode.class, Boolean.class, CheckBox.class)
                .nodeFactory(CheckBox::new)
                .getProperty(CheckBox::selectedProperty)
                .getValue(CheckBox::isSelected)
                .setValue(CheckBox::setSelected)
                .onUpdateAction((cb, methode) -> {
                    addValidierung(cb, methode, () -> methode.getAbstraktFinalValidierung());
                    updateMethodeAbstrakt(cb, getKlassifizierer().getTyp());
                    if (methode != null && (methode.istGetter() || methode.istSetter())) {
                        cb.setDisable(true);
                    }
                    var prop = cb.getProperties().get("typSubscriptionAbstrakt");
                    if (prop instanceof Subscription sub) {
                        sub.unsubscribe();
                    }
                    Subscription typSubscription = EasyBind.subscribe(klassifizierer.typProperty(), neuerTyp -> {
                        updateMethodeAbstrakt(cb, getKlassifizierer().getTyp());
                        if (methode != null && (methode.istGetter() || methode.istSetter())) {
                            cb.setDisable(true);
                        }
                    });
                    cb.getProperties().put("typSubscriptionAbstrakt", typSubscription);
                });
        abstraktSpalte.setCellFactory(col -> {
            var abstraktCell = abstraktBuilder.build();
            updateMethodeAbstrakt(abstraktCell.getNode(), getKlassifizierer().getTyp());
            return abstraktCell;
        });

        TableColumn<Methode, Boolean> staticSpalte = new TableColumn<>();
        SprachUtil.bindText(staticSpalte.textProperty(), sprache, "static", "static");
        staticSpalte.setCellValueFactory(param -> {
            return param.getValue().istStatischProperty();
        });
        staticSpalte.setEditable(true);
        var staticBuilder = CustomNodeTableCell.builder(Methode.class, Boolean.class, CheckBox.class)
                .nodeFactory(() -> new CheckBox())
                .getProperty(CheckBox::selectedProperty)
                .getValue(CheckBox::isSelected)
                .setValue(CheckBox::setSelected)
                .onUpdateAction((cb, methode) -> {
                    cb.setDisable(false);
                    if (methode != null) {
                        boolean instanzAttributeErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                                .erlaubtInstanzAttribute(getKlassifizierer().getTyp());
                        if (!instanzAttributeErlaubt && (methode.istGetter() || methode.istSetter())) {
                            // Getter und Setter werden über Attribut gesteuert
                            cb.setDisable(true);
                        }
                    }
                });
        staticSpalte.setCellFactory(col -> {
            var staticCell = staticBuilder.build();
            updateOnKlassifiziererTyp(neuerTyp -> {
                var cb = staticCell.getNode();
                cb.setDisable(false);
                if (staticCell.getIndex() < 0
                        || staticCell.getIndex() >= col.getTableView().getItems().size()) {
                    return;
                }
                Methode methode = col.getTableView().getItems().get(staticCell.getIndex());
                boolean instanzAttributeErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                        .erlaubtInstanzAttribute(getKlassifizierer().getTyp());
                if (!instanzAttributeErlaubt && (methode.istGetter() || methode.istSetter())) {
                    // Getter und Setter werden über Attribut gesteuert
                    cb.setDisable(true);
                }
            });
            return staticCell;
        });

        TableColumn<Methode, Boolean> finalSpalte = new TableColumn<>();
        SprachUtil.bindText(finalSpalte.textProperty(), sprache, "final", "final");
        finalSpalte.setCellValueFactory(param -> param.getValue().istFinalProperty());
        finalSpalte.setEditable(true);
        var finalBuilder = CustomNodeTableCell.builder(Methode.class, Boolean.class, CheckBox.class)
                .nodeFactory(() -> new CheckBox())
                .getProperty(CheckBox::selectedProperty)
                .getValue(CheckBox::isSelected)
                .setValue(CheckBox::setSelected)
                .onUpdateAction((cb, methode) -> {
                    addValidierung(cb, methode, () -> methode.getAbstraktFinalValidierung());
                });
        finalSpalte.setCellFactory(col -> finalBuilder.build());

        TableColumn<Methode, Methode> kontrollSpalte = new TableColumn<>();
        kontrollSpalte.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
        kontrollSpalte.setCellFactory(col -> {
            var controllCell = new ListControlsTableCell<Methode>();
            controllCell.setOnUpdateAction(params -> {
                params.loeschen().setDisable(false);
                Methode methode = params.rowItem();
                if (methode != null && (methode.istGetter() || methode.istSetter())) {
                    params.loeschen().setDisable(true);
                }
            });
            return controllCell;
        });
        kontrollSpalte.setResizable(false);

        return List.of(sichtbarkeitSpalte, nameSpalte, parameterlisteSpalte, rueckgabeSpalte, abstraktSpalte,
                staticSpalte, finalSpalte, kontrollSpalte);
    }

    private void updateMethodeSichtbarkeit(ComboBox<Modifizierer> combo, boolean istStatisch, boolean istAbstrakt,
            Modifizierer aktuellerModifizierer, KlassifiziererTyp typ) {
        List<Modifizierer> erlaubteModifizierer = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                .getMethodenModifizierer(getKlassifizierer().getTyp(), istStatisch, istAbstrakt);
        combo.getItems().setAll(erlaubteModifizierer);
        if (aktuellerModifizierer != null && erlaubteModifizierer.contains(aktuellerModifizierer)) {
            combo.getSelectionModel().select(aktuellerModifizierer);
        } else {
            combo.getSelectionModel().selectFirst();
        }
    }

    private void updateMethodeAbstrakt(Node n, KlassifiziererTyp typ) {
        boolean abstraktErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                .erlaubtAbstrakteMethode(typ);
        boolean abstraktErzwungen = !getKlassifizierer().getProgrammiersprache().getEigenschaften()
                .erlaubtNichtAbstrakteMethode(typ);

        n.setDisable(!abstraktErlaubt);

        if (abstraktErzwungen) {
            n.setDisable(true);
        }
    }

    private void bearbeiteParameter(TextField parameter, HatParameterListe typMitParameterliste) {
        var parameterListe = erzeugeTabellenAnzeige(typMitParameterliste.parameterListeProperty(),
                getParameterSpalten(), event -> {
                    if (!(typMitParameterliste instanceof Methode m) || (!m.istGetter() && !m.istSetter())) {
                        var programmierEigenschaften = getKlassifizierer().getProgrammiersprache().getEigenschaften();
                        typMitParameterliste.parameterListeProperty()
                                .add(new Parameter(programmierEigenschaften.getLetzerDatentyp()));
                    }
                });

        parameterListe.setPadding(new Insets(15));
        TableView<?> tabelle = (TableView<?>) parameterListe.getCenter();
        tabelle.setMinSize(450, 200);
        tabelle.setPrefHeight(200);

        PopOver parameterDialog = new PopOver(parameterListe);
        parameterDialog.setArrowLocation(ArrowLocation.TOP_CENTER);
        parameterDialog.getRoot().getStylesheets().addAll(parameter.getScene().getStylesheets());
        parameterDialog.show(parameter);
    }

    private Collection<TableColumn<Parameter, ?>> getParameterSpalten() {
        TableColumn<Parameter, String> nameSpalte = new TableColumn<>();
        SprachUtil.bindText(nameSpalte.textProperty(), sprache, "parametername", "Parametername");
        nameSpalte.setCellValueFactory(param -> param.getValue().nameProperty());
        nameSpalte.setCellFactory(param -> {
            var nameCell = new OnlyTextFieldTableCell<Parameter, String>();
            nameCell.setOnUpdateAction((tf, parameter) -> {
                addValidierung(tf, parameter, () -> parameter.getNameValidierung());
            });
            return nameCell;
        });

        TableColumn<Parameter, String> datentypSpalte = new TableColumn<>();
        SprachUtil.bindText(datentypSpalte.textProperty(), sprache, "datentyp", "Datentyp");
        datentypSpalte.setCellValueFactory(param -> param.getValue().getDatentyp().typNameProperty());
        var datentypBuilder = CustomNodeTableCell.builder(Parameter.class, String.class, DatentypFeld.class)
                .nodeFactory(() -> {
                    var dtf = new DatentypFeld(null, false, umlProjektRef.get().getProgrammiersprache());
                    Platform.runLater(dtf::cancel);
                    return dtf;
                })
                .getProperty(DatentypFeld::textProperty)
                .getValue(DatentypFeld::getText)
                .setValue(DatentypFeld::setText)
                .onUpdateAction((df, parameter) -> {
                    addValidierung(df, parameter, () -> parameter.getDatentyp().getTypValidierung());
                });
        datentypSpalte.setCellFactory(col -> datentypBuilder.build());

        TableColumn<Parameter, Parameter> kontrollSpalte = new TableColumn<>();
        kontrollSpalte.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
        kontrollSpalte.setCellFactory(col -> new ListControlsTableCell<>());
        kontrollSpalte.setResizable(false);

        return List.of(nameSpalte, datentypSpalte, kontrollSpalte);
    }

    private Collection<TableColumn<Konstruktor, ?>> getKonstruktorSpalten() {
        TableColumn<Konstruktor, Modifizierer> sichtbarkeitSpalte = new TableColumn<>();
        SprachUtil.bindText(sichtbarkeitSpalte.textProperty(), sprache, "sichtbarkeit", "Sichtbarkeit");
        sichtbarkeitSpalte.setCellValueFactory(param -> param.getValue().sichtbarkeitProperty());
        @SuppressWarnings("unchecked")
        var modifiziererCellBuilder = CustomNodeTableCell
                .builder(Konstruktor.class, Modifizierer.class,
                        (Class<ComboBox<Modifizierer>>) (Class<?>) ComboBox.class)
                .nodeFactory(() -> {
                    ComboBox<Modifizierer> combo = new ComboBox<>();
                    List<Modifizierer> modifizierer = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                            .getKonstruktorModifizierer(getKlassifizierer().getTyp());
                    combo.getItems().setAll(modifizierer);
                    updateOnKlassifiziererTyp(neuerTyp -> {
                        List<Modifizierer> neueModifizierer = getKlassifizierer().getProgrammiersprache()
                                .getEigenschaften()
                                .getKonstruktorModifizierer(getKlassifizierer().getTyp());
                        combo.getItems().setAll(neueModifizierer);
                        var aktuellerModifizierer = combo.getSelectionModel().getSelectedItem();
                        if (aktuellerModifizierer != null
                                && getKlassifizierer().getProgrammiersprache().getEigenschaften()
                                        .getKonstruktorModifizierer(getKlassifizierer().getTyp())
                                        .contains(aktuellerModifizierer)) {
                            combo.getSelectionModel().select(aktuellerModifizierer);
                        } else {
                            combo.getSelectionModel().selectFirst();
                        }
                    });
                    return combo;
                })
                .getProperty(combo -> combo.valueProperty())
                .getValue(combo -> combo.getSelectionModel().getSelectedItem())
                .setValue((combo, v) -> combo.getSelectionModel().select(v));
        sichtbarkeitSpalte.setCellFactory(spalte -> modifiziererCellBuilder.build());

        record KonstruktorParameter(Konstruktor konstruktor, StringProperty parameter) {
        }

        TableColumn<Konstruktor, KonstruktorParameter> parameterlisteSpalte = new TableColumn<>();
        SprachUtil.bindText(parameterlisteSpalte.textProperty(), sprache, "parameterliste", "Parameterliste");
        parameterlisteSpalte.setCellValueFactory(param -> {
            StringProperty parameter = new SimpleStringProperty();
            parameter.bind(Bindings.concat("(").concat(new StringBinding() {
                {
                    super.bind(param.getValue().parameterListeProperty());
                }
                StringExpression stringExpr = null;

                @Override
                protected String computeValue() {
                    if (stringExpr != null) {
                        super.unbind(stringExpr);
                    }
                    var params = param.getValue().parameterListeProperty().stream()
                            .map(param -> Bindings.concat(
                                    new When(Einstellungen.getBenutzerdefiniert().zeigeParameterNamenProperty())
                                            .then(param.nameProperty().concat(": ")).otherwise(""),
                                    param.getDatentyp().typNameProperty()))
                            .toArray();

                    if (params.length < 1) {
                        return "";
                    }

                    stringExpr = Bindings.concat(params[0]);
                    for (int i = 1; i < params.length; i++) {
                        stringExpr = stringExpr.concat(", ");
                        stringExpr = stringExpr.concat(params[i]);
                    }

                    super.bind(stringExpr);
                    return stringExpr.get();
                }
            }).concat(")"));
            loeseBindungen.add(parameter::unbind);
            return new SimpleObjectProperty<>(new KonstruktorParameter(param.getValue(), parameter));
        });
        var parameterCellBuilder = CustomNodeTableCell
                .builder(Konstruktor.class, KonstruktorParameter.class, TextField.class)
                .nodeFactory(() -> {
                    TextField tf = new TextField();
                    tf.setEditable(false);
                    tf.prefColumnCountProperty().bind(tf.textProperty().length());
                    return tf;
                })
                .updateCallback(new UpdateCallback<KonstruktorParameter, TextField>() {
                    @Override
                    public void onUpdate(TextField tf, KonstruktorParameter item) {
                        tf.textProperty().bind(item.parameter());
                        tf.setOnAction(e -> {
                            bearbeiteParameter(tf, item.konstruktor);
                        });
                        tf.setOnMousePressed(e -> bearbeiteParameter(tf, item.konstruktor));
                    }

                    @Override
                    public void onEmpty(TextField tf, KonstruktorParameter item) {
                        tf.textProperty().unbind();
                        tf.clear();
                        tf.setOnAction(null);
                        tf.setOnMousePressed(null);
                    }
                })
                .onUpdateAction((tf, konstruktor) -> {
                    addValidierung(tf, konstruktor, () -> konstruktor.getParameterValidierung(),
                            "parameterValidierung");
                    addValidierung(tf, konstruktor, () -> konstruktor.getSignaturValidierung(), "signaturValidierung");
                    addValidierungCollection(tf, konstruktor, () -> konstruktor.getParameterValid(),
                            "parameterNamenValidierung");
                });
        parameterlisteSpalte.setCellFactory(spalte -> parameterCellBuilder.build());

        TableColumn<Konstruktor, Konstruktor> kontrollSpalte = new TableColumn<>();
        kontrollSpalte.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue()));
        kontrollSpalte.setCellFactory(col -> new ListControlsTableCell<>());
        kontrollSpalte.setResizable(false);

        return List.of(sichtbarkeitSpalte, parameterlisteSpalte, kontrollSpalte);
    }

    private void initialisiereButtons() {
        ButtonType[] buttons = { new ButtonType(sprache.getText("APPLY", "Anwenden"), ButtonData.FINISH),
                new ButtonType(sprache.getText("CANCEL_CLOSE", "Abbrechen"), ButtonData.BACK_PREVIOUS) };
        this.getDialogPane().getButtonTypes().addAll(buttons);
        this.getDialogPane().lookupButton(buttons[0]).disableProperty().bind(eingabeValidierung.invalidProperty()
                .or(getKlassifizierer().getKlassifiziererValid().isValidProperty().not()));
        loeseBindungen.add(this.getDialogPane().lookupButton(buttons[0]).disableProperty()::unbind);
    }

    private void setzePlatzhalter(SearchField<?> eingabefeld) {
        ChangeListener<ValidationResult> beobachter = (p, alt, neu) -> {
            var fehler = eingabeValidierung.getHighestMessage(eingabefeld.getEditor());
            if (fehler.isPresent()) {
                eingabefeld.getEditor().setPromptText(fehler.get().getText());
                if (!eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().add(CSS_EINGABE_FEHLER);
                }
                eingabefeld.setTooltip(new Tooltip(fehler.get().getText()));
            } else {
                eingabefeld.getEditor().setPromptText(null);
                if (eingabefeld.getStyleClass().contains(CSS_EINGABE_FEHLER)) {
                    eingabefeld.getStyleClass().remove(CSS_EINGABE_FEHLER);
                }
                eingabefeld.setTooltip(null);
            }
        };
        eingabeValidierung.validationResultProperty().addListener(beobachter);
        validierungsBeobachter.add(beobachter);
    }

    private void updateSuperklasse(SearchField<?> superklasse, KlassifiziererTyp typ) {
        boolean superklasseErlaubt = getKlassifizierer().getProgrammiersprache().getEigenschaften()
                .erlaubtSuperklasse(typ);
        if (!superklasseErlaubt) {
            superklasse.clear();
        }
        superklasse.setDisable(!superklasseErlaubt);
    }

    private void updateOnKlassifiziererTyp(Consumer<KlassifiziererTyp> updateAction) {
        Subscription typSubscription = EasyBind.subscribe(klassifizierer.typProperty(), updateAction);
        this.loeseBindungen.add(typSubscription::unsubscribe);
    }

    private <T> void bindeBidirektional(Property<T> p1, Property<T> p2) {
        p1.bindBidirectional(p2);
        loeseBindungen.add(() -> p1.unbindBidirectional(p2));
    }

    private void addValidierung(TextInputControl node, Object sourceObj,
            Supplier<SimpleValidierung> validierungSupplier) {
        addValidierung(node, sourceObj, validierungSupplier, validierungKey);
    }

    private void addValidierung(SearchField<?> node, Object sourceObj,
            Supplier<SimpleValidierung> validierungSupplier) {
        addValidierung(node, sourceObj, validierungSupplier, validierungKey);
    }

    private void addValidierung(CheckBox node, Object sourceObj,
            Supplier<SimpleValidierung> validierungSupplier) {
        addValidierung(node, sourceObj, validierungSupplier, validierungKey);
    }

    private void addValidierung(TextInputControl node, Object sourceObj,
            Supplier<SimpleValidierung> validierungSupplier, String key) {
        if (node.getProperties().containsKey(key)) {
            var prop = node.getProperties().get(key);
            if (prop instanceof Subscription sub) {
                sub.unsubscribe();
            }
        }
        if (sourceObj != null) {
            Subscription nameBeobachter = FXValidierungUtil.setzePlatzhalter(node, validierungSupplier.get());
            this.loeseBindungen.add(nameBeobachter::unsubscribe);
            node.getProperties().put(key, nameBeobachter);
        }
    }

    private void addValidierung(SearchField<?> node, Object sourceObj,
            Supplier<SimpleValidierung> validierungSupplier, String key) {
        addValidierung(node.getEditor(), sourceObj, validierungSupplier, key);
    }

    private void addValidierung(CheckBox node, Object sourceObj,
            Supplier<SimpleValidierung> validierungSupplier, String key) {
        if (node.getProperties().containsKey(key)) {
            var prop = node.getProperties().get(key);
            if (prop instanceof Subscription sub) {
                sub.unsubscribe();
            }
        }
        if (sourceObj != null) {
            Subscription nameBeobachter = FXValidierungUtil.setzePlatzhalter(node, validierungSupplier.get());
            this.loeseBindungen.add(nameBeobachter::unsubscribe);
            node.getProperties().put(key, nameBeobachter);
        }
    }

    private void addValidierungCollection(Node node, Object sourceObj,
            Supplier<ValidierungCollection> validierungSupplier) {
        addValidierungCollection(node, sourceObj, validierungSupplier, validierungKey);
    }

    private void addValidierungCollection(Node node, Object sourceObj,
            Supplier<ValidierungCollection> validierungSupplier, String key) {
        if (node.getProperties().containsKey(key)) {
            var prop = node.getProperties().get(key);
            if (prop instanceof Subscription sub) {
                sub.unsubscribe();
            }
        }
        if (sourceObj != null) {
            Subscription nameBeobachter = FXValidierungUtil.setzeValidierungStyle(node, validierungSupplier.get());
            this.loeseBindungen.add(nameBeobachter::unsubscribe);
            node.getProperties().put(key, nameBeobachter);
        }
    }
}