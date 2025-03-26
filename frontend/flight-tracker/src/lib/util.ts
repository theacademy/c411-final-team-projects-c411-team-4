import L from "leaflet";
import { tick, createEventDispatcher } from "svelte";


export function createLeafletMap(mapElement: HTMLElement): L.Map {
    const dispatch = createEventDispatcher();
    let map = L.map(mapElement)
        .on("zoom", (e: any) => dispatch("zoom", e))
        .on("popupopen", async (e: any) => {
            await tick();
            e.popup.update();
        });
    return map;
}
