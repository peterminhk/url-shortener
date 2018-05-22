DROP TABLE IF EXISTS shortening_key_seq;

CREATE TABLE shortening_key_seq (
  next_seq BIGINT UNSIGNED NOT NULL,

  PRIMARY KEY (next_seq)
);


DROP TABLE IF EXISTS short_url;

CREATE TABLE short_url (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  shortening_key CHAR(8),
  original_url VARCHAR(2083),

  PRIMARY KEY (id),
  INDEX (shortening_key),
  INDEX (original_url(200))
);
