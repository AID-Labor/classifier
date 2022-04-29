package io.github.aid_labor.classifier.basis;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EinstellungenTest {
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		Ressourcen.setProgrammDetails(new ProgrammDetails(null, "Classifier-Test", null, null));
	}
	
	@AfterAll
	static void tearDownAfterClass() throws Exception {
//		Path nutzerEinstellungen = Ressourcen.get().NUTZER_EINSTELLUNGEN.alsPath();
//		Files.deleteIfExists(nutzerEinstellungen);
	}
	
	
	@Test
	void test() {
		
	}
	
}
