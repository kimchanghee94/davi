const config = require('./config');
const cors = require('cors');
const bodyParser = require('body-parser');
const express = require('express');
const app = express();

//js파일 import
const insertSeaDist = require('./src/davi_websocket/khoa/main/InsertSeaDistMain');
const getSeaDistPos = require('./src/davi_websocket/khoa/main/GetSeaDistPosMain');
const getBusRealTimePos = require('./src/davi_websocket/traffic/main/GetBusRealTimePosMain');
const getCarrierPos = require('./src/davi_websocket/mulyuu/main/GetCarrierPosMain');

//websocket 서버는 한번만 열리도록 flag처리
let insetSeaDistFlag = false;
let getSeaDistPosFlag = false;
let getBusRealTimePosFlag = false;
let getCarrierPosFlag = false;

const baseUrl = "ws://localhost:";
app.listen(config.APP_PORT, (req, res) => {
    console.log('Connected, ' + config.APP_PORT);
})

//cors설정
let corsOption = {
    origin : 'http://localhost:5170',
}
app.use(cors(corsOption));

//Json request body parser
app.use(bodyParser.json());

app.get('/', (req, res) => {
    res.send('Server started');
});

//해상거리 랜덤좌표 데이터 조작 후 넣어주는 서버
app.get('/davi/khoa/seadist/insert-pos', async (req, res) => {
    let result = {};
    try{
        if(insetSeaDistFlag !== true){
            //데이터 삽입은 실수로 한번더 추가할 수 있으니 임시로 막아둔다.
            //await insertSeaDist.start();

            result["success"] = true;
            result["message"] = "Insert Sea Dist Position Server Started Success";

            insetSeaDistFlag = true;
        }else{
            result["success"] = true;
            result["message"] = "Insert Sea Dist Position Server Already Started";
        }

        result["url"] = baseUrl + config.INSERT_SEADIST_POS_PORT;
    } catch (err){
        result["success"] = false;
        result["message"] = "Insert Sea Dist Position Server Started Fail : " + err.stack;
    }

    res.send(JSON.stringify(result));
});

//해상거리 랜덤좌표 데이터 실시간으로 받아와주는 서버
app.get('/davi/khoa/seadist/get-pos', async (req, res) => {
    let result = {};

    try{
        if(getSeaDistPosFlag !== true){
            await getSeaDistPos.start2();

            result["success"] = true;
            result["message"] = "Sea Dist Position Server Started Success";

            getSeaDistPosFlag = true;
        }else{
            result["success"] = true;
            result["message"] = "Sea Dist Position Server Already Started";
        }

        result["url"] = baseUrl + config.GET_SEADIST_POS_PORT;
    }catch (err){
        result["success"] = false;
        result["message"] = "Sea Dist Position Server Started Fail : " + err.stack;
    }

    res.send(JSON.stringify(result));
});

//버스 노선 실시간으로 받아와주는 서버
app.post('/davi/traffic/busrealtime/get-pos',async (req, res) => {
   let result = {};

   try{
       if(getBusRealTimePosFlag !== true){
           await getBusRealTimePos.start(req.body);

           result["success"] = true;
           result["message"] = "Get Bus Real Time Position Server Started Success";

           getBusRealTimePosFlag = true;
       }else{
           result["success"] = true;
           result["message"] = "Get Bus Real Time Position Server Already Started";
       }

       result["url"] = baseUrl + config.GET_BUSREALTIME_POS_PORT;
   }catch (err){
       result["success"] = false;
       result["message"] = "Get Bus Real Time Position Server Started Fail : " + err.stack;
   }

   res.send(JSON.stringify(result));
});

//택배차 실시간 좌표 받아와주는 서버
app.get('/davi/mulyuu/carrier/get-pos', async(req, res) => {
    let result = {};

    try{
        if(getCarrierPosFlag !== true){
            await getCarrierPos.start();

            result["success"] = true;
            result["message"] = "Get Carrier Position Server Started Success";

            getCarrierPosFlag =  true;
        }else{
            result["success"] = true;
            result["message"] = "Get Carrier Position Server Already Started";
        }

    }catch (err){
        result["success"] = false;
        result["message"] = "Get Carrier Position Server Started Fail : " + err.stack;
    }

    res.send(JSON.stringify(result));
});