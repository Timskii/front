CREATE TABLE front.task
(
    id uuid NOT NULL DEFAULT uuid_generate_v4(),
    name character varying(250) NOT NULL,
    title character varying(30),
    label character varying(30),
    description character varying(250),
    status character varying(30),
    PRIMARY KEY (id)
);


