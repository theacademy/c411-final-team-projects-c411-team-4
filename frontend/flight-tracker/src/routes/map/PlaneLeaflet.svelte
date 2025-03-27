<script lang="ts">
    import type { LatLngExpression } from "leaflet";
    import Leaflet from "$lib/Leaflet.svelte";
    import { onDestroy, onMount } from "svelte";
    import L from "leaflet";
    import { createLeafletMap } from "$lib/util";
    import SearchFlight from "./SearchFlight.svelte";

    const PLANE_POS_QUERY_INTERVAL_MS = 1000;

    const initialView = [39.8283, -98.5795];

    let planeNumberToMarker: Map<String, L.Marker> = new Map();

    let mapElement: HTMLElement;
    let map: L.Map | undefined = undefined;
    let activePopupMarker: L.Marker | null = null;

    let intervalId: number = 0;

    onMount(() => {
        map = createLeafletMap(mapElement);

        grabPlanePositions();
    });

    onDestroy(() => {
        clearInterval(intervalId);
    });

    function createPlaneIcon(rotation: number = 0) {
        const html = `<div class="plane-image" style="transform: rotate(${rotation}deg);">
                        <img src="https://buckets.kmfg.dev/mthree/plane.png" class="plane-image"/>
                      </div>`;
        return L.divIcon({
            html,
            className: "plane-image",
        });
    }

    /**
     * Grabs plane positions from the API.
     * Data is currently mocked.
     */
    function grabPlanePositions() {
        intervalId = setInterval(() => {
            let markers = planeNumberToMarker.values();
            for (const marker of markers) {
                updatePlanePosition(marker);

                if (activePopupMarker === marker && map) {
                    map.panTo(marker.getLatLng());
                }
            }
        }, PLANE_POS_QUERY_INTERVAL_MS);
    }

    function calculateBearing(start: number[], end: number[]) {
        const latOne = (Math.PI * start[0]) / 180;
        const longOne = (Math.PI * start[1]) / 180;
        const latTwo = (Math.PI * end[0]) / 180;
        const longTwo = (Math.PI * end[1]) / 180;

        const dLong = longTwo - longOne;

        const y = Math.sin(dLong) * Math.cos(latTwo);
        const x =
            Math.cos(latOne) * Math.sin(latTwo) -
            Math.sin(latOne) * Math.cos(latTwo) * Math.cos(dLong);

        const bearing = (Math.atan2(y, x) * 180) / Math.PI;

        return (bearing + 360) % 360;
    }

    function updatePlanePosition(marker: L.Marker) {
        const latLngObj = marker.getLatLng();
        const previousPos = [latLngObj.lat, latLngObj.lng];
        const addToCurrent = calculateNewTargets();
        latLngObj.lat += addToCurrent[0];
        latLngObj.lng += addToCurrent[1];
        const newPos = [latLngObj.lat, latLngObj.lng];
        const bearing = calculateBearing(previousPos, newPos);
        marker.setIcon(createPlaneIcon(bearing));
        marker.setLatLng(latLngObj);
    }

    function calculateNewTargets(): number[] {
        const angle = Math.PI / 4;

        const minStep = 0.05 / 16;
        const maxStep = 0.15 / 16;

        const stepSize = Math.random() * (maxStep - minStep) + minStep;

        const latChange = stepSize * Math.cos(angle);
        const lonChange = stepSize * Math.sin(angle);

        return [latChange, lonChange];
    }

    function addFlightToMap(iataFlightNumber) {
        let newPos = [...initialView];
        newPos[0] += Math.random();
        newPos[1] += Math.random();
        let planeMarker = L.marker(newPos, {
            icon: createPlaneIcon(),
            interactive: true,
            bubblingMouseEvents: false,
        }).addTo(map);

        planeMarker.on("popupopen", () => {
            activePopupMarker = planeMarker;
            if (map) {
                map.panTo(planeMarker.getLatLng());
            }
        });

        planeMarker.on("popupclose", () => {
            if (activePopupMarker === planeMarker) {
                activePopupMarker = null;
            }
        });

        planeMarker.bindPopup("IATA #" + iataFlightNumber).openPopup();

        activePopupMarker = planeMarker;
        if (map) {
            map.panTo(planeMarker.getLatLng());
            map.setZoom(14);
        }

        planeNumberToMarker.set(iataFlightNumber, planeMarker);
    }

    function trackThis(iataFlightNumber) {
        addFlightToMap(iataFlightNumber);
    }
</script>

<SearchFlight flightAdd={trackThis} />
<div class="w-full h-screen" bind:this={mapElement}>
    {#if map}
        <!-- We are creating the leaflet map like this in order to isolate it from the rest of the program. -->
        <Leaflet {map} view={initialView} zoom={6}></Leaflet>
    {/if}
</div>
