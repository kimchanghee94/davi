<script>
    import Map from "../../map/mapbox/Map.svelte";
    import {transData} from "$lib/store.js";

    let sqlJsResult = null;
    transData.subscribe((value) => {
        sqlJsResult = value;
    })

</script>
<Map {sqlJsResult}/>
