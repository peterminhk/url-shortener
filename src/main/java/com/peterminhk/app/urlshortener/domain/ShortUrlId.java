package com.peterminhk.app.urlshortener.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ShortUrlId {

	@Id
	private Long nextId;

}
