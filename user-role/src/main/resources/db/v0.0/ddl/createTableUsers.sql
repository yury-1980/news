DROP TYPE IF EXISTS users_type;
CREATE TYPE users_type AS ENUM ('ADMIN', 'JOURNALIST', 'SUBSCRIBER');

CREATE TABLE IF NOT EXISTS users
(
    id        SERIAL PRIMARY KEY NOT NULL UNIQUE,
    user_name VARCHAR(64)        NOT NULL,
    type      users_type         NOT NULL,
    password  VARCHAR            NOT NULL UNIQUE
);

alter table users
    owner to postgres;


-- ALTER SEQUENCE comment_id_seq RESTART WITH 201;



