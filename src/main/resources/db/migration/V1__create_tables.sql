create table ingredients (
    id SERIAL primary key,
    name varchar(50) not NULL,
    stock float,
    allergens varchar(255)
);

create table pizzas (
    id SERIAL primary key,
    name varchar(50) not NULL,
    allergens varchar(255),
    price float
);