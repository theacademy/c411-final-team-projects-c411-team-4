package com.mthree.flighttracker.model;

import jakarta.persistence.*;

@Entity
public class Airline {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 2, nullable = false)
    private String code;

    public Airline() {}

    public Airline setId(int id) {
        this.id = id;
        return this;
    }

    public Airline setName(String name) {
        this.name = name;
        return this;
    }

    public Airline setCode(String code) {
        this.code = code;
        return this;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
