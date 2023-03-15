/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.validierung;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public interface Validierung {
    public ReadOnlyBooleanProperty isValidProperty();
    public ObservableList<ObservableValue<String>> getErrorMessages();
}