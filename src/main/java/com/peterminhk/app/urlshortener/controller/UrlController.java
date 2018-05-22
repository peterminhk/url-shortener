package com.peterminhk.app.urlshortener.controller;

import com.peterminhk.app.urlshortener.dto.ShortUrlDto;
import com.peterminhk.app.urlshortener.repository.ShortUrlRepository;
import com.peterminhk.app.urlshortener.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {

	@Autowired
	private ShortUrlRepository shortUrlRepository;

	@Autowired
	private ShortUrlService shortUrlService;

	@PostMapping(value = "/urls", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ShortUrlDto> urls(@RequestParam String url) {
		ShortUrlDto shortUrlDto = shortUrlService.findByOriginalUrl(url)
				.orElse(null);

		if (shortUrlDto != null) {
			return new ResponseEntity<>(shortUrlDto, HttpStatus.OK);
		}

		shortUrlDto = shortUrlService.generateShortUrl(url);

		return new ResponseEntity<>(shortUrlDto, HttpStatus.CREATED);
	}

}
