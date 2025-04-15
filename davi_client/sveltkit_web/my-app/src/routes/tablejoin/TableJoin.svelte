<script>
    import {onMount} from "svelte";
    import { openDatabase, STORE_NAME } from "./indexedDB.js";
    // import {sqlDb} from "./SQLite.js";
    // import {createUser} from "$lib/server/database_TMP.js";

    import {
        joinSqlJs,
        insertSqlJs,
        createTableSqlJs,
        initDatabase,
        selectColData,
        insertColData
    } from "$lib/database.js";
    import TableDragSvelte from "../../tabledrag/tableDragSvelte/TableDragSvelte.svelte";
    import {goto} from "$app/navigation";

    export let firstData = null;
    export let secondData = null;
    let sqlInitBtn = false;
    let backendURL = import.meta.env.VITE_DAVI_URL;

    //vanillajs와 sqlJS에서 공통으로 테스트 사용중인 변수
    let joinDataFlag = false;

    // >>> VanillaJS
    let fiCheckHeader = new Array();
    let seCheckHeader = new Array();

    let joinShowTableHeader = new Array();
    let joinTableHeaderEn = new Array();
    let joinTableHeaderKr = new Array();
    let joinTableData = new Array();

    function combJoinData(){
        let fIdx = new Array();
        let sIdx = new Array();

        for(let i=0; i<firstData.header.length; i++){
            joinShowTableHeader.push(firstData.header[i] + "(" + firstData.krHeader[i] + ")");
            joinTableHeaderEn.push(firstData.header[i]);
            joinTableHeaderKr.push(firstData.krHeader[i]);
        }

        for(let i=0; i<secondData.header.length; i++){
            let tmpIdx = fiCheckHeader.indexOf(secondData.header[i]);
            if(tmpIdx < 0){
                joinShowTableHeader.push(secondData.header[i] + "(" + secondData.krHeader[i] + ")");
                joinTableHeaderEn.push(secondData.header[i]);
                joinTableHeaderKr.push(secondData.krHeader[i]);
            }else{
                fIdx.push(tmpIdx);
                sIdx.push(i);
            }
        }
        for(let i=0; i<firstData.body.length; i++){
            let fData = firstData.body[i];
            let fiFlag = false;

            for(let j=0; j<secondData.body.length; j++){
                let sData = secondData.body[j];

                let seFlag = true;
                let tmpArray = new Array();

                for(let k=0; k<fIdx.length; k++){
                    let fTmpIdx = fIdx[k];
                    let sTmpIdx = sIdx[k];

                    if(fData[fTmpIdx] !== sData[sTmpIdx]){
                        seFlag = false;
                        break;
                    }
                }
                if(seFlag === true){
                    fiFlag = true;
                    console.log("DATA COMP",fData, sData);
                    for(let k=0; k<fData.length; k++){
                        tmpArray.push(fData[k]);
                    }

                    for(let k=0; k<sData.length; k++){
                        if(sIdx.includes(k)) {
                            continue;
                        }

                        tmpArray.push(sData[k]);
                    }
                    joinTableData.push(tmpArray);
                }
            }

            if(fiFlag === false){
                let tmpArray = new Array();
                for(let j=0; j<fData.length; j++){
                    tmpArray.push(fData[j]);
                }
                for(let j=0; j<secondData.header.length - sIdx.length; j++){
                    tmpArray.push("null");
                }
                joinTableData.push(tmpArray);
            }
        }
    }

    function joinData(){
        joinShowTableHeader = new Array();
        joinTableHeaderEn = new Array();
        joinTableHeaderKr = new Array();
        joinTableData = new Array();
        joinDataFlag = false;

        if(fiCheckHeader.length != 0 && seCheckHeader.length != 0){
            let sameFlag = true;
            for(let i=0; i<fiCheckHeader.length; i++){
                let orgStr = fiCheckHeader[i];
                if(seCheckHeader.includes(orgStr) === false){
                    sameFlag = false;
                    break;
                }
            }

            if(sameFlag === true){
                for(let i=0; i<seCheckHeader.length; i++){
                    let orgStr = seCheckHeader[i];
                    if(fiCheckHeader.includes(orgStr) === false){
                        sameFlag = false;
                        break;
                    }
                }
            }

            if(sameFlag === true){
                combJoinData();
                joinDataFlag = true;
            }else{
                alert("같은 속성값들 선택할 것");
                joinDataFlag = false;
            }
        }
    }
    // <<< VanillaJS


    // >>> SQLJS
    let sqlJsDb;
    let sqlJSResult = null;

    function valSelHeader(){
        sqlInitBtn = true;
        joinDataFlag = false;

        if(fiCheckHeader.length != seCheckHeader.length) {
            alert("각 선택한 속성의 개수가 일치하지 않습니다.");
            joinDataFlag = false;
        }

        if(fiCheckHeader.length != 0 && seCheckHeader.length != 0){
            let sameFlag = true;
            // for(let i=0; i<fiCheckHeader.length; i++){
            //     let orgStr = fiCheckHeader[i];
            //     if(seCheckHeader.includes(orgStr) === false){
            //         sameFlag = false;
            //         break;
            //     }
            // }
            //
            // if(sameFlag === true){
            //     for(let i=0; i<seCheckHeader.length; i++){
            //         let orgStr = seCheckHeader[i];
            //         if(fiCheckHeader.includes(orgStr) === false){
            //             sameFlag = false;
            //             break;
            //         }
            //     }
            // }

            if(sameFlag === true){
                //join 이후 데이터 출력
                sqlJSResult = joinSqlJs(sqlJsDb, fiCheckHeader, seCheckHeader, firstData, secondData);
                joinDataFlag = true;
            } else{
                alert("같은 속성값들 선택할 것");
                joinDataFlag = false;
            }
        }else {
            alert("join할 값들을 선택해 주세요");
            joinDataFlag = false;
        }
    }

    //최초 돔 등록 시 한번 DB에 데이터를 넣어둔다
    onMount( async () => {
        database = await openDatabase();

        sqlJsDb = await initDatabase();
        createTableSqlJs(sqlJsDb, firstData, secondData);

        //테이블 생성 완료 후 insert
        insertSqlJs(sqlJsDb, firstData, secondData);
    });
    // <<< SQLJS


    // >>> SQLite3 실패
    // const sqlite3 = require('sqlite3').verbose();
    function createTable2(){
        // sqlDB = new sqlite3.verbose().Database('./db/davi.db');
        // sqlDB.run('CREATE TABLE student(id integer primary key, name text not null, email text unique)');
        //
        // sqlDB.close();
    }

    function insertData2(){
        // sqlDB = new sqlite3.verbose().Database('./db/chinook.db');
        //
        // sqlDB.run(`INSERT INTO student(name, email) VALUES('이종현', '1428ksu@gmail.com')`, function (err) {
        //     if (err) {
        //         return console.log(err.message);
        //     }
        //     console.log(`A row has been inserted with rowid ${this.lastID}`);
        // });
        //
        // sqlDB.close();
    }

    function selectData2(){
        // sqlDB = new sqlite3.Database('./db/chinook.db');
        //
        // let sql = `SELECT * FROM student
        //    WHERE name = '이종현'`;
        //
        // sqlDB.all(sql, [], (err, rows) => {
        //     if (err) {
        //         throw err;
        //     }
        //     rows.forEach((row) => {
        //         console.log(row);
        //     });
        // });
        // sqlDB.close();
    }
    //<<< SQLite3


    //>>> WebSQL
    let db;

    function createDB(){
        if(!!window.openDatabase){
            alert("현재 브라우저는 WEB SQL DB를 지원합니다.");
        }else{
            alert("현재 브라우저는 WEB SQL DB를 지원하지 않습니다.");
        }

        db = window.openDatabase("davi","1.0","테스트용DB", 1024*1024);
        console.log("DB CREATE!!!");
    }

    function createTable(){
        db.transaction(function (tx){
            tx.executeSql("create table Test(id, name)");
        });

        db.transaction(function (tx){
            tx.executeSql("delete from Test");
        });
        console.log("TABLE CREATE!!!");
    }

    function insertData(){
        db.transaction(function (tx){
            tx.executeSql("insert into Test values(?,?)", ["testKeyy", "testValue"]);
        });
        console.log("INSERT DATA!!!");
    }

    function selectData(){
        db.transaction(function (tx){
            tx.executeSql("select * from Test", [],
            function(tx, result){
                for(let i=0; i<result.rows.length; i++){
                    let row = result.rows.item(i);
                }
            });
        });

        console.log("SELECT DATA!!!");
    }
    //<<< WebSQL

    //>>>indexedDBA
    let database;
    let inputValue = '';

    async function saveData() {
        const value = inputValue.trim();

        if (value === '') return;

        await database.put(STORE_NAME, value, 'my-key');
        inputValue = '';
    }

    async function getData() {
        const value = await database.get(STORE_NAME, 'my-key');
        console.log(value);
    }
    //<<<indexedDBA

    //>>>관련있는 데이터만 출력해서 조인하기
    async function getRelateData(){
        if(fiCheckHeader.length == 0 || seCheckHeader == 0){
            alert("조인할 칼럼을 선택하세요");
        }

        sqlInitBtn = true;
        let molitDataFlag = 0;

        if(firstData.routeName === "국토부_도로" ||
            firstData.routeName === "국토부_링크" ||
            firstData.routeName === "국토부_시도" ||
            firstData.routeName === "국토부_시군구" ||
            firstData.routeName === "국토부_읍면동" ||
            firstData.routeName === "국토부_리"){
            molitDataFlag = 1;
        }else if(secondData.routeName === "국토부_도로" ||
            secondData.routeName === "국토부_링크" ||
            secondData.routeName === "국토부_시도" ||
            secondData.routeName === "국토부_시군구" ||
            secondData.routeName === "국토부_읍면동" ||
            secondData.routeName === "국토부_리"){
            molitDataFlag = 2;
        }

        let data = null;

        if(molitDataFlag === 1){
            data = {
                routeName : firstData.routeName,
                selectColumn : firstData.header,
                joinColumn : fiCheckHeader,
            };

            data["joinData"] = selectColData(sqlJsDb, fiCheckHeader, "second");
        }else if(molitDataFlag === 2) {
            data = {
                routeName: secondData.routeName,
                selectColumn: secondData.header,
                joinColumn: seCheckHeader,
            };

            data["joinData"] = selectColData(sqlJsDb, seCheckHeader, "first");
        }

        let res = await fetch(backendURL + '/common/join/related',{
            method : 'POST',
            body : JSON.stringify(data),
            headers : {
                'content-type' : 'application/json'
            }
        });

        const resJson = await res.json();

        if(resJson.success === true) {
            if (molitDataFlag === 1) {
                insertColData(sqlJsDb, firstData, 'first', resJson.data);
            } else if (molitDataFlag === 2) {
                insertColData(sqlJsDb, secondData, 'second', resJson.data);
            }
            sqlJSResult = joinSqlJs(sqlJsDb, fiSelectOrader, seSelectOrader, firstData, secondData);
            joinDataFlag = true;

            console.log('Test for SQL JS Result',sqlJSResult);
        }
    }
    //checkbox 조정
    let fiSelectOrader = [];
    let seSelectOrader = [];

    function handleCheckboxChange(event, type){
        const {value, checked} = event.target;
        if(type === 1){
            if(checked){
                fiSelectOrader = [...fiSelectOrader, value];
            }else{
                fiSelectOrader = fiSelectOrader.filter((item) => item !== value);
            }
        }else{
            if(checked){
                seSelectOrader = [...seSelectOrader, value];
            }else{
                seSelectOrader = seSelectOrader.filter((item) => item !== value);
            }
        }
    }
    //<<<관련있는 데이터만 출력해서 조인하기

