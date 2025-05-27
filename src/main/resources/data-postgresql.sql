INSERT INTO players (id, login, screen_name, password, age, gender, role)
SELECT 1, 'supervisor', 'Supervisor', 'SuperPass123', 30, 'male', 'SUPERVISOR'
WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 1);

INSERT INTO players (id, login, screen_name, password, age, gender, role)
SELECT 2, 'admin', 'Administrator', 'AdminPass123', 31, 'female', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 2);

INSERT INTO players (id, login, screen_name, password, age, gender, role)
SELECT 3, 'testuser', 'Test User', 'TestPass123', 32, 'male', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM players WHERE id = 3);

-- Reset the sequence to continue from the next available ID
SELECT setval('players_id_seq', (SELECT MAX(id) FROM players));