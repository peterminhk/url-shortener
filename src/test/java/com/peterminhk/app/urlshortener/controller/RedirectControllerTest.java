package com.peterminhk.app.urlshortener.controller;

import com.peterminhk.app.urlshortener.repository.ShortUrlRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RedirectControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShortUrlRepository shortUrlRepository;

	@Test
	public void redirect() throws Exception {
		String expected = "http://blahblah.com/foo/bar";
		given(shortUrlRepository.findOriginalUrlByShorteningKey("qwer1234"))
				.willReturn(Optional.of(expected));

		mockMvc.perform(get("/qwer1234"))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:" + expected));
	}

	@Test
	public void redirectWhenUrlNotExists() throws Exception {
		String expected = "http://blahblah.com/foo/bar";
		given(shortUrlRepository.findOriginalUrlByShorteningKey("asdf"))
				.willReturn(Optional.empty());

		mockMvc.perform(get("/asdf"))
				.andExpect(status().isNotFound())
				.andExpect(view().name("wrongUrl"));
	}

}
