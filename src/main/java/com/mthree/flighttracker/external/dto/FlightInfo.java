package com.mthree.flighttracker.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightInfo {
    private String number;
    private String iata;
    private String icao;
    private CodeShared codeshared;
}
