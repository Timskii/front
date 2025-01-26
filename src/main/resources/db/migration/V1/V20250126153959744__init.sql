CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE SCHEMA IF NOT EXISTS "front";

CREATE TABLE "front"."user" (
  "id" uuid NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
  "username" character varying(50) NOT NULL UNIQUE,
  "password" character varying(200),
  "name" character varying(100),
  "role" character varying(20),
  "status" character varying(20),
  "created_at" timestamp,
  PRIMARY KEY (id)
);



