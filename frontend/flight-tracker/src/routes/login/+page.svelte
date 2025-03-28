<script lang="ts">
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";

    let username: String = "";
    let password: String = "";
    let user: any;

    onMount(() => {
        window.addEventListener("profileLoaded", (_) => {
            user = localStorage.getItem("user");
        });
    });

    async function login(e: any) {
        e.preventDefault();

        const loginUser = {
            username,
            password,
        };

        const response = await fetch("http://localhost:8080/api/user/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(loginUser),
            credentials: "include",
        });

        if (response.status == 401) {
            alert("Incorrect username or password.");
        }

        if (response.status == 200) {
            window.location.href = "/profile";
        }
    }
</script>

<main
    class="grid grid-cols-1 md:grid-cols-2 gap-4 md:grid-cols-2 md:gap-8 h-screen items-center justify-center p-6 md:p-0 bg-sky-200"
>
    <div
        class="hidden md:flex items-center justify-center bg-sky-700 text-white p-10"
    >
        <div class="text-center">
            <h2 class="text-4xl font-bold mb-4">Welcome Back!</h2>
        </div>
    </div>

    <div
        class="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-600 dark:border-gray-700"
    >
        <form class="p-6 space-y-4 md:space-y-6 sm:p-8" on:submit={login}>
            {#if user}
                <h1
                    class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white"
                >
                    You are already logged in!
                </h1>
                <button
                    type="button"
                    on:click={() => goto("/profile")}
                    class="w-full hover:cursor-pointer text-white bg-sky-600 hover:bg-sky-700 focus:ring-4 focus:outline-none focus:ring-sky-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-sky-600 dark:hover:bg-sky-700 dark:focus:ring-sky-800"
                    >Go to Profile</button
                >
            {:else}
                <h1
                    class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white"
                >
                    Sign in to your account
                </h1>
                <div class="space-y-4 md:space-y-6">
                    <div>
                        <label
                            for="username"
                            class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                            >Your username</label
                        >
                        <input
                            type="username"
                            name="username"
                            id="username"
                            class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-sky-600 focus:border-sky-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-sky-500 dark:focus:border-sky-500"
                            placeholder="username"
                            required
                            bind:value={username}
                        />
                    </div>
                    <div>
                        <label
                            for="password"
                            class="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                            >Password</label
                        >
                        <input
                            bind:value={password}
                            type="password"
                            name="password"
                            id="password"
                            placeholder="••••••••"
                            class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-sky-600 focus:border-sky-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-sky-500 dark:focus:border-sky-500"
                            required
                        />
                    </div>
                    <button
                        type="submit"
                        class="w-full hover:cursor-pointer text-white bg-sky-600 hover:bg-sky-700 focus:ring-4 focus:outline-none focus:ring-sky-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-sky-600 dark:hover:bg-sky-700 dark:focus:ring-sky-800"
                        >Sign in</button
                    >
                    <p
                        class="text-sm font-light text-gray-500 dark:text-gray-400"
                    >
                        Don’t have an account yet? <a
                            href="/register"
                            class="font-medium text-sky-600 hover:underline dark:text-sky-500"
                            >Sign up</a
                        >
                    </p>
                </div>
            {/if}
        </form>
    </div>
</main>
