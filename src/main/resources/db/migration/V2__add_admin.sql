INSERT INTO users (id, archive, email, name, password, role)
VALUES (1, false, 'mail@mail.ru', 'admin', '$2a$10$yv31hhlx0/KB6DqSmz.2u..6Jc3zecBLfw82rMUHpgNqSu0FAMSf2', 'ADMIN');

ALTER SEQUENCE user_seq RESTART WITH 2;