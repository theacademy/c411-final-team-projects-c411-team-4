package com.mthree.flighttracker.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Airport {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 3, nullable = false)
    private String code;

    @Column(precision = 9, scale = 6, nullable = false)
    private BigDecimal latitude;

    @Column(precision = 9, scale = 6, nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return id == airport.id && Objects.equals(name, airport.name) && Objects.equals(code, airport.code) && Objects.equals(latitude, airport.latitude) && Objects.equals(longitude, airport.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, latitude, longitude);
    }
}
