<script lang="js">
    let airline = "";
    let departure = "";
    let arrival = "";
    let flights = [];

    const mockFlights = [
        {
            id: 1,
            flightNumber: "AC101",
            airline: "Air Canada",
            departure: "YUL",
            arrival: "YYZ",
            scheduledDeparture: "2025-03-24T08:00:00",
            scheduledArrival: "2025-03-24T09:30:00",
        },
        {
            id: 2,
            flightNumber: "WS202",
            airline: "WestJet",
            departure: "YVR",
            arrival: "YYC",
            scheduledDeparture: "2025-03-24T10:00:00",
            scheduledArrival: "2025-03-24T11:15:00",
        },
        {
            id: 3,
            flightNumber: "AC202",
            airline: "Air Canada",
            departure: "YUL",
            arrival: "YVR",
            scheduledDeparture: "2025-03-25T14:00:00",
            scheduledArrival: "2025-03-25T17:30:00",
        },
    ];

    const searchFlights = () => {
        flights = mockFlights.filter(
            (f) =>
                (!airline ||
                    f.airline.toLowerCase().includes(airline.toLowerCase())) &&
                (!departure || f.departure === departure.toUpperCase()) &&
                (!arrival || f.arrival === arrival.toUpperCase()),
        );
    };
</script>

  

<main class="flex flex-col min-h-screen bg-gray-50">

    <div class="bg-sky-700 py-20">
        <h1 class="text-3xl font-bold text-white text-center">
          ✈️ Flight Tracker Search 🛫
        </h1>
      </div>
  

    <div class="bg-white p-4 rounded shadow flex flex-wrap justify-center gap-4 border border-gray-200">
        <div class="flex flex-wrap justify-center gap-4">
        <input
         bind:value={airline} 
         placeholder="Airline (e.g. Air Canada)"
         class="border border-gray-300 rounded px-4 py-2 shadow-sm" />

        <input 
        bind:value={departure} 
        placeholder="From (e.g. YUL)"
        class="border border-gray-300 rounded px-4 py-2 shadow-sm"  />

        <input
         bind:value={arrival} 
         placeholder="To (e.g. YYZ)" 
        class="border border-gray-300 rounded px-4 py-2 shadow-sm" />
        

        <button on:click={searchFlights}
            class="bg-sky-600 text-white rounded px-4 py-2 shadow-sm">
            🔍 Search Flights
        </button>
    </div>
    </div>

    {#if flights.length > 0}
        <h2>Results</h2>
        <ul>
            {#each flights as f}
                <li>
                    ✈️ {f.airline}
                    {f.flightNumber}<br />
                    {f.departure} → {f.arrival}<br />
                    Departure: {f.scheduledDeparture}<br />
                    Arrival: {f.scheduledArrival}
                </li>
            {/each}
        </ul>
    {:else}
        <p>No flights found.</p>
    {/if}
</main>

<style>

    main {
        font-family: sans-serif;
        padding: 2rem;
        text-align: center;
    }

    input {
        margin: 0.5rem;
        padding: 0.5rem;
        font-size: 1rem;
        width: 200px;
    }

    button {
        padding: 0.5rem 1rem;
        font-size: 1rem;
        margin-top: 0.5rem;
        cursor: pointer;
    }

    ul {
        list-style: none;
        padding: 0;
    }

    li {
        margin: 1rem 0;
        background: #f4f4f4;
        padding: 1rem;
        border-radius: 10px;
    }
</style>
