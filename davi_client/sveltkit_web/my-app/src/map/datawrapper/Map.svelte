<script>
    // import {onMount} from "svelte";
    export let dataWrapperData = null;
    console.log("dataWrapperData", dataWrapperData);

    /*onMount(async() => {
        const response = await fetchChartData();
        chartData = response.data;
    });

    async function fetchChartData(){
        const apiKey = 'IDJe8lxnwi50baG6wWjnJTzAKy6TpU1JXS1TE4t7Bryl2t2qxkB3bU5CUajIQCd5';
        const data = {
            labels: ['January', 'February', 'March', 'April', 'May'],
            values: [15, 10, 12, 8, 20]
        };
        const type = { "title": "Where do people live?", "type": "d3-bars-stacked" };

        const response = await fetch('/api', {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${apiKey}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(type)
        });

        let resData = await response.json();
        return resData;
    }*/
</script>

{#if dataWrapperData}
    <div>
        <h2>{dataWrapperData.title}</h2>
        {#if dataWrapperData.url}
<!--            <img src={dataWrapperData.thumbnails.full} alt="Chart" />-->
        {:else}
            <p>No chart URL available.</p>
        {/if}
    </div>
    {:else}
        <p>Loading chart...</p>
{/if}

