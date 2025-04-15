const commonUtil = require('../../common/util/CommonUtil');

let period = 60;

exports.getPos = async function(connection, carrierMap){
    while(true){
        let result = {};

        try{
            let timeStamp = commonUtil.getTimeStampSec();

            for(let [key, value] of carrierMap){
                let length = value.length;
                let idx = (timeStamp % length);



                break;
            }
        }catch (err){
            console.error(err);
            result["success"] = false;
            result["message"] = "Get Carrier Pos Fail " + err.stack;
            connection.sendUTF(JSON.stringify(result));

            break;
        }

        await commonUtil.sleep(period);
    }
};