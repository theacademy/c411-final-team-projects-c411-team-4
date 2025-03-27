package com.mthree.flighttracker.service;

import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface FlightServiceInterface {
    /**
     * Retrieves all flights in the system
     *
     * @return List of all flights
     */
    List<Flight> getAllFlights();


    /**
     * Gets flights by airline
     *
     * @param airline The airline to filter by
     * @return List of flights operated by the specified airline
     */
    List<Flight> getFlightsByAirline(Airline airline);


    /**
     * Gets flights by date
     *
     * @param date The date to filter by
     * @return List of flights scheduled on the specified date
     */
    List<Flight> getFlightsByDate(LocalDateTime date);


    /**
     * Gets flights by status
     * @param status The flight status to filter by
     * @return List of flights with the specified status
     */
    List<Flight> getFlightsByStatus(FlightStatus status);
    Page<Flight> getFlightsByStatus(String status, Pageable pageable);


    /**
     * Gets a specific flight by its ID
     *
     * @param id The flight ID
     * @return The flight if found, otherwise null
     */
    Flight getFlightById(int id);


    /**
     * Gets a flight by its number and airline
     *
     * @param number  The flight number
     * @param airline The airline
     * @return The flight if found, otherwise null
     */
    Flight getFlightByNumber(short number, Airline airline);

    /**
     * Gets an airline by its code
     *
     * @param code Airline Code
     * @return The airline if found, otherwise null
     */
    Airline getAirlineByCode(String code);

    /**
     * Get the latest flight by IATA Flight Number.
     * Flight numbers can be historically reused, even across airlines. This will get the latest.
     * @param number flight number
     * @param airline airline for the flight
     * @return latest flight, if it exists, otherwise null
     */
    Flight getLatestFlightByNumber(short number, Airline airline);
}
