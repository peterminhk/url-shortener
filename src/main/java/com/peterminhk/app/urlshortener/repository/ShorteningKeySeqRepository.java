package com.peterminhk.app.urlshortener.repository;

import com.peterminhk.app.urlshortener.domain.ShorteningKeySeq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ShorteningKeySeqRepository extends JpaRepository<ShorteningKeySeq, Long> {

	@Transactional(readOnly = true)
	Optional<ShorteningKeySeq> findFirstBy();

	@Transactional
	@Modifying
	@Query("UPDATE ShorteningKeySeq SET nextSeq = ?1")
	void updateNextSeq(long nextSeq);

}
