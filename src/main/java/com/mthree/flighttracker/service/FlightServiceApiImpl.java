package com.mthree.flighttracker.service;

import com.mthree.flighttracker.dao.AirlineDao;
import com.mthree.flighttracker.dao.AirportDao;
import com.mthree.flighttracker.dao.FlightDao;
import com.mthree.flighttracker.dao.FlightStatusDao;
import com.mthree.flighttracker.external.ApiRateLimiter;
import com.mthree.flighttracker.external.AviationStackApi;
import com.mthree.flighttracker.helper.CoordinateHelper;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service("flightServiceApi")
public class FlightServiceApiImpl implements FlightServiceInterface {
    private final AviationStackApi aviationStackApi;
    private final Map<String, Long> flightNumberLastApiCall;

    private FlightDao flightDao;
    private FlightStatusDao flightStatusDao;
    private AirlineDao airlineDao;
    private AirportDao airportDao;

    @Autowired
    FlightServiceApiImpl(FlightDao flightDao, FlightStatusDao flightStatusDao, AirlineDao airlineDao, AirportDao airportDao) {
        Dotenv dotenv;
        try {
            dotenv = Dotenv.load();
        } catch (Exception e) {
            dotenv = null;
        }
        String aviationStackApiToken = dotenv == null ? "" : dotenv.get("AVIATION_STACK_API_TOKEN");
        this.aviationStackApi = new AviationStackApi(aviationStackApiToken);
        this.flightNumberLastApiCall = new HashMap<>();

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
        return flightDao.getFlightsByAirport(airport);
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
            flight.setSchedArrival(LocalDateTime.now());
            flight.setSchedDeparture(LocalDateTime.now());
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
            flight.setSchedArrival(LocalDateTime.now());
            flight.setSchedDeparture(LocalDateTime.now());
        }

