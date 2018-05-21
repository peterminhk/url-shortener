package com.peterminhk.app.urlshortener.util;

import java.util.Arrays;

/**
 * 숫자를 Base62로 인코딩하는 기능을 구현한 클래스.
 *
 * @author Minhyeok Jeong
 */
public class Base62 {

	/**
	 * Base62 인코딩 문자를 빠르게 찾기 위한 배열.
	 * 숫자 0~9, 10~35, 36~61은 각각 문자 0~9, A~Z, a~z에 대응한다.
	 */
	private static final char[] BASE62_CHARACTERS = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
			'y', 'z'
	};

	private Base62() {
	}

	/**
	 * 0 또는 양의 정수를 Base62로 인코딩한다.
	 *
	 * @param src 인코딩할 숫자 값
	 * @param length 인코딩 결과로 받을 String의 길이. length 값이 실제 인코딩된 String 길이
	 *               보다 클 경우 빈자리는 앞에서부터 0으로 채운다.
	 * @return Base62로 인코딩 된 String
	 * @throws IllegalArgumentException {@code src} 값이 음수일 경우
	 */
	public static String encode(long src, int length) {
		if (src < 0) {
			throw new IllegalArgumentException("The number to encode cannot be negative");
		}

		if (length <= 0) {
			throw new IllegalArgumentException("The length should be greater than zero");
		}

		final char[] outBuffer = new char[length];
		Arrays.fill(outBuffer, BASE62_CHARACTERS[0]);
		int outIndex = length - 1;

		do {
			outBuffer[outIndex--] = BASE62_CHARACTERS[(int) (src % 62)];
			src /= 62;
		} while (src > 0 && outIndex >= 0);

		return String.valueOf(outBuffer);
	}

}
