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
