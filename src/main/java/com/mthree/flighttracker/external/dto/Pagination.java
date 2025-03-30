package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {
    private int limit;
    private int offset;
    private int count;
    private int total;
}
