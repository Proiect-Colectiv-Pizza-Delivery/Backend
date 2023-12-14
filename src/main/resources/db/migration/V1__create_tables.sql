create table ingredients (
    id SERIAL primary key,
    name varchar(50) not NULL,
    stock integer,
    allergens varchar(255)
);

create table pizzas (
    id SERIAL primary key,
    name varchar(50) not NULL,
    allergens varchar(255),
    price float,
    blat_type varchar(100),
    blat_quantity integer,
    base_name varchar(100),
    base_quantity integer,
    ingredients varchar(1000) NOT NULL
);