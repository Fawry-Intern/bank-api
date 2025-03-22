CREATE TABLE password_change_requests
(
    id              BIGSERIAL PRIMARY KEY,
    token           VARCHAR(255) NOT NULL UNIQUE,
    user_id         BIGINT       NOT NULL,
    expiration_date TIMESTAMP    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);
