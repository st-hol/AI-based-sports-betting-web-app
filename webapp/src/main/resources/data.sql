INSERT IGNORE INTO role (id, name_role)
 VALUES
 ('1', 'USER'),
 ('2', 'ADMIN');

# upsert
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('1', '0');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('2', '1');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('3', '2');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('4', '3');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('5', '4');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('6', '5');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('7', '6');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('8', '7');
INSERT IGNORE INTO `ai_bet_app`.`bet` (`id`, `type`) VALUES ('1', '0');
