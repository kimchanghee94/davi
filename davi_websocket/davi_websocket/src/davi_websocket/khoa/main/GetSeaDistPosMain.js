const getSeaDistPosDbc = require('../dbc/GetSeaDistPosDbc');
const getSeaDistPosUtil = require('../util/GetSeaDistPosUtil');
const { GET_SEADIST_POS_PORT } = require('../../../../config');
const webSocket = require('../../common/socket/WebSocket');

exports.start = async function () {
    try{
        await webSocket.callWebSocket(GET_SEADIST_POS_PORT, function callback(connection, resData){
            getSeaDistPosUtil.getPos(connection, resData);
        });
    }catch (err){
        throw err;
    }
}

exports.start2 = async function() {
    try{
        //맨처음 서버가 시작할 때 각 선박들의 order 개수를 담아둔다.
        let rows = await getSeaDistPosDbc.selectList2();
        let shipMap = new Map();

        for(let row of rows){
            let object = {};
            object["count"] = row.count;
            object["domStrTypeNm"] = row.dom_str_type_nm;
            object["domStrNm"] = row.dom_str_nm;
            object["domEndNm"] = row.dom_end_nm;
            object["inverseFlag"] = true;

            shipMap.set(row.sea_dist_id, object);
        }

        await webSocket.callWebSocket(GET_SEADIST_POS_PORT, function callback(connection, firstChk){
            getSeaDistPosUtil.getPos2(connection, firstChk, shipMap);
        });
    }catch (err){
        throw err;
    }
}