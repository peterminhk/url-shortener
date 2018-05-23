package com.peterminhk.app.urlshortener.service;

import com.peterminhk.app.urlshortener.domain.ShorteningKeySeq;
import com.peterminhk.app.urlshortener.properties.ShorteningKeyQueueProperties;
import com.peterminhk.app.urlshortener.repository.ShorteningKeySeqRepository;
import com.peterminhk.app.urlshortener.util.ShorteningKeyGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Shortening key 생성을 담당하는 서비스.
 *
 * @author Minhyeok Jeong
 */
@Service
public class ShorteningKeyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShorteningKeyService.class);

	@Autowired
	private ShorteningKeyQueueProperties keyQueueProperties;

	/** {@link #fillKeys}를 실행하는데 사용할 {@link Executor} */
	private final ExecutorService executor = Executors.newSingleThreadExecutor(
			runnable -> new Thread(runnable, "ShorteningKey"));

	/** 미리 생성해 둔 key를 저장할 queue */
	private ArrayBlockingQueue<String> keyQueue;

	/** key 생성에 사용할 seq */
	private long nextSeq;

	@Autowired
	private ShorteningKeySeqRepository shorteningKeySeqRepository;

	/**
	 * {@link #nextSeq}와 {@link #keyQueue}를 초기화한다.
	 */
	@PostConstruct
	public void init() {
		keyQueue = new ArrayBlockingQueue<>(keyQueueProperties.getCapacity());

		ShorteningKeySeq seq = shorteningKeySeqRepository.findFirstBy()
				.orElse(null);

		if (seq == null) {
			nextSeq = 1L;
			shorteningKeySeqRepository.saveAndFlush(new ShorteningKeySeq(nextSeq));
			LOGGER.warn("ShorteningKeySeq is empty. Stored a new value: {}", nextSeq);
		} else {
			nextSeq = seq.getNextSeq();
		}

		fillKeysAsync();
	}

	/**
	 * {@link #fillKeys()}를 비동기 방식으로 실행한다.
	 */
	private void fillKeysAsync() {
		CompletableFuture.runAsync(this::fillKeys, executor).exceptionally(e -> {
			LOGGER.error("An error occurred while filling keys", e);
			return null;
		});
	}

	/**
	 * {@link #keyQueue}에 여유 공간만큼 key를 채워 넣는다.
	 */
	private synchronized void fillKeys() {
		LOGGER.info("Filling keys");

		if (keyQueue.size() >= keyQueueProperties.getFillThreshold()) {
			LOGGER.info("Skip filling keys: queueSize = {}, threshold = {}",
					keyQueue.size(), keyQueueProperties.getFillThreshold());
			return;
		}

		final int count = keyQueue.remainingCapacity();

		// key 생성
		String[] keys = ShorteningKeyGenerator.generateKeys(nextSeq, count);

		// DB에 seq 업데이트
		nextSeq += count;
		shorteningKeySeqRepository.updateNextSeq(nextSeq);

		// queue에 key 추가
		for (String key : keys) {
			keyQueue.add(key);
		}

		LOGGER.info("Filling keys: done");
	}

	/**
	 * 새 shortening key를 반환한다.
	 *
	 * @return 새 shortening key 반환. 새 key를 발급할 수 없을 때는 null을 반환한다.
	 */
	public String newKey() {
		LOGGER.debug("Get a new key");
		String key = null;

		try {
			key = keyQueue.poll(keyQueueProperties.getTimeout(), TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.error("Cannot get a new key", e);
		}

		if (keyQueue.size() < keyQueueProperties.getFillThreshold()) {
			fillKeysAsync();
		}

		return key;
	}

}
