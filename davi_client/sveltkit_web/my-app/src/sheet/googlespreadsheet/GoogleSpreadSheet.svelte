<script>
    import {onMount} from "svelte";

    const CLIENT_ID = '186983649065-sigq0i4e7d2g91q4fsfa8q3303p8ngvc.apps.googleusercontent.com';
    const API_KEY = 'AIzaSyAx5Gu-0OUoz_UVT54ZvoYFlu1zH9tycyc';
    const DISCOVERY_DOCS = ["https://sheets.googleapis.com/$discovery/rest?version=v4"];
    const SCOPES = "https://www.googleapis.com/auth/spreadsheets.readonly";

    onMount(() => {
        // create('testGoogle', false);
    });

    function create(title, callback){
        try{
            gapi.client.sheets.spreadsheets.create({
                properties: {
                    title: title,
                },
            }).then((response) => {
                if(callback) callback(response);
                console.log('Spreadsheet ID: ' + response.result.spreadsheetId);
            });
        } catch (err){
            document.getElementById('spread-sheet-content').innerText = err.message;
            return;
        }
    }
</script>


<div id="spread-sheet-content"></div>
<iframe src="https://docs.google.com/spreadsheets/d/e/2PACX-1vTFhiINN0urfhf-jkRnoo4g2ox6JeR3uBt59aDslnF_s9bDUJK2G0hmMSH3v5Sr5LPT5-U-1Q_0CJI2/pubhtml?widget=true&amp;headers=false"></iframe>
<!--<iframe src="-->
<!--        https://docs.google.com/spreadsheets/d/1xqsfn5lSDU3V0wABz1Ek-LH5-VsFX6XMFbNedmdSyng/edit#gid=0/pubhtml?gid=0&single=true/sheet?headers=false&gid=0&range=A1:D13"-->
<!--        width="700"-->
<!--        height="350"-->
<!--        frameborder="0">-->
<!--</iframe>-->
<style>
    iframe{
        border: none;
        height: 400px;
        width: 588px;
    }
</style>