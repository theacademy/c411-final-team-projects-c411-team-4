<script lang="ts">

import { onMount } from 'svelte';
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
  
    let airline ="";
    let depAirport ="";
    let arrAirport ="";
    loadAllFlights();
    let flights: Flight[] = [];
  
    onMount(async () => {
      loadAllFlights();
    });
    onMount(() => {
  loadAllFlights();
});

async function loadAllFlights() {
  try {
    const response = await fetch('http://localhost:8080/api/flights?page=0&size=10');
    const data: { content: Flight[] } = await response.json();
    flights = data.content;
  } catch (err) {
    console.error('Fetch error:', err);
  }
}
    // search flights, from and to
    async function searchFlights() {
    const params = new URLSearchParams();

    if (!airline && !depAirport && !arrAirport) {
    alert('Please enter at least one search filter.');
     return;
    }   
    
    const isOnlyAirline = airline && !depAirport && !arrAirport;

    if (isOnlyAirline) {
    params.append('airline', airline);
    } else if (depAirport && arrAirport) {
    params.append('departing', depAirport);
    params.append('arrival', arrAirport);
    } else if (depAirport) {
     params.append('airport', depAirport);
    } else if (arrAirport) {
     params.append('arrival', arrAirport);
    }


    params.append('page', '0');
    params.append('size', '10');

    const url = `http://localhost:8080/api/search?${params.toString()}`;

    try {
     const response = await fetch(url);
        const data: { content: Flight[] } = await response.json();
        flights = data.content;
    } catch (err) {
     console.error('Search error:', err);
    }
    }



</script>
  
  <div class="bg-sky-700 py-20">
    <h1 class="text-3xl font-bold text-white text-center">
      ✈️ Flight Tracker Search 🛫
    </h1>
  </div>
  
  <main class="flex flex-col min-h-screen bg-gray-50 py-10">
  
    

    <div class="bg-white p-4 rounded shadow flex flex-wrap justify-center gap-4 border border-gray-200 max-w-4xl mx-auto mt-6">
        <input
          bind:value={airline}
          placeholder="Airline (e.g. JetBlue)"
          class="border border-gray-300 rounded px-4 py-2 shadow-sm w-60"
        />
        <input
          bind:value={depAirport}
          placeholder="From (e.g. JFK)"
          class="border border-gray-300 rounded px-4 py-2 shadow-sm w-40"
        />
        <input
          bind:value={arrAirport}
          placeholder="To (e.g. LAX)"
          class="border border-gray-300 rounded px-4 py-2 shadow-sm w-40"
        />
        <button
          on:click={searchFlights}
          class="bg-sky-600 text-white rounded px-4 py-2 shadow-sm"
        >
          🔍 Search Flights
        </button>
      </div>

      <div class="max-w-3xl w-full mx-auto text-center mt-10">
        <h2 class="text-2xl font-bold text-gray-800 text-sky-600">
          Flights Available
        </h2>
      </div>
  
    <ul class="max-w-3xl w-full mx-auto space-y-4 mt-6">
        {#if flights.length === 0}
          <li class="bg-white border border-gray-200 rounded-lg p-4 text-center shadow-sm">
            No flights found.
          </li>
        {/if}
        
        {#each flights as flight}
          <li class="bg-white border border-gray-200 rounded-lg p-4 text-left shadow-sm">
            <div class="text-lg font-semibold text-sky-700">
              ✈️ {flight.airline?.name} — {flight.number}
            </div>
            <div class="text-sm text-gray-700 mb-1">
              {flight.depAirport?.code} ➡ {flight.arrAirport?.code}
            </div>
            <div class="text-sm text-gray-600">
              <strong>Departure:</strong> {flight.schedDeparture}
              <span class="mx-2">|</span>
              <strong>Arrival:</strong> {flight.estArrival}
            </div>
          </li>
        {/each}
      </ul>
      

</main>
  