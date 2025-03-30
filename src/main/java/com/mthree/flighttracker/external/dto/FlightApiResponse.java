package com.mthree.flighttracker.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightApiResponse {
    private Pagination pagination;
    private List<FlightData> data;
}
