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
public class AirlineDAOTest {
    @Autowired
    AirlineDao airlineDao;

    @Test
    @Sql(scripts = {"/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetKnownAirlines() {
        final List<Airline> allAirlines = airlineDao.getAllAirlines();
        assertEquals(5, allAirlines.size());

        final Airline deltaAirline = airlineDao.getAirlineByCode("DL");
        assertNotNull(deltaAirline);
        assertNotEquals(0, deltaAirline.getId());
        assertTrue(deltaAirline.getName().toLowerCase().contains("delta"));
    }

    @Test
    @Sql(scripts = {"/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAddAirline() {
        final Airline kyleAir = new Airline().setCode("KY").setName("Kyle's Airline Intl");
        System.out.println(airlineDao.save(kyleAir));
    }
}
