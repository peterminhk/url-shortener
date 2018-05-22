package com.peterminhk.app.urlshortener.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortUrlDto {

	private String shortUrl;
	private String originalUrl;

}
