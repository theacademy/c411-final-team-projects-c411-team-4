package com.mthree.flighttracker.controller;


import com.mthree.flighttracker.model.*;
import com.mthree.flighttracker.service.FlightServiceInterface;
import com.mthree.flighttracker.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api")
public class FlightController {
    @Autowired
    @Qualifier("flightServiceApi")
    FlightServiceInterface flightService;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/flight/{airlineCode}/{flightNumber}")
    public ResponseEntity<Flight> getFlightByIataNumber(@PathVariable String airlineCode,
                                                              @PathVariable short flightNumber,
                                                            Pageable pageable) {
        final Airline airline = flightService.getAirlineByCode(airlineCode);

        if(airline == null) {
            return ResponseEntity.notFound().build();
        }

        final Flight flight = flightService.getLatestFlightByNumber(flightNumber, airline);

        if(flight == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(flight);
    }
}
