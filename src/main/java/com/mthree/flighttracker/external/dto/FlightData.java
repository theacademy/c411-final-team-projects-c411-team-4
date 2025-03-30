package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
