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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        final Dotenv dotenv = Dotenv.load();
        final String aviationStackApiToken = dotenv.get("AVIATION_STACK_API_TOKEN");
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
        if (airline == null) {
            if(departing != null && arrival != null) {
                final Airport depAirport = airportDao.getAirportByCode(departing);
                final Airport arrAirport = airportDao.getAirportByCode(arrival);
                return flightDao.getFlightsByDepAirportAndArrAirport(depAirport, arrAirport, pageable);
            }
            if (airport != null && (arrival == null && departing == null)) {
                Airport airport1 = airportDao.getAirportByCode(airport);
                return flightDao.getFlightsByAirport(airport1, pageable);
            }
            if (arrival != null && (airport == null && departing == null)) {
                Airport airport1 = airportDao.getAirportByCode(arrival);
                return flightDao.getFlightsByArrAirport(airport1, pageable);
            }
            if (departing != null && (airport == null && arrival == null)) {
                Airport airport1 = airportDao.getAirportByCode(departing);
                return flightDao.getFlightsByDepAirport(airport1, pageable);
            }
        }


        if (airline != null) {
            if(arrival != null && departing != null) {
                Airline airlineOne = airlineDao.getAirlineByName(airline);
                Airport arrAirport = airportDao.getAirportByCode(arrival);
                Airport depAirport = airportDao.getAirportByCode(departing);

                return flightDao.getFlightsByDepAirportAndArrAirportAndAirline(depAirport, arrAirport, airlineOne, pageable);
            }

            if (airport == null && arrival == null && departing == null) {
                Airline airline1 = airlineDao.getAirlineByName(airline);
                return flightDao.getFlightsByAirline(airline1, pageable);
            }

            if (airport != null && (arrival == null && departing == null)) {
                Airport airport1 = airportDao.getAirportByCode(airport);
                Airline airline1 = airlineDao.getAirlineByName(airline);

                return flightDao.getFlightsByAirportAndAirline(airport1, airline1, pageable);
            }


            if (arrival != null && (airport == null && departing == null)) {
                Airport airport1 = airportDao.getAirportByCode(arrival);
                Airline airline1 = airlineDao.getAirlineByName(airline);

                return flightDao.getFlightsByArrAirportAndAirline(airport1, airline1, pageable);
            }


            if (departing != null && (airport == null && arrival == null)) {
                Airport airport1 = airportDao.getAirportByCode(departing);
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
        final Flight dbGivenFlight = flightDao.findFirstByNumberAndAirlineOrderByScheduledDepartureDesc(number, airline);

        apiGivenFlight.getStatus().setStatus(apiGivenFlight.getStatus().getStatus().toUpperCase());
        FlightStatus flightStatus = flightStatusDao.getFlightStatus(apiGivenFlight.getStatus().getStatus());

        if(flightStatus == null) {
            flightStatus = flightStatusDao.save(apiGivenFlight.getStatus());
        }

        Flight currentFlight;

        if(dbGivenFlight == null) {
            // need to provide the airline which has our db ID
            apiGivenFlight.setAirline(airline);
            // same for the below airports
            final Airport depAirport = airportDao.getAirportByCode(apiGivenFlight.getDepAirport().getCode());
            final Airport arrAirport = airportDao.getAirportByCode(apiGivenFlight.getArrAirport().getCode());
            apiGivenFlight.setDepAirport(depAirport);
            apiGivenFlight.setArrAirport(arrAirport);

            currentFlight = flightDao.save(apiGivenFlight);
        } else {
            currentFlight = dbGivenFlight
                    .setStatus(flightStatus)
                    .setEstArrival(apiGivenFlight.getEstArrival())
                    .setEstDeparture(apiGivenFlight.getEstDeparture())
                    .setLatitude(apiGivenFlight.getLatitude())
                    .setLongitude(apiGivenFlight.getLongitude());
            flightDao.save(currentFlight);
            currentFlight.setFromApi(true);
        }

        if(currentFlight.isFromApi()) {
            return currentFlight;
        }

        return flightDao.save(currentFlight);
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
