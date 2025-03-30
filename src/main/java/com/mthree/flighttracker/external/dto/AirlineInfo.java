package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirlineInfo {
    private String name;
    private String iata;
    private String icao;
}
