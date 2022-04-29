/* 
 * Dieser Quellcode steht unter der MIT-License.
 * Copyright (c) 2022 - Tim Muehle (GitHub: @encrypTimM)
 *
 */
package io.github.aid_labor.classifier.basis.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonFactory;

class JsonUtilTest {
	
	@Test
	void testGetJsonFabrik() {
		var fabrik = JsonUtil.getJsonFabrik();
		assertNotNull(fabrik);
		MatcherAssert.assertThat(fabrik, IsInstanceOf.instanceOf(JsonFactory.class));
		assertEquals(fabrik, JsonUtil.getJsonFabrik());
	}
	
}