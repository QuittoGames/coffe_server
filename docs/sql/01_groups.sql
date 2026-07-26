CREATE TABLE IF NOT EXISTS "groups" (
    gid         INTEGER     PRIMARY KEY,
    name        VARCHAR     NOT NULL,
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,

    CONSTRAINT uq_groups_name UNIQUE (name),
    CONSTRAINT chk_groups_gid_positive CHECK (gid > 0)
);
