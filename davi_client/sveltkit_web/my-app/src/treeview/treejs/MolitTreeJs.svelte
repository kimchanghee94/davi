<script>
    import { onMount } from "svelte";
    import camelcase from "camelcase";
    import Tree from '@widgetjs/tree';
    import MolitTableSvelte from "../../table/tablesvelte/MolitTableSvelte.svelte";

    export let molitColList = null;
    export let molitData = null;
    export let firstData = null;
    export let secondData = null;
    export let childMolitComponentFlag = null;

    let myTree = null;
    let tableData = null;

    function makeTreeBasicNode(){
        let node = new Object();
        node["children"] = new Array();
        node["disabled"] = false;
        node["checked"] = false;
        return node;
    }

    function makeTreeData(jsonArray){
        let treeData = new Array();

        let body = makeTreeBasicNode();
        body["id"] = "body";
        body["text"] = "body";

        for(let i=0; i<jsonArray.length; i++){
            let tmpData = jsonArray[i];

            let tmpNode = makeTreeBasicNode();
            tmpNode["id"] = tmpData.colName;
            tmpNode["text"] = tmpData.colName + "===============" + tmpData.colDescript;

            body["children"].push(tmpNode);
        }

        treeData.push(body);

        return treeData;
    }

    onMount(() => {
        let treeData = makeTreeData(molitColList);

        myTree = new Tree('#container', {
            data: treeData,
            onChange: function (){
                console.log("VALUES", this.values);
            }
        });
    })

    function selectData(){
        tableData = new Object();
        //헤더넣기
        tableData["header"] = myTree.values;

        //바디넣기
        //json_data의 경우 좌표길이 값이 매우 길기 때문에 테이블에 보여주는 용도는 50자내외로 보여주고
        //실제 데이터는 따로 담아둔다.
        let tableBody = new Array();
        let showTableBody = new Array();

        for(let i=0; i<molitData.length; i++){
            let rowData = molitData[i];
            let tmpArr = new Array();
            let showTmpArr = new Array();

            for(let key of myTree.values){
                let camelKey = camelcase(key);

                if(camelKey === "jsonData"){
                    showTmpArr.push(rowData[camelKey].substring(0, 50) + " ...");
                }else{
                    showTmpArr.push(rowData[camelKey]);
                }
                tmpArr.push(rowData[camelKey]);

            }
            tableBody.push(tmpArr);
            showTableBody.push(showTmpArr);
        }
        tableData["showBody"] = showTableBody;
        tableData["body"] = tableBody;

        //한국어 헤더 넣기
        let krHeader = new Array();
        for(let i=0; i<molitColList.length;i++){
            let colName = molitColList[i].colName;
            let colKrName = molitColList[i].colDescript;
            if(myTree.values.includes(colName)){
                krHeader.push(colKrName);
            }
        }

        tableData["krHeader"] = krHeader;
        console.log("MOLIT TABLE BODY!!!!", tableData);
    }
</script>
{#if childMolitComponentFlag === true}
<h3>응답출력 결과 선택</h3>
<div id="container"></div>
<button class="btn btn-primary"
        style="width: 300px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-bottom: 50px"
        type="button" on:click={() => selectData()}>선택 완료
</button>
{#if tableData}
    <MolitTableSvelte {tableData} bind:firstData bind:secondData bind:childMolitComponentFlag/>
{/if}
{/if}

<style>
    h3{
        margin-top: 30px;
    }

    #container{
        width: 500px;
    }
</style>
