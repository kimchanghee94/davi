const http = require('http');
const WebSocketServer = require('websocket').server;
const configJs = require('../../../../config');

//각 클라이언트의 값을 배열에 담고  모든 클라이언트에게 응답하기
//혹은 각각의 원하는 클라이언트에 대해서만 응답히기, 전역변수에 대한 관리 등
//해당 로직으로 각각의 채널을 열어 관리가 가능하다.
//기존 CLIENTS배열 없이 사용했을 때는 connection에 담긴 값을 이용해 응답하기 때문에
//개별 채널로 사용했다고 보면 된다.
let CLIENTS = [];

exports.callWebSocket = async function (port, func){
    try{
        let server = http.createServer((req, res) => {
            console.log(req, res);
            res.writeHead(404);
            res.end();
        });

        // Create a WebSocket server by attaching it to the HTTP server
        const wsServer = new WebSocketServer({
            httpServer: server
        });

        // Listen for WebSocket connection requests
        wsServer.on('request', (request) => {
            const connection = request.accept(null, request.origin);
            console.log('WebSocket Connection Accepted');

            // 클라이언트에게 일반적인 송신만 처리할 경우
            if(connection.readyState === connection.OPEN){
                if(port === configJs.GET_SEADIST_POS_PORT){
                    if(CLIENTS[connection] === undefined){
                        CLIENTS.push(connection);
                        func(connection, true);
                    }else{
                        func(connection, false);
                    }
                }else if(port === configJs.GET_BUSREALTIME_POS_PORT
                || port === configJs.GET_CARRIER_POS_PORT){
                    func(connection);
                }
            }

            // 클라이언트와 주고받으며 데이터를 처리할 경우
            connection.on('message',(message) => {
                if (message.type === 'utf8') {
                    console.log(`Received message: ${message.utf8Data}`);

                    let resData = message.utf8Data;

                    if(port === configJs.INSERT_SEADIST_POS_PORT){
                        resData = JSON.parse(resData);
                        func(connection, resData);
                    }
                }
            });

            connection.on('close', (reasonCode, description) => {
                console.log(`WebSocket Connection Closed: ${reasonCode} - ${description}`);
            });
        });

        server.on('error', (error) => {
            console.error('WebSocket Server Error:', error.message);
        });

        // Start the HTTP server
        server.listen(port, () => {
            console.log('WebSocket is listening on port ' + port);
        });
    }catch (err){
        throw err;
    }
}

