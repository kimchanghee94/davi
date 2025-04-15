<script>
    import mapboxgl from 'mapbox-gl';
    import {onDestroy, onMount} from "svelte";
    import {getColorRandom} from "../../util/CommonUtil.js";
    import {initLngLat, inputBasicSeaData, getShipImage} from "../../util/SeaDistUtil.js";

    let webSocketServerUrl = import.meta.env.VITE_WEBSOCKET_URL;
    let map;

    let socket = null;

    //websocket 서버 호출하기
    async function connectSocketServer(){
        let res = await fetch(webSocketServerUrl + "/davi/khoa/seadist/get-pos",{
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
                // console.log("Socket Received Data", receivedData);

                if(receivedData.success === true){
                    await showDataOnMap(receivedData);
                }
            };

            socket.onclose = (event) => {
                console.log('Disconnected from WebSocket server');
            };
        }else{
            console.log(resJson.message);
        }
    }

    //데이터 맵에 표시하기
    let resultDirGeoJson = {
        type : "FeatureCollection",
        features : [
        ]
    };

    let resultShipGeoJson = {
        type : 'FeatureCollection',
        features : [
        ]
    };

    async function showDataOnMap(receivedData){
        let seaDistData = receivedData.data;

        if(receivedData.firstChk === true){
            //각 데이터 feature에 담기
            for(let tmpData of seaDistData){
                let eachColor = getColorRandom();

                let dirFeature = {
                    type : "Feature",
                    geometry : {
                        type : "LineString",
                        coordinates : tmpData.coordinates,
                    },
                    properties : {
                        color : eachColor,
                        domStrTypeNm : tmpData.domStrTypeNm,
                        domStrNm : tmpData.domStrNm,
                        domEndNm : tmpData.domEndNm
                    }
                };

                let shipFeature = {
                    type : 'Feature',
                    geometry : {
                        type : 'Point',
                        coordinates : tmpData.coordinates[tmpData.coordinates.length - 1],
                    },
                    properties : {
                        color : eachColor,
                        domStrTypeNm : tmpData.domStrTypeNm,
                        domStrNm : tmpData.domStrNm,
                        domEndNm : tmpData.domEndNm
                    }
                };

                resultDirGeoJson.features.push(dirFeature);
                resultShipGeoJson.features.push(shipFeature);
            }

            //최종 Features 담기
            map.addSource('dir-source', {
                type: 'geojson',
                data: resultDirGeoJson
            });

            // map.addLayer({
            //     'id': 'dir-layer',
            //     'type': 'line',
            //     'source': 'dir-source',
            //     'paint': {
            //         'line-color': ['get','color'],
            //         'line-opacity': 0.8,
            //         'line-width': 3
            //     },
            // });

            map.addSource('ship-source', {
                type: 'geojson',
                data: resultShipGeoJson
            });

            map.addLayer({
                'id' : 'ship-layer',
                'type' : 'symbol',
                'source' : 'ship-source',
                'layout' : {
                    'icon-image' : 'ship-image',
                    'icon-allow-overlap' : true,
                },
                "paint" : {
                    "icon-color" : ['get', 'color'],
                }
            });

            // map.addLayer({
            //     'id': 'ship-layer',
            //     'type': 'circle',
            //     'source': 'ship-source',
            //     'paint' : {
            //         'circle-color': ['get','color'],
            //         'circle-radius': 6,
            //         'circle-opacity' : 1
            //     }
            // });
        }
        //이미 커넥션 유지된 상태에서 받은 데이터는 마지막 데이터만 추가해주기
        else {
            for (let i = 0; i < seaDistData.length; i++) {
                if(seaDistData[i].loop === true){
                    resultDirGeoJson.features[i].geometry.coordinates = [];
                }

                resultDirGeoJson.features[i].geometry.coordinates.push(seaDistData[i].coordinates);
                resultShipGeoJson.features[i].geometry.coordinates = seaDistData[i].coordinates;

                map.getSource('dir-source').setData(resultDirGeoJson);
                map.getSource('ship-source').setData(resultShipGeoJson);
            }
        }
    }

    onMount(async () => {
        //MapBox GL 초기설정
        map = new mapboxgl.Map({
            container: 'map-container',
            style: 'mapbox://styles/mapbox/dark-v11',
            center: initLngLat,
            zoom: 6,
            dragRotate:  false,
        });

        map.on('load', async () => {
            await inputBasicSeaData(map);
            await getShipImage(map);
            await connectSocketServer();
        });
    });

    //다른 페이지로 이동했을 때는 소켓 종료
    onDestroy(async () => {
       socket.close();
    });
</script>

<div style="display: flex">
    <div id="map-container"></div>
</div>

<style>
    #map-container {
        width: 90%;
        height: 800px;
    }
</style>