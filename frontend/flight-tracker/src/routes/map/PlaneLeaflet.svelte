<script lang="ts">
    import type { LatLngExpression } from "leaflet";
    import Leaflet from "$lib/Leaflet.svelte";
    import Marker from "$lib/Marker.svelte";
    import Popup from "$lib/Popup.svelte";
    import PlaneSvg from "$lib/PlaneSvg.svelte";

    const initialView = [39.8283, -98.5795];
    let markerLocations: Array<LatLngExpression> = $state([
        [39.8283, -98.5795],
    ]);
</script>

<div class="w-full h-screen">
    <Leaflet view={initialView} zoom={14}>
        {#each markerLocations as latLang}
            <Marker {latLang} width={100} height={100}>
                <PlaneSvg />
                <Popup>Flight DL0123</Popup>
            </Marker>
        {/each}
    </Leaflet>
</div>
