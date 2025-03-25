package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightDao extends JpaRepository<Flight, Integer> {

    List<Flight> findAll();

    Flight findById(int id);

    Flight findByNumber(short number, Airline airline);

    List<Flight> findByAirline(Airline airline);

    List<Flight> findFlightsByDate(LocalDateTime date);

    List<Flight> findFlightsByAirport(Airport airport);

    List<Flight> findFlightsByStatus(FlightStatus status);

    default void editFlight(Flight flight){
        save(flight);
    }

    @Modifying
    @Transactional
    void removeFlight(Flight flight);
}