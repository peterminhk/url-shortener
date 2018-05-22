package com.peterminhk.app.urlshortener.controller;

import com.peterminhk.app.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
public class RedirectController {

	@Autowired
	private ShortUrlRepository shortUrlRepository;

	@GetMapping("/{shorteningKey}")
	public String redirect(HttpServletResponse response,
						   @PathVariable String shorteningKey) {
		String originalUrl = shortUrlRepository
				.findOriginalUrlByShorteningKey(shorteningKey)
				.orElse(null);

		if (originalUrl == null) {
			response.setStatus(HttpStatus.NOT_FOUND.value());
			return "wrongUrl";
		}

		return "redirect:" + originalUrl;
	}

}
