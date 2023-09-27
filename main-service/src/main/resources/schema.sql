CREATE TABLE IF NOT EXISTS event
(
    id                 BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    annotation         VARCHAR(2000)    NOT NULL,
    description        VARCHAR(7000)    NOT NULL,
    category_id        BIGINT           NOT NULL,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP        NOT NULL,
    event_date         TIMESTAMP,
    user_id            BIGINT           NOT NULL,
    lat                DOUBLE PRECISION NOT NULL,
    lon                DOUBLE PRECISION NOT NULL,
    paid               BOOLEAN,
    participant_limit  INTEGER,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN,
    state              VARCHAR(256),
    title              VARCHAR(1024),
    views              BIGINT
);

CREATE TABLE IF NOT EXISTS category
(
    id   BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(256) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS request
(
    id       BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    event_id BIGINT    NOT NULL,
    user_id  BIGINT    NOT NULL,
    status   VARCHAR(256),
    created  TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    email VARCHAR(256) UNIQUE NOT NULL,
    name  VARCHAR(256)        NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    title  VARCHAR(256) NOT NULL,
    pinned BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    compilation_id BIGINT NOT NULL REFERENCES compilation (id),
    event_id       BIGINT NOT NULL REFERENCES event (id)
);

CREATE TABLE IF NOT EXISTS view
(
    id       BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    event_id BIGINT NOT NULL REFERENCES event (id),
    user_ip  VARCHAR(256)
);

ALTER TABLE view
    ADD CONSTRAINT fk_user_ip_unique UNIQUE (event_id, user_ip);

ALTER TABLE event
    ADD CONSTRAINT fk_event_category FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE event
    ADD CONSTRAINT fk_event_user FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE request
    ADD CONSTRAINT fk_request_event FOREIGN KEY (event_id) REFERENCES event (id);

ALTER TABLE request
    ADD CONSTRAINT fk_request_user FOREIGN KEY (user_id) REFERENCES users (id);