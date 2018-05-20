CREATE TABLE IF NOT EXISTS short_url_id (
  next_id BIGINT UNSIGNED NOT NULL,

  PRIMARY KEY (next_id)
);


CREATE TABLE IF NOT EXISTS short_url (
  id BIGINT UNSIGNED NOT NULL,
  shortening_key CHAR(8),
  original_url VARCHAR(2083),

  PRIMARY KEY (id),
  INDEX (shortening_key),
  INDEX (original_url)
);
