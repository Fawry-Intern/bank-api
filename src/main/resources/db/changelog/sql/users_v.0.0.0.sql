CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL CHECK (phone_number ~ '^\+?[0-9]{10,20}$'
    ),
    user_address TEXT NOT NULL,
   is_active BOOLEAN NOT NULL DEFAULT TRUE,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
