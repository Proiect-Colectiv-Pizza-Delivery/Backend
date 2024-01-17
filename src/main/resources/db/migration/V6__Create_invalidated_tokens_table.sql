ALTER TABLE refreshtoken RENAME TO invalidated_tokens;

ALTER TABLE invalidated_tokens DROP COLUMN username;

ALTER TABLE invalidated_tokens RENAME COLUMN token TO tokenId;