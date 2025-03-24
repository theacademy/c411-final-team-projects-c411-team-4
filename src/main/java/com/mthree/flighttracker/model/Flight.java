package com.mthree.flighttracker.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Flight {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne
    private FlightStatus status;

    @ManyToOne
    private Airport depAirport;

    @ManyToOne
    private Airport arrAirport;

    @ManyToOne
    private Airline airline;

    @Column(name = "number")
    private int number;

    @Column(name = "scheduled_arrival")
    private LocalDateTime scheduledArrival;

    @Column(name = "scheduled_departure")
    private LocalDateTime scheduledDeparture;

    @Column(name = "estimated_arrival")
    private LocalDateTime estimatedArrival;

    @Column(name = "estimated_departure")
    private LocalDateTime estimatedDeparture;

    @Column(name = "latitude")
    private BigDecimal latitude;

    @Column(name = "longitude")
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
