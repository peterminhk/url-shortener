INSERT INTO shortening_key_seq (next_seq) VALUES (1);

INSERT INTO short_url (
  shortening_key, original_url
) VALUES (
  'asdfqwer', 'https://github.com'
), (
  'zxcvasdf', 'https://gitlab.com'
);
