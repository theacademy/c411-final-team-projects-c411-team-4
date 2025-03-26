package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.model.Airport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AirportDaoTest {
    @Autowired
    AirportDao airportDao;

    @Test
    public void testgetAllAirports() {
        final List<Airport> airports = airportDao.getAllAirports();
        assertEquals(10, airports.size());
    }

    @Test
    public void testGetAirportByCode() {
        final Airport losAngeles = airportDao.getAirportByCode("LAX");
        assertNotNull(losAngeles, "LAX airport should not be null");
        assertNotEquals(0,losAngeles.getId());
    }

    @Test
    public void testGetAirportById() {
        //Id 1 should be JFK
        final Airport airport = airportDao.getAirportById(1);
        assertNotNull(airport);
        assertEquals("JFK", airport.getCode());
    }

    //do a test for adding an airport
    @Test
    public void testAddAirport(){
        Airport airport = new Airport();
        airport.setName("Montreal Trudeau Intl");
        airport.setCode("YUL");
        airport.setLatitude(BigDecimal.valueOf(45.4580));
        airport.setLongitude(BigDecimal.valueOf(-73.7497));

        airport = airportDao.save(airport);

        assertNotEquals(0,airport.getId());
        Airport fetched = airportDao.getAirportByCode("YUL");
        assertNotNull(fetched);
        assertEquals("Montreal Trudeau Intl", fetched.getName());
    }
}
