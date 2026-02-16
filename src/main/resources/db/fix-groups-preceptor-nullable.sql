-- Permite preceptor_id nulo na tabela groups (grupo pode existir sem preceptor definido).
-- Execute uma vez no banco: psql ... -f fix-groups-preceptor-nullable.sql
-- ou rode o comando abaixo no cliente SQL do PostgreSQL.

ALTER TABLE "groups" ALTER COLUMN preceptor_id DROP NOT NULL;
