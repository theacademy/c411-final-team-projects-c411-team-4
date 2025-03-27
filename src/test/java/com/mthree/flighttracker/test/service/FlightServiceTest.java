package com.mthree.flighttracker.test.service;

import com.mthree.flighttracker.FlighttrackerApplication;
import com.mthree.flighttracker.dao.AirlineDao;
import com.mthree.flighttracker.dao.AirportDao;
import com.mthree.flighttracker.dao.FlightDao;
import com.mthree.flighttracker.helper.CoordinateHelper;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.service.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlighttrackerApplication.class)
@Sql(scripts = {"/test_clean_tables.sql", "/test_data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class FlightServiceTest {
    @Autowired
    private FlightServiceImpl flightService;

    @Autowired
    private AirlineDao airlineDao;

    @Autowired
    private AirportDao airportDao;


    @Test
    public void testGetAllFlights() {
        List<Flight> flightList = flightService.getAllFlights();
        assertNotNull(flightList);
        assertEquals(15, flightList.size());
    }

    @Test
    public void testGetFlightsByAirline() {
        Airline airline = airlineDao.getAirlineByCode("DL");
        List<Flight> flightListByAirline = flightService.getFlightsByAirline(airline);
        assertNotNull(flightListByAirline);
        assertEquals(3, flightListByAirline.size());
    }

    @Test
    public void testGetFlightById() {
        Flight flight = flightService.getFlightById(1);
        assertNotNull(flight);
        assertEquals(1001, flight.getNumber());
    }

    @Test
    public void testGetFlightByNumber() {
        Airline airline = airlineDao.getAirlineByCode("AA");
        Flight flight = flightService.getFlightByNumber((short)1001, airline);
        assertNotNull(flight);
        assertEquals(1, flight.getId());
    }

    @Test
    public void testGetFlightsByDate() {
        List<Flight> flightsListByDate = flightService.getFlightsByDate(flightService.getFlightById(1).getScheduledArrival());
        assertNotNull(flightsListByDate);
        assertEquals(1, flightsListByDate.size());
    }

    @Test
    public void testGetFlightsByStatus() {
        Flight flight = flightService.getFlightById(1);
        List<Flight> flightListByStatus = flightService.getFlightsByStatus(flight.getStatus());
        assertNotNull(flightListByStatus);
        assertEquals(6, flightListByStatus.size());
    }

    @Test
    public void testGetFlightsByAirport() {
        Airport airport = airportDao.getAirportById(1);
        List<Flight> flightListByAirport = flightService.getFlightsByAirport(airport);
        assertNotNull(flightListByAirport);
        assertEquals(3, flightListByAirport.size());
    }

    @Test
    public void testGetFlightByNumberInvalid() {
        Airline airline = airlineDao.getAirlineByCode("AA");
        Flight flight = flightService.getFlightByNumber((short)3001, airline);
        assertNotNull(flight);
        assertEquals(0, flight.getNumber());
        assertEquals(CoordinateHelper.createCoord("000.000000"), flight.getLatitude());
    }

    @Test
    public void testGetFlightByIdInvalid() {
        Flight flight = flightService.getFlightById(60);
        assertNotNull(flight);
        assertEquals(0, flight.getNumber());
        assertEquals(CoordinateHelper.createCoord("000.000000"), flight.getLatitude());
    }
}