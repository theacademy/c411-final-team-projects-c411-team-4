package com.mthree.flighttracker.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeShared {
    private String airline_name;
    private String airline_iata;
    private String airline_icao;
    private String flight_number;
    private String flight_iata;
    private String flight_icao;
}
