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
import java.util.Optional;

public class AviationStackApi {
    private static final String AVIATION_STACK_BASE_URI = "https://api.aviationstack.com/v1";
    private static final String AVIATION_STACK_ACCESS_KEY = "?access_key=%s";
    private static final String AVIATION_STACK_IATA_FLIGHT_NUMBER = AVIATION_STACK_BASE_URI + "/flights" + AVIATION_STACK_ACCESS_KEY + "&flight_status=active" + "&flight_iata=%s";

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
        return Optional.ofNullable(
                convert(flightApiResponse.getData().get(0))
        );
    }

    private Flight convert(FlightData flightData) {
        final LiveInfo liveInfo = flightData.getLive();
        if(liveInfo.getLatitude() == null || liveInfo.getLongitude() == null) {
            return null;
        }

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

        flight.setBearing(liveInfo.getDirection());

        return flight;
    }
}
