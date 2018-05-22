package com.peterminhk.app.urlshortener.controller.api;

import com.peterminhk.app.urlshortener.dto.ShortUrlDto;
import com.peterminhk.app.urlshortener.service.ShortUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api")
@Validated
public class UrlController {

	@Autowired
	private ShortUrlService shortUrlService;

	private static final String URL_PATTERN = "^http(s)?://[^<>'\"`\\s]+";

	@PostMapping(value = "/urls", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ShortUrlDto> urls(
			@Size(max = 2083, message = "Url is too long")
			@Pattern(regexp = URL_PATTERN, message = "Invalid url")
			@RequestParam String url) {
		ShortUrlDto shortUrlDto = shortUrlService.findByOriginalUrl(url)
				.orElse(null);

		if (shortUrlDto != null) {
			return new ResponseEntity<>(shortUrlDto, HttpStatus.OK);
		}

		shortUrlDto = shortUrlService.generateShortUrl(url);

		return new ResponseEntity<>(shortUrlDto, HttpStatus.CREATED);
	}

}
