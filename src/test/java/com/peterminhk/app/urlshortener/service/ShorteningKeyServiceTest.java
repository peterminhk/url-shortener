package com.peterminhk.app.urlshortener.service;

import com.peterminhk.app.urlshortener.MockBaseTest;
import com.peterminhk.app.urlshortener.properties.ShorteningKeyQueueProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ArrayBlockingQueue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class ShorteningKeyServiceTest extends MockBaseTest {

	@InjectMocks
	private ShorteningKeyService shorteningKeyService;

	@MockBean
	private ShorteningKeyQueueProperties keyQueueProperties;

	@Mock
	private MockArrayBlockingQueue<String> keyQueue;

	@Before
	public void setUp() {
		given(keyQueueProperties.getCapacity()).willReturn(3);
		given(keyQueueProperties.getFillThreshold()).willReturn(2);
		given(keyQueueProperties.getTimeout()).willReturn(500);
	}

	@Test
	public void newKey() throws InterruptedException {
		given(keyQueue.poll(anyLong(), any())).willReturn("somekey0");
		given(keyQueue.size()).willReturn(3);
		assertEquals("somekey0", shorteningKeyService.newKey());

		// keyQueue에서 key를 꺼낼 수 없을 경우
		given(keyQueue.poll(anyLong(), any())).willReturn(null);
		assertNull(shorteningKeyService.newKey());
	}

	private static class MockArrayBlockingQueue<T> extends ArrayBlockingQueue<T> {
		public MockArrayBlockingQueue(int capacity) {
			super(capacity);
		}
	}

}
