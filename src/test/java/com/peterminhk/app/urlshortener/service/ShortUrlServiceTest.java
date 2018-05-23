package com.peterminhk.app.urlshortener.service;

import com.peterminhk.app.urlshortener.MockBaseTest;
import com.peterminhk.app.urlshortener.domain.ShortUrl;
import com.peterminhk.app.urlshortener.dto.ShortUrlDto;
import com.peterminhk.app.urlshortener.repository.ShortUrlRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class ShortUrlServiceTest extends MockBaseTest {

	private static final String EXPECTED_URL_PREFIX = "http://te.st/";

	@InjectMocks
	private ShortUrlService shortUrlService;

	@MockBean
	private ShortUrlRepository shortUrlRepository;

	@MockBean
	private ShorteningKeyService shorteningKeyService;

	@Before
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		initField(shortUrlService, "shortUrlPrefix", EXPECTED_URL_PREFIX);
	}

	@Test
	public void findByOriginalUrl() {
		String expectedKey = "keykey00";
		String expectedOriginalUrl = "http://some.url.com/blahblah";

		given(shortUrlRepository.findByOriginalUrl(anyString())).willReturn(
				Optional.of(new ShortUrl(expectedKey, expectedOriginalUrl)));

		String input = "http://some.url.com/blahblah";
		ShortUrlDto result = shortUrlService.findByOriginalUrl(input)
				.orElse(null);

		assertNotNull(result);
		assertEquals(EXPECTED_URL_PREFIX + expectedKey, result.getShortUrl());
		assertEquals(expectedOriginalUrl, result.getOriginalUrl());
	}

	@Test
	public void generateShortUrl() {
		String expectedKey = "newkey00";
		given(shorteningKeyService.newKey()).willReturn(expectedKey);

		ShortUrlDto result = shortUrlService.generateShortUrl("http://some.url.com/blahblah");

		assertEquals(EXPECTED_URL_PREFIX + expectedKey, result.getShortUrl());
		assertEquals("http://some.url.com/blahblah", result.getOriginalUrl());
	}

	@Test(expected = HttpServerErrorException.class)
	public void generateShortUrlWhenKeyIsNull() {
		given(shorteningKeyService.newKey()).willReturn(null);
		shortUrlService.generateShortUrl("http://some.url.com/blahblah");
	}

}
