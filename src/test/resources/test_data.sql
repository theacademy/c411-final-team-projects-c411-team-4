INSERT INTO flight_status (id, status) VALUES (1, 'On Time');
INSERT INTO flight_status (id, status) VALUES (2, 'Delayed');
INSERT INTO flight_status (id, status) VALUES (3, 'Cancelled');
INSERT INTO flight_status (id, status) VALUES (4, 'Landed');
INSERT INTO flight_status (id, status) VALUES (5, 'Diverted');

INSERT INTO airline (id, name, code) VALUES (1, 'American Airlines', 'AA');
INSERT INTO airline (id, name, code) VALUES (2, 'Delta Air Lines', 'DL');
INSERT INTO airline (id, name, code) VALUES (3, 'United Airlines', 'UA');
INSERT INTO airline (id, name, code) VALUES (4, 'Southwest Airlines', 'WN');
INSERT INTO airline (id, name, code) VALUES (5, 'JetBlue Airways', 'B6');

INSERT INTO airport (id, name, code, latitude, longitude) VALUES (1, 'John F. Kennedy Intl', 'JFK', 40.639751, -73.778925);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (2, 'Los Angeles Intl', 'LAX', 33.942536, -118.408075);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (3, 'O Hare Intl', 'ORD', 41.978603, -87.904842);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (4, 'Hartsfield-Jackson Atlanta Intl', 'ATL', 33.640728, -84.427700);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (5, 'San Francisco Intl', 'SFO', 37.618808, -122.375680);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (6, 'Seattle-Tacoma Intl', 'SEA', 47.449474, -122.309912);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (7, 'Denver Intl', 'DEN', 39.856096, -104.673738);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (8, 'Dallas/Fort Worth Intl', 'DFW', 32.896828, -97.037996);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (9, 'Miami Intl', 'MIA', 25.795865, -80.287046);
INSERT INTO airport (id, name, code, latitude, longitude) VALUES (10, 'Phoenix Sky Harbor Intl', 'PHX', 33.435036, -112.011719);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (1, 1, 1, 1, 2, TIMESTAMPADD(HOUR, 1, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 6, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 1, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 6, CURRENT_TIMESTAMP()), 1001, 40.639751, -73.778925);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (2, 2, 2, 3, 4, TIMESTAMPADD(HOUR, 2, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 4, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 5, CURRENT_TIMESTAMP()), 2002, 41.978603, -87.904842);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (3, 3, 3, 5, 6, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 5, CURRENT_TIMESTAMP()), NULL, NULL, 3003, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (4, 4, 4, 7, 8, TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 0, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, -2, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 0, CURRENT_TIMESTAMP()), 4004, 32.896828, -97.037996);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (5, 5, 5, 9, 10, TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 2, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, -1, CURRENT_TIMESTAMP()), TIMESTAMPADD(HOUR, 4, CURRENT_TIMESTAMP()), 5005, 34.567890, -118.123456);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (6, 1, 2, 2, 1, TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, 1, TIMESTAMPADD(HOUR, 5, CURRENT_TIMESTAMP())), NULL, NULL, 2101, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (7, 1, 1, 4, 3, TIMESTAMPADD(DAY, 1, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, 1, TIMESTAMPADD(HOUR, 2, CURRENT_TIMESTAMP())), NULL, NULL, 1102, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (8, 1, 3, 6, 5, TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, 2, TIMESTAMPADD(HOUR, 2, CURRENT_TIMESTAMP())), NULL, NULL, 3203, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (9, 1, 4, 8, 7, TIMESTAMPADD(DAY, 2, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, 2, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP())), NULL, NULL, 4304, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (10, 1, 5, 10, 9, TIMESTAMPADD(DAY, 3, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, 3, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP())), NULL, NULL, 5405, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (11, 4, 1, 1, 3, TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -1, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP())), TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -1, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP())), 1201, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (12, 4, 2, 4, 6, TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -1, TIMESTAMPADD(HOUR, 4, CURRENT_TIMESTAMP())), TIMESTAMPADD(DAY, -1, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -1, TIMESTAMPADD(HOUR, 4, CURRENT_TIMESTAMP())), 2302, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (13, 4, 3, 7, 9, TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -2, TIMESTAMPADD(HOUR, 5, CURRENT_TIMESTAMP())), TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -2, TIMESTAMPADD(HOUR, 5, CURRENT_TIMESTAMP())), 3403, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (14, 3, 4, 2, 10, TIMESTAMPADD(DAY, -2, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -2, TIMESTAMPADD(HOUR, 4, CURRENT_TIMESTAMP())), NULL, NULL, 4504, NULL, NULL);

INSERT INTO flight (id, status_id, airline_id, dep_airport_id, arr_airport_id, scheduled_departure, scheduled_arrival, estimated_departure, estimated_arrival, number, latitude, longitude)
VALUES (15, 4, 5, 5, 8, TIMESTAMPADD(DAY, -3, CURRENT_TIMESTAMP()), TIMESTAMPADD(DAY, -3, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP())), TIMESTAMPADD(DAY, -3, TIMESTAMPADD(MINUTE, 15, CURRENT_TIMESTAMP())), TIMESTAMPADD(DAY, -3, TIMESTAMPADD(HOUR, 3, CURRENT_TIMESTAMP())), 5105, NULL, NULL);
