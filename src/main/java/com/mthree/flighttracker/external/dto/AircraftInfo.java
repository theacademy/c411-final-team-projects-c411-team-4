package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AircraftInfo {
    private String registration;
    private String iata;
    private String icao;
    private String icao24;
}
