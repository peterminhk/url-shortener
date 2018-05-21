package com.peterminhk.app.urlshortener.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base62Test {

	@Test
	public void encode() {
		assertEquals("0", Base62.encode(0L, 1));
		assertEquals("00000000", Base62.encode(0L, 8));
		assertEquals("00000001", Base62.encode(1L, 8));
		assertEquals("00000002", Base62.encode(2L, 8));
		assertEquals("0000000A", Base62.encode(10L, 8));
		assertEquals("0000000Z", Base62.encode(35L, 8));
		assertEquals("0000000a", Base62.encode(36L, 8));
		assertEquals("0000000z", Base62.encode(61L, 8));
		assertEquals("00000010", Base62.encode(62L, 8));
		assertEquals("00001000", Base62.encode(238328L, 8));
		assertEquals("000010Aa", Base62.encode(238984L, 8));
		assertEquals("zzzzzzzz", Base62.encode(218340105584895L, 8));
		assertEquals("100000000", Base62.encode(218340105584896L, 9));
	}

	@Test(expected = IllegalArgumentException.class)
	public void encodeNegativeSrc() {
		Base62.encode(-1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void encodeNegativeLength() {
		Base62.encode(1, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void encodeZeroLength() {
		Base62.encode(1, 0);
	}

}
