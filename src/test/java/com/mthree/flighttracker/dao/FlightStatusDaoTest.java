package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.FlighttrackerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class FlightStatusDaoTest {

    @Test
    void getFlightStatus() {
    }

    @Test
    void testGetFlightStatus() {
    }
}