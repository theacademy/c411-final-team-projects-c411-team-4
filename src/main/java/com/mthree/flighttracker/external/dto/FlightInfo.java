package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightInfo {
    private String number;
    private String iata;
    private String icao;
    private String codeshared;
}
