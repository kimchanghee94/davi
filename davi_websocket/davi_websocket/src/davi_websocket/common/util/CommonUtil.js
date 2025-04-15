const {XMLParser} = require('fast-xml-parser');
const fs = require('fs/promises');

/**
 * 비동기 sleep 메서드
 * @param s 1초 단위로 계산한다.
 * @return 사용자가 입력한 초 단위로 잠재운다.
 */
exports.sleep = async function (s) {
    try{
        return new Promise(resolve => setTimeout(resolve, s * 1000));
    }catch (err){
        return err.stack;
    }
}

/**
 * TimeStamp 초단위 계산
 * @return TimeStamp를 초단위로 계산하여 반환한다.
 */
exports.getTimeStampSec = function(){
    try{
        return Math.floor(Date.now()/1000);
    }catch (err){
        return err.stack;
    }
}

/**
 * post fetch
 * @Param url 타겟 url
 * @Param data 전달한 data는 json형태로 넘긴다.
 * @return api로 부터 받은 값
 */
exports.postFetch = async function(url, data){
    let res;

    try{
        data = JSON.stringify(data);
    }catch (err){
        data = null;
    }

    res = await fetch(url, {
        method : 'POST',
        headers : {
            'content-type' : 'application/json'
        },
        body : data
    });

    let resJson = await res.json();
    return resJson;
}

/**
 * get fetch
 * @Param url 타겟 url
 * @Param paramMap 파라미터로 들어갈 데이터
 * @return api로 부터 받은 값
 */
exports.getFetch = async function(url, paramMap){
    let res;

    if(paramMap !== undefined){
        url += '?';
        for(let [key, value] of paramMap){
            url += (key + "=" + value + "&");
        }
        url = url.slice(0, -1);
    }

    console.log("FETCH URL ::: " , url);

    res = await fetch(url, {
        method : 'GET',
    });

    const resText = await res.text();

    //json형태 처리
    try{
        let resJson = JSON.parse(resText);
        return resJson;
    }
    //json 형태가 아닌 xml 형태일 경우
    catch (err){
        const parser = new XMLParser();
        let resJson = parser.parse(resText);

        return resJson;
    }
}

/**
 * file fetch
 * @Param path 다운받을 파일 경로
 * @Param fileName 다운받을 파일 명
 * @Param type 파일 형태 'json, xml' 형태로 입력, default는 text로 반환
 * @return file 내용을 사용자가 입력한 type으로 반환
 */
exports.fileFetch = async function(path, fileName, type){
    let resText = await fs.readFile(path + fileName, {encoding : 'utf-8'});

    try{
        if(type.toLowerCase() === 'json'){
            return JSON.parse(resText);
        }else if(type.toLowerCase() === 'xml'){
            let parser = new XMLParser();
            return parser.parse(resText);
        }else{
            return resText;
        }
    }catch (err){
        throw err;
    }
}