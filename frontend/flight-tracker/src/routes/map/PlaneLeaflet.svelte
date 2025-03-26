<script lang="ts">
    import type { LatLngExpression } from "leaflet";
    import Leaflet from "$lib/Leaflet.svelte";
    import { onMount } from "svelte";
    import L from "leaflet";
    import { createLeafletMap } from "$lib/util";

    const initialView = [39.8283, -98.5795];

    let mapElement: HTMLElement;
    let map: L.Map | undefined = undefined;

    onMount(() => {
        map = createLeafletMap(mapElement);
    });
</script>

<div class="w-full h-screen" bind:this={mapElement}>
    {#if map}
        <Leaflet {map} view={initialView} zoom={14}></Leaflet>
    {/if}
</div>
