package com.mthree.flighttracker.controller;


import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightController {

    // TODO: Add service layer dependency injection

    @GetMapping("/flights")
    public ResponseEntity<Page<Flight>> getAllFlights(
            Pageable pageable,
            @RequestParam(required = false) FlightStatus status) {
        try {
            //Implement service call
            // if (status != null) {
            //     return ResponseEntity.ok(flightService.getAllFlightsByStatus(status, pageable));
            // }
            // return ResponseEntity.ok(flightService.getAllFlights(pageable));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/airports")
    public ResponseEntity<Page<Airport>> getAllAirports(
            Pageable pageable,
            @RequestParam(required = false) String country) {
        try {
            //Implement service call
            // if (country != null) {
            //     return ResponseEntity.ok(airportService.getAirportsByCountry(country, pageable));
            // }
            // return ResponseEntity.ok(airportService.getAllAirports(pageable));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Flight>> searchFlights(
            @RequestParam(required = false) String airline,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String arrival,
            @RequestParam(required = false) String airport) {

        try {
            // Validate that at least one parameter is provided
            if (airline == null && destination == null && arrival == null && airport == null) {
                return ResponseEntity.badRequest().build();
            }

            // Validate that airport is not used with destination or arrival
            if (airport != null && (destination != null || arrival != null)) {
                return ResponseEntity.badRequest().build();
            }

            // Implement service call
            // List<Flight> flights = flightService.searchFlights(airline, destination, arrival, airport);
            // return ResponseEntity.ok(flights);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<Flight> getFlightByNumber(@PathVariable int flightNumber) {
        try {
            // Optional<Flight> flight = flightService.getFlightByNumber(flightNumber);
            // return flight.map(ResponseEntity::ok)
            //             .orElse(ResponseEntity.notFound().build());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}