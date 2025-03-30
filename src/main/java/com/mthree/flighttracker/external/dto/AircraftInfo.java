package com.mthree.flighttracker.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AircraftInfo {
    private String registration;
    private String iata;
    private String icao;
    private String icao24;
}
