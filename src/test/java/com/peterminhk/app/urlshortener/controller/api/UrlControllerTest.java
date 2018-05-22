package com.peterminhk.app.urlshortener.controller.api;

import com.peterminhk.app.urlshortener.dto.ShortUrlDto;
import com.peterminhk.app.urlshortener.service.ShortUrlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UrlControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShortUrlService shortUrlService;

	@Test
	public void urls() throws Exception {
		String inputUrl = "http://some.url.com/blahblah";
		String expectedShortUrl = "http://localhost/qwerasdf";

		given(shortUrlService.findByOriginalUrl(anyString())).willReturn(
				Optional.empty());
		given(shortUrlService.generateShortUrl(anyString())).willReturn(
				new ShortUrlDto(expectedShortUrl, inputUrl));

		mockMvc.perform(post("/api/urls")
				.param("url", inputUrl)
				.characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.shortUrl")
						.value(expectedShortUrl))
				.andExpect(jsonPath("$.originalUrl")
						.value(inputUrl));
	}

	@Test
	public void urlsWhenUrlExists() throws Exception {
		String inputUrl = "http://some.url.com/blahblah";
		String expectedShortUrl = "http://localhost/qwerasdf";

		given(shortUrlService.findByOriginalUrl(anyString())).willReturn(
				Optional.of(new ShortUrlDto(expectedShortUrl, inputUrl)));

		mockMvc.perform(post("/api/urls")
				.param("url", inputUrl)
				.characterEncoding("UTF-8")
				.accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.shortUrl")
						.value(expectedShortUrl))
				.andExpect(jsonPath("$.originalUrl")
						.value(inputUrl));
	}

	@Test
	public void urlsGet() throws Exception {
		mockMvc.perform(get("/api/urls"))
				.andExpect(status().isMethodNotAllowed());
	}

}
