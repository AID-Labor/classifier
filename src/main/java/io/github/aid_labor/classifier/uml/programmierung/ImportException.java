/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.uml.programmierung;


public class ImportException extends Exception {

	private static final long serialVersionUID = -8872423588699639991L;

	public ImportException() {
		super();
	}

	public ImportException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ImportException(String message, Throwable cause) {
		super(message, cause);
	}

	public ImportException(String message) {
		super(message);
	}

	public ImportException(Throwable cause) {
		super(cause);
	}
	
}