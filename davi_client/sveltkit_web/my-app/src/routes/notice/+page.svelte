<script>
    // import Cookies from 'js-cookie';

    export let data;
    let items = (data.body !== undefined ? data.body.items : false);

    // import { onMount } from "svelte";

    // onMount(async()=>{
    //     const sessionId = Cookies.get('SESSION_ID');
    //     const response = await fetch('', {
    //         headers: {
    //             'x-auth-token': sessionId
    //         }
    //     });
    //     console.log("notice",sessionId);
    //     console.log("notice",response);
    // });

</script>

<div id="notice-container">
    <h1>공지사항 홈페이지</h1>
    <div>
        {#if items}
            {#each items as item}
                <p>id: {item.id}, title: {item.title}</p>
            {/each}
        {/if}
    </div>
</div>