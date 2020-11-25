USE AuthTest;
SET foreign_key_checks=0;

DELETE FROM AuthTest.User WHERE userId <> 0;
ALTER TABLE AuthTest.User AUTO_INCREMENT = 1;
INSERT INTO AuthTest.User( userName, firstName, lastName, email, password, lastPasswordResetDate, enabled) VALUES ('Kiselevich','Alexander','Kiselevich','sasha.kiselevich.by@gmail.com','$2y$04$.BQVG/Z8/cPVkmmSkqPPA.vxXm/ebwXMpOh7iHToDoJ3/NeMQE136','2019-11-02', true);
INSERT INTO AuthTest.User( userName, firstName, lastName, email, password, lastPasswordResetDate, enabled) VALUES ('123user','Ivan','Kiselevich','kiselevich.by@gmail.com','$2y$04$7na0xWX1v.bVonuHxH/PhudZDlzX93MSSkh0Ar8atsbKxRsJGPRYO','2019-11-02', true);

DELETE FROM AuthTest.Role WHERE roleId <> 0;
ALTER TABLE AuthTest.Role AUTO_INCREMENT=1;
INSERT INTO AuthTest.Role(roleName) VALUES ("USER");
INSERT INTO AuthTest.Role(roleName) VALUES ("ADMIN");
INSERT INTO AuthTest.Role(roleName) VALUES ("MANAGER");

DELETE FROM AuthTest.UserRole WHERE userId <> 0;
INSERT INTO AuthTest.UserRole(userId, roleId) VALUES (1, 1);
INSERT INTO AuthTest.UserRole(userId, roleId) VALUES (1, 2);
INSERT INTO AuthTest.UserRole(userId, roleId) VALUES (1, 3);
INSERT INTO AuthTest.UserRole(userId, roleId) VALUES (2, 1);

SET foreign_key_checks=1;