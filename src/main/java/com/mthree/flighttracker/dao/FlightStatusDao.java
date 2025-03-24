package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.FlightStatus;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightStatusDao extends JpaRepository<FlightStatus, Integer> {
    @Nullable
    @Query("SELECT f FROM FlightStatus f WHERE id = ?1")
    FlightStatus getFlightStatus(int id);

    @Nullable
    @Query("SELECT f FROM FlightStatus f WHERE f.status = ?1")
    FlightStatus getFlightStatus(String status);
}