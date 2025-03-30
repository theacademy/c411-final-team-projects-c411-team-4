package com.mthree.flighttracker.dto;

import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.FlightStatus;

import java.time.LocalDateTime;

public class FlightProjection {
    private int id;
    private FlightStatus status;
    private Airport depAirport;
    private Airport arrAirport;
    private int number;
    private Airline airline;
    private LocalDateTime schedArrival;
    private LocalDateTime schedDeparture;
    private LocalDateTime estArrival;
    private LocalDateTime estDeparture;

    // Constructor
    public FlightProjection(int id, FlightStatus status, Airport depAirport, Airport arrAirport, int number,
                            Airline airline, LocalDateTime schedArrival, LocalDateTime schedDeparture,
                            LocalDateTime estArrival, LocalDateTime estDeparture) {
        this.id = id;
        this.status = status;
        this.depAirport = depAirport;
        this.arrAirport = arrAirport;
        this.number = number;
        this.airline = airline;
        this.schedArrival = schedArrival;
        this.schedDeparture = schedDeparture;
        this.estArrival = estArrival;
        this.estDeparture = estDeparture;
    }

    public int getId() {
        return id;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public Airport getDepAirport() {
        return depAirport;
    }

    public Airport getArrAirport() {
        return arrAirport;
    }

    public int getNumber() {
        return number;
    }

    public Airline getAirlineName() {
        return airline;
    }

    public LocalDateTime getSchedArrival() {
        return schedArrival;
    }

    public LocalDateTime getSchedDeparture() {
        return schedDeparture;
    }

    public LocalDateTime getEstArrival() {
        return estArrival;
    }

    public LocalDateTime getEstDeparture() {
        return estDeparture;
    }
}
