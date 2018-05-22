package com.peterminhk.app.urlshortener;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) {
		web
			.ignoring()
				.antMatchers("/js/**/*.js");
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO enable csrf in production
		http
			.csrf()
				.disable()
			.authorizeRequests()
				.anyRequest().permitAll();
	}

}
