const commonUtil = require("../../common/util/CommonUtil");
const configJs = require("../../../../config");

let period = 10;

exports.getPos = async function(connection, routeMap){
    while(true){
        let result = {};

        try{
            let data = [];

            for(let [key, value] of routeMap){
                let url = 'http://ws.bus.go.kr/api/rest/buspos/getBusPosByRtid';
                let paramMap = new Map();
                paramMap.set('serviceKey', configJs.PDP_SERVICE_KEY);
                paramMap.set('busRouteId', key);

                let resData = await commonUtil.getFetch(url, paramMap);
                console.log(resData);

                let object = {};
                object["routeId"] = key;
                object["routeNm"] = value;

                if(resData.ServiceResult.msgHeader.headerCd != 0){
                    object["itemList"] = null;
                }else{
                    object["itemList"] = resData.ServiceResult.msgBody.itemList;
                }

                data.push(object);
            }

            result["success"] = true;
            result["message"] = "Get Bus Real Time Pos Success";
            result["data"] = data;
        }catch (err){
            console.error(err);
            result["success"] = false;
            result["message"] = "Get Bus Real Time Pos Fail " + err.stack;

            break;
        }

        connection.sendUTF(JSON.stringify(result));
        await commonUtil.sleep(period);
    }
}