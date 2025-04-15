ALTER TABLE transactions
ADD COLUMN state transaction_state NOT NULL DEFAULT 'CREATED';

