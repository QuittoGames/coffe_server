-- Migration: seed test user for login validation
-- Target DB: coffe_server application database
--   (run against the SAME database the running app uses, typically localhost:5432)
-- Credentials created: name='teste', password='123', email='teste@coffee.local', role='USER'
-- Password hash generated with BCrypt strength 10 ($2a$10$...).
-- Idempotent: clears any prior 'teste' row before insert.
-- Execute with a role that has INSERT/DELETE on table "user".

DO $$
BEGIN
    DELETE FROM "user" WHERE name = 'teste' OR email = 'teste@coffee.local';

    INSERT INTO "user" (name, password_hash, email, role)
    VALUES (
        'teste',
        '$2a$10$ig/4MajbOINzAF4GmcX81OP/A2Ov39fZNn6C6Ha6j5TUS/DLprbpa',
        'teste@coffee.local',
        'USER'
    );
END $$;
