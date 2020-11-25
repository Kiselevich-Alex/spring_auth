USE Auth;
SET foreign_key_checks=0;

DELETE FROM Auth.User WHERE userId <> 0;
ALTER TABLE Auth.User AUTO_INCREMENT = 1;
INSERT INTO Auth.User( userName, firstName, lastName, email, password, lastPasswordResetDate, enabled) VALUES ('Kiselevich','Alexander','Kiselevich','sasha.kiselevich.by@gmail.com','$2y$04$.BQVG/Z8/cPVkmmSkqPPA.vxXm/ebwXMpOh7iHToDoJ3/NeMQE136','2019-11-02', true);
INSERT INTO Auth.User( userName, firstName, lastName, email, password, lastPasswordResetDate, enabled) VALUES ('123user','Ivan','Kiselevich','kiselevich.by@gmail.com','$2y$04$7na0xWX1v.bVonuHxH/PhudZDlzX93MSSkh0Ar8atsbKxRsJGPRYO','2019-11-02', true);

DELETE FROM Auth.Role WHERE roleId <> 0;
ALTER TABLE Auth.Role AUTO_INCREMENT=1;
INSERT INTO Auth.Role(roleName) VALUES ("USER");
INSERT INTO Auth.Role(roleName) VALUES ("ADMIN");
INSERT INTO Auth.Role(roleName) VALUES ("MANAGER");

DELETE FROM Auth.UserRole WHERE userId <> 0;
INSERT INTO Auth.UserRole(userId, roleId) VALUES (1, 1);
INSERT INTO Auth.UserRole(userId, roleId) VALUES (1, 2);
INSERT INTO Auth.UserRole(userId, roleId) VALUES (1, 3);
INSERT INTO Auth.UserRole(userId, roleId) VALUES (2, 1);

SET foreign_key_checks=1;