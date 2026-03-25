-- Migration para adicionar os campos 'model' e 'vendor' na tabela history
ALTER TABLE history
    ADD COLUMN model VARCHAR(255) NOT NULL,
    ADD COLUMN vendor VARCHAR(255) NOT NULL

