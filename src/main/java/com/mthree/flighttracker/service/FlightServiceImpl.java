package com.mthree.flighttracker.service;

import com.mthree.flighttracker.dao.FlightDao;
import com.mthree.flighttracker.dao.FlightStatusDao;
import com.mthree.flighttracker.helper.CoordinateHelper;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightServiceInterface {
    private FlightDao flightDao;
    private FlightStatusDao flightStatusDao;
    private AirlineDao airlineDao;
    private AirportDao airportDao;




    @Autowired
    FlightServiceImpl(FlightDao flightDao, FlightStatusDao flightStatusDao, AirlineDao airlineDao, AirportDao airportDao) {
        this.flightStatusDao = flightStatusDao;
        this.flightDao = flightDao;
        this.airlineDao = airlineDao;
        this.airportDao = airportDao;
    }


    @Override
    public List<Flight> getAllFlights() {
        return flightDao.getAllFlights();
    }


    public Page<Flight> findAll(Pageable pageable) {
        return flightDao.findAll(pageable);
    }


    @Override
    public List<Flight> getFlightsByAirline(Airline airline) {
        return flightDao.getFlightsByAirline(airline);
    }
    public Page<Flight> getFlightsByAirline(Airline airline, Pageable pageable) {
        return flightDao.getFlightsByAirline(airline, pageable);
    }


    @Override
    public List<Flight> getFlightsByDate(LocalDateTime date) {
        return flightDao.getFlightsByDate(date);
    }


    @Override
    public List<Flight> getFlightsByArrAirport(Airport airport) {
        return flightDao.getFlightsByDepAirport(airport);
    }
    public Page<Flight> getFlightsByArrAirport(Airport airport, Pageable pageable) {
        return flightDao.getFlightsByArrAirport(airport, pageable);
    }


    @Override
    public List<Flight> getFlightsByDepAirport(Airport airport) {
        return flightDao.getFlightsByDepAirport(airport);
    }
    public Page<Flight> getFlightsByDepAirport(Airport airport, Pageable pageable) {
        return flightDao.getFlightsByDepAirport(airport, pageable);
    }


    @Override
    public List<Flight> getFlightsByAirport(Airport airport) {
        return flightDao.getFlightsByDepAirport(airport);
    }
    public Page<Flight> getFlightsByAirport(Airport airport, Pageable pageable) {
        return flightDao.getFlightsByAirport(airport, pageable);
    }


    @Override
    public List<Flight> getFlightsByStatus(FlightStatus status) {
        return flightDao.getFlightsByStatus(status);
    }


    public Page<Flight> getFlightsByStatus(String status, Pageable pageable) {
        FlightStatus flightStatus = flightStatusDao.getFlightStatus(status);
        return flightDao.getFlightsByStatus(flightStatus, pageable);
    }


    @Override
    public Flight getFlightById(int id) {
        Flight flight = flightDao.getFlightById(id);


        // For testing purposes
        if (flight == null) {
            flight = new Flight();


            Airport airport  = new Airport();
            airport.setName("Doesn't Exist");
            airport.setCode("000");
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));


            Airline airline = new Airline();
            airline.setName("Doesn't exist");
            airline.setCode("000");


            flight.setAirline(airline);
            flight.setArrAirport(airport);
            flight.setDepAirport(airport);
            flight.setNumber(0);
            flight.setLatitude(CoordinateHelper.createCoord("000.000000"));
            flight.setLongitude(CoordinateHelper.createCoord("000.000000"));
            flight.setScheduledArrival(LocalDateTime.now());
            flight.setScheduledDeparture(LocalDateTime.now());
        }


        return flight;
    }


    @Override
    public Flight getFlightByNumber(short number, Airline airline) {
        Flight flight = flightDao.getFlightByNumber(number, airline);


        // For testing purposes
        if (flight == null) {
            flight = new Flight();


            Airport airport  = new Airport();
            airport.setName("Doesn't Exist");
            airport.setCode("000");
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));
            airport.setLatitude(CoordinateHelper.createCoord("000.000000"));


            flight.setAirline(airline);
            flight.setArrAirport(airport);
            flight.setDepAirport(airport);
            flight.setNumber(0);
            flight.setLatitude(CoordinateHelper.createCoord("000.000000"));
            flight.setLongitude(CoordinateHelper.createCoord("000.000000"));
            flight.setScheduledArrival(LocalDateTime.now());
            flight.setScheduledDeparture(LocalDateTime.now());
        }


        return flight;
    }


   /*public Page<?> searchFlights(String airline, String destination, String arrival, String airport, Pageable pageable) {
       if (airline == null) {
           if (airport != null && (arrival == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByName(airport);
               return getFlightsByAirport(airport1, pageable);
           }
           if (arrival != null && (airport == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByName(arrival);
               return getFlightsByArrAirport(airport1, pageable);
           }
           if (destination != null && (airport == null && arrival == null)) {
               Airport airport1 = airportDao.getAirportByName(destination);
               return getFlightsByDepAirport(airport1, pageable);
           }
       }


       if (airline != null) {
           if (airport == null && arrival == null && destination == null) {
               Airline airline1 = airlineDao.getAirlineByName(airline);
               getFlightsByAirline(airline1, pageable);
           }
           if (airport != null && (arrival == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByName(airport);
               Airline airline1 = airlineDao.getAirlineByName(airline);
               Page<Flight> flightPage = getFlightsByAirline(airline1, pageable);
               List<Flight> flightsFilteredByAirport = flightPage.getContent().stream()
                       .filter(flight -> flight.getArrAirport() == airport1 && flight.getDepAirport() == airport1)
                       .collect(Collectors.toList());


               return new PageImpl<>(flightsFilteredByAirport, pageable, flightPage.getTotalElements());
           }


           if (arrival != null && (airport == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByName(arrival);
               Airline airline1 = airlineDao.getAirlineByName(airline);
               Page<Flight> flightPage = getFlightsByAirline(airline1, pageable);
               List<Flight> flightsFilteredByAirport = flightPage.getContent().stream()
                       .filter(flight -> flight.getArrAirport() == airport1)
                       .collect(Collectors.toList());


               return new PageImpl<>(flightsFilteredByAirport, pageable, flightPage.getTotalElements());
           }


           if (destination != null && (airport == null && arrival == null)) {
               Airport airport1 = airportDao.getAirportByName(destination);
               Airline airline1 = airlineDao.getAirlineByName(airline);
               Page<Flight> flightPage = getFlightsByAirline(airline1, pageable);
               List<Flight> flightsFilteredByAirport = flightPage.getContent().stream()
                       .filter(flight -> flight.getDepAirport() == airport1)
                       .collect(Collectors.toList());


               return new PageImpl<>(flightsFilteredByAirport, pageable, flightPage.getTotalElements());
           }
       }


       return null;
   }*/


    public Optional<Flight> getByNumber(short number, String airline) {
        System.out.println(airline);
        Airline airline1 = airlineDao.getAirlineByCode(airline);
        if (airline1 == null) {
            System.out.println("Null airline");
        }
        Optional<Flight> flight = flightDao.getByNumberAirline(number, airline1);


        return flight;
    }


    public Page<Flight> findByNumber(short number, Pageable pageable) {
        return flightDao.findByNumber(number, pageable);
    }


    public Optional<Flight> findByNumber (int number) {
        return flightDao.findByNumber((short) number);
    }


    public Page<Airport> findAllAirports(Pageable pageable) {
        return airportDao.findAll(pageable);
    }
}
