<script>
    import { onMount } from "svelte";
    import {initLngLat, getKhoaSeaDistData, inputBasicSeaData} from "../../util/SeaDistUtil.js";
    import {getColorRandom, sleep} from "../../util/CommonUtil.js";
    import mapboxgl from "mapbox-gl";

    let map;
    let inputStrTypeNm = "주요항로";
    let inputStrNm = "감천항";
    let inputEndNm = "고현항";
    let inputOrder = "20";

    let socket = null;
    let shipMovePeriod = 100;

    //websocket 서버 호출하기
    async function connectSocketServer(){
        let res = await fetch("http://localhost:9292/davi/khoa/seadist/get-pos",{
            method : 'GET',
        });

        let resJson = await res.json();
        console.log("START WEBSOCKET SERVER", resJson)
        if(resJson.success){
            socket = new WebSocket(resJson.url);

            socket.onopen = (event) => {
                console.log('Connected to WebSocket server');
            };

            socket.onmessage = async (event) => {
                let receivedData = JSON.parse(event.data);
                let srcLayerName = receivedData.req_data.data.dom_str_type_nm + "-" + receivedData.req_data.data.dom_str_nm + "-" +
                    receivedData.req_data.data.dom_end_nm + receivedData.req_data.data.input_time;

                //이전에 받은 데이터 기반으로 다음 데이터를 받아올 수 있도록 한다.
                let reqData = receivedData.req_data.data;

                if(receivedData.finish === true){
                    reqData.order = '0';

                    resultDirGeoJson.features[mapIdx.get(srcLayerName)].geometry.coordinates = [];
                    resultShipGeoJson.features[mapIdx.get(srcLayerName)].geometry.coordinates = [];
                    map.getSource('dir-source').setData(resultDirGeoJson);
                    map.getSource('ship-source').setData(resultShipGeoJson);
                }else{
                    executeRealTimeData3(receivedData, srcLayerName);
                }

                await sleep(shipMovePeriod);

                getRealTimeData(reqData.dom_str_type_nm, reqData.dom_str_nm,
                    reqData.dom_end_nm, Number(reqData.order) + 1, false, reqData.input_time);
            };

            socket.onclose = (event) => {
                console.log('Disconnected from WebSocket server');
            };
        }else{
            console.log(resJson.message);
        }
    }

    //모든 실시간 데이터 받아와서 배 이동 시키기
    async function getRealTimeAllData(){
        let allSeaDistData = await getKhoaSeaDistData();

        for(let i=0; i<allSeaDistData.data.length; i++){
            let seaDistData = allSeaDistData.data[i];
            getRealTimeData(seaDistData.domStrTypeNm, seaDistData.domStrNm, seaDistData.domEndNm, 1, true, i);
        }

    }

    //실시간 데이터 받아오기
    async function getRealTimeData(domStrTypeNm, domStrNm, domEndNm, order, firstChk, inputTime){
        if (socket !== null && socket.readyState === WebSocket.OPEN) {

            //초기입력으로 선박 이동데이터를 받아와서 실행되도록 한다.
            if(domStrTypeNm === null){
                domStrTypeNm = inputStrTypeNm;
                domStrNm = inputStrNm;
                domEndNm = inputEndNm;
                order = inputOrder;
                firstChk = true;

                let date = new Date();
                inputTime = date.getFullYear() + '' + (date.getMonth() + 1) + '' +
                    date.getDate() + '' + date.getHours() + '' +
                    date.getMinutes() + '' + date.getSeconds() + '' + date.getMilliseconds();
            }

            let sendData = {
                "success" : "true",
                "message" : "Send To Server Success",
                "data" : {
                    "dom_str_type_nm" : domStrTypeNm,
                    "dom_str_nm" : domStrNm,
                    "dom_end_nm" : domEndNm,
                    "order" : order,
                    "input_time" : inputTime,
                    "first_chk" : firstChk,
                }
            }
            socket.send(JSON.stringify(sendData));
        }else if(socket === null){
            connectSocketServer();
        }
    }

    //실시간 데이터 한개씩 받아와서 실행하기 & 하나의 layer에서 사용해보기
    let mapIdx = new Map();
    let featureIdx = 0;

    //배 이동 경로 표시
    let resultDirGeoJson = {
        type : "FeatureCollection",
        features : [
        ]
    };

    let resultShipGeoJson = {
        type : 'FeatureCollection',
        features : [
        ],
    };

    async function executeRealTimeData3(receivedData, srcLayerName){
        let totalCombPosArr = [];
        let eachColor = getColorRandom();

        for(let i=0; i<receivedData.res_data.length; i++){
            let tmpData = receivedData.res_data[i];
            let lat = parseFloat(tmpData.lat);
            let lng = parseFloat(tmpData.lng);

            totalCombPosArr.push(new Array(lng, lat));
        }

        let dirFeature = {
            type : "Feature",
            geometry : {
                type : "LineString",
                coordinates : totalCombPosArr,
            },
            properties : {
                color : eachColor,
            }
        };

        let shipFeature = {
            type : 'Feature',
            geometry : {
                type : 'Point',
                coordinates : totalCombPosArr[totalCombPosArr.length - 1],
            },
            properties : {
                color : eachColor,
            }
        };

        //가장 맨처음일 경우에 대한 layer와 source를 만들어준다.
        if(mapIdx.size === 0) {
            mapIdx.set(srcLayerName, featureIdx++);

            resultDirGeoJson.features.push(dirFeature);
            resultShipGeoJson.features.push(shipFeature);

            map.addSource('dir-source', {
                type: 'geojson',
                data: resultDirGeoJson
            });

            map.addLayer({
                'id': 'dir-layer',
                'type': 'line',
                'source': 'dir-source',
                'paint': {
                    'line-color': ['get','color'],
                    'line-opacity': 0.8,
                    'line-width': 3
                },
            });

            map.addSource('ship-source', {
                type: 'geojson',
                data: resultShipGeoJson
            });

            map.addLayer({
                'id': 'ship-layer',
                'type': 'symbol',
                'source': 'ship-source',
                'layout': {
                    'icon-image' : ['concat', 'pulsing-dot-', ['get', 'color']],
                    'icon-allow-overlap': true,
                }
            });
        }
        else{
            //이미 이전에 데이터가 존재한 경우
            if(mapIdx.has(srcLayerName) === true){
                resultDirGeoJson.features[mapIdx.get(srcLayerName)].geometry.coordinates.push(totalCombPosArr[0]);
                resultShipGeoJson.features[mapIdx.get(srcLayerName)].geometry.coordinates = totalCombPosArr[0];
                map.getSource('dir-source').setData(resultDirGeoJson);
                map.getSource('ship-source').setData(resultShipGeoJson);
            }
            //기존에 데이터가 존재하지 않는 경우
            else{
                mapIdx.set(srcLayerName, featureIdx++);

                resultDirGeoJson.features.push(dirFeature);
                resultShipGeoJson.features.push(shipFeature);
                map.getSource('dir-source').setData(resultDirGeoJson);
                map.getSource('ship-source').setData(resultShipGeoJson);
            }
        }
    }

    //실시간 데이터 한개씩 받아와서 실행하기
    async function executeRealTimeData2(receivedData){
        let totalCombPosArr = [];
        let srcLayerName = "-" + receivedData.req_data.data.dom_str_type_nm + "-" + receivedData.req_data.data.dom_str_nm + "-" +
            receivedData.req_data.data.dom_end_nm;

        for(let i=0; i<receivedData.res_data.length; i++){
            let tmpData = receivedData.res_data[i];
            let lat = parseFloat(tmpData.lat);
            let lng = parseFloat(tmpData.lng);

            totalCombPosArr.push(new Array(lng, lat));
        }

        //이미 이전에 데이터가 존재한 경우
        if(map.getLayer('dir-layer' + srcLayerName)){
            let dirGeoJson = map.getSource('dir-source' + srcLayerName)._options.data;
            let shipGeoJson = map.getSource('ship-source' + srcLayerName)._options.data;

            dirGeoJson.features[0].geometry.coordinates.push(totalCombPosArr[0]);
            shipGeoJson.features[0].geometry.coordinates = totalCombPosArr[0];
            map.getSource('dir-source' + srcLayerName).setData(dirGeoJson);
            map.getSource('ship-source' + srcLayerName).setData(shipGeoJson);
        }else{
            //배 이동 경로 표시
            let dirGeoJson = {
                type : "FeatureCollection",
                features : [
                ]
            };

            let shipGeoJson = {
                type : 'FeatureCollection',
                features : [
                ],
            };


            let dirFeature = {
                type : "Feature",
                geometry : {
                    type : "LineString",
                    coordinates : totalCombPosArr,
                }
            };

            let shipFeature = {
                type : 'Feature',
                geometry : {
                    type : 'Point',
                    coordinates : totalCombPosArr[totalCombPosArr.length - 1],
                }
            };

            dirGeoJson.features.push(dirFeature);
            shipGeoJson.features.push(shipFeature);

            map.addSource('dir-source' + srcLayerName, {
                type : 'geojson',
                data : dirGeoJson
            });

            map.addLayer({
                'id' : 'dir-layer' + srcLayerName,
                'type' : 'line',
                'source' : 'dir-source' + srcLayerName,
                'paint' : {
                    'line-color' : getColorRandom(),
                    'line-opacity' : 0.8,
                    'line-width' : 3
                }
            });

            map.addSource('ship-source' + srcLayerName, {
                type : 'geojson',
                data : shipGeoJson
            });

            map.addLayer({
                'id' : 'ship-layer' + srcLayerName,
                'type' : 'symbol',
                'source' : 'ship-source' + srcLayerName,
                'layout' : {
                    'icon-image' : 'pulsing-dot',
                    'icon-allow-overlap' : true,
                }
            });
        }
    }

    //실시간 데이터 실행하기
    async function executeRealTimeData(receivedData){
        let jsonData = JSON.parse(receivedData);
        let lngArr = jsonData.lng;
        let latArr = jsonData.lat;

        let totalCombPosArr = [];

        //하나의 해상 경로에 존재하는 배처리
        for(let i=0; i < lngArr.length; i++){
            let lngDist = lngArr[i];
            let latDist = latArr[i];

            let combPosArr = [];

            //하나의 해상 경로 중 각각의 선 처리
            for(let j=0; j<lngDist.length; j++){
                let lngLine = lngDist[j];
                let latLine = latDist[j];

                for(let k=0; k<lngLine.length; k++){
                    let x = lngLine[k];
                    let y = latLine[k];

                    let tmpArr = [];
                    tmpArr.push(x, y);
                    combPosArr.push(tmpArr);
                }
            }

            totalCombPosArr.push(combPosArr);
        }

        //배 이동 경로 표시
        let dirGeoJson = {
            type : "FeatureCollection",
            features : [
            ]
        };

        let shipGeoJson = {
            type : 'FeatureCollection',
            features : [
            ],
        };

        for(let i=0; i<totalCombPosArr.length; i++){
            let dirFeature = {
                type : "Feature",
                geometry : {
                    type : "LineString",
                    coordinates : [totalCombPosArr[i][0]],
                }
            };

            let shipFeature = {
                type : 'Feature',
                geometry : {
                    type : 'Point',
                    coordinates : totalCombPosArr[i][0],
                }
            };

            dirGeoJson.features.push(dirFeature);
            shipGeoJson.features.push(shipFeature);
        }

        //초기화
        if(map.getLayer('dir-layer')){
            map.removeLayer('dir-layer');
        }
        if(map.getSource('dir-source')){
            map.removeSource('dir-source');
        }
        if(map.getLayer('ship-layer')){
            map.removeLayer('ship-layer');
        }
        if(map.getSource('ship-source')){
            map.removeSource('ship-source');
        }
        if(map.hasImage('pulsing-dot')){
            map.removeImage('pulsing-dot');
        }

        map.addSource('ship-source', {
            type : 'geojson',
            data : shipGeoJson
        });

        map.addSource('dir-source', {
            type : 'geojson',
            data : dirGeoJson
        });

        map.addLayer({
            'id' : 'ship-layer',
            'type' : 'symbol',
            'source' : 'ship-source',
            'layout' : {
                'icon-image' : 'pulsing-dot',
                'icon-allow-overlap' : true,
            }
        });

        map.addLayer({
            'id' : 'dir-layer',
            'type' : 'line',
            'source' : 'dir-source',
            'paint' : {
                'line-color' : 'red',
                'line-opacity' : 0.6,
                'line-width' : 3
            }
        });

        //layer순서 바꾸기
        map.moveLayer('dir-layer', 'ship-layer');


        let endFlagArr = [];
        let timeIdx = 0;
        let timer = setInterval(() => {
            for(let i=0; i < totalCombPosArr.length; i++){
                if(timeIdx < totalCombPosArr[i].length){
                    dirGeoJson.features[i].geometry.coordinates.push(totalCombPosArr[i][timeIdx]);
                    map.getSource('dir-source').setData(dirGeoJson);
                    shipGeoJson.features[i].geometry.coordinates = totalCombPosArr[i][timeIdx];
                    map.getSource('ship-source').setData(shipGeoJson);

                }else if(!endFlagArr.includes(i)){
                    endFlagArr.push(i);
                    if(endFlagArr.length === totalCombPosArr.length){
                        window.clearInterval(timer);
                    }
                }
            }

            timeIdx++;
        }, 100);

    }

    onMount(async () => {
        map = new mapboxgl.Map({
            container: 'map-container',
            style: 'mapbox://styles/mapbox/dark-v11',
            center: initLngLat,
            zoom: 6,
            dragRotate:  false,
        });

        //map에 클릭 옵션 넣기
        /*map.on('click', async (e) => {
            let data = {
                lng : e.lngLat.lng,
                lat : e.lngLat.lat
            };

            let clickSeaDistJson = await getKhoaSeaDistData(data);
            let clickHangJson = await getKhoaHangData(data);
            let clickByouJson = await getKhoaBuoyData(data);
            let clickLsmdContJson = await getKhoaLsmdContData(data);


        });*/

        //이미지 넣기
        map.on('styleimagemissing', (e) => {
            const id = e.id;

            const prefix = 'pulsing-dot-';
            if(!id.includes(prefix)) return;

            const color = id.replace(prefix, '');

            //배 현재 위치 표시
            const size = 100;

            // This implements `StyleImageInterface`
            // to draw a pulsing dot icon on the map.
            const pulsingDot = {
                width: size,
                height: size,
                data: new Uint8Array(size * size * 4),

// When the layer is added to the map,
// get the rendering context for the map canvas.
                onAdd: function () {
                    const canvas = document.createElement('canvas');
                    canvas.width = this.width;
                    canvas.height = this.height;
                    this.context = canvas.getContext('2d');
                },

// Call once before every frame where the icon will be used.
                render: function () {
                    const duration = 1000;
                    const t = (performance.now() % duration) / duration;

                    const radius = (size / 2) * 0.3;
                    const outerRadius = (size / 2) * 0.7 * t + radius;
                    const context = this.context;

// Draw the outer circle.
                    context.clearRect(0, 0, this.width, this.height);
                    context.beginPath();
                    context.arc(
                        this.width / 2,
                        this.height / 2,
                        outerRadius,
                        0,
                        Math.PI * 2
                    );
                    context.fillStyle = `rgba(255, 200, 200, ${1 - t})`;
                    context.fill();

// Draw the inner circle.
                    context.beginPath();
                    context.arc(
                        this.width / 2,
                        this.height / 2,
                        radius,
                        0,
                        Math.PI * 2
                    );
                    context.fillStyle = color;
                    context.strokeStyle = 'white';
                    context.lineWidth = 2 + 4 * (1 - t);
                    context.fill();
                    context.stroke();

// Update this image's data with data from the canvas.
                    this.data = context.getImageData(
                        0,
                        0,
                        this.width,
                        this.height
                    ).data;

// Continuously repaint the map, resulting
// in the smooth animation of the dot.
                    map.triggerRepaint();

// Return `true` to let the map know that the image was updated.
                    return true;
                }
            };
            map.addImage(id, pulsingDot, {pixelRatio : 2});
        });

        //map에 데이터 넣기
        map.on('load', async () => {
            await inputBasicSeaData(map);
        });
    });
