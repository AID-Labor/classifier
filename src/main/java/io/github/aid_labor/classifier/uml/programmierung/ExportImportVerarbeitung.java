/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.programmierung;

import java.io.File;
import java.io.OutputStream;

import io.github.aid_labor.classifier.uml.klassendiagramm.UMLKlassifizierer;

public interface ExportImportVerarbeitung {
	
	public void exportiere(UMLKlassifizierer klassifizierer, OutputStream ziel);
	public UMLKlassifizierer importiere(File quelle);
	
}