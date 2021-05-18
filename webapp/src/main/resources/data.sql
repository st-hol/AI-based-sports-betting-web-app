INSERT IGNORE INTO role (id, name_role)
 VALUES
 ('1', 'USER'),
 ('2', 'ADMIN');

# upsert
INSERT IGNORE INTO bet (id, type) VALUES ('1', '0');
INSERT IGNORE INTO bet (id, type) VALUES ('2', '1');
INSERT IGNORE INTO bet (id, type) VALUES ('3', '2');
INSERT IGNORE INTO bet (id, type) VALUES ('4', '3');
INSERT IGNORE INTO bet (id, type) VALUES ('5', '4');
INSERT IGNORE INTO bet (id, type) VALUES ('6', '5');
INSERT IGNORE INTO bet (id, type) VALUES ('7', '6');
INSERT IGNORE INTO bet (id, type) VALUES ('8', '7');
INSERT IGNORE INTO bet (id, type) VALUES ('1', '0');


INSERT IGNORE INTO parameters_area (name, value, description)
VALUES
('onlyStat', 'false', '');


INSERT IGNORE INTO `user` (`id`, `account_number`, `balance`, `birth`, `currency`, `email`, `enabled`, `name`, `password`)
VALUES ('1', '123', '999999', '2000-10-22', '0', 'admin@gmail.com', '0', 'admin', '$2a$10$IinfdWPgPR3P4iY8PXlx/usRTHty2J3k16hi7qwf5xaA8lCpmzpCq');
INSERT IGNORE  INTO `users_has_roles` (`role_id`, `user_id`) VALUES ('2', '1');
