package com.mthree.flighttracker.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightData {
    private String flight_date;
    private String flight_status;
    private DepartureInfo departure;
    private ArrivalInfo arrival;
    private AirlineInfo airline;
    private FlightInfo flight;
    private AircraftInfo aircraft;
    private LiveInfo live;
}
