import initSqlJs from 'sql.js';
import camelcase from "camelcase";

export async function initDatabase(){
    const SQL = await initSqlJs({
        locateFile: file => `../../../node_modules/sql.js/dist/sql-wasm.wasm`
    });
    const sqlJsDb = new SQL.Database();
    return sqlJsDb;
}

export function createTableSqlJs(sqlJsDb, firstData, secondData){
    let fiHeader = new Array();
    let seHeader = new Array();

    for(let i = 0; i < firstData.header.length; i++){
        let tmpHeader = firstData.header[i];
        fiHeader.push(tmpHeader + " varchar");
    }

    for(let i = 0; i < secondData.header.length; i++){
        let tmpHeader = secondData.header[i];
        seHeader.push(tmpHeader + " varchar");
    }

    let fiDropTableQuery = 'DROP TABLE IF EXISTS first';
    let seDropTableQuery = 'DROP TABLE IF EXISTS second';

    let fiCreateTableQuery = 'CREATE TABLE first (';
    for(let i=0; i<fiHeader.length; i++){
        fiCreateTableQuery += fiHeader[i];
        if(i + 1 !== fiHeader.length){
            fiCreateTableQuery += ',';
        }else{
            fiCreateTableQuery += ')';
        }
    }

    let seCreateTableQuery = 'CREATE TABLE second (';
    for(let i=0; i<seHeader.length; i++){
        seCreateTableQuery += seHeader[i];
        if(i + 1 !== seHeader.length){
            seCreateTableQuery += ',';
        }else{
            seCreateTableQuery += ')';
        }
    }

    sqlJsDb.run(fiCreateTableQuery);
    sqlJsDb.run(seCreateTableQuery);
}

export function insertSqlJs(sqlJsDb, firstData, secondData) {
    let fiHeader = firstData.header;
    let seHeader = secondData.header;
    let fiBody = firstData.body;
    let seBody = secondData.body;

    let fiInsertQuery = 'INSERT INTO first (';
    let seInsertQuery = 'INSERT INTO second (';

    for(let i=0; i<fiHeader.length; i++){
        fiInsertQuery += fiHeader[i];
        if(i + 1 !== fiHeader.length){
            fiInsertQuery += ',';
        }else{
            fiInsertQuery += ') VALUES (';
        }
    }

    for(let i=0; i<fiHeader.length; i++){
        fiInsertQuery += '?';
        if(i + 1 !== fiHeader.length){
            fiInsertQuery += ',';
        }else{
            fiInsertQuery += ')';
        }
    }

    for(let i=0; i<seHeader.length; i++){
        seInsertQuery += seHeader[i];
        if(i + 1 !== seHeader.length){
            seInsertQuery += ',';
        }else{
            seInsertQuery += ') VALUES (';
        }
    }

    for(let i=0; i<seHeader.length; i++){
        seInsertQuery += '?';
        if(i + 1 !== seHeader.length){
            seInsertQuery += ',';
        }else{
            seInsertQuery += ')';
        }
    }

    for(let i=0; i<fiBody.length; i++){
        let params = fiBody[i];
        sqlJsDb.run(fiInsertQuery, params);
    }

    for(let i=0; i<seBody.length; i++){
        let params = seBody[i];
        sqlJsDb.run(seInsertQuery, params);
    }
}

export function joinSqlJs(sqlJsDb, fiChkHeader, seChkHeader, firstData, secondData){
    let joinQuery = 'SELECT f.* '

    //두번째 테이블에 대해서는 중복값을 제외한 나머지를 출력한다.
    let seHeader = secondData.header;

    for(let i=0; i < seHeader.length; i++){
        if(!seChkHeader.includes(seHeader[i])){
            joinQuery += ', s.' + seHeader[i];
        }
    }

    joinQuery += ' FROM first AS f LEFT JOIN second AS s ON '

    for(let i=0; i<fiChkHeader.length; i++){
        joinQuery += 'f.' + fiChkHeader[i] + ' = ' + 's.' + seChkHeader[i];
        if(i + 1 !== fiChkHeader.length){
            joinQuery += ' AND '
        }
    }

    let joinData = sqlJsDb.exec(joinQuery);
    let result = new Object();

    result["headerEn"] = joinData[0].columns;
    result["body"] = joinData[0].values;

    //속성 한국명 담기
    let headerKr = new Array();

    for(let tmpKrHeader of firstData.krHeader){
        headerKr.push(tmpKrHeader);
    }

    for(let i=0; i < secondData.krHeader.length; i++) {
        if (!fiChkHeader.includes(seHeader[i])) {
            headerKr.push(secondData.krHeader[i]);
        }
    }
    result["headerKr"] = headerKr;

    //미리보기 테이블에 보여질 영어명 + 한국명 담기
    let showTableHeader = new Array();
    for(let i=0; i<result["headerEn"].length; i++){
        showTableHeader.push(result["headerEn"][i] + "(" + result["headerKr"][i] + ")");
    }
    result["showTableHeader"] = showTableHeader;

    return result;
}

export function selectColData(sqlJsDb, chkHeader, tableName){
    let selectQry = 'SELECT ';

    for(let i=0; i<chkHeader.length; i++){
        selectQry += chkHeader[i];

        if(i < chkHeader.length - 1) {
            selectQry += ", ";
        }
    }

    selectQry += ' FROM ' + tableName;

    let selectData = sqlJsDb.exec(selectQry);
    let result = new Object();

    result["header"] = selectData[0].columns;
    result["body"] = selectData[0].values;

    let combData = new Array();

    for(let i=0; i<result["header"].length; i++){
        for(let j=0; j<result["body"].length; j++){
            let tmpObj = new Object();
            tmpObj[result["header"][i]] = result["body"][j][i];
            combData.push(tmpObj);
        }
    }

    return combData;
}

export function insertColData(sqlJsDb, data, tableName, body){
    let deleteQry = 'DELETE FROM ' + tableName;
    sqlJsDb.run(deleteQry);

    let insertQry = 'INSERT INTO ' + tableName + '('
    for(let i=0; i < data.header.length; i++){
        insertQry += data.header[i];

        if( i < data.header.length - 1){
            insertQry += ',';
        }
    }
    insertQry += ') VALUES (';

    for(let i=0; i < data.header.length; i++){
        insertQry += '?';

        if( i < data.header.length - 1){
            insertQry += ',';
        }
    }
    insertQry += ')';

    for(let i=0; i<body.length; i++){
        let params = [];
        let tmpBody = body[i];

        for(let j=0; j<data.header.length; j++){
            params.push(tmpBody[camelcase(data.header[j])]);
        }
        sqlJsDb.run(insertQry, params);
    }
}