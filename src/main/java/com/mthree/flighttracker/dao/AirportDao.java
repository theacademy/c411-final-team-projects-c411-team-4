package com.mthree.flighttracker.dao;

import com.mthree.flighttracker.model.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportDao extends JpaRepository<Airport, Integer> {
    @Query("SELECT a FROM Airport a")
    public List<Airport> getAllAirports();
    @Query("SELECT a FROM Airport a")
    Page<Airport> findAllAirports(Pageable pageable);


    @Query("SELECT a FROM Airport a WHERE a.code = ?1")
    public Airport getAirportByCode(String code);


    @Query("SELECT a FROM Airport a WHERE a.name = ?1")
    public Airport getAirportByName(String name);


    @Query("SELECT a FROM Airport a WHERE a.id = ?1")
    public Airport getAirportById(int id);
}

