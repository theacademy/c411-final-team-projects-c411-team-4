package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.model.Airline;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AirlineDAOTest {
    @Autowired
    AirlineDao airlineDao;

    @Test
    public void testGetAirlineByCode() {
        final Airline deltaAirline = airlineDao.getAirlineByCode("DL");
        assertNotNull(deltaAirline);
        assertNotEquals(0, deltaAirline.getId());
        assertTrue(deltaAirline.getName().toLowerCase().contains("delta"));
    }

    @Test
    public void testGetAllAirlines() {
        final List<Airline> airlines = airlineDao.getAllAirlines();
        assertEquals(5, airlines.size());
    }

    @Test
    public void testAddAirline() {
        Airline kyleAir = new Airline().setCode("KY").setName("Kyle's Airline Intl");
        kyleAir = airlineDao.save(kyleAir);
        assertNotEquals(0, kyleAir.getId());
        Airline willAir = new Airline().setCode("WC").setName("Will's Airline Intl");
        willAir = airlineDao.save(willAir);
        assertNotEquals(0, willAir.getId());
    }

    @Test
    public void testGetByAirlineName() {
        final Airline delta = airlineDao.getAirlineByName("Delta Air Lines");
        assertNotNull(delta);
        assertEquals("DL", delta.getCode());
    }

    @Test
    public void testGetAirlineById() {
        final Airline delta = airlineDao.getAirlineById(2);
        assertNotNull(delta);
        assertEquals("DL", delta.getCode());
    }
}
