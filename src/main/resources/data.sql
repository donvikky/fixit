INSERT INTO roles (id, name) VALUES (1, 'USER');
INSERT INTO roles (id, name) VALUES (2, 'ADMIN');
INSERT INTO users (id, username, password) VALUES (1, 'admin', '1234');
INSERT INTO users_roles (user_id, role_id) VALUES (1,2);
INSERT INTO profiles (id, user_id) VALUES (1, 1);