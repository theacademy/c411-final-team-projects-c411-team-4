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
    let flights: Flight[] = [];
  
    onMount(async () => {
      try {
        const response = await fetch('http://localhost:8080/api/flights?page=0&size=10');
        const data: { content: Flight[] } = await response.json();
        flights = data.content;
        flights = data.content; // THIS is key: `data.content`
      } catch (err) {
        console.error('Fetch error:', err);
      }
    });

    // search flights, from and to
    async function searchFlights() {
    const params = new URLSearchParams();



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
  
    <h2 class="text-2xl font-bold text-gray-800 mt-10 text-sky-600">
      Flights Available
    </h2>
  
    <ul>
      {#each flights as flight}
        <li>
          <div class="flex justify-between text-sm text-gray-700"></div>
          <span> ✈️ {flight.airline?.name} — {flight.id}</span>
          <span>{flight.depAirport?.code} ➡ {flight.arrAirport?.code} </span>
          <p>
            <strong>Departure:</strong> {flight.schedDeparture}
            <span class="mx-2">|</span>
            <strong>Arrival:</strong> {flight.estArrival}
          </p>
        </li>
      {/each}
    </ul>

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
 

</main>
  