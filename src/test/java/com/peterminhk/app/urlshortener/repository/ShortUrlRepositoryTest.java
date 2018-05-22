package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ShortUrlRepositoryTest {

	@Autowired
	private ShortUrlRepository shortUrlRepository;

	@Test
	public void save() {
		ShortUrl shortUrl = new ShortUrl();
		shortUrl.setOriginalUrl("http://original.com/foo/bar");
		shortUrl.setShorteningKey("qwerasdf");
		shortUrlRepository.save(shortUrl);

		shortUrl = new ShortUrl();
		shortUrl.setOriginalUrl("http://original.com/apple/banana");
		shortUrl.setShorteningKey("asdfzxcv");
		shortUrlRepository.save(shortUrl);

		ShortUrl result = shortUrlRepository.findById(1L).get();
		assertEquals(1L, result.getId().longValue());
		assertEquals("http://original.com/foo/bar",
				result.getOriginalUrl());
		assertEquals("qwerasdf", result.getShorteningKey());

		result = shortUrlRepository.findById(2L).get();
		assertEquals(2L, result.getId().longValue());
		assertEquals("http://original.com/apple/banana",
				result.getOriginalUrl());
		assertEquals("asdfzxcv", result.getShorteningKey());
	}

}
