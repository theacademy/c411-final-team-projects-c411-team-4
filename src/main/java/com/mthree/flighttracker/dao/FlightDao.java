package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {
    @Query("SELECT f FROM Flight f")
    List<Flight> getAllFlights();


    @Query("SELECT f FROM Flight f WHERE f.id = ?1")
    Flight getFlightById(int id);


    @Query("SELECT f FROM Flight f WHERE f.number = ?1 AND f.airline = ?2")
    Flight getFlightByNumber(short number, Airline airline);


    @Query("SELECT f FROM Flight f WHERE f.number = ?1 AND f.airline = ?2")
    Optional<Flight> getByNumberAirline(short number, Airline airline);
    Optional<Flight> findByNumber(short number);

    // Flight Number search function
    Page<Flight> findByNumber(short number, Pageable pageable);


    @Query("SELECT f FROM Flight f WHERE f.airline = ?1")
    List<Flight> getFlightsByAirline(Airline airline);

    // Airline Search function
    Page<Flight> getFlightsByAirline(Airline airline, Pageable pageable);


    @Query("SELECT f FROM Flight f WHERE f.scheduledArrival = ?1")
    List<Flight> getFlightsByDate(LocalDateTime date);


    @Query("SELECT f FROM Flight f WHERE f.depAirport = ?1")
    List<Flight> getFlightsByDepAirport(Airport airport);

    // Departure airport search function
    Page<Flight> getFlightsByDepAirport(Airport airport, Pageable pageable);


    @Query("SELECT f FROM Flight f WHERE f.arrAirport = ?1")
    List<Flight> getFlightsByArrAirport(Airport airport);

    // Arrival airport search function
    Page<Flight> getFlightsByArrAirport(Airport airport, Pageable pageable);


    @Query("SELECT f FROM Flight f WHERE f.arrAirport = ?1 OR f.depAirport = ?1")
    List<Flight> getFlightsByAirport(Airport airportCode);

    // General Aiport search function
    @Query("SELECT f FROM Flight f WHERE f.arrAirport = ?1 OR f.depAirport = ?1")
    Page<Flight> getFlightsByAirport(Airport airport, Pageable pageable);


    @Query("SELECT f FROM Flight f WHERE f.status = ?1")
    List<Flight> getFlightsByStatus(FlightStatus status);

    // Flight status search function
    Page<Flight> getFlightsByStatus(FlightStatus status, Pageable pageable);


    // Airline & Airport Search functions
    @Query("SELECT f FROM Flight f WHERE (f.arrAirport = ?1 OR f.depAirport = ?1) AND f.airline = ?2")
    Page<Flight> getFlightsByAirportAndAirline(Airport airport, Airline airline, Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE f.arrAirport = ?1 AND f.airline = ?2")
    Page<Flight> getFlightsByArrAirportAndAirline(Airport airport, Airline airline, Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE f.depAirport = ?1 AND f.airline = ?2")
    Page<Flight> getFlightsByDepAirportAndAirline(Airport airport, Airline airline, Pageable pageable);
}
