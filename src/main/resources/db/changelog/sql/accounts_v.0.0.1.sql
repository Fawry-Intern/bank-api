CREATE TABLE accounts (
    account_id BIGSERIAL PRIMARY KEY,
    card_number VARCHAR(20) UNIQUE NOT NULL,
    cvv VARCHAR(4) NOT NULL,
    balance NUMERIC(15,2) NOT NULL CHECK (balance >= 0),
    account_status account_status_enum NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);