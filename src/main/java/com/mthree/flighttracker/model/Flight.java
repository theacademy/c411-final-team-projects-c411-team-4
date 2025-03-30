package com.mthree.flighttracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Flight {
    @Transient
    private int bearing; // NOT A DATABASE FIELD

    @Transient
    private boolean fromApi; // NOT A DATABASE FIELD

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private FlightStatus status;

    @ManyToOne
    @JoinColumn(name = "dep_airport_id", nullable = false)
    private Airport depAirport;

    @ManyToOne
    @JoinColumn(name = "arr_airport_id", nullable = false)
    private Airport arrAirport;

    @Column(name = "number", nullable = false)
    private int number;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(name = "scheduled_arrival", nullable = false)
    private LocalDateTime scheduledArrival;

    @Column(name = "scheduled_departure", nullable = false)
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

    public int getId() {
        return id;
    }

    public Flight setId(int id) {
        this.id = id;
        return this;
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

    public Airline getAirline() {
        return airline;
    }

    public LocalDateTime getSchedArrival() {
        return scheduledArrival;
    }

    public LocalDateTime getSchedDeparture() {
        return scheduledDeparture;
    }

    public LocalDateTime getEstArrival() {
        return estimatedArrival;
    }

    public LocalDateTime getEstDeparture() {
        return estimatedDeparture;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public Flight setFromApi(boolean fromApi) {
        this.fromApi = fromApi;
        return this;
    }

    public boolean isFromApi() {
        return fromApi;
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

    public Flight setNumber(int number) {
        this.number = number;
        return this;
    }

    public Flight setAirline(Airline airline) {
        this.airline = airline;
        return this;
    }

    public Flight setSchedArrival(LocalDateTime schedArrival) {
        this.scheduledArrival = schedArrival;
        return this;
    }

    public Flight setSchedDeparture(LocalDateTime schedDeparture) {
        this.scheduledDeparture = schedDeparture;
        return this;
    }

    public Flight setEstArrival(LocalDateTime estArrival) {
        this.estimatedArrival = estArrival;
        return this;
    }

    public Flight setEstDeparture(LocalDateTime estDeparture) {
        this.estimatedDeparture = estDeparture;
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

    public Flight setBearing(int bearing) {
        this.bearing = bearing;
        return this;
    }

    public int getBearing() {
        return bearing;
    }
}
