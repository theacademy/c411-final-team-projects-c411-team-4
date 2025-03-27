package com.mthree.flighttracker.model;

import jakarta.persistence.*;

@Entity
public class FlightStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "status")
    private String status;

    public FlightStatus() {}

    public int getId(){return id;}

    public FlightStatus setId(int id) {
        this.id = id;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public FlightStatus setStatus(String status) {
        this.status = status;
        return this;
    }
}