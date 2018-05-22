package com.peterminhk.app.urlshortener;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

public abstract class MockBaseTest {

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

}
