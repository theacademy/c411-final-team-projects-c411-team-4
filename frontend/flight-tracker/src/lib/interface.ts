interface Airport {
    id: number;
    name: string;
    code: string;
    latitude: number;
    longitude: number;
}

interface Airline {
    id: number;
    name: string;
    code: string;
}

interface Status {
    id: number;
    status: string;
}

interface Flight {
    id: number;
    status: Status;
    depAirport: Airport;
    arrAirport: Airport;
    number: number;
    airline: Airline;
    latitude: number | null;
    longitude: number | null;
    estArrival: string;
    schedArrival: string;
    schedDeparture: string;
    estDeparture: string;
}
