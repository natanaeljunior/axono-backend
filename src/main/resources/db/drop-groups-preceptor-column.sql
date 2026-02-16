-- Remove a coluna preceptor_id da tabela groups.
-- O preceptor por período fica apenas em rotation_assignments.
-- Execute uma vez no banco se quiser limpar a coluna.

ALTER TABLE "groups" DROP COLUMN IF EXISTS preceptor_id;
