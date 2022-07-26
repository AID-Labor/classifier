package io.github.aid_labor.classifier.basis.io;

import io.github.aid_labor.classifier.basis.io.system.OS;

public enum Theme {
	LIGHT, DARK, SYSTEM;
	
	
	public Ressource getStylesheet() {
		return switch (this) {
			case DARK -> Ressourcen.get().DARK_THEME_CSS;
			case LIGHT -> Ressourcen.get().LIGHT_THEME_CSS;
			case SYSTEM -> {
				if (OS.getDefault().systemNutztDarkTheme()) {
					yield Ressourcen.get().DARK_THEME_CSS;
				} else {
					yield Ressourcen.get().LIGHT_THEME_CSS;
				}
			}
		};
	}
}
