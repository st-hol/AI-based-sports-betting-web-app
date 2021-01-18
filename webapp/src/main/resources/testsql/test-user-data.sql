INSERT INTO role (`id`, `name_role`) VALUES ('0', 'ADMIN');
INSERT INTO role (`id`, `name_role`) VALUES ('1', 'USER');


INSERT INTO `user` (`id`, `account_number`, `balance`, `birth`, `currency`, `email`, `enabled`, `name`, `password`)
    VALUES ('1', '123', '123', '2020-10-22', '0', 'test1@gmail.com', '0', 'user1', '$2a$10$IinfdWPgPR3P4iY8PXlx/usRTHty2J3k16hi7qwf5xaA8lCpmzpCq');
INSERT INTO `user` (`id`, `account_number`, `balance`, `birth`, `currency`, `email`, `enabled`, `name`, `password`)
    VALUES ('2', '123', '123', '2020-10-22', '0', 'test2@gmail.com', '0', 'user2', '$2a$10$IinfdWPgPR3P4iY8PXlx/usRTHty2J3k16hi7qwf5xaA8lCpmzpCq');
INSERT INTO `user` (`id`, `account_number`, `balance`, `birth`, `currency`, `email`, `enabled`, `name`, `password`)
    VALUES ('3', '123', '123', '2020-10-22', '0', 'test3@gmail.com', '0', 'user3', '$2a$10$IinfdWPgPR3P4iY8PXlx/usRTHty2J3k16hi7qwf5xaA8lCpmzpCq');
INSERT INTO `user` (`id`, `account_number`, `balance`, `birth`, `currency`, `email`, `enabled`, `name`, `password`)
    VALUES ('4', '123', '123', '2020-10-22', '0', 'admin@gmail.com', '0', 'user4', '$2a$10$IinfdWPgPR3P4iY8PXlx/usRTHty2J3k16hi7qwf5xaA8lCpmzpCq');
INSERT INTO `user` (`id`, `account_number`, `balance`, `birth`, `currency`, `email`, `enabled`, `name`, `password`)
    VALUES ('5', '123', '123', '2020-10-22', '0', 'client@gmail.com', '0', 'user5', '$2a$10$IinfdWPgPR3P4iY8PXlx/usRTHty2J3k16hi7qwf5xaA8lCpmzpCq');

INSERT  INTO `users_has_roles` (`role_id`, `user_id`) VALUES ('1', '1');
INSERT  INTO `users_has_roles` (`role_id`, `user_id`) VALUES ('1', '2');
INSERT  INTO `users_has_roles` (`role_id`, `user_id`) VALUES ('1', '3');
INSERT  INTO `users_has_roles` (`role_id`, `user_id`) VALUES ('0', '4');
INSERT  INTO `users_has_roles` (`role_id`, `user_id`) VALUES ('1', '5');

