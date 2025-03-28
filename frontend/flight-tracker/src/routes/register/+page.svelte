<script lang="ts">
    import { goto } from "$app/navigation";

    let username: String = "";
    let email: String = "";
    let password: String = "";
    let confirmPassword: String = "";
    let user: any;

    async function register(e: any) {
        e.preventDefault();

        const newUser = {
            username,
            email,
            password
        };

        if (password !== confirmPassword) {
        alert("Passwords do not match.");
        return;
        }


        const response = await fetch("http://localhost:8080/api/user/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(newUser),
            credentials: "include",
        });

        if (response.status == 200) {
            window.location.href = "/profile";
        } else {
            alert("Registration failed. Please try again.");
        }
    }

</script>


<main class="grid grid-cols-1 md:grid-cols-2 gap-4 md:grid-cols-2 md:gap-8 h-screen items-center justify-center p-6 md:p-0">

    <div class="hidden md:flex items-center justify-center bg-sky-700 text-white p-10">
        <div class="text-center">
          <h2 class="text-4xl font-bold mb-4">Join Flight Tracker</h2>
          <p class="text-lg">Create your account to track flights and manage your trips effortlessly.</p>
        </div>
      </div>

    <div class="w-full bg-white rounded-lg shadow dark:border md:mt-0 sm:max-w-md xl:p-0 dark:bg-gray-600 dark:border-gray-700"> 
        <div class="p-6 space-y-4 md:space-y-6 sm:p-8">
            <h1 class="text-xl font-bold leading-tight tracking-tight text-gray-900 md:text-2xl dark:text-white">
                Register for an account
            </h1>
        
            <form class="space-y-4 md:space-y-6" on:submit|preventDefault={register}>
            <div>
                <label for="userName" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">User Name</label>
                <input 
                bind:value={username}
                type="text" 
                name="userName" 
                id="userName"
                placeholder="Username"
                class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-sky-600 focus:border-sky-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-sky-500 dark:focus:border-sky-500" required>
            </div>

            <div>
                <label for="email" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Email address</label>
                <input 
                bind:value={email}
                type="email" 
                name="email" 
                id="email" 
                class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-sky-600 focus:border-sky-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-sky-500 dark:focus:border-sky-500" placeholder="name@company.com" required>
            </div>

            <div>
                <label for="password" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Password</label>
                <input 
                bind:value={password} 
                type="password"
                name="password" id="password" 
                placeholder="••••••••" 
                class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-sky-600 focus:border-sky-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-sky-500 dark:focus:border-sky-500" required>
            </div>

            <div>
                <label for="confirmPassword" class="block mb-2 text-sm font-medium text-gray-900 dark:text-white">Confirm Password</label>
                <input bind:value={confirmPassword} 
                type="password" 
                name="confirmPassword" 
                id="confirmPassword" 
                placeholder="••••••••" 
                class="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-sky-600 focus:border-sky-600 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-sky-500 dark:focus:border-sky-500" required>
            </div>

            <button type="submit" class="w-full text-white bg-sky-600 hover:bg-sky-700 focus:ring-4 focus:outline-none focus:ring-sky-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-sky-600 dark:hover:bg-sky-700 dark:focus:ring-sky-800">Register</button>
            <p class="text-sm font-light text-gray-500 dark:text-gray-400">
                Already have an account? <a href="/login" class="font-medium text-sky-600 hover:underline dark:text-sky-500">Sign in</a> 
            </p>
        </form>
        </div>
    </div>
</main>