CREATE TABLE IF NOT EXISTS hits
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    app character varying(255)  NOT NULL,
    uri character varying(512) NOT NULL,
    ip character varying(15) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_hit PRIMARY KEY (id)
);