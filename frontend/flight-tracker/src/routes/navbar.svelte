<script lang="ts">
    import { onMount } from "svelte";

    let user: User | undefined;

    interface User {
        id: number;
        username: String;
        email: String;
    }

    onMount(async () => {
        const response = await fetch("http://localhost:8080/api/user/profile", {
            credentials: "include",
        });

        if (response.status == 200) {
            user = await response.json();
            localStorage.setItem("user", JSON.stringify(user));
        } else {
            localStorage.removeItem("user");
        }

        const event = new CustomEvent("profileLoaded");
        window.dispatchEvent(event);
    });
</script>

<nav class="bg-sky-800 text-white p-4 flex justify-between items-center">
    <div class="text-xl font-bold flex items-center space-x-2">
        <span>✈️</span>
        <a href="/">Flight Tracker</a>
    </div>

    <ul class="flex space-x-4">
        <li><a href="/">Home</a></li>
        <li><a href="/about">About</a></li>
        {#if user}
            <li><a href="/map">Map</a></li>
            <li><a href="/profile">Profile</a></li>
        {:else}
            <li><a href="/login">Login</a></li>
        {/if}
    </ul>
</nav>

