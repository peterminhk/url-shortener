package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ShortUrlRepositoryTest {

	@Autowired
	private ShortUrlRepository shortUrlRepository;

	@Test
	public void save() {
		ShortUrl shortUrl = new ShortUrl("qwerasdf", "http://original.com/foo/bar");
		shortUrlRepository.save(shortUrl);

		shortUrl = new ShortUrl("asdfzxcv", "http://original.com/apple/banana");
		shortUrlRepository.save(shortUrl);

		ShortUrl result = shortUrlRepository.findById(3L).get();
		assertEquals(3L, result.getId().longValue());
		assertEquals("http://original.com/foo/bar",
				result.getOriginalUrl());
		assertEquals("qwerasdf", result.getShorteningKey());

		result = shortUrlRepository.findById(4L).get();
		assertEquals(4L, result.getId().longValue());
		assertEquals("http://original.com/apple/banana",
				result.getOriginalUrl());
		assertEquals("asdfzxcv", result.getShorteningKey());
	}

	@Test
	public void findOriginalUrlByShorteningKey() {
		Optional<String> result = shortUrlRepository
				.findOriginalUrlByShorteningKey("asdfqwer");
		assertTrue(result.isPresent());
		assertEquals("https://github.com", result.get());

		result = shortUrlRepository.findOriginalUrlByShorteningKey("blahblah");
		assertFalse(result.isPresent());
	}

}
