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
