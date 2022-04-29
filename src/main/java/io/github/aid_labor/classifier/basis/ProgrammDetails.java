package io.github.aid_labor.classifier.basis;

public record ProgrammDetails (
		String version,
		String name,
		String info,
		String lizenz
) {
	public String getVersionName() {
		return this.name  + " V-" + this.version;
	}
}