        return flight;
    }


    public Page<?> searchFlights(String airline, String departing, String arrival, String airport, Pageable pageable) {
        if(airport != null && !airport.isBlank())  {
            final Airport soleAirport = airportDao.getAirportByCode(airport);
            if(soleAirport == null) {
                return null;
            }
            return flightDao.getFlightsByAirport(soleAirport, pageable);
        }

        final Airline dbAirline = airlineDao.getAirlineByName(airline);
        if(airline != null && !airline.isEmpty() && dbAirline == null) {
            return null;
        }

        final Airport depAirport = airportDao.getAirportByCode(departing);
        final Airport arrAirport = airportDao.getAirportByCode(arrival);
        if(airline == null && depAirport == null && arrAirport == null) {
            return null;
        }

        List<Flight> flightsFromApi = new ArrayList<>();
        try {
            flightsFromApi = aviationStackApi.getLiveFlightsByDepArrAirportAirline(departing, arrival, dbAirline == null ? null : dbAirline.getCode());
        } catch (InterruptedException e) {
            return flightDao.getFlightsByDepAirportAndArrAirport(depAirport, arrAirport, pageable);
        }

        if(flightsFromApi.isEmpty()) {
            return flightDao.getFlightsByDepAirportAndArrAirport(depAirport, arrAirport, pageable);
        }

        flightsFromApi = processFlightsFromApi(flightsFromApi);
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), flightsFromApi.size());

        List<Flight> subFlightsFromApi = start > end ? Collections.emptyList() : flightsFromApi.subList(start, end);
        return new PageImpl<Flight>(subFlightsFromApi, pageable, subFlightsFromApi.size());
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

    @Override
    public Airline getAirlineByCode(String code) {
        return airlineDao.getAirlineByCode(code);
    }

    @Override
    public Flight getLatestFlightByNumber(short number, Airline airline) {
        final String iataFlightNumber = airline.getCode() + number;
        final Long now = System.currentTimeMillis();
        final Long lastApiCall = flightNumberLastApiCall.get(iataFlightNumber);

        if(lastApiCall != null && now - lastApiCall < ApiRateLimiter.RECOMMENDED_MS_BETWEEN_CALLS) {
            return flightDao.findFirstByNumberAndAirlineOrderByScheduledDepartureDesc(number, airline);
        }

        Optional<Flight> liveFlight;
        try {
            liveFlight = aviationStackApi.getLiveFlight(airline.getCode(), number);
            flightNumberLastApiCall.put(iataFlightNumber, System.currentTimeMillis()); // not reusing now because the API calls can take a long time
        } catch (InterruptedException e) {
            return flightDao.findFirstByNumberAndAirlineOrderByScheduledDepartureDesc(number, airline);
        }

        if(liveFlight.isEmpty()) {
            return flightDao.findFirstByNumberAndAirlineOrderByScheduledDepartureDesc(number, airline);
        }

        final Flight apiGivenFlight = liveFlight.get();
        return processFlightFromApi(apiGivenFlight);
    }

    public Flight processFlightFromApi(Flight apiFlight) {
        Airline airline = airlineDao.getAirlineByCode(apiFlight.getAirline().getCode());
        if (airline == null) {
            airline = airlineDao.getAirlineByName(apiFlight.getAirline().getName());
        }
        if (airline == null) {
            airline = airlineDao.save(apiFlight.getAirline());
        }

        apiFlight.getStatus().setStatus(apiFlight.getStatus().getStatus().toUpperCase());
        FlightStatus flightStatus = flightStatusDao.getFlightStatus(apiFlight.getStatus().getStatus());
        if (flightStatus == null) {
            flightStatus = flightStatusDao.save(apiFlight.getStatus());
        }

        Flight dbFlight = flightDao.findFirstByNumberAndAirlineOrderByScheduledDepartureDesc((short) apiFlight.getNumber(), airline);
        Flight currentFlight;

        if (dbFlight == null) {
            apiFlight.setStatus(flightStatus);
            apiFlight.setAirline(airline);

            Airport depAirport = airportDao.getAirportByCode(apiFlight.getDepAirport().getCode());
            if(depAirport == null) {
                depAirport = airportDao.getAirportByName(apiFlight.getDepAirport().getName());
            }
            if (depAirport == null) {
                apiFlight.getDepAirport().setLatitude(CoordinateHelper.createCoord("0"));
                apiFlight.getDepAirport().setLongitude(CoordinateHelper.createCoord("0"));
                depAirport = airportDao.save(apiFlight.getDepAirport());
            }
            Airport arrAirport = airportDao.getAirportByCode(apiFlight.getArrAirport().getCode());
            if (arrAirport == null) {
                arrAirport = airportDao.getAirportByName(apiFlight.getArrAirport().getName());
            }
            if (arrAirport == null) {
                apiFlight.getArrAirport().setLatitude(CoordinateHelper.createCoord("0"));
                apiFlight.getArrAirport().setLongitude(CoordinateHelper.createCoord("0"));
                arrAirport = airportDao.save(apiFlight.getArrAirport());
            }

            apiFlight.setDepAirport(depAirport);
            apiFlight.setArrAirport(arrAirport);

            currentFlight = flightDao.save(apiFlight);
        } else {
            currentFlight = dbFlight.setStatus(flightStatus)
                    .setEstArrival(apiFlight.getEstArrival())
                    .setEstDeparture(apiFlight.getEstDeparture())
                    .setLatitude(apiFlight.getLatitude())
                    .setLongitude(apiFlight.getLongitude());
            flightDao.save(currentFlight);
            currentFlight.setFromApi(true);
        }

        return flightDao.save(currentFlight);
    }

    public List<Flight> processFlightsFromApi(List<Flight> apiFlights) {
        List<Flight> processedFlights = new ArrayList<>();
        for (Flight apiFlight : apiFlights) {
            processedFlights.add(processFlightFromApi(apiFlight));
        }
        return processedFlights;
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

    @Override
    public Flight updateFlight(Flight flight) {
        return flightDao.save(flight);
    }

    @Override
    public void deleteFlight(Flight flight) {
        flightDao.delete(flight);
    }
}
