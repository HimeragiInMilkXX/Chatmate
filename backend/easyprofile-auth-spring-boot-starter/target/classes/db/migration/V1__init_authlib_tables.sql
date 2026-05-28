CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(64) NOT NULL,
    email VARCHAR(128) NOT NULL,
    password_hash VARCHAR(120) NOT NULL,
    avatar_url VARCHAR(512) NULL,
    last_password_reset_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

CREATE TABLE user_profile_fields (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    field_name VARCHAR(64) NOT NULL,
    data_type VARCHAR(16) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_profile_field_name UNIQUE (field_name)
);

CREATE TABLE user_profile_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    field_id BIGINT NOT NULL,
    string_value VARCHAR(255) NULL,
    int_value INT NULL,
    long_value BIGINT NULL,
    double_value DOUBLE NULL,
    boolean_value BOOLEAN NULL,
    date_value DATE NULL,
    datetime_value TIMESTAMP NULL,
    text_value TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_field UNIQUE (user_id, field_id),
    CONSTRAINT fk_profile_value_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_profile_value_field FOREIGN KEY (field_id) REFERENCES user_profile_fields (id)
);

CREATE INDEX idx_profile_values_user_id ON user_profile_values (user_id);
CREATE INDEX idx_profile_values_field_id ON user_profile_values (field_id);
