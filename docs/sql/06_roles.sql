-- Usuários externos/API clients
CREATE USER api_client WITH PASSWORD 'dnoineoidnodnoidnewioncowncoencoiwenciowecioewbobewicbwecdjcnkdscdsknckwjecndjscnwjkd#@@&*&@#@';


-- Coloca usuários nos grupos
GRANT admins TO coffe_admin;
GRANT users TO coffe_api;
GRANT api_consumers TO api_client;


-- Permissões básicas no banco
GRANT CONNECT ON DATABASE coffe_server TO admins, users, api_consumers;


-- Schema principal
GRANT USAGE ON SCHEMA public TO users, api_consumers;


-- Usuários da aplicação podem manipular dados
GRANT SELECT, INSERT, UPDATE, DELETE
ON ALL TABLES IN SCHEMA public
TO users;


-- Consumidores externos só leem
GRANT SELECT
ON ALL TABLES IN SCHEMA public
TO api_consumers;


-- Admins recebem tudo
GRANT ALL PRIVILEGES
ON DATABASE coffe_server
TO admins;

GRANT ALL PRIVILEGES
ON ALL TABLES IN SCHEMA public
TO admins;


-- Para tabelas futuras criadas pela aplicação
ALTER DEFAULT PRIVILEGES
IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE
ON TABLES TO users;


ALTER DEFAULT PRIVILEGES
IN SCHEMA public
GRANT SELECT
ON TABLES TO api_consumers;


ALTER DEFAULT PRIVILEGES
IN SCHEMA public
GRANT ALL
ON TABLES TO admins;
