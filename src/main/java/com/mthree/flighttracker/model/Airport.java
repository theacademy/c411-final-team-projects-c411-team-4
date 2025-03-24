package com.mthree.flighttracker.model;

import java.math.BigDecimal;

public class Airport {
    private int id;
    private String name;
    private String code;
    private BigDecimal latitude;
    private BigDecimal longitude;

    public Airport() {}

    public Airport setId(int id) {
        this.id = id;
        return this;
    }

    public Airport setCode(String code) {
        this.code = code;
        return this;
    }

    public Airport setName(String name) {
        this.name = name;
        return this;
    }

    public Airport setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
        return this;
    }

    public Airport setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }
}
