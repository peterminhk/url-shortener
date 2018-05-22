package com.peterminhk.app.urlshortener.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ShortUrl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String shorteningKey;
	private String originalUrl;

	public ShortUrl(String shorteningKey, String originalUrl) {
		this.shorteningKey = shorteningKey;
		this.originalUrl = originalUrl;
	}

}
