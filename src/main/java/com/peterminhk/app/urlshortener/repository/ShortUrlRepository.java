package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {
}
