package com.peterminhk.app.urlshortener.service;

import com.peterminhk.app.urlshortener.domain.ShorteningKeySeq;
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

	// TODO capacity, threshold 조정
	/** Key를 저장할 queue의 크기 */
	private static final int KEY_QUEUE_CAPACITY = 3;

	/**
	 * kqyQueue에 추가로 key를 채워 넣을 경계점.
	 * kqyQueue의 크기가 이 값 미만이 되면 key를 추가로 채워넣는다.
	 */
	private static final int KEY_QUEUE_FILL_THRESHOLD = 3;

	/** keyQueue에서 key를 꺼낼 때 기다릴 시간 (단위: ms) */
	private static final long KEY_QUEUE_TIMEOUT = 500;

	/** {@link #fillKeys}를 실행하는데 사용할 {@link Executor} */
	private final ExecutorService executor = Executors.newSingleThreadExecutor(
			runnable -> new Thread(runnable, "ShorteningKey"));

	/** 미리 생성해 둔 key를 저장할 queue */
	private ArrayBlockingQueue<String> keyQueue =
			new ArrayBlockingQueue<>(KEY_QUEUE_CAPACITY);

	/** key 생성에 사용할 seq */
	private long nextSeq;

	@Autowired
	private ShorteningKeySeqRepository shorteningKeySeqRepository;

	/**
	 * {@link #nextSeq}와 {@link #keyQueue}를 초기화한다.
	 */
	@PostConstruct
	public void init() {
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

		if (keyQueue.size() >= KEY_QUEUE_FILL_THRESHOLD) {
			LOGGER.info("Skip filling keys: queueSize = {}, threshold = {}",
					keyQueue.size(), KEY_QUEUE_FILL_THRESHOLD);
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
			key = keyQueue.poll(KEY_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			LOGGER.error("Cannot get a new key", e);
		}

		if (keyQueue.size() < KEY_QUEUE_FILL_THRESHOLD) {
			fillKeysAsync();
		}

		return key;
	}

}
