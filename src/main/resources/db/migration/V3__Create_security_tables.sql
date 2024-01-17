CREATE TABLE IF NOT EXISTS users(
    username varchar(30) PRIMARY KEY,
    first_name varchar(30),
    last_name varchar(30),
    phone_number varchar(20) UNIQUE,
    email varchar(40) UNIQUE,
    password varchar(120) NOT NULL
    );

CREATE TABLE IF NOT EXISTS roles(
    name varchar(20) NOT NULL
    );

INSERT INTO roles(name) VALUES('ROLE_USER');
INSERT INTO roles(name) VALUES('ROLE_ADMIN');


CREATE TABLE IF NOT EXISTS user_roles(
    username varchar(30) ,
    rolename varchar(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS refreshtoken(
    username varchar(30) NOT NULL,
    token varchar NOT NULL UNIQUE,
    expiry_date timestamp NOT NULL
    );