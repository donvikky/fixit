DROP ALL OBJECTS;

CREATE TABLE users (
   id INT NOT NULL,
   username VARCHAR(50) NOT NULL,
   password VARCHAR(255) NOT NULL,
   enabled INT DEFAULT NULL,
   last_login_time DATE DEFAULT NULL,
   provider VARCHAR(255) DEFAULT 'LOCAL',
   provider_id VARCHAR(45) DEFAULT NULL
);

CREATE TABLE profiles (
  id INT NOT NULL,
  user_id INT DEFAULT NULL,
  email VARCHAR(255) DEFAULT NULL,
  first_name VARCHAR(255) DEFAULT NULL,
  last_name VARCHAR(255) DEFAULT NULL,
  mobile_number VARCHAR(45) DEFAULT NULL,
  address VARCHAR(45) DEFAULT NULL,
  state_id INT DEFAULT NULL,
  photo VARCHAR(255) DEFAULT NULL,
  hourly_rate INT DEFAULT NULL,
  short_description VARCHAR(45) DEFAULT NULL,
  long_description VARCHAR(45) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  update_time DATE DEFAULT NULL,
  create_user INT DEFAULT NULL,
  update_user INT DEFAULT NULL
);

CREATE TABLE roles (
    id INT NOT NULL,
    name VARCHAR(255)
);

CREATE TABLE users_roles (
     user_id INT NOT NULL,
     role_id INT NOT NULL
);

CREATE TABLE state (
  id INT NOT NULL IDENTITY,
  name VARCHAR(255) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  create_user INT DEFAULT NULL,
  update_time DATE DEFAULT NULL,
  update_user INT DEFAULT NULL
);

CREATE TABLE jobs (
  id INT NOT NULL IDENTITY,
  poster_id INT DEFAULT NULL,
  short_description VARCHAR(255) DEFAULT NULL,
  craft_id INT DEFAULT NULL,
  state_id INT DEFAULT NULL,
  budget_minimum INT DEFAULT NULL,
  budget_maximum INT DEFAULT NULL,
  job_type INT DEFAULT NULL,
  long_description VARCHAR(255) DEFAULT NULL,
  completed INT DEFAULT NULL,
  create_time datetime DEFAULT NULL,
  create_user INT DEFAULT NULL,
  update_time DATE DEFAULT NULL,
  update_user INT DEFAULT NULL
);

CREATE TABLE job_reviews (
  id INT NOT NULL IDENTITY,
  job_id INT DEFAULT NULL,
  bidder_id INT DEFAULT NULL,
  completed_on_time INT DEFAULT NULL,
  completed_on_budget INT DEFAULT NULL,
  rating INT DEFAULT NULL,
  comments VARCHAR(255) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  create_user INT DEFAULT NULL,
  update_time DATE DEFAULT NULL,
  update_user INT DEFAULT NULL
);

INSERT INTO users (id, username, password) VALUES (1, 'user', '1234');
INSERT INTO roles (id, name) VALUES (1, 'USER');
INSERT INTO users_roles (user_id, role_id) VALUES (1,1);
INSERT INTO profiles (id, user_id) VALUES (1, 1);