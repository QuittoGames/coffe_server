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
