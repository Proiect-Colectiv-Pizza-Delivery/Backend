-- V1__Create_tables.sql

-- Create 'ingredients' table
CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    allergens VARCHAR(255)
);

-- Create 'pizzas' table
CREATE TABLE pizzas (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    allergens VARCHAR(255) NOT NULL,
    price FLOAT NOT NULL,
    blat_type VARCHAR(255) NOT NULL,
    blat_quantity INT NOT NULL,
    base_name VARCHAR(255) NOT NULL,
    base_quantity INT NOT NULL
);

-- Create 'pizza_ingredients' table
CREATE TABLE pizza_ingredients (
    pizza_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    quantity INT,
    PRIMARY KEY (pizza_id, ingredient_id),
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE
);

