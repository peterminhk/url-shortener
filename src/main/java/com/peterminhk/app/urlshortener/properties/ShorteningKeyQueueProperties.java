package com.peterminhk.app.urlshortener.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "custom.shortening-key-queue")
@Validated
@Data
public class ShorteningKeyQueueProperties {

	@NotNull
	private int capacity;

	@NotNull
	private int fillThreshold;

	@NotNull
	private int timeout;

}
