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
    Optional<Flight> findByNumber(int number);
    Page<Flight> findByNumber(short number, Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE f.airline = ?1")
    List<Flight> getFlightsByAirline(Airline airline);

    @Query("SELECT f FROM Flight f WHERE f.scheduledArrival = ?1")
    List<Flight> getFlightsByDate(LocalDateTime date);

    @Query("SELECT f FROM Flight f WHERE f.depAirport = ?1")
    List<Flight> getFlightsByAirport(Airport airport);

    @Query("SELECT f FROM Flight f WHERE f.status = ?1")
    List<Flight> getFlightsByStatus(FlightStatus status);
    Page<Flight> getFlightsByStatus(FlightStatus status, Pageable pageable);
}