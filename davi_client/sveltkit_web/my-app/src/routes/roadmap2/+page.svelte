<script>
    import Map2 from "../../map/mapbox/Map2.svelte";
    import {transData} from "$lib/store.js";

    let sqlJsResult = null;
    transData.subscribe((value) => {
        sqlJsResult = value;
    })

</script>
<Map2 {sqlJsResult}/>