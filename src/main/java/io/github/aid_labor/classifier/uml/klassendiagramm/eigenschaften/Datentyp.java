/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.uml.klassendiagramm.eigenschaften;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.aid_labor.classifier.basis.ClassifierUtil;
import io.github.aid_labor.classifier.basis.Einstellungen;
import io.github.aid_labor.classifier.basis.io.Ressourcen;
import io.github.aid_labor.classifier.basis.json.JsonStringProperty;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarBasis;
import io.github.aid_labor.classifier.basis.projekt.editierung.EditierbarerBeobachter;
import io.github.aid_labor.classifier.basis.sprachverwaltung.SprachUtil;
import io.github.aid_labor.classifier.basis.sprachverwaltung.Sprache;
import io.github.aid_labor.classifier.basis.validierung.Validierung;
import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.StringProperty;

//@formatter:off
@JsonAutoDetect(
		getterVisibility = Visibility.NONE,
		isGetterVisibility = Visibility.NONE,
		setterVisibility = Visibility.NONE,
		fieldVisibility = Visibility.ANY
)
//@formatter:on
public class Datentyp extends EditierbarBasis implements EditierbarerBeobachter {

    private static long naechsteId = 0;

    private final JsonStringProperty typName;
    @JsonIgnore
    private final List<Object> beobachterListe;
    @JsonIgnore
    private final long id;
    @JsonIgnore
    private final Sprache sprache;
    @JsonIgnore
    private Validierung typValidierung;

    @JsonCreator
    public Datentyp(@JsonProperty("typName") String typName) {
        this.typName = new JsonStringProperty(this, "typName", typName);
        this.beobachterListe = new LinkedList<>();
        this.id = naechsteId++;
        this.sprache = new Sprache();
        boolean spracheGesetzt = SprachUtil.setUpSprache(sprache, Ressourcen.get().SPRACHDATEIEN_ORDNER.alsPath(),
                "UMLKlassifiziererBearbeitenDialog");
        if (!spracheGesetzt) {
            sprache.ignoriereSprachen();
        }

        this.ueberwachePropertyAenderung(this.typName, id + "_datentyp_name");
    }

    private void initialisiereNameValidierung(UMLKlassifizierer klassifizierer, boolean erlaubtVoid) {
        BooleanBinding isVoid = Bindings.createBooleanBinding(
                () -> klassifizierer.getProgrammiersprache().getEigenschaften().istVoid(this), typName);
        if (erlaubtVoid) {
            this.typValidierung = Validierung.of(typName.isNotEmpty(),
                    sprache.getTextProperty("datentypValidierung", "Datentyp angeben"))
                    .build();
        } else {
            this.typValidierung = Validierung.of(typName.isNotEmpty(),
                    sprache.getTextProperty("datentypValidierung", "Datentyp angeben"))
                    .and(isVoid.not(),
                            sprache.getTextProperty("attributValidierungVoid", "Der Typ void ist hier nicht erlaubt"))
                    .build();
        }
    }
    
    public void setValidierung(UMLKlassifizierer klassifizierer, boolean erlaubtVoid) {
        this.initialisiereNameValidierung(klassifizierer, erlaubtVoid);
    }
    
    public Validierung getTypValidierung() {
        return typValidierung;
    }

    @Override
    public List<Object> getBeobachterListe() {
        return beobachterListe;
    }

    public void set(Datentyp datentyp) {
        this.setTypName(datentyp.getTypName());
    }

    public String getTypName() {
        return typName.get();
    }

    public void setTypName(String typName) {
        this.typName.set(typName);
    }

    public StringProperty typNameProperty() {
        return typName;
    }

    public Datentyp erzeugeTiefeKopie() {
        return new Datentyp(typName.get());
    }

    @Override
    public String toString() {
        return this.getTypName();
    }

    @Override
    public int hashCode() {
        return ClassifierUtil.hashAlle(typName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Datentyp other = (Datentyp) obj;
        return Objects.equals(typName, other.typName);
    }

    @Override
    public void close() throws Exception {
        beobachterListe.clear();
    }

}
