<script>
    export let tableData = null;
    export let firstData = null;
    export let secondData = null;
    export let childMolitComponentFlag = null;

    function saveFirstData(){
        childMolitComponentFlag = false;

        if(firstData === null){
            firstData = new Object();
            firstData["header"] = tableData.header;
            firstData["body"] = tableData.body;
            firstData["showBody"] = tableData.showBody;
            firstData["krHeader"] = tableData.krHeader;
            alert("첫번째 데이터 저장 완료");
        }else if(firstData !== null && secondData === null){
            secondData = new Object();
            secondData["header"] = tableData.header;
            secondData["body"] = tableData.body;
            secondData["showBody"] = tableData.showBody;
            secondData["krHeader"] = tableData.krHeader;
            alert("두번째 데이터 저장 완료");
        }
    }
</script>

{#if childMolitComponentFlag === true}
<table>
    <thead>
    <tr>
        <th>No.</th>
        {#each tableData.header as item}
        <th>{item}</th>
        {/each}
    </tr>
    </thead>
    <tbody>
    {#each tableData.showBody as item, index}
    {#if index < 10}
    <tr>
        <td style="font-weight: bold; pointer-events: none">{index + 1}</td>
        {#each tableData.showBody[index] as item}
            <td>{item}</td>
        {/each}
    </tr>
    {/if}
    {/each}
    </tbody>
</table>
{/if}

<button class="btn btn-warning"
        style="width: 300px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-bottom: 50px"
        type="button" on:click="{saveFirstData}">확인 완료
</button>

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