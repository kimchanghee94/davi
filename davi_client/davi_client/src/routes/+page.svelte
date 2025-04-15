<script>
    import {onMount} from "svelte";
    import {testPageLoad} from "./+page.server.js";

    export let data;

    onMount(async () => {
        console.log('Page loaded', data);

    });

    async function handleClick() {
        await testPageLoad();
    }
</script>


<main>
    <h1>Welcome to SvelteKit SSR Example</h1>
    <button on:click={handleClick}>Click me</button>
</main>