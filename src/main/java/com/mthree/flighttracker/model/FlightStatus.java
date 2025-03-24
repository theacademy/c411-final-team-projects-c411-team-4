package com.mthree.flighttracker.model;

public enum FlightStatus {
    SCHEDULED("Flight is scheduled and has not yet departed"),
    DEPARTED("Flight has left the gate"),
    IN_AIR("Flight is currently airborne"),
    LANDED("Flight has landed at its destination"),
    CANCELLED("Flight has been cancelled"),
    DELAYED("Flight has been delayed"),
    DIVERTED("Flight has been diverted to an alternative airport"),
    BOARDING("Passengers are currently boarding the flight"),
    ON_TIME("Flight is operating on time"),
    GATE_CLOSED("Gate has closed, and passengers can no longer board"),
    WAITING("Flight is on hold, possibly waiting to depart"),
    ARRIVED("Flight has arrived at the destination"),
    CLEARED("The flight has cleared customs (if applicable)"),
    OVERBOOKED("More tickets have been sold than seats available"),
    UNKNOWN("Flight status is unknown"),
    IN_PROGRESS("A non-specific status for flights currently in motion");

    private final String description;

    FlightStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}