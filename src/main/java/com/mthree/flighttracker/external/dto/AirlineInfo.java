package com.mthree.flighttracker.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirlineInfo {
    private String name;
    private String iata;
    private String icao;
}
