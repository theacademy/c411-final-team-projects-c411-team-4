<script>
    import { onMount } from 'svelte';
  
    /** @type {any[]} */
    let flights = [];
    onMount(async () => {
  fetch("http://localhost:8080/api/flights?page=0&size=10")
  .then(response => response.json())
  .then(data => {
		console.log(data);
    apiData.set(data);
  }).catch(error => {
    console.log(error);
    return [];
  });
});
</script>
  
  
  <h1>Flights</h1>
  
  <ul>
    {#each flights as flight}
      <li>
        ✈️ {flight.airline.name} — {flight.flightNumber}<br />
        {flight.depAirport.code} → {flight.arrAirport.code}<br />
        Departure: {flight.scheduledDeparture}<br />
        Arrival: {flight.estimatedArrival}
      </li>
    {/each}
  </ul>
  
  