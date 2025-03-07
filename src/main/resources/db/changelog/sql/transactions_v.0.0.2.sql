CREATE TABLE transactions (
    transaction_id BIGSERIAL PRIMARY KEY,
    transaction_type transaction_type_enum NOT NULL,
    transaction_amount NUMERIC(15,2) NOT NULL CHECK (transaction_amount > 0),
    transaction_note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    account_id BIGINT NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);