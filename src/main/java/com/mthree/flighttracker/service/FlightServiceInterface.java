package com.mthree.flighttracker.service;

import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightServiceInterface {
    List<Flight> getAllFlights();

    List<Flight> getFlightsByAirline(Airline airline);
    List<Flight> getFlightsByDate(LocalDateTime date);
    List<Flight> getFlightsByAirport(Airport airport);
    List<Flight> getFlightsByStatus(FlightStatus status);

    Flight getFlightById(int id);
    Flight getFlightByNumber(short number, Airline airline);
}
