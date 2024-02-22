CREATE TABLE IF NOT EXISTS news
(
    id    SERIAL PRIMARY KEY NOT NULL UNIQUE,
    title VARCHAR            NOT NULL UNIQUE,
    time  TIMESTAMP          NOT NULL
);

alter table news
    owner to postgres;

ALTER SEQUENCE news_id_seq RESTART WITH 21;

CREATE TABLE IF NOT EXISTS text_news
(
    id      SERIAL PRIMARY KEY NOT NULL UNIQUE,
    text    TEXT               NOT NULL,
    news_id BIGINT             NOT NULL
        CONSTRAINT text_news_news_id_fk
            REFERENCES news
            ON DELETE CASCADE UNIQUE
);

alter table text_news
    owner to postgres;

ALTER SEQUENCE text_news_id_seq RESTART WITH 21;
