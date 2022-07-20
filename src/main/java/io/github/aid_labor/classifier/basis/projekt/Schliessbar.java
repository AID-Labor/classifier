/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.projekt;


public interface Schliessbar extends AutoCloseable {
	
	boolean darfGeschlossenWerden();
	void setDarfGeschlossenWerden(boolean wert);
	void schliesse() throws Exception;
	
	@Override
	default void close() throws Exception {
		if (darfGeschlossenWerden()) {
			schliesse();
		}
	}
}