package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirlineDao extends JpaRepository<Airline, Integer> {

    @Query("SELECT a FROM Airline a")
    public List<Airline> getAllAirlines();

    @Query("SELECT a FROM Airline a WHERE a.name = ?1")
    public Airline getAirlineByName(String name);

    @Query("SELECT a FROM Airline a WHERE a.code = ?1")
    public Airline getAirlineByCode(String code);

    @Query("SELECT a FROM Airline a WHERE a.id = ?1")
    public Airline getAirlineById(int id);
}
