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
