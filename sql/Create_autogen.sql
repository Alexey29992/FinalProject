-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema radb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `radb` ;

-- -----------------------------------------------------
-- Schema radb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `radb` DEFAULT CHARACTER SET utf8 ;
USE `radb` ;

-- -----------------------------------------------------
-- Table `radb`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`role_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(20) NOT NULL,
  `password` VARCHAR(20) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `fk_user_role1_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `radb`.`role` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`client`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`client` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_user1`
    FOREIGN KEY (`id`)
    REFERENCES `radb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status_name` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`status_name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`master`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`master` (
  `id` INT NOT NULL,
  INDEX `fk_master_copy1_user1_idx` (`id` ASC) VISIBLE,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_master_user1`
    FOREIGN KEY (`id`)
    REFERENCES `radb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`request`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`request` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `creation_date` DATETIME NOT NULL,
  `description` TEXT NULL,
  `completion_date` DATETIME NULL,
  `user_review` TEXT NULL,
  `cancel_reason` TEXT NULL,
  `price` INT NULL,
  `client_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `master_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_request_client1_idx` (`client_id` ASC) VISIBLE,
  INDEX `fk_request_status1_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_request_master1_idx` (`master_id` ASC) VISIBLE,
  CONSTRAINT `fk_request_client1`
    FOREIGN KEY (`client_id`)
    REFERENCES `radb`.`client` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_request_status1`
    FOREIGN KEY (`status_id`)
    REFERENCES `radb`.`status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_request_master1`
    FOREIGN KEY (`master_id`)
    REFERENCES `radb`.`master` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`wallet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`wallet` (
  `balance` INT NOT NULL DEFAULT 0,
  `id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_wallet_client1_idx` (`id` ASC) VISIBLE,
  CONSTRAINT `fk_wallet_client1`
    FOREIGN KEY (`id`)
    REFERENCES `radb`.`client` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `radb`.`payment_record`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `radb`.`payment_record` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL,
  `destination` VARCHAR(50) NULL,
  `sum` INT NOT NULL,
  `wallet_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_payment_record_wallet1_idx` (`wallet_id` ASC) VISIBLE,
  CONSTRAINT `fk_payment_record_wallet1`
    FOREIGN KEY (`wallet_id`)
    REFERENCES `radb`.`wallet` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `radb`.`role`
-- -----------------------------------------------------
START TRANSACTION;
USE `radb`;
INSERT INTO `radb`.`role` (`id`, `role_name`) VALUES (1, 'CLIENT');
INSERT INTO `radb`.`role` (`id`, `role_name`) VALUES (2, 'MASTER');
INSERT INTO `radb`.`role` (`id`, `role_name`) VALUES (3, 'MANAGER');
INSERT INTO `radb`.`role` (`id`, `role_name`) VALUES (4, 'ADMIN');

COMMIT;


-- -----------------------------------------------------
-- Data for table `radb`.`status`
-- -----------------------------------------------------
START TRANSACTION;
USE `radb`;
INSERT INTO `radb`.`status` (`id`, `status_name`) VALUES (1, 'NEW');
INSERT INTO `radb`.`status` (`id`, `status_name`) VALUES (2, 'WAIT_FOR_PAYMENT');
INSERT INTO `radb`.`status` (`id`, `status_name`) VALUES (3, 'PAID');
INSERT INTO `radb`.`status` (`id`, `status_name`) VALUES (4, 'CANCELLED');
INSERT INTO `radb`.`status` (`id`, `status_name`) VALUES (5, 'IN_PROCESS');
INSERT INTO `radb`.`status` (`id`, `status_name`) VALUES (6, 'DONE');

COMMIT;

