<script>
    import {afterUpdate, onMount} from "svelte";
    import GoogleSpreadSheet from "../../sheet/googlespreadsheet/GoogleSpreadSheet.svelte";

    const jq = window.$;
    export let tableData = null;
    export let childComponentFlag = null;
    export let firstData;
    export let secondData;

    let jsonToTableData = [];
    let showTableData = [];
    let lastFlag = false;

    function IsJsonString(str) {
        try {
            var json = JSON.parse(JSON.stringify(str));
            return (typeof json === 'object');
        } catch (e) {
            return false;
        }
    }

    function jsonToTableFunc(jsonData, data, arrFlag){
        let tmpObject = JSON.parse(JSON.stringify(data));
        let tmpFlag = false;

        //{}객체 처리
        for(let key in jsonData){
            if(IsJsonString(jsonData[key]) && !Array.isArray(jsonData[key])){
                if(Object.keys(jsonData[key]).length === 0){
                    tmpObject[key] = "";
                    continue;
                }

                tmpFlag = true;
                let tmpObject2 = jsonToTableFunc(jsonData[key], tmpObject, true);
                if(arrFlag === true && lastFlag === true){
                    jsonToTableData.push(tmpObject2);
                    lastFlag = false;
                }
                else{
                    for(let key2 in tmpObject2){
                        tmpObject[key2] = tmpObject2[key2];
                    }
                }
            }
        }

        //value 처리
        for(let key in jsonData){
            if(!IsJsonString(jsonData[key])){
                tmpObject[key] = jsonData[key];
            }
            else if(Array.isArray(jsonData[key]) && !IsJsonString(jsonData[key][0])){
                tmpObject[key] = jsonData[key];
            }
        }

        //array 처리
        for(let key in jsonData){
            if(Array.isArray(jsonData[key]) && !IsJsonString(jsonData[key][0])){
                continue;
            }
            if(IsJsonString(jsonData[key]) && Array.isArray(jsonData[key])){
                tmpFlag = true;
                jsonToTableFunc(jsonData[key], tmpObject, true);
            }
        }

        //더이상 재귀를 타지 않는다.
        if(tmpFlag === false){
            lastFlag = true;
        }

        //재귀적으로 데이터 내부에 배열 형태가 존재하지 않을 경우
        if(tmpFlag === false && arrFlag === false && lastFlag === true){
            jsonToTableData.push(tmpObject);
        }

        return tmpObject;
    }

    function showOnylSelectData(selectHeader){
        let resultArray = new Array();

        for(let i=0; i<jsonToTableData.length; i++){
            let tmpArray = new Array();
            let tmpData = jsonToTableData[i];

            for(let j=0; j<selectHeader.length; j++){
                if(tmpData[selectHeader[j]] === undefined){
                    tmpArray.push("null");
                }else{
                    tmpArray.push(tmpData[selectHeader[j]]);
                }
            }
            resultArray.push(tmpArray);
        }

        return resultArray;
    }

    function saveFirstData(){
        console.log("SAVE FIRST DATA");
        childComponentFlag = false;

        if(firstData === null){
            firstData = new Object();
            firstData["header"] = tableData.values;
            firstData["body"] = showTableData;
            alert("첫번째 데이터 저장 완료");
        }else if(firstData !== null && secondData === null){
            secondData = new Object();
            secondData["header"] = tableData.values;
            secondData["body"] = showTableData;
            alert("두번째 데이터 저장 완료");
        }

    }

    onMount(() => {
        jq('table').tableCellsSelection();
    });

    afterUpdate(() => {
        jsonToTableData = [];
        showTableData = [];
        lastFlag = false;

        if(tableData.jsonData !== null){
            for(let i=0; i<tableData.jsonData.length; i++){
                jsonToTableFunc(tableData.jsonData[i], new Object(), false);
            }

            showTableData = showOnylSelectData(tableData.values);
        }
    });
</script>

{#if childComponentFlag === true}
    <table>
        <thead>
        <tr>
            <th>No.</th>
            {#each tableData.values as item}
            <th>{item}</th>
            {/each}
        </tr>
        </thead>
        <tbody>
        {#each showTableData as item, index}
        {#if index < 10}
            <tr>
            <td style="font-weight: bold; pointer-events: none">{index + 1}</td>
            {#each showTableData[index] as item}
            <td>{item}</td>
            {/each}
        </tr>
        {/if}
        {/each}
        </tbody>
    </table>
        <button class="btn btn-warning"
            style="width: 300px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-bottom: 50px"
            type="button" on:click="{saveFirstData}">확인 완료
    </button>
{/if}

<!--<GoogleSpreadSheet/>-->

<style>
    table{
        margin: 10px 10px 10px 10px;
    }

    table, th, td {
        border: 1px solid black;
        text-align: center;
        -ms-user-select: none;
        -moz-user-select: -moz-none;
        -khtml-user-select: none;
        -webkit-user-select: none;
        user-select: none;
    }

    th {
        pointer-events: none;
        background-color: darkgray;
        width: auto;
    }
</style>
