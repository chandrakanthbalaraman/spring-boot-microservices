-- Runs once on first Postgres volume init (docker-entrypoint-initdb.d).
-- One schema per planned bounded context (modular monolith, shared DB).

CREATE SCHEMA IF NOT EXISTS product;

