-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema c411_final_project_flights
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `c411_final_project_flights` ;

-- -----------------------------------------------------
-- Schema c411_final_project_flights
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `c411_final_project_flights` DEFAULT CHARACTER SET utf8mb3 ;
USE `c411_final_project_flights` ;

-- -----------------------------------------------------
-- Table `c411_final_project_flights`.`airline`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c411_final_project_flights`.`airline` ;

CREATE TABLE IF NOT EXISTS `c411_final_project_flights`.`airline` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `code` CHAR(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `abbreviation_UNIQUE` (`code` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `c411_final_project_flights`.`airport`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c411_final_project_flights`.`airport` ;

CREATE TABLE IF NOT EXISTS `c411_final_project_flights`.`airport` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `code` CHAR(3) NOT NULL,
  `latitude` DECIMAL(9,6) NOT NULL,
  `longitude` DECIMAL(9,6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `abbreviation_UNIQUE` (`code` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `c411_final_project_flights`.`flight_status`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c411_final_project_flights`.`flight_status` ;

CREATE TABLE IF NOT EXISTS `c411_final_project_flights`.`flight_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(12) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`status` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `c411_final_project_flights`.`flight`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c411_final_project_flights`.`flight` ;

CREATE TABLE IF NOT EXISTS `c411_final_project_flights`.`flight` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `status_id` INT NOT NULL,
  `airline_id` INT NOT NULL,
  `dep_airport_id` INT NOT NULL,
  `arr_airport_id` INT NOT NULL,
  `scheduled_departure` TIMESTAMP NOT NULL,
  `scheduled_arrival` TIMESTAMP NOT NULL,
  `estimated_arrival` TIMESTAMP NULL DEFAULT NULL,
  `estimated_departure` TIMESTAMP NULL DEFAULT NULL,
  `number` SMALLINT NOT NULL,
  `latitude` DECIMAL(9,6) NULL DEFAULT NULL,
  `longitude` DECIMAL(9,6) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_flight_status_id_idx` (`status_id` ASC) VISIBLE,
  INDEX `fk_flight_airline_id_idx` (`airline_id` ASC) VISIBLE,
  INDEX `fk_flight_dep_airport_id_idx` (`dep_airport_id` ASC) VISIBLE,
  INDEX `fk_flight_arr_aiport_id_idx` (`arr_airport_id` ASC) VISIBLE,
  CONSTRAINT `fk_flight_airline_id`
    FOREIGN KEY (`airline_id`)
    REFERENCES `c411_final_project_flights`.`airline` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_flight_arr_aiport_id`
    FOREIGN KEY (`arr_airport_id`)
    REFERENCES `c411_final_project_flights`.`airport` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_flight_dep_airport_id`
    FOREIGN KEY (`dep_airport_id`)
    REFERENCES `c411_final_project_flights`.`airport` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT `fk_flight_status_id`
    FOREIGN KEY (`status_id`)
    REFERENCES `c411_final_project_flights`.`flight_status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `c411_final_project_flights`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c411_final_project_flights`.`user` ;

CREATE TABLE IF NOT EXISTS `c411_final_project_flights`.`user` (
  `id` INT NOT NULL,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `c411_final_project_flights`.`user_flight`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c411_final_project_flights`.`user_flight` ;

CREATE TABLE IF NOT EXISTS `c411_final_project_flights`.`user_flight` (
  `user_id` INT NOT NULL,
  `flight_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `flight_id`),
  INDEX `fk_user_has_flight_flight1_idx` (`flight_id` ASC) VISIBLE,
  INDEX `fk_user_has_flight_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_flight_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `c411_final_project_flights`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_flight_flight1`
    FOREIGN KEY (`flight_id`)
    REFERENCES `c411_final_project_flights`.`flight` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;