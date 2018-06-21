package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

	@Cacheable(cacheNames = "shortUrl", unless = "#result == null")
	@Transactional(readOnly = true)
	Optional<ShortUrl> findByOriginalUrl(String originalUrl);

	@SuppressWarnings("unchecked")
	@CacheEvict(cacheNames = "originalUrl", key = "#a0?.shorteningKey")
	ShortUrl save(ShortUrl entity);

	@Cacheable(cacheNames = "originalUrl")
	@Transactional(readOnly = true)
	@Query("SELECT new java.lang.String(s.originalUrl) " +
			"FROM ShortUrl s " +
			"WHERE s.shorteningKey = ?1")
	Optional<String> findOriginalUrlByShorteningKey(String shorteningKey);

}
