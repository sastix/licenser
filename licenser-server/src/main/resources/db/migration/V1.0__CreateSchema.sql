DROP TABLE IF EXISTS `tenant_config`;
DROP TABLE IF EXISTS `user_global_role`;
DROP TABLE IF EXISTS `global_role`;
DROP TABLE IF EXISTS `package_access_code`;
DROP TABLE IF EXISTS `package`;
DROP TABLE IF EXISTS `access_code`;
DROP TABLE IF EXISTS `tenant`;
DROP TABLE IF EXISTS `user_access_code`;


CREATE TABLE IF NOT EXISTS `tenant` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(255),
  `level` INT(15),
  `parent_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`parent_id`) REFERENCES `tenant`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `tenant_config` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `component` VARCHAR(8),
  `name` VARCHAR(64),
  `data` TEXT,
  `tenant_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`tenant_id`) REFERENCES `tenant`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `access_code` (
  `id` BIGINT NOT NULL AUTO_INCREMENT UNIQUE,
  `access_code` VARCHAR(127) NOT NULL,
  `level` INT(15) NOT NULL,
  `price` DECIMAL(20,4),
  `duration` INT(15) NOT NULL,
  `is_activated` BIT DEFAULT NULL,
  `tenant_id` INT NOT NULL,
  `deleted` BIT DEFAULT FALSE,
  PRIMARY KEY (`id`),
  UNIQUE KEY `access_code_tenant_unique` (`access_code`,`tenant_id`),
  FOREIGN KEY (`tenant_id`) REFERENCES `tenant`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `global_role` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `role_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(255),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `user_global_role` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `user_id` BIGINT NOT NULL,
  `activation_date` DATETIME ,
  `global_role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`global_role_id`) REFERENCES `global_role`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `package` (
  `id` INT(11) NOT NULL AUTO_INCREMENT UNIQUE,
  `name` VARCHAR(255),
  `category` VARCHAR(64),
  `description` VARCHAR(255),
  `price` DECIMAL(20,4),
  `duration` INT(11),
  `max_users` INT(11),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `package_access_code` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `package_id` INT(11) NOT NULL,
  `access_code_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`package_id`)
    REFERENCES `package`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`access_code_id`)
    REFERENCES `access_code`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `max_users_by_package_global_role` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `package_id` INT(11) NOT NULL,
  `global_role_id` INT NOT NULL,
  `max_allowed` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`package_id`)
    REFERENCES `package`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`global_role_id`)
    REFERENCES `global_role`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `user_access_code` (
  `id` INT NOT NULL AUTO_INCREMENT UNIQUE,
  `user_id` BIGINT NOT NULL,
  `access_code_id` BIGINT NOT NULL,
  `activation_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`access_code_id`) REFERENCES `access_code`(`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
