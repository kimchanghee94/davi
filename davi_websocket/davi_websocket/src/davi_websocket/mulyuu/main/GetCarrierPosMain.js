const getCarrierPosUtil = require('../util/GetCarrierPosUtil');
const webSocket = require('../../common/socket/WebSocket.js');
const commonUtil = require('../../common/util/CommonUtil');
const {GET_CARRIER_POS_PORT} = require("../../../../config");

exports.start = async function(){
    try{
        //맨 처음 서버가 시작할 때 택배차들의 노선들을 받아온다.
        let filePath = './static/json/';
        let guyeJson = await commonUtil.fileFetch(filePath, 'mulyuu_guyedong_route.json', 'json');
        let gwanjangJson = await commonUtil.fileFetch(filePath, 'mulyuu_gwanjang_route.json', 'json');
        let jayangJson = await commonUtil.fileFetch(filePath, 'mulyuu_jayangdong_route.json', 'json');
        let junggokJson = await commonUtil.fileFetch(filePath, 'mulyuu_junggok_route.json', 'json');
        let otherJson = await commonUtil.fileFetch(filePath, 'mulyuu_other_route.json', 'json');

        let carrierMap = new Map();
        carrierMap.set("guye", guyeJson.features[0].geometry.coordinates);
        carrierMap.set("gwanjang", gwanjangJson.features[0].geometry.coordinates);
        carrierMap.set("jayang", jayangJson.features[0].geometry.coordinates);
        carrierMap.set("junggok", junggokJson.features[0].geometry.coordinates);
        carrierMap.set("other", otherJson.features[0].geometry.coordinates);

        await webSocket.callWebSocket(GET_CARRIER_POS_PORT, function callback(connection){
            getCarrierPosUtil.getPos(connection, carrierMap);
        });
    }catch (err){
        throw err;
    }
}