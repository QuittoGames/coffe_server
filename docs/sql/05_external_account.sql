CREATE TABLE IF NOT EXISTS external_account (
    id              BIGSERIAL       PRIMARY KEY,
    user_id         BIGINT          NOT NULL,
    provider        VARCHAR(50)     NOT NULL,
    external_client VARCHAR(255)    NOT NULL,
    access_token    TEXT            NOT NULL,
    refresh_token   TEXT,
    expires_at      TIMESTAMP,

    CONSTRAINT chk_external_account_provider CHECK (provider IN ('GOOGLE', 'GITHUB')),
    CONSTRAINT fk_external_account_user FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_external_account_user_id ON external_account(user_id);
CREATE INDEX IF NOT EXISTS idx_external_account_provider ON external_account(provider);
