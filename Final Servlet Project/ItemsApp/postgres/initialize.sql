create database itemdb;

-- create specific schema for the project.
create schema itemSchema;

-- make this schema as default one.
SET search_path to itemSchema,public;

-- create users table.
CREATE TABLE users (
                       ID BIGSERIAL PRIMARY KEY,
                       USERNAME VARCHAR(50) UNIQUE NOT NULL,
                       PASSWORD TEXT NOT NULL
);

-- create item table.
CREATE TABLE item (
                      ID BIGSERIAL PRIMARY KEY,
                      NAME VARCHAR(50),
                      PRICE Numeric(10,2),
                      TOTALNUMBER INTEGER,
                      ISDELETED BOOLEAN,
                      USER_ID INTEGER REFERENCES users(ID)
);