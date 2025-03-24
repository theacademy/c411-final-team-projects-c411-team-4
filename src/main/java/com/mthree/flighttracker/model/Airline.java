package com.mthree.flighttracker.model;

public class Airline {
    private int id;
    private String name;
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
