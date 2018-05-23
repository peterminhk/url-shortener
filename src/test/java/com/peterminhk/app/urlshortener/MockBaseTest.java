package com.peterminhk.app.urlshortener;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

public abstract class MockBaseTest {

	protected static void initField(Object object, String fieldName, Object value)
			throws NoSuchFieldException, IllegalAccessException {
		Field field = object.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(object, value);
	}

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

}
