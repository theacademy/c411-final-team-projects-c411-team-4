package com.mthree.flighttracker.service;

import com.mthree.flighttracker.dao.AirlineDao;
import com.mthree.flighttracker.dao.AirportDao;
import com.mthree.flighttracker.dao.FlightDao;
import com.mthree.flighttracker.dao.FlightStatusDao;
import com.mthree.flighttracker.helper.CoordinateHelper;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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


    @Override
    public List<Flight> getFlightsByDate(LocalDateTime date) {
        return flightDao.getFlightsByDate(date);
    }

    public List<Flight> getFlightsByAirport(Airport airport) {
        Airport airport1 = airport;
        return flightDao.getFlightsByAirport(airport1);
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


   public Page<?> searchFlights(String airline, String destination, String arrival, String airport, Pageable pageable) {
       if (airline == null) {
           if (airport != null && (arrival == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByCode(airport);
               return flightDao.getFlightsByAirport(airport1, pageable);
           }
           if (arrival != null && (airport == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByCode(arrival);
               return flightDao.getFlightsByArrAirport(airport1, pageable);
           }
           if (destination != null && (airport == null && arrival == null)) {
               Airport airport1 = airportDao.getAirportByCode(destination);
               return flightDao.getFlightsByDepAirport(airport1, pageable);
           }
       }


       if (airline != null) {
           if (airport == null && arrival == null && destination == null) {
               Airline airline1 = airlineDao.getAirlineByName(airline);
               return flightDao.getFlightsByAirline(airline1, pageable);
           }
           if (airport != null && (arrival == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByCode(airport);
               Airline airline1 = airlineDao.getAirlineByName(airline);

               return flightDao.getFlightsByAirportAndAirline(airport1, airline1, pageable);
           }


           if (arrival != null && (airport == null && destination == null)) {
               Airport airport1 = airportDao.getAirportByCode(arrival);
               Airline airline1 = airlineDao.getAirlineByName(airline);

               return flightDao.getFlightsByArrAirportAndAirline(airport1, airline1, pageable);
           }


           if (destination != null && (airport == null && arrival == null)) {
               Airport airport1 = airportDao.getAirportByCode(destination);
               Airline airline1 = airlineDao.getAirlineByName(airline);
               return flightDao.getFlightsByDepAirportAndAirline(airport1, airline1, pageable);
           }
       }


       return null;
   }


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