/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis;

public abstract sealed class OS permits Unix, Windows {
	
	private static OS instanz;
	
	private static OS createInstanz() throws UnsupportedOSException {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return new Windows();
		} else if (os.contains("mac")) {
			return new MacOS();
		} else if (os.contains("linux")) {
			return new Linux();
		} else if (os.contains("unix")) {
			return new Unix();
		} else {
			throw new UnsupportedOSException("Unbekanntes OS: " + os);
		}
	}
	
	static OS getDefault() {
		if (instanz == null) {
			try {
				instanz = createInstanz();
			} catch (UnsupportedOSException e) {
				
				e.printStackTrace();
			}
		}
		
		return instanz;
	}
	
	OS(){
	}
	
	abstract String getKonfigurationsOrdner(ProgrammDetails programm);
	abstract boolean useDarkTheme();
}
