package com.mthree.flighttracker.service;

import com.mthree.flighttracker.dao.AirlineDao;
import com.mthree.flighttracker.dao.AirportDao;
import com.mthree.flighttracker.dao.FlightDao;
import com.mthree.flighttracker.dao.FlightStatusDao;
import com.mthree.flighttracker.helper.CoordinateHelper;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightServiceInterface {
    private FlightDao flightDao;
    private FlightStatusDao flightStatusDao;
    private AirlineDao airlineDao;
    private AirportDao airportDao;




    @Autowired
    FlightServiceImpl(FlightDao flightDao, FlightStatusDao flightStatusDao, AirlineDao airlineDao, AirportDao airportDao) {
        this.flightStatusDao = flightStatusDao;
        this.flightDao = flightDao;
        this.airlineDao = airlineDao;
        this.airportDao = airportDao;
    }


    @Override
    public List<Flight> getAllFlights() {
        return flightDao.getAllFlights();
    }

    public Page<Flight> findAll(Pageable pageable) {
        return flightDao.findAll(pageable);
    }

    @Override
    public List<Flight> getFlightsByAirline(Airline airline) {
        return flightDao.getFlightsByAirline(airline);
    }

    @Override
    public List<Flight> getFlightsByDate(LocalDateTime date) {
        return flightDao.getFlightsByDate(date);
    }

    @Override
    public List<Flight> getFlightsByAirport(Airport airport) {
        return flightDao.getFlightsByAirport(airport);
    }

    @Override
    public List<Flight> getFlightsByStatus(FlightStatus status) {
        return flightDao.getFlightsByStatus(status);
    }

    public Page<Flight> getFlightsByStatus(String status, Pageable pageable) {
        FlightStatus flightStatus = flightStatusDao.getFlightStatus(status);
        return flightDao.getFlightsByStatus(flightStatus, pageable);
    }

    @Override
    public Flight getFlightById(int id) {
        Flight flight = flightDao.getFlightById(id);

        // For testing purposes
        if (flight == null) {
            flight = new Flight();

            Airport airport  = new Airport();
            airport.setName("Doesn't Exist");
            airport.setCode("000");
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));

            Airline airline = new Airline();
            airline.setName("Doesn't exist");
            airline.setCode("000");

            flight.setAirline(airline);
            flight.setArrAirport(airport);
            flight.setDepAirport(airport);
            flight.setNumber(0);
            flight.setLatitude(CoordinateHelper.createCoord("000.000000"));
            flight.setLongitude(CoordinateHelper.createCoord("000.000000"));
            flight.setScheduledArrival(LocalDateTime.now());
            flight.setScheduledDeparture(LocalDateTime.now());
        }

        return flight;
    }

    @Override
    public Flight getFlightByNumber(short number, Airline airline) {
        Flight flight = flightDao.getFlightByNumber(number, airline);

        // For testing purposes
        if (flight == null) {
            flight = new Flight();

            Airport airport  = new Airport();
            airport.setName("Doesn't Exist");
            airport.setCode("000");
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));

            flight.setAirline(airline);
            flight.setArrAirport(airport);
            flight.setDepAirport(airport);
            flight.setNumber(0);
            flight.setLatitude(CoordinateHelper.createCoord("000.000000"));
            flight.setLongitude(CoordinateHelper.createCoord("000.000000"));
            flight.setScheduledArrival(LocalDateTime.now());
            flight.setScheduledDeparture(LocalDateTime.now());
        }

        return flight;
    }

    @Override
    public Airline getAirlineByCode(String code) {
        return airlineDao.getAirlineByCode(code);
    }

    @Override
    public Flight getLatestFlightByNumber(short number, Airline airline) {
        return flightDao.findFirstByNumberAndAirlineOrderByScheduledDepartureDesc(number, airline);
    }

    public Page<Flight> findByNumber(short number, Pageable pageable) {
        return flightDao.findByNumber(number, pageable);
    }

    public Optional<Flight> findByNumber (int number) {
        return flightDao.findByNumber(number);
    }

    public Page<Airport> findAllAirports(Pageable pageable) {
        return airportDao.findAll(pageable);
    }
}
