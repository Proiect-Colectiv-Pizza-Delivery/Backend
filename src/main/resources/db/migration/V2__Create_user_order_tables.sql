-- V2__Create_user_order_tables.sql

-- Enum type for 'role'
CREATE TYPE role_enum AS ENUM ('ADMIN', 'CUSTOMER');

-- Create 'app_users' table
CREATE TABLE app_users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    role role_enum NOT NULL
);

-- Create 'orders' table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    value FLOAT NOT NULL,
    app_user_id BIGINT,
    FOREIGN KEY (app_user_id) REFERENCES app_users(id) ON DELETE CASCADE
);

-- Create 'order_pizza' table
CREATE TABLE order_pizza (
    order_id BIGINT NOT NULL,
    pizza_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, pizza_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE
);