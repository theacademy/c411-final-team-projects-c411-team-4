<script lang="js">
    import PlaneMap from "./PlaneMap.svelte";

    const planes = [
        {
            id: 1,
            name: "Flight AA123",
            latitude: 40.6413,
            longitude: -73.7781,
            rotation: 0,
            heading: 270,
            speed: 0.2,
            iconUrl: "https://buckets.kmfg.dev/mthree/plane.png",
        },
        {
            id: 2,
            name: "Flight DL456",
            latitude: 55.1231,
            longitude: -98.5705,
            rotation: 0,
        },
    ];

    const airports = [
        {
            id: "jfk",
            code: "JFK",
            latitude: 40.6413,
            longitude: -73.7781,
        },
        {
            id: "lax",
            code: "LAX",
            latitude: 33.9416,
            longitude: -118.4085,
        },
        {
            id: "chs",
            code: "CHS",
            latitude: 32.8986,
            longitude: -80.0407,
        },
    ];

    const routes = [
        {
            id: "jfk-lax",
            fromAirportId: "jfk",
            toAirportId: "lax",
            color: "blue",
            weight: 2,
        },
        {
            id: "jfk-chs",
            fromAirportId: "jfk",
            toAirportId: "chs",
            color: "green",
            weight: 2,
        },
    ];
</script>

<PlaneMap {planes} {airports} {routes} />
