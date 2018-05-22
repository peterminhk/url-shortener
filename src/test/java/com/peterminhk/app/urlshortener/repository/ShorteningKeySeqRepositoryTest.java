package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShorteningKeySeq;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ShorteningKeySeqRepositoryTest {

	@Autowired
	private ShorteningKeySeqRepository shorteningKeySeqRepository;

	@Test
	public void updateNextSeq() {
		shorteningKeySeqRepository.updateNextSeq(123L);
		Optional<ShorteningKeySeq> result = shorteningKeySeqRepository.findFirstBy();
		assertEquals(123L, result.get().getNextSeq().longValue());

		shorteningKeySeqRepository.updateNextSeq(456L);
		result = shorteningKeySeqRepository.findFirstBy();
		assertEquals(456L, result.get().getNextSeq().longValue());
	}

}
