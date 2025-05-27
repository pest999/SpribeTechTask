-- H2 Database initialization (for dev profile)
-- Insert initial users for testing
INSERT INTO players (id, login, screen_name, password, age, gender, role)
VALUES (1, 'supervisor', 'Supervisor', 'SuperPass123', 21, 'male', 'SUPERVISOR');

INSERT INTO players (id, login, screen_name, password, age, gender, role)
VALUES (2, 'admin', 'Administrator', 'AdminPass123', 25, 'female', 'ADMIN');

INSERT INTO players (id, login, screen_name, password, age, gender, role)
VALUES (3, 'testuser', 'Test User', 'TestPass123', 22, 'male', 'USER'); 

-- Reset the auto-increment sequence to continue from ID 4 (H2 syntax)
ALTER TABLE players ALTER COLUMN id RESTART WITH 4; 