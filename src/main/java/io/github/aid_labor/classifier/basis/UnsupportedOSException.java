/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis;

/**
 * Zeigt an, dass eine Systemoperation oder das verwendete Betriebssystem nicht unterstuetzt 
 * wird.
 * 
 * @author Tim Muehle
 *
 */
public class UnsupportedOSException extends UnsupportedOperationException {

	private static final long serialVersionUID = -85835558246563414L;

	public UnsupportedOSException() {
		super();
	}

	public UnsupportedOSException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsupportedOSException(String message) {
		super(message);
	}

	public UnsupportedOSException(Throwable cause) {
		super(cause);
	}
	
}
