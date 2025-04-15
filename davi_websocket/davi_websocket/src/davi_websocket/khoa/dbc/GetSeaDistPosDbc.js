const { dbClient } = require('../../common/dbc/CommonDbc');

exports.selectList = async function (resData){
    let queryStr = '';

    if(resData.data.first_chk){
        queryStr = 'SELECT * FROM ' +
            'dv_khoa_sea_dist_random a,' +
            '(SELECT sea_dist_id FROM dv_khoa_sea_dist WHERE "order" = 1 ' +
            'AND dom_str_type_nm = $1 AND dom_str_nm = $2 AND dom_end_nm = $3) b ' +
            'WHERE a.sea_dist_id = b.sea_dist_id AND \"order\" <= $4';
    }else{
        queryStr = 'SELECT * FROM ' +
        'dv_khoa_sea_dist_random a,' +
        '(SELECT sea_dist_id FROM dv_khoa_sea_dist WHERE "order" = 1 ' +
        'AND dom_str_type_nm = $1 AND dom_str_nm = $2 AND dom_end_nm = $3) b ' +
        'WHERE a.sea_dist_id = b.sea_dist_id AND \"order\" = $4'
    }

    try{
        let resQry = await dbClient.query(queryStr,
            [resData.data.dom_str_type_nm, resData.data.dom_str_nm, resData.data.dom_end_nm, resData.data.order]);
        return resQry.rows;
    }catch(err){
        throw err;
    }
}

exports.selectList2 = async function(){
    try{
        let queryStr = 'SELECT a.sea_dist_id, a.count, b.dom_str_type_nm, b.dom_str_nm, b.dom_end_nm FROM ' +
            '(SELECT sea_dist_id, COUNT(sea_dist_id)::int4 FROM dv_khoa_sea_dist_random GROUP BY sea_dist_id ORDER BY sea_dist_id) a ' +
            'LEFT JOIN dv_khoa_sea_dist b ON a.sea_dist_id = b.sea_dist_id ' +
            'WHERE b.order = 1 ORDER BY a.sea_dist_id';
        let resQry = await dbClient.query(queryStr);
        return resQry.rows;
    }catch (err){
        throw err;
    }
}

exports.selectRanPos = async function(seaDistId, idx){
    try{
        let queryStr = 'SELECT lng, lat FROM dv_khoa_sea_dist_random WHERE sea_dist_id = $1 AND \"order\" = $2';
        let resQry = await dbClient.query(queryStr, [seaDistId, idx]);
        return resQry.rows;
    }catch (err){
        throw err;
    }
}

exports.selectRanPosList = async function(seaDistId, idx){
    try{
        let queryStr = 'SELECT lng, lat FROM dv_khoa_sea_dist_random WHERE sea_dist_id = $1 AND \"order\" <= $2 ORDER BY \"order\"';
        let resQry = await dbClient.query(queryStr, [seaDistId, idx]);
        return resQry.rows;
    }catch (err){
        throw err;
    }
}