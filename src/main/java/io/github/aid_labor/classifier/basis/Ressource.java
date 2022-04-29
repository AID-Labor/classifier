/*
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */

package io.github.aid_labor.classifier.basis;

import java.nio.file.Path;


public record Ressource(String ordnerPfad, String name) {
	
	public Path alsPath() {
		if (this.ordnerPfad != null) {
			return Path.of(this.ordnerPfad, this.name);
		} else {
			return Path.of(this.name);
		}
	}
	
}