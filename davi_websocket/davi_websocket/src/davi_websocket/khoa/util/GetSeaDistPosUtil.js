const getSeaDistPosDbc = require('../dbc/GetSeaDistPosDbc');
const commonUtil = require('../../common/util/CommonUtil');

let period = 60;

exports.getPos2 = async function(connection, firstChk, shipMap){
    while(true){
        let result = {};
        try{
            let data = [];
            let timeStamp = commonUtil.getTimeStampSec();

            if(firstChk === true){
                firstChk = false;

                for(let [key, value] of shipMap){
                    let idx = (Math.floor(timeStamp/period) % value.count) + 1;

                    let rows = await getSeaDistPosDbc.selectRanPosList(key, idx);
                    let lngLat = [];

                    for(let row of rows){
                        let tmpArr = new Array(row.lng, row.lat);
                        lngLat.push(tmpArr);
                    }

                    let object = {};
                    object["domStrTypeNm"] = value.domStrTypeNm;
                    object["domStrNm"] = value.domStrNm;
                    object["domEndNm"] = value.domEndNm;
                    object["coordinates"] = lngLat

                    data.push(object);
                }

                result["success"] = true;
                result["firstChk"] = true;
                result["message"] = "Get Sea Dist Pos Success";
                result["data"] = data;
                connection.sendUTF(JSON.stringify(result));
            }else{
                //응답 주기에 맞춰서 응답한다.
                if(timeStamp % period === 0){
                    for(let [key, value] of shipMap){
                        let idx = (Math.floor(timeStamp/period) % value.count) + 1;

                        if(idx === 1 && value.inverseFlag === true){
                            value.inverseFlag = false;
                        }else if(idx === 1 && value.inverseFlag === false){
                            value.inverseFlag = true;
                        }

                        if(value.inverseFlag === false){
                            idx = value.count + 1 - idx;
                        }

                        let rows = await getSeaDistPosDbc.selectRanPos(key, idx);

                        let object = {};
                        object["domStrTypeNm"] = value.domStrTypeNm;
                        object["domStrNm"] = value.domStrNm;
                        object["domEndNm"] = value.domEndNm;
                        object["coordinates"] = new Array(rows[0].lng, rows[0].lat);
                        if(idx === 1){
                            object["loop"] = true;
                        }else{
                            object["loop"] = false;
                        }

                        data.push(object);
                    }

                    result["success"] = true;
                    result["firstChk"] = false;
                    result["message"] = "Get Sea Dist Pos Success";
                    result["data"] = data;
                    connection.sendUTF(JSON.stringify(result));
                }
            }
        }catch(err){
            console.error(err);
            result["success"] = false;
            result["message"] = "Get Sea Dist Pos Fail " + err.stack;
            connection.sendUTF(JSON.stringify(result));

            break;
        }

        //속도가 너무 빠르면 동시 1초안에 여러 데이터를 전송하는 문제가 발생한다.
        await commonUtil.sleep(period);
    }
};

exports.getPos = async function (connection, resData){
    let result = {};

    try{
        let dbcResult = await getSeaDistPosDbc.selectList(resData);

        result["success"] = true;
        result["message"] = "Get Sea Dist Pos Success";
        result["req_data"] = resData;

        if(rows.length === 0){
            result["finish"] = true;
        }else{
            result["finish"] = false;
            result["res_data"] = dbcResult;
        }
    }catch (err){
        console.error(err);
        result["success"] = false;
        result["message"] = "Get Sea Dist Pos Fail " + err.stack;
    }

    connection.sendUTF(JSON.stringify(result));
};