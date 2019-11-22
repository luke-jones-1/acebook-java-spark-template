CREATE TABLE comments(
  comment_id VARCHAR PRIMARY KEY,
  post_id VARCHAR,
  comment VARCHAR,
  FOREIGN KEY(post_id) REFERENCES posts(post_id) ON DELETE CASCADE
)