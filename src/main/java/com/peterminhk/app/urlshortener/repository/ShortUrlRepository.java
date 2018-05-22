package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

	@Transactional(readOnly = true)
	Optional<ShortUrl> findByOriginalUrl(String originalUrl);

	@Transactional(readOnly = true)
	@Query("SELECT new java.lang.String(s.originalUrl) " +
			"FROM ShortUrl s " +
			"WHERE s.shorteningKey = ?1")
	Optional<String> findOriginalUrlByShorteningKey(String shorteningKey);

}
