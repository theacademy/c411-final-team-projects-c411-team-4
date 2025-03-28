package com.mthree.flighttracker.external.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiveInfo {
    private String updated;
    private Double latitude;
    private Double longitude;
    private Double altitude;
    private int direction;
    private Double speed_horizontal;
    private int speed_vertical;
    private boolean is_ground;
}
