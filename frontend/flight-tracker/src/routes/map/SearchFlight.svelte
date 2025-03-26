<script lang="ts">
    import { Button } from "flowbite-svelte";
    import { PlusOutline } from "flowbite-svelte-icons";

    let { flightAdd, iataFlightNumber } = $props();

    function clicked() {
        // cant check something that doesnt exist
        if (iataFlightNumber == undefined || iataFlightNumber == null) {
            return;
        }

        iataFlightNumber = iataFlightNumber.trim();
        // dont click an empty box
        if (iataFlightNumber == "") {
            return;
        }

        if (iataFlightNumber.length != 6) {
            window.alert("An IATA flight number must be 6 characters long.");
            return;
        }

        flightAdd(iataFlightNumber);
        iataFlightNumber = "";
    }
</script>

<div class="flex justify-center items-center gap-4 py-4 bg-sky-700">
    <div class="flex items-center gap-2">
        <input
            type="text"
            placeholder="Enter IATA Flight Number"
            class="rounded-lg bg-white border border-gray-300 p-2.5 w-60 focus:ring-blue-500 focus:border-blue-500"
            bind:value={iataFlightNumber}
        />
        <Button
            class="p-2.5 bg-white text-white rounded-lg hover:cursor-pointer"
            onclick={clicked}
        >
            <PlusOutline class="invert w-5 h-5" />
        </Button>
    </div>
</div>

