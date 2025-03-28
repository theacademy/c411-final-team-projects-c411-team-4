<script lang="ts">
    import {
        onMount,
        onDestroy,
        setContext,
        createEventDispatcher,
        tick,
    } from "svelte";
    import L from "leaflet";
    import "leaflet/dist/leaflet.css";

    export let bounds: L.LatLngBoundsExpression | undefined = undefined;
    export let view: L.LatLngExpression | undefined = undefined;
    export let zoom: number | undefined = undefined;

    const dispatch = createEventDispatcher();

    export let map: L.Map | undefined;
    export let mapElement: HTMLElement;

    onMount(() => {
        if (!bounds && (!view || !zoom)) {
            throw new Error("Must set either bounds, or view and zoom.");
        }
        if (!map) {
            throw new Error("You must provide a map!");
        }

        L.tileLayer(
            "https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png",
            {
                attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,
					&copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
                subdomains: "abcd",
                maxZoom: 14,
            },
        ).addTo(map);
    });

    onDestroy(() => {
        map?.remove();
        map = undefined;
    });

    setContext("map", {
        getMap: () => map,
    });

    $: if (map) {
        if (bounds) {
            map.fitBounds(bounds);
        } else if (view && zoom) {
            map.setView(view, zoom);
        }
    }
</script>

<div class="w-full h-full" bind:this={mapElement}>
    {#if map}
        <slot />
    {/if}
</div>
