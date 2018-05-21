package com.peterminhk.app.urlshortener.util;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ShorteningKeyGeneratorTest {

	@Test
	public void generateKey() {
		// 최소 ID 값으로 key를 생성할 때
		assertEquals("yZyH7Y9A",
				ShorteningKeyGenerator.generateKey(1));

		// 최대 ID 값으로 key를 생성할 때
		assertEquals("0000003V",
				ShorteningKeyGenerator.generateKey(218340105584896L));

		// (최대 ID 값 + 1)로 key를 생성할 때
		// 최소 ID 값으로 key를 생성한 것과 같은 값이 나와야 한다.
		assertEquals("yZyH7Y9A",
				ShorteningKeyGenerator.generateKey(218340105584897L));

		// (최대 ID 값 * 2)로 key를 생성할 때
		// 최대 ID 값으로 key를 생성한 것과 같은 값이 나와야 한다.
		assertEquals("0000003V",
				ShorteningKeyGenerator.generateKey(218340105584896L * 2));

	}

	@Test
	public void generateKeys() {
		String[] result = ShorteningKeyGenerator.generateKeys(1, 1000);

		// 100개의 key가 생성되어야 한다.
		assertEquals(1000, result.length);

		// 중복 key가 없어야 한다.
		assertEquals(1000, Arrays.stream(result).distinct().count());

		// null이거나 8자리가 아닌 key가 없어야 한다.
		assertEquals(0, Arrays.stream(result).filter(
				key -> key == null || key.length() != 8).count());
	}

}
