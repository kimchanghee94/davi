<script>
    import {beforeUpdate} from "svelte";
    import {transData} from "$lib/store.js";
    import {goto} from "$app/navigation";

    export let sqlJSResult = null;
    export let sqlInitBtn = false;

    let finalData = new Object();

    let state = null;
    let isDrag = false;

    let firstFlag = false;
    let firstPos = new Array();
    let fillRemoveFlag = true;
    // let tableClick = false;

    beforeUpdate(() => {
        if(sqlInitBtn === true){
            state = new Array(sqlJSResult.showTableHeader.length + sqlJSResult.body.length*sqlJSResult.body.length).fill(false);
            isDrag = false;
            sqlInitBtn = false;
        }
    })

    const beginDrag = () => {
        // if(tableClick === false){
        //     if(state !== null){
        //         state = new Array(sqlJSResult.showTableHeader.length + sqlJSResult.body.length*sqlJSResult.body.length).fill(false);
        //     }
        // }else {
        //     tableClick = false;
        // }

        isDrag = true;
    }

    const endDrag = () => {
        // tableClick = false;
        isDrag = false;
        firstFlag = false;
    }

    const toggle = (r, c) => {
        // let h = Math.abs(firstPos[0] - r);
        // let w = Math.abs(firstPos[1] - c);
        //
        // let strtR = firstPos[0] < r ? firstPos[0] : r;
        // let strtC = firstPos[1] < c ? firstPos[1] : c;
        //
        // for(let i=strtR; i <= h + strtR; i++){
        //     for(let j=strtC; j <= w + strtC; j++){
        //         state[i*sqlJSResult.showTableHeader.length+j] = fillRemoveFlag;
        //     }
        // }

        for(let i=0; i<=sqlJSResult.body.length; i++){
            state[i*(sqlJSResult.showTableHeader.length)+c] = fillRemoveFlag;
        }
    }

    const mouseHandler = (r, c) => (e) => {
        if (isDrag || e.type === 'mousedown') {
            //테이블 클릭한 경우'
            // tableClick = true;

            //첫 드래그 시작
            if(firstFlag === false){
                firstFlag = true;
                firstPos = [r , c];

                //CTRL 키 누를때, 누르지 않았을 때 조건 분기
                if(!event.ctrlKey){
                    if(state[r*sqlJSResult.showTableHeader.length+c] === true){
                        state.fill(false);
                        fillRemoveFlag = false;
                    }else{
                        state.fill(false);
                        fillRemoveFlag = true;
                    }
                }
                else{
                    if(state[r*sqlJSResult.showTableHeader.length+c] === true){
                        fillRemoveFlag = false;
                    }else{
                        fillRemoveFlag = true;
                    }
                }
            }
            toggle(r, c);
        }
    }

    function goToMapBox(){
        let selectCol = new Array();

        //선택한 헤더 인덱스 값 저장
        for(let i=0; i<sqlJSResult.showTableHeader.length; i++){
            if(state[i] === true){
                selectCol.push(i);
            }
        }

        //최종 전달 객체에 헤더 값과 바디 값을 담는다.
        finalData["header"] = new Array();
        finalData["body"] = new Array();

        //헤더 값 담기
        for(let i=0; i<selectCol.length; i++){
            finalData["header"].push(sqlJSResult.showTableHeader[selectCol[i]]);
        }

        //바디 값 담기
        for(let i=0; i < sqlJSResult.body.length; i++){
            let tmpArr = new Array();
            for(let j=0; j < selectCol.length; j++){
                tmpArr.push(sqlJSResult.body[i][selectCol[j]]);
            }
            finalData["body"].push(tmpArr);
        }

        console.log("FINAL TRANS DATA", finalData);

        transData.set(finalData);
        goto('/roadmap2');
    }
</script>

<svelte:window on:mousedown={beginDrag} on:mouseup={endDrag}/>
<table>
    <thead>
    <tr>
        <th>No.</th>
        {#each sqlJSResult.showTableHeader as item, c}
            <th on:mousedown={mouseHandler(0, c)} on:mouseenter={mouseHandler(0, c)} class:header-selected="{state[c]}">{item}</th>
        {/each}
    </tr>
    </thead>
    <tbody>
    {#each sqlJSResult.body as item, r}
        <tr>
            <td style="font-weight: bold; pointer-events: none">{r + 1}</td>
            {#each sqlJSResult.body[r] as item, c}
                <td on:mousedown={mouseHandler(r + 1, c)} on:mouseenter={mouseHandler(r + 1, c)} class:selected="{state[(r+1)*sqlJSResult.showTableHeader.length+c]}">
                    {#if item != null && item.indexOf("featureCollection") > 0}
                       <span class="text truncate" style="width: 200px">{item}</span>
                    {:else}
                        <span class="text truncate">{item}</span>
                    {/if}
                </td>
            {/each}
        </tr>
    {/each}
    </tbody>
</table>
<button class="btn btn-success ml-5 mt-5 mb-5" on:click={goToMapBox}>
    맵박스로 출력해보기
</button>

<style>
    table, th, td{
        border: 1px solid black;
        text-align: center;
        -ms-user-select: none;
        -moz-user-select: -moz-none;
        -khtml-user-select: none;
        -webkit-user-select: none;
        user-select: none;
    }

    td {
        background-color: white;
    }

    th{
        font-weight: bold;
        /*pointer-events: none;*/
        background-color: darkcyan;
        width: auto;
    }

    .selected {
        background-color: darkgray;
    }

    .header-selected{
        background-color: lightblue;
    }

    .text {
        display: block;
    }

    .truncate {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }
</style>
