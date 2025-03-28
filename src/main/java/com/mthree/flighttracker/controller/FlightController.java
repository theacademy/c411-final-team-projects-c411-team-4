package com.mthree.flighttracker.controller;


import com.mthree.flighttracker.model.*;
import com.mthree.flighttracker.service.FlightServiceImpl;
import com.mthree.flighttracker.service.UserHistoryService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightController {
    @Autowired
    FlightServiceImpl flightService;
    @Autowired
    UserHistoryService userHistoryService;

    @GetMapping("/flights")
    public ResponseEntity<Page<Flight>> getAllFlights(
            Pageable pageable,
            @RequestParam(required = false) String status) {
        try {
            //Implement service call
            if (status != null) {
                Page<Flight> flight = flightService.getFlightsByStatus(status, pageable);
                return ResponseEntity.ok(flight);
            }
            return ResponseEntity.ok(flightService.findAll(pageable));
            //return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   /*
   // TODO Unsure what to do since we don't have countries in data
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
    */


    @GetMapping("/airports")
    public ResponseEntity<Page<Airport>> getAllAirports(
            Pageable pageable,
            @RequestParam(required = false) String country) {
        try {
            //Implement service call
            return ResponseEntity.ok(flightService.findAllAirports(pageable));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


   @GetMapping("/search")
   public ResponseEntity<?> searchFlights(
           Pageable pageable,
           @RequestParam(required = false) String airline,
           @RequestParam(required = false) String departing,
           @RequestParam(required = false) String arrival,
           @RequestParam(required = false) String airport) {


       try {
           // Validate that at least one parameter is provided
           if (airline == null && departing == null && arrival == null && airport == null) {
               return ResponseEntity.badRequest().build();
           }


           // Validate that airport is not used with destination or arrival
           if (airport != null && (departing != null || arrival != null)) {
               return ResponseEntity.badRequest().build();
           }

           final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
           updateUserSearchHistory(auth, airline, departing, arrival, airport);

           return ResponseEntity.ok(flightService.searchFlights(airline, departing, arrival, airport, pageable));
           //return ResponseEntity.ok().build();
       } catch (IllegalArgumentException e) {
           return ResponseEntity.badRequest().build();
       } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }
   }

    private void updateUserSearchHistory(Authentication auth, String airline, String departing, String arrival, String airport) {
        if(auth.getName() == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof UserDetails)) {
            return;
        }
        final UserSearchHistory history = new UserSearchHistory();
        history.setUser(new User().setUsername(auth.getName()));

        if(airline != null) {
            history.setAirline(new Airline().setName(airline));
        }

        if(departing != null) {
            history.setDepAirport(new Airport().setCode(departing));
        }

        if(arrival != null) {
            history.setArrAirport(new Airport().setCode(arrival));
        }

        if(airport != null) {
            history.setSoleAirport(new Airport().setCode(airport));
        }

        userHistoryService.addHistoryToUser(history);
    }

    @GetMapping("/flight/{flightNumber}")
    public ResponseEntity<Page<Flight>> getFlightByNumber(
            Pageable pageable,
            @PathVariable short flightNumber) {
        try {
            Page<Flight> flight = flightService.findByNumber(flightNumber, pageable);
            return ResponseEntity.ok(flight);
            //return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private HashMap<String, FlightLocation> flightLocations = new HashMap<>();

    private static class FlightLocation {
        @Getter
        private BigDecimal latitude;
        @Getter
        private BigDecimal longitude;
        private final BigDecimal latIncrement;
        private final BigDecimal longIncrement;

        public FlightLocation(BigDecimal initialLat, BigDecimal initialLong) {
            this.latitude = initialLat;
            this.longitude = initialLong;
            this.latIncrement = new BigDecimal(Math.random() * 0.003 - 0.001).setScale(6, RoundingMode.HALF_UP);
            this.longIncrement = new BigDecimal(Math.random() * 0.003 - 0.001).setScale(6, RoundingMode.HALF_UP);
        }

        public void updateLocation() {
            latitude = latitude.add(latIncrement);
            longitude = longitude.add(longIncrement);
        }
    }

    @GetMapping("/flight/{airlineCode}/{flightNumber}")
    public ResponseEntity<Flight> getFlightByIataNumber(@PathVariable String airlineCode, @PathVariable short flightNumber) {
        final Airline airline = flightService.getAirlineByCode(airlineCode);

        if(airline == null) {
            return ResponseEntity.notFound().build();
        }

        final Flight flight = flightService.getLatestFlightByNumber(flightNumber, airline);

        if(flight == null) {
            return ResponseEntity.notFound().build();
        }

        final String flightKey = airlineCode + "-" + flightNumber;

        final FlightLocation location = flightLocations.computeIfAbsent(flightKey, k -> {
            double minLat = 24.396308;  // florida
            double maxLat = 49.384358;  // canada
            double minLng = -125.000000;  // west
            double maxLng = -66.934570;  // east

            double randomLat = minLat + Math.random() * (maxLat - minLat);
            double randomLng = minLng + Math.random() * (maxLng - minLng);

            return new FlightLocation(
                    new BigDecimal(randomLat).setScale(6, RoundingMode.HALF_UP),
                    new BigDecimal(randomLng).setScale(6, RoundingMode.HALF_UP)
            );
        });

        flight.setLatitude(location.getLatitude());
        flight.setLongitude(location.getLongitude());

        location.updateLocation();

        return ResponseEntity.ok(flight);
    }
}
