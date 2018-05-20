package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrlId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ShortUrlIdRepositoryTest {

	@Autowired
	private ShortUrlIdRepository shortUrlIdRepository;

	@Test
	public void testFindOne() {
		Optional<ShortUrlId> result = shortUrlIdRepository.findOne(
				Example.of(new ShortUrlId()));
		assertTrue(result.isPresent());
		assertEquals(1L, result.get().getNextId().longValue());
	}

}
