<script>
    import { onMount } from 'svelte';

    let header_explain_visible = false;

    function typewriter(node, { speed = 3 }) {
		const valid = node.childNodes.length === 1 && node.childNodes[0].nodeType === Node.TEXT_NODE;

		if (!valid) {
			throw new Error(`This transition only works on elements with a single text node child`);
		}

		const text = node.textContent;
		const duration = text.length / (speed * 0.01);

		return {
			duration,
			tick: (t) => {
				const i = Math.trunc(text.length * t);
				node.textContent = text.slice(0, i);
			}
		};
	}

    onMount(async () => {
        header_explain_visible = true;
    });

</script>
    
<header class="jumbotron text-center header">
    <h1>Kenny Company</h1>
    {#if header_explain_visible}
    <p transition:typewriter>
        Tell me What you need. We're developing a program for you</p>
    {/if}
</header>

<style>
    .header{
        margin-bottom: 0;
    }
</style>