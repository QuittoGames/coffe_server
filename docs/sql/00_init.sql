-- ============================================================
-- PostgreSQL Schema — coffe_server or quitto_server
-- Ordem de criação respeitando dependências de FK:
--   1. groups        (sem FK)
--   2. user          (sem FK)
--   3. linux_user    (FK → groups, user)
--   4. machine       (FK → user)
--   5. external_account (FK → user)
-- ============================================================

BEGIN;

-- 1. Groups
CREATE TABLE IF NOT EXISTS "groups" (
    gid         INTEGER     PRIMARY KEY,
    name        VARCHAR     NOT NULL,
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_groups_name UNIQUE (name),
    CONSTRAINT chk_groups_gid_positive CHECK (gid > 0)
);

-- 2. User
CREATE TABLE IF NOT EXISTS "user" (
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(150)    NOT NULL,
    password_hash   VARCHAR(150)    NOT NULL,
    email           VARCHAR(150)    NOT NULL,
    role            VARCHAR(50)     NOT NULL DEFAULT 'USER',

    CONSTRAINT uq_user_name       UNIQUE (name),
    CONSTRAINT uq_user_email      UNIQUE (email),
    CONSTRAINT chk_user_role      CHECK (role IN ('ADMIN', 'USER', 'MCP', 'API')),
    CONSTRAINT chk_user_name_min  CHECK (LENGTH(TRIM(name)) >= 1),
    CONSTRAINT chk_user_email_format CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

-- 3. Linux User
CREATE TABLE IF NOT EXISTS linux_user (
    uid         INTEGER     PRIMARY KEY,
    name        VARCHAR     NOT NULL,
    shell       VARCHAR,
    home_dir    VARCHAR     NOT NULL,
    gid         INTEGER,
    user_id     BIGINT      NOT NULL,

    CONSTRAINT uq_linux_user_user_id   UNIQUE (user_id),
    CONSTRAINT uq_linux_user_name      UNIQUE (name),
    CONSTRAINT chk_linux_user_uid_positive CHECK (uid > 0),
    CONSTRAINT fk_linux_user_groups    FOREIGN KEY (gid) REFERENCES "groups"(gid)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_linux_user_user      FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 4. Machine
CREATE TABLE IF NOT EXISTS machine (
    id                  BIGSERIAL       PRIMARY KEY,
    hostname            VARCHAR(100)    NOT NULL,
    tailscale_node_key  VARCHAR         NOT NULL,
    current_ip          VARCHAR         NOT NULL,
    mac_address         VARCHAR,
    wol_enabled         BOOLEAN         NOT NULL DEFAULT FALSE,
    status              BOOLEAN         NOT NULL DEFAULT TRUE,
    os                  VARCHAR(50),
    user_id             BIGINT          NOT NULL,

    CONSTRAINT uq_machine_hostname          UNIQUE (hostname),
    CONSTRAINT uq_machine_tailscale_node    UNIQUE (tailscale_node_key),
    CONSTRAINT uq_machine_current_ip        UNIQUE (current_ip),
    CONSTRAINT uq_machine_mac_address       UNIQUE (mac_address),
    CONSTRAINT chk_machine_hostname_min     CHECK (LENGTH(TRIM(hostname)) >= 1),
    CONSTRAINT chk_machine_ip_format        CHECK (current_ip ~* '^(\d{1,3}\.){3}\d{1,3}$'),
    CONSTRAINT chk_machine_mac_format       CHECK (mac_address IS NULL OR mac_address ~* '^([0-9A-Fa-f]{2}[:-]){5}[0-9A-Fa-f]{2}$'),
    CONSTRAINT fk_machine_user              FOREIGN KEY (user_id) REFERENCES "user"(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 5. External Account
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

COMMIT;
