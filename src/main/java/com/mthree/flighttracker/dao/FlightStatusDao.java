package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightStatusDao extends JpaRepository<FlightStatus, Integer> {

    @Nullable
    @Query("SELECT f FROM flight_status f WHERE id = ?1")
    FlightStatus getFlightStatus(int id);

    @Nullable
    @Query("SELECT f FROM flight_status f WHERE status = ?1")
    FlightStatus getFlightStatus(String status);
}