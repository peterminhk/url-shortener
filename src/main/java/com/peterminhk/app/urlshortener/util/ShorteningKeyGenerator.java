package com.peterminhk.app.urlshortener.util;

import java.math.BigInteger;
import java.util.Random;

/**
 * Shortening Key 생성기.
 *
 * @author Minhyeok Jeong
 */
public final class ShorteningKeyGenerator {

	private ShorteningKeyGenerator() {
	}

	/**
	 * Shortening key의 길이.
	 */
	public static final int KEY_LENGTH = 8;

	/**
	 * 조합 가능한 key의 최대 개수.
	 * key는 62진수 8자리를 사용하므로 62<sup>8</sup>으로 계산.
	 */
	public static final long MAX_COUNT = 218340105584896L;
	private static final BigInteger MAX_COUNT_BIG_INT = BigInteger.valueOf(MAX_COUNT);

	/**
	 * 연속적으로 key를 생성할 때 각 key 값을 분산시키기 위한 상수.
	 * {@link #MAX_COUNT} 값과 서로소인 값 중 상당히 큰 값 하나를 임의로 택한다.
	 */
	private static final BigInteger LEAP = BigInteger.valueOf(213340105584895L);

	/**
	 * key 값 예측을 어렵게 하기 위한 상수.
	 * {@link #MAX_COUNT} 값보다 작은 양의 정수 중 하나를 임의로 택한다.
	 */
	private static final int SHIFT = 217;

	/**
	 * 62진수 8자리의 key를 생성한다.
	 * <p>
	 * 생성 가능한 key의 조합은 {@link #MAX_COUNT}만큼이며, 따라서 의미있는 입력 seq 값의 범위는
	 * 1(inclusive)부터 {@link #MAX_COUNT}(inclusive)까지이다. 이 범위를 벗어난 seq 값을
	 * 입력할 경우에는 범위 안의 seq 값을 입력했을 때와 동일한 key가 반복해서 생성된다.
	 * </p>
	 *
	 * @param seq key 생성을 위한 seq
	 * @return 62진수로 인코딩한 8자리 key 값
	 */
	public static String generateKey(long seq) {
		return Base62.encode((BigInteger.valueOf(seq).multiply(LEAP)
						.mod(MAX_COUNT_BIG_INT).longValueExact() + SHIFT) % MAX_COUNT,
				KEY_LENGTH);
	}

	/**
	 * 62진수 8자리의 key 배열을 생성한다.
	 * <p>
	 * {@link #generateKey(long)}를 반복 호출하여 key 목록을 생성하는데 이때 seq는
	 * beginSeq(inclusive)부터 beginSeq + count(exclusive)까지의 값을 사용하며 key 생성
	 * 순서는 랜덤이다.
	 * </p>
	 *
	 * @param beginSeq key 생성을 위한 seq의 최소값
	 * @param count key 생성 개수
	 * @return key 배열
	 */
	public static String[] generateKeys(long beginSeq, int count) {
		final String[] keys = new String[count];
		final Random random = new Random();
		int randomIndex;
		int resultIndex = 0;

		while (resultIndex < count) {
			randomIndex = random.nextInt(count);

			if (keys[randomIndex] != null) {
				continue;
			}

			keys[resultIndex] = generateKey(beginSeq + randomIndex);
			resultIndex++;
		}

		return keys;
	}

}
