CREATE TABLE notifications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    content VARCHAR(1000) NOT NULL,
    notification_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_notifications_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_notifications_user_id ON notifications (user_id);
CREATE INDEX idx_notifications_user_date ON notifications (user_id, notification_at);
