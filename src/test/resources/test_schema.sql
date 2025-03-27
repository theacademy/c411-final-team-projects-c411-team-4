-- -----------------------------------------------------
-- Schema c411_final_project_test_flights
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema c411_final_project_test_flights
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS c411_final_project_test_flights;
SET SCHEMA c411_final_project_test_flights;

-- -----------------------------------------------------
-- Table airline
-- -----------------------------------------------------
DROP TABLE IF EXISTS airline;

CREATE TABLE IF NOT EXISTS airline (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  code CHAR(2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT name_UNIQUE UNIQUE (name),
  CONSTRAINT abbreviation_UNIQUE UNIQUE (code)
);

-- -----------------------------------------------------
-- Table airport
-- -----------------------------------------------------
DROP TABLE IF EXISTS airport;

CREATE TABLE IF NOT EXISTS airport (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(50) NOT NULL,
  code CHAR(3) NOT NULL,
  latitude DECIMAL(9,6) NOT NULL,
  longitude DECIMAL(9,6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT airport_name_UNIQUE UNIQUE (name),
  CONSTRAINT airport_abbreviation_UNIQUE UNIQUE (code)
);

-- -----------------------------------------------------
-- Table flight_status
-- -----------------------------------------------------
DROP TABLE IF EXISTS flight_status;

CREATE TABLE IF NOT EXISTS flight_status (
  id INT NOT NULL AUTO_INCREMENT,
  status VARCHAR(12) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT status_UNIQUE UNIQUE (status)
);

-- -----------------------------------------------------
-- Table flight
-- -----------------------------------------------------
DROP TABLE IF EXISTS flight;

CREATE TABLE IF NOT EXISTS flight (
  id INT NOT NULL AUTO_INCREMENT,
  status_id INT NOT NULL,
  airline_id INT NOT NULL,
  dep_airport_id INT NOT NULL,
  arr_airport_id INT NOT NULL,
  scheduled_departure TIMESTAMP NOT NULL,
  scheduled_arrival TIMESTAMP NOT NULL,
  estimated_arrival TIMESTAMP,
  estimated_departure TIMESTAMP,
  number SMALLINT NOT NULL,
  latitude DECIMAL(9,6),
  longitude DECIMAL(9,6),
  PRIMARY KEY (id),
  CONSTRAINT fk_flight_status_id
    FOREIGN KEY (status_id)
    REFERENCES flight_status (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_flight_airline_id
    FOREIGN KEY (airline_id)
    REFERENCES airline (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_flight_dep_airport_id
    FOREIGN KEY (dep_airport_id)
    REFERENCES airport (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE,
  CONSTRAINT fk_flight_arr_aiport_id
    FOREIGN KEY (arr_airport_id)
    REFERENCES airport (id)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
);

-- -----------------------------------------------------
-- Table `user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `email_UNIQUE` UNIQUE (`email`)
);

-- -----------------------------------------------------
-- Table `user_flight`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_flight`;

CREATE TABLE IF NOT EXISTS `user_flight` (
  `user_id` INT NOT NULL,
  `flight_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `flight_id`),
  CONSTRAINT `fk_user_has_flight_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_flight_flight1`
    FOREIGN KEY (`flight_id`)
    REFERENCES `flight` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE INDEX `fk_user_has_flight_flight1_idx` ON `user_flight` (`flight_id`);
CREATE INDEX `fk_user_has_flight_user1_idx` ON `user_flight` (`user_id`);

-- -----------------------------------------------------
-- Table `user_search_history`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `user_search_history`;

CREATE TABLE IF NOT EXISTS `user_search_history` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `airline_id` INT DEFAULT NULL,
  `dep_airport_id` INT DEFAULT NULL,
  `arr_airport_id` INT DEFAULT NULL,
  `sole_airport_id` INT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_search_history_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_search_history_airline`
    FOREIGN KEY (`airline_id`)
    REFERENCES `airline` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_search_history_dep_airport`
    FOREIGN KEY (`dep_airport_id`)
    REFERENCES `airport` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_search_history_arr_airport`
    FOREIGN KEY (`arr_airport_id`)
    REFERENCES `airport` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_search_history_sole_airport`
    FOREIGN KEY (`sole_airport_id`)
    REFERENCES `airport` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE
);

CREATE INDEX `fk_user_search_history_user_idx` ON `user_search_history` (`user_id`);
CREATE INDEX `fk_user_search_history_airline_idx` ON `user_search_history` (`airline_id`);
CREATE INDEX `fk_user_search_history_dep_airport_idx` ON `user_search_history` (`dep_airport_id`);
CREATE INDEX `fk_user_search_history_arr_airport_idx` ON `user_search_history` (`arr_airport_id`);
CREATE INDEX `fk_user_search_history_sole_airport_idx` ON `user_search_history` (`sole_airport_id`);