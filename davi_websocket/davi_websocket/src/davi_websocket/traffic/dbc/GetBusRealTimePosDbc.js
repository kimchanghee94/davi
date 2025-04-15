const { dbClient } = require('../../common/dbc/CommonDbc');

exports.selectBusSeoulInfoList = async function(){
    try{
        let queryStr = 'SELECT * FROM DV_TRAFFIC_BUS_SEOUL_INFO WHERE node_order = \'1\' limit 10';
        let resQry = await(dbClient.query(queryStr));
        return resQry.rows;
    }catch (err){
        throw err;
    }
}