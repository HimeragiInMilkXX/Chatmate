ALTER TABLE user_profile_fields
    ADD COLUMN reference_target VARCHAR(128) NULL;

ALTER TABLE user_profile_fields
    ADD COLUMN reference_key VARCHAR(64) NULL;

ALTER TABLE user_profile_fields
    ADD COLUMN reference_required BOOLEAN NOT NULL DEFAULT FALSE;
