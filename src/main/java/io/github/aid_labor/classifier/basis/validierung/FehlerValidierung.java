/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.validierung;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public interface FehlerValidierung extends Validierung {
    
    public ObservableList<ObservableValue<String>> getErrorMessages();
    
}