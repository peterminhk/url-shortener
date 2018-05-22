package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

	Optional<ShortUrl> findByOriginalUrl(String originalUrl);

}
