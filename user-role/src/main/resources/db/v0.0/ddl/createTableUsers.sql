DROP TYPE IF EXISTS role;
CREATE TYPE role AS ENUM ('ADMIN', 'JOURNALIST', 'SUBSCRIBER');

CREATE TABLE IF NOT EXISTS users
(
    id        SERIAL PRIMARY KEY NOT NULL UNIQUE,
    user_name VARCHAR(64)        NOT NULL,
    email     VARCHAR(64)        NOT NULL,
    password  VARCHAR            NOT NULL UNIQUE,
    role      role               NOT NULL
);

alter table users
    owner to postgres;


-- ALTER SEQUENCE comment_id_seq RESTART WITH 201;



