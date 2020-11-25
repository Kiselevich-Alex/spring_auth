CREATE DATABASE IF NOT EXISTS Auth;
USE Auth;

DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS UserRole;
DROP TABLE IF EXISTS Role;

CREATE TABLE User (
	userId INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(50) NOT NULL,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(60) NOT NULL,
    lastPasswordResetDate DATE,
    enabled BOOLEAN
);

CREATE TABLE UserRole (
	userId INT UNSIGNED,
	roleId INT UNSIGNED
);

CREATE TABLE Role (
	roleId INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
	roleName VARCHAR(50) NOT NULL
);

ALTER TABLE UserRole ADD FOREIGN KEY (userId) REFERENCES User(userId) on update cascade on delete cascade;
ALTER TABLE UserRole ADD FOREIGN KEY (roleId) REFERENCES Role(roleId) on update cascade on delete cascade;