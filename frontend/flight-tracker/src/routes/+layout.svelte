<script lang="js">
    let { children } = $props();
    import "../app.css";
    import Navbar from "./navbar.svelte";
</script>

<Navbar />
{@render children()}
