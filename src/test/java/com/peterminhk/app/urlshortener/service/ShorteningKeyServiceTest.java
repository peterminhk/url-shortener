package com.peterminhk.app.urlshortener.service;

import com.peterminhk.app.urlshortener.MockBaseTest;
import com.peterminhk.app.urlshortener.domain.ShorteningKeySeq;
import com.peterminhk.app.urlshortener.repository.ShorteningKeySeqRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
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
	private ShorteningKeySeqRepository shorteningKeySeqRepository;

	@Mock
	private ArrayBlockingQueue<String> keyQueue;

	private static final long GIVEN_NEXT_SEQ = 12345L;

	@Before
	public void setUp() {
		given(shorteningKeySeqRepository.findFirstBy()).willReturn(
				Optional.of(new ShorteningKeySeq(GIVEN_NEXT_SEQ)));
		shorteningKeyService.init();
	}

	@Test
	public void newKey() throws InterruptedException {
		given(keyQueue.poll(anyLong(), any())).willReturn("somekey0");
		assertEquals("somekey0", shorteningKeyService.newKey());

		// keyQueue에서 key를 꺼낼 수 없을 경우
		given(keyQueue.poll(anyLong(), any())).willReturn(null);
		assertNull(shorteningKeyService.newKey());
	}

}
