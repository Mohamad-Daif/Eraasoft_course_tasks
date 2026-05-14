create schema itemSchema;

CREATE TABLE users
(
    id       bigserial primary key,
    username varchar(50) unqiue,
    password varchar(50)
);

create table item
(
    id          bigserial primary key,
    name        varchar(50),
    price       numeric(10, 2),
    totalNumber integer,
    isDeleted   boolean,
    user_id     integer references users (id) on delete cascade
);

create table itemDetails
(
    item_id     integer references item (id) on delete cascade,
    description varchar(200),
    issuedAt    date,
    expiredAt   date
);