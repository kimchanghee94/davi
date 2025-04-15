<script>
    import { onMount } from 'svelte';
    import Tree from '@widgetjs/tree';
    import TableSvelte from "../../table/tablesvelte/TableSvelte.svelte";

    export let jsonData = null;
    export let childComponentFlag = null;
    export let firstData = null;
    export let secondData = null;

    let myTree = null;
    let tableData = null;

    let headerBodyKey = new Set();
    let bodyParentKeys = new Array();

    let testData = {
        "response": {
            "service": {
                "name": "data",
                "version": "2.0",
                "operation": "getfeature",
                "time": "655(ms)"
            },
            "status": "OK",
            "record": {
                "total": "17",
                "current": "17"
            },
            "page": {
                "total": "1",
                "current": "1",
                "size": "1000"
            },
            "result": {
                "featureCollection": {
                    "type": "FeatureCollection",
                    "bbox": [
                        0.0,
                        0.0,
                        -1.0,
                        -1.0
                    ],
                    "data": [
                        {
                            "시": "서울특별시",
                            "소속구": [
                                {
                                    "구명": "영등포구",
                                    "소속지번": [
                                        {
                                            "지번1": "당산로",
                                            "지번2": "49길",
                                            "지번3": "20"
                                        },
                                        {
                                            "지번1": "당산로",
                                            "지번2": "49길",
                                            "지번3": "19"
                                        }
                                    ]
                                },
                                {
                                    "구명": "금천구",
                                    "소속지번": [
                                        {
                                            "지번1": "남부순환로",
                                            "지번3": "10"
                                        },
                                        {
                                            "지번1": "남부순환로",
                                            "지번2": "112길"
                                        }
                                    ]
                                }
                            ],
                            "dummy" : {
                                "dummy_2" : "hello",
                                "dymmy_arr" : [1,2,3]
                            }
                        },
                        {
                            "시": "광주광역시",
                            "소속구": [
                                {
                                    "구명": "남구",
                                    "소속지번": [
                                        {
                                            "지번1": "봉선로",
                                            "지번2": "48길",
                                            "지번3": "30"
                                        }
                                    ]
                                },
                                {
                                    "구명": "북구"
                                },
                                {
                                    "구명": "서구"
                                },
                                {
                                    "구명": "동구"
                                }
                            ]
                        },
                        {
                            "시": "대전광역시"
                        }
                    ]
                }
            }
        }
    };

    //체크박스 기본 데이터 형태로 json 변환
    function recursiveJson(jsonData){
        //배열일 경우
        if(Array.isArray(jsonData)){
            let tmpObject = new Object();

            for(let key in jsonData){
                for(let key2 in jsonData[key]){
                    //check box able
                    headerBodyKey.add(key2);

                    if(IsJsonString(jsonData[key][key2])){
                        let tmpObject2 = recursiveJson(jsonData[key][key2]);

                        //body부 내부 배열 2차 이상인 경우들에 이전 배열객체와 비교 처리 로직
                        if(tmpObject[key2] !== undefined){
                            for(let key3 in tmpObject[key2]) {
                                if (!Array.isArray(tmpObject[key2][key3]) && IsJsonString(tmpObject[key2][key3])) {
                                    if(tmpObject2[key3] === undefined){
                                        tmpObject2[key3] = tmpObject[key2][key3];
                                        break;
                                    }
                                     for(let key4 in tmpObject[key2][key3]){
                                        tmpObject2[key3][key4] = tmpObject[key2][key3][key4];
                                    }
                                }
                            }
                        }
                        tmpObject[key2] = tmpObject2;
                    }else{
                        tmpObject[key2] = jsonData[key][key2];
                    }
                }
            }

            //빈값처리
            if(Object.keys(tmpObject).length === 0){
                return jsonData;
            }else{
                return tmpObject;
            }
        }else{
            let tmpObject = new Object();

            for(let key in jsonData){
                if(IsJsonString(jsonData[key])){
                    tmpObject[key] = recursiveJson(jsonData[key]);
                }else{
                    tmpObject[key] = jsonData[key];
                }
            }
            return tmpObject;
        }
    }

    //부모노드들에 대해서도 체크가 가능하도록 한다.
    function setDisableNode(jsonData){
        for(let key in jsonData){
            if(key === "header" || key === "head")
                continue;

            if(IsJsonString(jsonData[key])){
                if(setDisableNode(jsonData[key])){
                    headerBodyKey.add(key);
                    bodyParentKeys.push(key);
                    return true;
                }
            }else {
                if(headerBodyKey.has(key)){
                    return true;
                }
            }
        }
    }

    //data부 아래에 선택가능으로 지명되지 않은 데이터 처리를 해준다.
    function setDisableNode2(jsonData){
        let tmpData = new Array();
        //jsonData에는 body부만 담아서 넘긴다.
        for(let i=bodyParentKeys.length - 1; i>=0; i--){
            let key = bodyParentKeys[i];
            if(i === bodyParentKeys.length - 1){
                //TEST 해당 부분 testData => jsonData로 사용
                tmpData["jsonData"] = jsonData[key];
            }else {
                tmpData["jsonData"] = tmpData["jsonData"][key];
            }
        }

        checkableChildNode(tmpData);
    }

    //하위 모든 노드들을 선택 가능으로 한다.
    function checkableChildNode(jsonData){
        for(let key in jsonData){
            if(Array.isArray(jsonData[key]) && !IsJsonString(jsonData[key][0])){
                headerBodyKey.add(key);
            }
            else if(IsJsonString(jsonData[key])){
                checkableChildNode(jsonData[key]);
            }
            else{
                headerBodyKey.add(key);
            }
        }
    }

    //header나 head로 시작하는 하위 노드 headerBodyKey내부에 속하는지 확인
    function nonCheckHeader(jsonData){
        for(let key in jsonData){
            if(Array.isArray(jsonData[key]) && !IsJsonString(jsonData[key][0])){
                headerBodyKey.delete(key);
            }
            else if(IsJsonString(jsonData[key])){
                headerBodyKey.delete(key);
                nonCheckHeader(jsonData[key]);
            }
            else{
                headerBodyKey.delete(key);
            }
        }
    }

    //header나 head로 시작하는 키값 노드 찾기
    function headerNode(jsonData){
        for(let key in jsonData){
            if(key === 'header' || key === 'head'){
                headerBodyKey.delete(key);
                nonCheckHeader(jsonData[key]);
                return;
            }
            else if(IsJsonString(jsonData[key])){
                headerNode(jsonData[key]);
            }
        }
    }

    //treejs 노드형태로 json데이터 변환
    function makeTreeJson(jsonData){
        let treeData = new Array();

        for(let key in jsonData){
            let tmpRoot = null;
            if(headerBodyKey.has(key)){
                tmpRoot = makeTreeBasicNode(Boolean(false));
            }else{
                tmpRoot = makeTreeBasicNode(Boolean(true));
            }

            tmpRoot["id"] = key;

            if(IsJsonString(jsonData[key]) && !Array.isArray(jsonData[key])){
                tmpRoot["children"] = makeTreeJson(jsonData[key]);
                tmpRoot["text"] = key;
            }else{
                if(Array.isArray(jsonData[key])){
                    tmpRoot["text"] = key + "_____________[ " + jsonData[key] + " ]";
                }else{
                    tmpRoot["text"] = key + "_____________" + jsonData[key];
                }
            }
            treeData.push(tmpRoot);
        }

        return treeData;
    }

    function makeTreeData(jsonData){
        let treeData = new Array();

        if(jsonData.hasOwnProperty("header")){
            let headerRoot = makeTreeBasicNode(true);
            headerRoot["id"] = "header";
            headerRoot["text"] = "header";

            for(let hk in jsonData.header){
                let headerVal = makeTreeBasicNode(true);
                headerVal["id"] = hk;
                headerVal["text"] = hk + ": " + jsonData.header[hk];
                headerRoot["children"].push(headerVal);
            }

            treeData.push(headerRoot);

            let bodyRoot = new makeTreeBasicNode(false);
            bodyRoot["id"] = "body";
            bodyRoot["text"] = "body";
            let index = 0;

            for(let body of jsonData.body){
                for(let bk in body){
                    let bodyVal = new makeTreeBasicNode(false);
                    bodyVal["id"] = bk;
                    bodyVal["text"] = bk + ": " + body[bk];
                    bodyRoot["children"].push(bodyVal);
                }
                break;
            }
            treeData.push(bodyRoot);
        }else{
        }

        return treeData;
    }

    function makeTreeBasicNode(disable){
        let node = new Object();
        node["children"] = new Array();
        node["disabled"] = disable;
        node["checked"] = false;
        return node;
    }

    function IsJsonString(str) {
        try {
            var json = JSON.parse(JSON.stringify(str));
            return (typeof json === 'object');
        } catch (e) {
            return false;
        }
    }

    function selectData(){
        tableData = new Object();
        tableData["values"] = myTree.values;

        //jsonData에는 body부만 담아서 넘긴다.
        for(let i=bodyParentKeys.length - 1; i>=0; i--){
            let key = bodyParentKeys[i];
            if(i === bodyParentKeys.length - 1){
                //TEST 해당 부분 testData => jsonData로 사용
                tableData["jsonData"] = jsonData[key];
            }else {
                //header배열, data배열로 분리형태일 경우
                if(tableData["jsonData"][key] === undefined){
                    if(Array.isArray(tableData["jsonData"])){
                        let flag = false;
                        for(let j=0; j<tableData["jsonData"].length; j++){
                            for(let key2 in tableData["jsonData"][j]){
                                if(key === key2){
                                    tableData["jsonData"] = tableData["jsonData"][j][key];
                                    flag = true;
                                    break;
                                }
                            }
                            if(flag === true){
                                break;
                            }
                        }
                    }
                }
                else{
                    tableData["jsonData"] = tableData["jsonData"][key];
                }
            }
        }

        // 테이블에 뿌려줄 칼럼 데이터 조합
        if(tableData.jsonData.hasOwnProperty("header")){
            let rowDatas = tableData.jsonData.body;
            let colDatas = new Object();
            let index = 0;

            for(let rowData of rowDatas){
                let colData = new Array();
                for(let val of tableData.values){
                    colData.push(rowData[val]);
                }
                colDatas[index++] = colData;
            }
            tableData["colDatas"] = colDatas;
        }
    }

    onMount(() => {
        //TEST testData부분 jsonData로 수정
        let resultData = recursiveJson(jsonData);
        setDisableNode(resultData);
        setDisableNode2(resultData);
        headerNode(resultData);
        let treeData = makeTreeJson(resultData);

        myTree = new Tree('#container', {
            // data: makeTreeData(jsonData),
            data: treeData,
            onChange: function (){
                console.log("VALUSE", this.values)
            }
        });
    });
</script>
{#if childComponentFlag === true}
    <h3>응답출력 결과 선택</h3>
    <div id="container"></div>
    <button class="btn btn-primary"
            style="width: 300px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-bottom: 50px"
            type="button" on:click={() => selectData()}>선택 완료
    </button>
    {#if tableData}
        <TableSvelte tableData={tableData} bind:childComponentFlag bind:firstData bind:secondData/>
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
