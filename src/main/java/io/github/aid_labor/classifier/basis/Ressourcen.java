/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.util.Objects;


public class Ressourcen {
	
	private static Ressourcen instanz;
	private static ProgrammDetails programm;
	
	public static Ressourcen get() {
		if (programm == null) {
			throw new IllegalStateException("Bevor eine Instanz erzeugt werden kann "
					+ "muessen mit Ressourcen.setProgrammDetails die "
					+ "Programminformationen mitgeteilt werden!");
		}
		if (instanz == null) {
			instanz = new Ressourcen(programm);
		}
		
		return instanz;
	}
	
	public static void setProgrammDetails(ProgrammDetails programm) {
		Ressourcen.programm = Objects.requireNonNull(programm);
	}
	
	public final Ressource LIGHT_THEME_CSS;
	public final Ressource DARK_THEME_CSS;
	public final Ressource NUTZER_THEME_CSS;
	
	public final Ressource NUTZER_EINSTELLUNGEN;
	
	private Ressourcen(ProgrammDetails programm) {
		RessourceBuilder builder = new RessourceBuilder(programm);
		this.LIGHT_THEME_CSS = builder.erzeuge(RessourceTyp.CSS, "lightTheme.css");
		this.DARK_THEME_CSS = builder.erzeuge(RessourceTyp.CSS, "darkTheme.css");
		this.NUTZER_THEME_CSS = builder.erzeuge(RessourceTyp.CSS, "customTheme.css");
		this.NUTZER_EINSTELLUNGEN = builder.erzeuge(RessourceTyp.EINSTELLUNGSDATEI,
				"nutzerEinstellung.json");
	}
}