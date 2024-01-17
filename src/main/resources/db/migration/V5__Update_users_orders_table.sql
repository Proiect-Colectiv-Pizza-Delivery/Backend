ALTER TABLE users
ALTER COLUMN device_id SET NOT NULL;

ALTER TABLE users
RENAME COLUMN device_id TO fingerprint;

DROP TABLE order_pizza;

-- Drop and recreate the 'orders' table with modified 'app_user_id'
DROP TABLE orders;

-- Drop the temporary 'app_users' table, its now the 'users' table
DROP TABLE app_users;

DROP TYPE role_enum;

CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    value FLOAT NOT NULL,
    app_user_id VARCHAR(30),
    FOREIGN KEY (app_user_id) REFERENCES users(username) ON DELETE CASCADE
);

CREATE TABLE order_pizza (
    order_id BIGINT NOT NULL,
    pizza_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, pizza_id),
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (pizza_id) REFERENCES pizzas(id) ON DELETE CASCADE
);