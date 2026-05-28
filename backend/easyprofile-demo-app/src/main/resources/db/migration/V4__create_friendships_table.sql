CREATE TABLE friendships (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    friend_since TIMESTAMP NULL,
    CONSTRAINT fk_friendships_sender FOREIGN KEY (sender_id) REFERENCES users (id),
    CONSTRAINT fk_friendships_receiver FOREIGN KEY (receiver_id) REFERENCES users (id),
    CONSTRAINT chk_friendships_sender_receiver_diff CHECK (sender_id <> receiver_id)
);

CREATE INDEX idx_friendships_sender_id ON friendships (sender_id);
CREATE INDEX idx_friendships_receiver_id ON friendships (receiver_id);
CREATE INDEX idx_friendships_status ON friendships (status);

-- Optional but recommended: prevent duplicate pair regardless of direction.
-- Works by normalizing pair order into generated columns.
ALTER TABLE friendships
    ADD COLUMN user_low_id BIGINT AS (LEAST(sender_id, receiver_id)) STORED,
    ADD COLUMN user_high_id BIGINT AS (GREATEST(sender_id, receiver_id)) STORED,
    ADD CONSTRAINT uk_friendships_pair UNIQUE (user_low_id, user_high_id);