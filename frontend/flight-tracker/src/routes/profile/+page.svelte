<script lang="ts">
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";

    let user: any;
    let isEditing = false;
    let editedUser: any;
    let successMessage = "";
    let errorMessage = "";

    onMount(() => {
        window.addEventListener("profileLoaded", (_) => {
            user = localStorage.getItem("user");
            if (!user) {
                goto("/login");
            } else {
                user = JSON.parse(user);
                editedUser = { ...user };
            }
        });
    });

    function toggleEdit() {
        if (isEditing) {
            editedUser = { ...user };
        }
        isEditing = !isEditing;
        successMessage = "";
        errorMessage = "";
    }

    async function saveChanges() {
        try {
            const haveEmail =
                editedUser.email != null && editedUser.email.trim() != "";
            const havePassword =
                editedUser.password != null && editedUser.password.trim() != "";

            const sendEditedUser = {
                email: haveEmail ? editedUser.email : null,
                password: havePassword ? editedUser.password : null,
            };

            const response = await fetch(
                "http://localhost:8080/api/user/profile",
                {
                    credentials: "include",
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(sendEditedUser),
                },
            );

            if (response.status != 200) {
                errorMessage = await response.text();
                if (errorMessage.length > 32) {
                    errorMessage = errorMessage.substring(0, 23);
                }
                errorMessage = `ERROR ${response.status}: ${errorMessage}...`;
            } else {
                successMessage = "Profile updated successfully!";
                editedUser = await response.json();
                user = { ...editedUser };
                localStorage.setItem("user", JSON.stringify(user));
            }

            isEditing = false;
        } catch (error) {
            errorMessage = "Failed to update profile. Please try again.";
            successMessage = "";
        }
    }

    async function logout() {
        await fetch("http://localhost:8080/api/user/logout", {
            credentials: "include",
        });
        window.location.reload();
    }
</script>

<div class="min-h-screen bg-gray-50 flex items-center justify-center py-8 px-4">
    <div class="max-w-xl w-full">
        <div
            class="bg-gradient-to-r from-sky-600 to-sky-400 shadow-lg rounded-t-lg py-6 px-4 text-center"
        >
            {#if user}
                <h1 class="text-4xl font-bold text-white">
                    Welcome, {user.username}!
                </h1>
                <div class="mt-2">
                    <span class="text-gray-200">Not {user.username}?&nbsp;</span
                    >
                    <a
                        on:click={logout}
                        class="underline text-gray-100 hover:text-white cursor-pointer transition-colors"
                        >Logout</a
                    >
                </div>
            {/if}
        </div>
        <div class="bg-white shadow-md rounded-b-lg p-6">
            {#if successMessage}
                <p class="mb-4 text-center text-green-600 font-medium">
                    {successMessage}
                </p>
            {/if}

            {#if errorMessage}
                <p class="mb-4 text-center text-red-600 font-medium">
                    {errorMessage}
                </p>
            {/if}

            {#if user}
                <div class="space-y-4">
                    <div>
                        <label class="block text-gray-700 font-semibold mb-1"
                            >Email</label
                        >
                        {#if isEditing}
                            <input
                                class="w-full border border-gray-300 rounded-lg p-2 shadow-sm focus:outline-none focus:border-sky-400"
                                type="text"
                                bind:value={editedUser.email}
                            />
                        {:else}
                            <input
                                class="w-full border border-gray-100 bg-gray-100 text-gray-500 rounded-lg p-2 cursor-not-allowed"
                                type="text"
                                value={user.email}
                                disabled
                            />
                        {/if}
                    </div>
                    <div>
                        <label class="block text-gray-700 font-semibold mb-1"
                            >New Password</label
                        >
                        {#if isEditing}
                            <input
                                class="w-full border border-gray-300 rounded-lg p-2 shadow-sm focus:outline-none focus:border-sky-400"
                                type="text"
                                bind:value={editedUser.password}
                            />
                        {:else}
                            <input
                                class="w-full border border-gray-100 bg-gray-100 text-gray-500 rounded-lg p-2 cursor-not-allowed"
                                type="text"
                                value={user.password}
                                disabled
                            />
                        {/if}
                    </div>
                    <div class="flex justify-end space-x-3 pt-2">
                        {#if isEditing}
                            <button
                                class="bg-red-500 hover:bg-red-600 text-white font-semibold py-2 px-4 rounded-lg transition duration-200"
                                on:click={saveChanges}>Save</button
                            >
                            <button
                                class="bg-gray-300 hover:bg-gray-400 text-black font-semibold py-2 px-4 rounded-lg transition duration-200"
                                on:click={toggleEdit}>Cancel</button
                            >
                        {:else}
                            <button
                                class="bg-sky-500 hover:bg-sky-600 text-white font-semibold py-2 px-4 rounded-lg transition duration-200"
                                on:click={() => (isEditing = true)}>Edit</button
                            >
                        {/if}
                    </div>
                </div>
            {/if}
        </div>
    </div>
</div>
