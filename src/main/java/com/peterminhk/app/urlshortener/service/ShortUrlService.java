package com.peterminhk.app.urlshortener.service;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import com.peterminhk.app.urlshortener.dto.ShortUrlDto;
import com.peterminhk.app.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Optional;

@Service
public class ShortUrlService {

	private static final String SHORT_URL_PREFIX = "http://localhost/";

	@Autowired
	private ShortUrlRepository shortUrlRepository;

	@Autowired
	private ShorteningKeyService shorteningKeyService;

	private static ShortUrlDto convertToDto(ShortUrl shortUrl) {
		return new ShortUrlDto(
				SHORT_URL_PREFIX + shortUrl.getShorteningKey(),
				shortUrl.getOriginalUrl()
		);
	}

	public Optional<ShortUrlDto> findByOriginalUrl(String originalUrl) {
		return shortUrlRepository.findByOriginalUrl(originalUrl)
				.map(ShortUrlService::convertToDto);
	}

	public ShortUrlDto generateShortUrl(String url) {
		String key = shorteningKeyService.newKey();

		if (key == null) {
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ShortUrl shortUrl = new ShortUrl(key, url);
		shortUrlRepository.save(shortUrl);

		return convertToDto(shortUrl);
	}

}
