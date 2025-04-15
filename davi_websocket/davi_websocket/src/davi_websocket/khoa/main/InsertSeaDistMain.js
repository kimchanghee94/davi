const seaDistDbc = require('../dbc/InsertSeaDistDbc');
const seaDistUtil = require('../util/InsertSeaDistUtil');
const { INSERT_SEADIST_POS_PORT } = require("../../../../config");
const webSocket = require('../../common/socket/WebSocket');

exports.start = function () {
    try{
        webSocket.callWebSocket(INSERT_SEADIST_POS_PORT, function callback(connection, resData){
            seaDistUtil.insertPos(connection, resData);
        });
    }catch (err){
        throw err;
    }
}