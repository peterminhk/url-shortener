package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShortUrlId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlIdRepository extends JpaRepository<ShortUrlId, Long> {
}
