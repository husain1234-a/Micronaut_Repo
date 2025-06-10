-- Create users table
CREATE TABLE users (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(15),
    gender VARCHAR(10) NOT NULL,
    date_of_birth DATE,
    role VARCHAR(20) NOT NULL,
    address_id UUID,
    FOREIGN KEY (address_id) REFERENCES addresses (id)
);

-- Create addresses table
CREATE TABLE addresses (
    id UUID PRIMARY KEY,
    street_address VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    postal_code VARCHAR(10) NOT NULL,
    address_type VARCHAR(20) NOT NULL
);