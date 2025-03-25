package com.mthree.flighttracker.service;

import com.mthree.flighttracker.dao.FlightDao;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightServiceImpl implements FlightServiceInterface {

    private final FlightDao flightDao;

    public FlightServiceImpl(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightDao.findAll();
    }

    @Override
    public Flight getFlightById(int id) {
        return flightDao.findById(id);
    }

    @Override
    public Flight getFlightByNumber(short number, Airline airline) {
        return flightDao.findByNumber(number, airline);
    }

    @Override
    public List<Flight> getFlightsByAirline(Airline airline) {
        return flightDao.findByAirline(airline);
    }

    @Override
    public List<Flight> getFlightsByDate(LocalDateTime date) {
        return flightDao.findFlightsByDate(date);
    }

    @Override
    public List<Flight> getFlightsByAirport(Airport airport) {
        return flightDao.findFlightsByAirport(airport);
    }

    @Override
    public List<Flight> getFlightsByStatus(FlightStatus status) {
        return flightDao.findFlightsByStatus(status);
    }

    @Override
    public Flight editFlight(Flight flight) {
        flightDao.editFlight(flight);
        return flight;
    }

    @Override
    public void removeFlight(Flight flight) {
        flightDao.removeFlight(flight);
    }

}