</script>

<div style="margin:50px 50px 50px 50px; display: flow">
    <h3>Join 시작</h3>
    <div style="display:flex;">
        <div class="ml-5" style="display: flow">
            <p><strong>{firstData.routeName}</strong></p>
            {#each firstData.header as item, index}
                <input type="checkbox" name="fiCheckHeader"
                       value="{item}" bind:group={fiCheckHeader}
                        on:change={() => handleCheckboxChange(event, 1)}/>
                {item}({firstData.krHeader[index]})<br>
            {/each}
        </div>
        <div class="ml-5" style="display: flow">
            <p><strong>{secondData.routeName}</strong></p>
            {#each secondData.header as item, index}
                <input type="checkbox" name="seCheckHeader"
                       value="{item}" bind:group={seCheckHeader}
                       on:change={() => handleCheckboxChange(event, 2)}/>
                {item}({secondData.krHeader[index]})<br>
            {/each}
        </div>
    </div>
    <button class="btn btn-danger ml-5 mt-5" on:click={joinData}>
        선택한 속성값들로 조인해볼게요 (VanillaJS)
    </button>
    {#if joinDataFlag === true}
        <div class="mt-5">
            <table>
                <thead>
                <tr>
                    <th>No.</th>
                    <!--{#each joinShowTableHeader as item}-->
                    {#each sqlJSResult.showTableHeader as item}
                        <th>{item}</th>
                    {/each}
                </tr>
                </thead>
                <tbody>
                <!--{#each joinTableData as item, index}-->
                {#each sqlJSResult.body as item, index}
                    <tr>
                        <td style="font-weight: bold; pointer-events: none">{index + 1}</td>
                        <!--{#each joinTableData[index] as item}-->
                        {#each sqlJSResult.body[index] as item}
                            {#if item != null && item.indexOf("featureCollection") > 0}
                                <td><span class="text truncate" style="width: 200px">{item}</span></td>
                            {:else}
                                <td><span class="text truncate">{item}</span></td>
                            {/if}
                        {/each}
                    </tr>
                {/each}
                </tbody>
            </table>
        </div>
    {/if}
    {#if joinDataFlag === true}
    <TableDragSvelte bind:sqlInitBtn {sqlJSResult}/>
    {/if}
</div>

<input class="input ml-3" type="text" bind:value={inputValue} placeholder="Enter value" />
<button class="btn btn-primary" on:click={saveData}>IndexedDB 데이터 저장하기</button>
<button class="btn btn-primary" on:click={getData}>IndexedDB 데이터 가져오기</button>


<br>
<button class="btn btn-info mt-3 ml-3" on:click={createDB}>WebSQL DB 생성</button>
<button class="btn btn-info mt-3" on:click={createTable2}>WebSQL 테이블 생성</button>
<button class="btn btn-info mt-3" on:click={insertData2}>WebSQL 데이터 입력</button>
<button class="btn btn-info mt-3" on:click={selectData2}>WebSQL 데이타 조회</button>
<br>

<br>
<button class="btn btn-danger ml-3 mb-4" on:click={valSelHeader}>sqlJS 테스트</button>
<br>

<br>
<button class="btn btn-primary ml-3 mb-4" on:click={getRelateData}>관련데이터 불러와서 조합하기</button>
<br>

<style>
    table{
        margin: 10px 10px 10px 10px;
    }

    table, th, td {
        border: 1px solid black;
        text-align: left;
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

    .text {
        display: block;
    }

    .truncate {
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

</style>