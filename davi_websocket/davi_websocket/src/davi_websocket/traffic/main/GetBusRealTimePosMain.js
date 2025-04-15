const getBusRealTimePosDbc = require('../../traffic/dbc/GetBusRealTimePosDbc');
const getBusRealTimePosUtil = require('../util/GetBusRealTimePosUtil');
const {GET_BUSREALTIME_POS_PORT} = require("../../../../config");
const webSocket = require('../../common/socket/WebSocket');


exports.start = async function(data) {
    //맨 처음 서버가 시작할 때 버스 라우트 id들을 받아온다.
    try{
        let rows;
        let routeMap = new Map();

        if(data !== undefined && data !== null && data.length != 0){
            rows = data.seoulBusRouteIds;
            for(let row of rows){
                routeMap.set(row.routeId, row.routeNm);
            }
        }else{
            rows = await getBusRealTimePosDbc.selectBusSeoulInfoList();
            for(let row of rows){
                routeMap.set(row.route_id, row.route_nm);
            }
        }

        await webSocket.callWebSocket(GET_BUSREALTIME_POS_PORT, function callback(connection){
            getBusRealTimePosUtil.getPos(connection, routeMap);
        });
    }catch (err){
        throw err;
    }
}