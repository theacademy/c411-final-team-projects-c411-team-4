package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.model.FlightStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)

public class FlightStatusDaoTest {
    @Autowired
    FlightStatusDao flightStatusDao;

    // flight status by ID
    @Test
    public void getFlightStatus() {
        //checks flight status with ID1, if ID exists or not on db
        FlightStatus status = flightStatusDao.getFlightStatus(1);
        assertNotNull(status);
        assertEquals("On Time", status.getStatus(), "Status for ID 1 should be 'On Time'" );

        // if ID is not found/doesn't exist on db, return null
        FlightStatus notFound = flightStatusDao.getFlightStatus(50);
        assertNull(notFound, "Return null");
    }

    // test for valid status
    @Test
    public void testGetFlightStatus() {
        // valid status
        FlightStatus valid = flightStatusDao.getFlightStatus("Cancelled");
        assertNotNull(valid, "'Cancelled' status exists");
        assertEquals("Cancelled", valid.getStatus());

        //invalid status
        FlightStatus invalid = flightStatusDao.getFlightStatus("Status invalid");
        assertNull(invalid, "No flight should be found for invalid status");
    }

    // add a new status

}