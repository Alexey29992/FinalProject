CREATE SCHEMA IF NOT EXISTS radb DEFAULT CHARACTER SET utf8;
USE radb;

CREATE TABLE IF NOT EXISTS status (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(30) NOT NULL UNIQUE,
    INDEX status_status_name_idx (status_name)
);

CREATE TABLE IF NOT EXISTS role (
    id INT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(15) NOT NULL UNIQUE,
    INDEX role_role_name_idx (role_name)
);

CREATE TABLE IF NOT EXISTS user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(20) NOT NULL UNIQUE,
    password BINARY(32) NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        ON DELETE RESTRICT,
    INDEX user_login_idx (login)
);

CREATE TABLE IF NOT EXISTS client (
    id INT PRIMARY KEY,
    ph_number VARCHAR(16),
    balance INT NOT NULL DEFAULT 0,
    FOREIGN KEY (id)
        REFERENCES user (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS master (
    id INT PRIMARY KEY,
    FOREIGN KEY (id)
        REFERENCES user (id)
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS request (
    id INT PRIMARY KEY AUTO_INCREMENT,
    creation_date DATETIME NOT NULL,
    description TEXT NOT NULL,
    completion_date DATETIME NULL,
    user_review TEXT NULL,
    cancel_reason TEXT NULL,
    master_id INT NULL,
    price INT NULL,
    client_id INT NOT NULL,
    status_id INT NOT NULL,
    FOREIGN KEY (client_id)
        REFERENCES client (id)
        ON DELETE CASCADE,
    FOREIGN KEY (status_id)
        REFERENCES status (id)
        ON DELETE CASCADE,
    INDEX request_client_id_idx (client_id),
    INDEX request_status_id_idx (status_id),
    INDEX request_master_id_idx (master_id)
);

CREATE TABLE IF NOT EXISTS payment_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    date DATETIME NOT NULL,
    sum INT NOT NULL,
    destination VARCHAR(50) NULL,
    client_id INT NOT NULL,
    FOREIGN KEY (client_id)
        REFERENCES client (id)
        ON DELETE CASCADE,
    INDEX payment_record_client_id_idx (client_id)
);

INSERT INTO role (role_name) VALUES ('CLIENT');
INSERT INTO role (role_name) VALUES ('MASTER');
INSERT INTO role (role_name) VALUES ('MANAGER');
INSERT INTO role (role_name) VALUES ('ADMIN');

INSERT INTO status (status_name) VALUES ('NEW');
INSERT INTO status (status_name) VALUES ('WAIT_FOR_PAYMENT');
INSERT INTO status (status_name) VALUES ('PAID');
INSERT INTO status (status_name) VALUES ('CANCELLED');
INSERT INTO status (status_name) VALUES ('IN_PROCESS');
INSERT INTO status (status_name) VALUES ('DONE');

INSERT INTO user (login, password, role_id) VALUES ('admin', UNHEX(SHA2('admin', 256)), (SELECT id FROM role WHERE role_name = 'ADMIN'));