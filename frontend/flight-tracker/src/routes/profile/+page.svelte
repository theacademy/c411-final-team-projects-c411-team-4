<script lang="ts">
    import { goto } from "$app/navigation";
    import { onMount } from "svelte";

    let user: any;
    let isEditing = false;
    let editedUser = { username: "", email: "", id: "" };
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
        isEditing = !isEditing;
        if (!isEditing) {
            editedUser = { ...user };
        }
        successMessage = "";
        errorMessage = "";
    }

    async function saveChanges() {
        try {
            user = { ...editedUser };
            localStorage.setItem("user", JSON.stringify(user));

            successMessage = "Profile updated successfully!";
            errorMessage = "";
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

<button
    on:click={logout}
    class="hover:cursor-pointer hover:bg-red-900 transition-all focus:outline-none text-white bg-red-700 focus:ring-4 focus:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-red-600 dark:hover:bg-red-700 dark:focus:ring-red-900"
>
    Logout
</button>
