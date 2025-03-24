package com.mthree.flighttracker.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Flight {
    private int id;
    private FlightStatus status;
    private Airport depAirport;
    private Airport arrAirport;
    private Airline airline;
    private int number;
    private LocalDateTime scheduledArrival;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime estimatedArrival;
    private LocalDateTime estimatedDeparture;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Flight() {}

    public Flight setId(int id) {
        this.id = id;
        return this;
    }

    public Flight setStatus(FlightStatus status) {
        this.status = status;
        return this;
    }

    public Flight setDepAirport(Airport depAirport) {
        this.depAirport = depAirport;
        return this;
    }

    public Flight setArrAirport(Airport arrAirport) {
        this.arrAirport = arrAirport;
        return this;
    }

    public Flight setAirline(Airline airline) {
        this.airline = airline;
        return this;
    }

    public Flight setNumber(int number) {
        this.number = number;
        return this;
    }

    public Flight setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
        return this;
    }

    public Flight setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
        return this;
    }

    public Flight setEstimatedArrival(LocalDateTime estimatedArrival) {
        this.estimatedArrival = estimatedArrival;
        return this;
    }

    public Flight setEstimatedDeparture(LocalDateTime estimatedDeparture) {
        this.estimatedDeparture = estimatedDeparture;
        return this;
    }

    public Flight setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public Flight setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
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

    public Airline getAirline() {
        return airline;
    }

    public int getNumber() {
        return number;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public LocalDateTime getEstimatedArrival() {
        return estimatedArrival;
    }

    public LocalDateTime getEstimatedDeparture() {
        return estimatedDeparture;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
