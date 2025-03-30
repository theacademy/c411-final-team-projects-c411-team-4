package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FlightApiResponse {
    private Pagination pagination;
    private List<FlightData> data;
}
