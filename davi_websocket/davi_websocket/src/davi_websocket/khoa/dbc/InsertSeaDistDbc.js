const { dbClient, dbPool } = require('../../common/dbc/CommonDbc');


exports.selectList = async function () {
    try{
        let resQry = await dbClient.query('SELECT * FROM (SELECT ROW_NUMBER() OVER() AS rn, * FROM dv_khoa_sea_dist WHERE \"order\" = 1) a ORDER BY a.rn');
        return resQry.rows;
    }catch (err){
        throw err;
    }

}

exports.deleteList = async function(){
    try{
        dbClient.query('DELETE FROM dv_khoa_sea_dist_random');
        dbClient.query('SELECT SETVAL(\'dv_khoa_sea_dist_random_sea_dist_random_id_seq\', 0)');
    }catch (err){
        throw err;
    }
}

exports.insertList = async function (info, lngLat) {
    try{
        for (let i = 0; i < info.length; i++) {
            let seaDistId = info[i].sea_dist_id;
            let order = 1;
            let lngDist = lngLat.lng[i];
            let latDist = lngLat.lat[i];

            for(let j = 0; j < lngDist.length; j++){
                let lngDist2 = lngDist[j];
                let latDist2 = latDist[j];

                for(let k=0; k < lngDist2.length; k++){
                    let lng = lngDist2[k];
                    let lat = latDist2[k];

                    const resClient = await dbPool.connect();
                    try{
                        await resClient.query("INSERT INTO dv_khoa_sea_dist_random(sea_dist_id, \"order\", lng, lat) " +
                            "VALUES( $1, $2, $3, $4)", [seaDistId, order, lng, lat]);
                        order++;
                    }catch(err){
                        console.error('Error executing query', err);
                    }finally {
                        resClient.release();
                    }
                }
            }
        }
    }catch (err){
        throw err;
    }
}