</script>

<div style="display: flex">
    <div id="map-container"></div>
    <div style="display: flow">
        <button class="btn btn-primary"
                style="width: 250px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-right: 50px"
                on:click={() => getRealTimeData(null, null, null, null, null, null)}>실시간 특정 선박 이동</button>

        <div style="display: flow">
            <div class = "ml-5 mt-5 mb-5" style="width : 400px">
                <div class="list-group">
                    <li>항로 타입</li>
                    <input class="mt-2" placeholder="항로 타입" bind:value={inputStrTypeNm}>
                    <li>출발항</li>
                    <input class="mt-2" placeholder="출발항" bind:value={inputStrNm}>
                    <li>도착항</li>
                    <input class="mt-2" placeholder="도착항" bind:value={inputEndNm}>
                    <li>순서</li>
                    <input class="mt-2" placeholder="순서" bind:value={inputOrder}>
                </div>
            </div>
        </div>

        <button class="btn btn-danger"
                style="width: 250px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-right: 50px"
                on:click={getRealTimeAllData}>모든 선박 이동하기</button>
        <div style="display: flow">
            <div class = "ml-5 mt-5 mb-5" style="width : 400px">
                <div class="list-group">
                    <li>이동 속도</li>
                    <input class="mt-2" placeholder="이동 속도" bind:value={shipMovePeriod}>
                </div>
            </div>
        </div>
    </div>
</div>

<style>
    #map-container {
        width: 80%;
        height: 800px;
    }
</style>