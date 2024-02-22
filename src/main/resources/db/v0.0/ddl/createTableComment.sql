CREATE TABLE IF NOT EXISTS comment
(
    id        SERIAL PRIMARY KEY NOT NULL UNIQUE,
    user_name VARCHAR(64)        NOT NULL,
    time      TIMESTAMP          NOT NULL,
    news_id   BIGINT             NOT NULL
        CONSTRAINT comment_news_id_fk
            REFERENCES news
);

alter table comment
    owner to postgres;

ALTER SEQUENCE comment_id_seq RESTART WITH 201;

CREATE TABLE IF NOT EXISTS text_comment
(
    id         SERIAL PRIMARY KEY NOT NULL UNIQUE,
    text       TEXT,
    comment_id BIGINT             NOT NULL
        CONSTRAINT text_comment_comment_id_fk
            REFERENCES comment
            ON DELETE CASCADE UNIQUE
);

alter table text_comment
    owner to postgres;

ALTER SEQUENCE text_comment_id_seq RESTART WITH 201;