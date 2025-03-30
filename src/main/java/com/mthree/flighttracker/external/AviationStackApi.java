package com.mthree.flighttracker.external;

import com.mthree.flighttracker.external.dto.*;
import com.mthree.flighttracker.helper.CoordinateHelper;
import com.mthree.flighttracker.model.Airline;
import com.mthree.flighttracker.model.Airport;
import com.mthree.flighttracker.model.Flight;
import com.mthree.flighttracker.model.FlightStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AviationStackApi {
    private static final String AVIATION_STACK_BASE_URI = "https://api.aviationstack.com/v1";
    private static final String AVIATION_STACK_ACCESS_KEY = "?access_key=%s";
    private static final String AVIATION_STACK_IATA_FLIGHT_NUMBER = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&flight_iata=%s";

    private static final String AVIATION_STACK_ALL_ACTIVE = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active";
    private static final String AVIATION_STACK_AIRLINE_IATA = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&airline_iata=%s";
    private static final String AVIATION_STACK_DEP_ARR_IATAS = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&dep_iata=%s&arr_iata=%s";
    private static final String AVIATION_STACK_DEP_ARR_AIRLINE_IATAS = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&dep_iata=%s&arr_iata=%s&airline_iata=%s";
    private static final String AVIATION_STACK_ARR_AIRLINE_IATAS = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&arr_iata=%s^airline_iata=%s";
    private static final String AVIATION_STACK_ARR_IATA = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&arr_iata=%s";
    private static final String AVIATION_STACK_DEP_AIRLINE_IATAS = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&dep_iata=%s&airline_iata=%s";
    private static final String AVIATION_STACK_DEP_IATA = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&dep_iata=%s";

    private final String apiToken;
    private final ApiRateLimiter rateLimiter;

    public AviationStackApi(String apiToken) {
        this.rateLimiter = new ApiRateLimiter();
        this.apiToken = apiToken;
    }

    public boolean canCallApi() {
        return rateLimiter.canCall();
    }

    /**
     * Gets a flight by it's IATA Flight Number, if it is live, and exists.
     * @param airlineCode The two character airline code.
     * @param flightNumber The 4 digit flight number.
     * @return Flight if it is live and our API call is successful, empty optional otherwise.
     */
    public Optional<Flight> getLiveFlight(String airlineCode, short flightNumber) throws InterruptedException {
        if(!canCallApi()) {
            return Optional.empty();
        }

        rateLimiter.prepareCall();

        final String iataFlightNumber = String.format(
                "%s%d",
                airlineCode,
                flightNumber
        );
        final String getFlight = String.format(AVIATION_STACK_IATA_FLIGHT_NUMBER, apiToken, iataFlightNumber);

        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<FlightApiResponse> response = restTemplate.getForEntity(getFlight, FlightApiResponse.class);
        final FlightApiResponse flightApiResponse = response.getBody();

        if(
                flightApiResponse == null ||
                flightApiResponse.getData() == null ||
                flightApiResponse.getData().isEmpty()
        ) {
            rateLimiter.releaseCall();
            return Optional.empty();
        }

        rateLimiter.releaseCall();
        final boolean includeLocation = true;
        return Optional.ofNullable(
                convert(flightApiResponse.getData().get(0), includeLocation)
        );
    }

    public List<Flight> getLiveFlightsByDepArrAirportAirline(String depIata, String arrIata, String airIata) throws InterruptedException {
        if(!canCallApi()) {
            return List.of();
        }

        rateLimiter.prepareCall();

        final String getFlights = determineSearchUri(depIata, arrIata, airIata);

        final RestTemplate restTemplate = new RestTemplate();
        final ResponseEntity<FlightApiResponse> response = restTemplate.getForEntity(getFlights, FlightApiResponse.class);
        final FlightApiResponse flightApiResponse = response.getBody();

        if(
                flightApiResponse == null ||
                        flightApiResponse.getData() == null ||
                        flightApiResponse.getData().isEmpty()
        ) {
            rateLimiter.releaseCall();
            return List.of();
        }

        rateLimiter.releaseCall();

        List<Flight> flights = new ArrayList<>();
        final boolean includeLocation = false;
        for(FlightData flightData : flightApiResponse.getData()) {
            flights.add(convert(flightData, includeLocation));
        }
        return flights;
    }

    private String determineSearchUri(String depIata, String arrIata, String airIata) {
        String getFlights;
        final boolean haveDeparting = depIata != null && !depIata.isBlank();
        final boolean haveArrival = arrIata != null && !arrIata.isBlank();
        final boolean haveAirline = airIata != null && !airIata.isBlank();
        if(haveDeparting && haveArrival && haveAirline) {
            getFlights = String.format(AVIATION_STACK_DEP_ARR_AIRLINE_IATAS, apiToken, depIata, arrIata, airIata);
        } else if(haveDeparting && haveArrival) {
            getFlights = String.format(AVIATION_STACK_DEP_ARR_IATAS, apiToken, depIata, arrIata);
        } else if(haveAirline && haveDeparting) {
            getFlights = String.format(AVIATION_STACK_DEP_AIRLINE_IATAS, apiToken, depIata, airIata);
        } else if(haveAirline && haveArrival) {
            getFlights = String.format(AVIATION_STACK_ARR_AIRLINE_IATAS, apiToken, arrIata, airIata);
        } else if(haveAirline) {
            getFlights = String.format(AVIATION_STACK_AIRLINE_IATA, apiToken, airIata);
        } else if(haveDeparting) {
            getFlights = String.format(AVIATION_STACK_DEP_IATA, apiToken, depIata);
        } else if(haveArrival) {
            getFlights = String.format(AVIATION_STACK_ARR_IATA, apiToken, arrIata);
        } else {
            getFlights = String.format(AVIATION_STACK_ALL_ACTIVE, apiToken);
        }
        return getFlights;
    }

    private Flight convert(FlightData flightData, boolean includeLocation) {
        final LiveInfo liveInfo = flightData.getLive();

        final AirlineInfo airlineInfo = flightData.getAirline();
        final Airline airline = new Airline()
                .setName(airlineInfo.getName())
                .setCode(airlineInfo.getIata());

        final DepartureInfo departureInfo = flightData.getDeparture();
        final Airport depAirport = new Airport()
                .setName(departureInfo.getAirport())
                .setCode(departureInfo.getIata());

        final ArrivalInfo arrivalInfo = flightData.getArrival();
        final Airport arrAirport = new Airport()
                .setName(arrivalInfo.getAirport())
                .setCode(arrivalInfo.getIata());

        final FlightInfo flightInfo = flightData.getFlight();
        final FlightStatus flightStatus = new FlightStatus().setStatus(flightData.getFlight_status());
        final Flight flight = new Flight()
                .setStatus(flightStatus)
                .setNumber(Integer.parseInt(flightInfo.getNumber()))
                .setAirline(airline)
                .setDepAirport(depAirport)
                .setArrAirport(arrAirport)
                .setSchedDeparture(
                        OffsetDateTime.parse(departureInfo.getScheduled()).toLocalDateTime()
                ).setSchedArrival(
                        OffsetDateTime.parse(departureInfo.getScheduled()).toLocalDateTime()
                );

        if(includeLocation && liveInfo != null && liveInfo.getLatitude() != null && liveInfo.getLongitude() != null) {
            flight.setBearing(liveInfo.getDirection());
            flight.setLatitude(
                    CoordinateHelper.createCoord(
                            "" + liveInfo.getLatitude()
                    )
            );
            flight.setLongitude(
                    CoordinateHelper.createCoord(
                            "" + liveInfo.getLongitude()
                    )
            );
        }

        if(departureInfo.getEstimated() != null && !departureInfo.getEstimated().isBlank()) {
            flight.setEstDeparture(
                    OffsetDateTime.parse(departureInfo.getEstimated()).toLocalDateTime()
            );
        }
        if(arrivalInfo.getEstimated() != null && !arrivalInfo.getEstimated().isBlank()) {
            flight.setEstArrival(
                    OffsetDateTime.parse(arrivalInfo.getEstimated()).toLocalDateTime()
            );
        }

        return flight;
    }
}
