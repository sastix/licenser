INSERT INTO `tenant`(`id`, `name`,`description`,`level`,`parent_id`) VALUES (1, 'Test', 'Test', 0, 1);
INSERT INTO `access_code` VALUES (1, 'test', 1, 1.0, 1500, FALSE, 1, FALSE);
INSERT INTO `access_code` VALUES (2, 'activated', 1, 1.0, 1500, TRUE, 1, FALSE);
INSERT INTO `global_role` VALUES (1, 'Test', 'Test description');
INSERT INTO `user_global_role`(`user_id`, `global_role_id`) VALUES (1,1);
INSERT INTO `user_access_code`(`user_id`, `access_code_id`, `activation_date`) VALUES (1, 2, NOW());