<script>
    import {onDestroy, onMount} from "svelte";
    import {
        initLngLat,
        getFogData,
        getFrostData,
        getRseData,
        getSpeedDumpData,
        getConAccInfo,
        getRealTimeTrafficInfo,
        getRiskArea,
        getCrosswalkPedis,
        getCrossRisk,
        getPedisCollision,
        getTunnelRisk,
        getCctvData,
        getTrafficLightData,
        getChildZoneData,
        getCrosswalkData
    } from "./TrafficMap";
    import {getMap, loadMapImage} from "../../util/CommonUtil.js";
    import speedDumpSvg from "../../routes/svg/speed-dump.svg"
    import rsePng from "../../routes/svg/rse.png";
    import accidentPng from "../../routes/svg/accident.png";
    import constructPng from "../../routes/svg/construct.png";
    import cctvSvg from "../../routes/svg/cctv.svg";
    import crossWalkSvg from "../../routes/svg/crosswalk.svg";
    import childZoneSvg from "../../routes/svg/childzone.svg";
    import trafficLightPng from "../../routes/svg/trafficLight.png";
    import itisPng from "../../routes/svg/itis.png";
    import busSvg from "../../routes/svg/bus.svg";
    import {getBusRoute, getBusRoute2, getSeoulBusInfo, showBusRealTimeDataOnMap} from "./Bus.js";
    import {connectSocketServer} from "../../util/CommonUtil.js";

    let map;
    let socket = null;

    //데이터 맵에 표시하기
    let resultFeatures = {
        type : "FeatureCollection",
        features : [
        ]
    };

    //버스 실시간 맵에 표시하기
    let busRealTimeFeatures = {
        type : "FeatureCollection",
        features : [],
    };

    onMount(async () => {
        map = await getMap('map-container', 'mapbox://styles/mapbox/dark-v11', initLngLat, 7);

        //맵관련 이미지 띄우기
        await loadMapImage(map, 'speed-dump-img', 15, 15, speedDumpSvg, {sdf : false});
        await loadMapImage(map, 'rse-img', 30, 30, rsePng, {sdf : false});
        await loadMapImage(map, 'accident-img', 20, 20, accidentPng, {sdf : false});
        await loadMapImage(map, 'construct-img', 20, 20, constructPng, {sdf : false});
        await loadMapImage(map, 'cctv-img', 20, 20, cctvSvg, {sdf : false});
        await loadMapImage(map, 'crosswalk-img', 20, 20, crossWalkSvg, {sdf : false});
        await loadMapImage(map, 'childZone-img', 20, 20, childZoneSvg, {sdf : false});
        await loadMapImage(map, 'trafficLight-img', 20, 20, trafficLightPng, {sdf : false});
        await loadMapImage(map, 'itis-img', 20, 20, itisPng, {sdf : true});
        await loadMapImage(map, 'bus-img', 20, 20, busSvg, {sdf : true});

        map.on('load', async () => {
            let fogFeature = await getFogData();
            let frostFeature = await getFrostData();
            let rseFeature = await getRseData();
            let speedDumpFeature = await getSpeedDumpData();
            let conAccFeature = await getConAccInfo();
            let realTimeTrafficInfoFeature = await getRealTimeTrafficInfo();

            for(let i=0; i<realTimeTrafficInfoFeature.length; i++){
                resultFeatures.features.push(realTimeTrafficInfoFeature[i]);
            }

            resultFeatures.features.push(conAccFeature.accFeature);
            resultFeatures.features.push(conAccFeature.conFeature);
            resultFeatures.features.push(fogFeature);
            resultFeatures.features.push(frostFeature);
            resultFeatures.features.push(rseFeature);
            resultFeatures.features.push(speedDumpFeature);

            //itis처리
           /* let tunnelRisk = await getTunnelRisk();
            let riskArea = await getRiskArea();
            let crosswalkPedis = await getCrosswalkPedis();
            let crossRisk = await getCrossRisk();
            let pedisCollisiont = await getPedisCollision();

            for(let tmp of tunnelRisk){
                resultFeatures.features.push(tmp);
            }
            for(let tmp of riskArea){
                resultFeatures.features.push(tmp);
            }
            for(let tmp of crosswalkPedis){
                resultFeatures.features.push(tmp);
            }
            for(let tmp of crossRisk){
                resultFeatures.features.push(tmp);
            }
            for(let tmp of pedisCollisiont){
                resultFeatures.features.push(tmp);
            }*/

            //공공데이터포털 처리
            let cctvFeature = await getCctvData();
            let trafficLightFeature = await getTrafficLightData();
            let childZoneFeature = await getChildZoneData();
            let crosswalkFeature = await getCrosswalkData();

            resultFeatures.features.push(cctvFeature);
            resultFeatures.features.push(trafficLightFeature);
            resultFeatures.features.push(childZoneFeature);
            resultFeatures.features.push(crosswalkFeature);

            //자율주행 버스 노선 좌표 받아오기
            let seoulBusInfos = await getSeoulBusInfo();
            let busRouteInfos = await getBusRoute2(seoulBusInfos);
            // let busRouteInfos = await getBusRoute(seoulBusInfos);

            for(let busRouteInfo of busRouteInfos){
                resultFeatures.features.push(busRouteInfo);
            }

            console.log("Result Feature : ", resultFeatures);

            map.addSource('base-source', {
                type: 'geojson',
                data: resultFeatures
            });

            //자율주행 및 결빙 및 안개지역 layer
            map.addLayer({
                'id': 'line-layer',
                'type': 'line',
                'source': 'base-source',
                'paint': {
                    'line-color': ['get', 'color'],
                    'line-opacity': 0.6,
                    'line-width': 3
                },
                'filter': ['==', '$type', 'LineString']
            });

            //횡단보도, 어린이보호구역, rse icon뒤에 반원 표시하기
            map.addLayer({
                'id' : 'base-circle-layer',
                'type' : 'circle',
                'source' : 'base-source',
                'paint' : {
                    'circle-radius' : 20,
                    'circle-opacity' : 0.4,
                    'circle-color' : [
                        'match',
                        ['get', 'content'],
                        'childZone',
                        '#ffff00',
                        'crosswalk',
                        '#ff0000',
                        'rse',
                        '#008000',
                        'transparent'
                    ]
                },
            });

            //rse layer
            map.addLayer({
                'id' : 'rse-layer',
                'type' : 'symbol',
                'source' : 'base-source',
                'layout' : {
                    'icon-image' : 'rse-img',
                    'icon-allow-overlap' : false,
                },
                'filter': ['==', 'content', 'rse']
            });

            //과속방지턱 layer
            map.addLayer({
                'id': 'speeddump-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'speed-dump-img',
                    'icon-allow-overlap': false,
                },
                'filter': ['==', 'content', 'speedDump']
            });

            //공사구간 layer
            map.addLayer({
                'id': 'construct-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'construct-img',
                    'icon-allow-overlap': false,
                },
                'filter': ['==', 'content', 'construct']
            });

            //사고구간 layer
            map.addLayer({
                'id': 'accident-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'accident-img',
                    'icon-allow-overlap': false,
                },
                'filter': ['==', 'content', 'accident']
            });

            //itis layer
            map.addLayer({
                'id': 'itis-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'itis-img',
                    'icon-allow-overlap': false,
                },
                "paint" : {
                    'icon-color' : ['get', 'color'],
                    'icon-opacity' : 0.5
                },
                'filter': ['==', 'content', 'itis']
            });

            //cctv
            map.addLayer({
                'id': 'cctv-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'cctv-img',
                    'icon-allow-overlap': false,
                },
                'filter': ['==', 'content', 'cctv']
            });

            //횡단보도
            map.addLayer({
                'id': 'crosswalk-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'crosswalk-img',
                    'icon-allow-overlap': true,
                },
                'filter': ['==', 'content', 'crosswalk']
            });

            //신호등
            map.addLayer({
                'id': 'trafficLight-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'trafficLight-img',
                    'icon-allow-overlap': false,
                },
                'filter': ['==', 'content', 'trafficLight']
            });

            //어린이 보호구역
            map.addLayer({
                'id': 'childZone-layer',
                'type': 'symbol',
                'source': 'base-source',
                'layout': {
                    'icon-image': 'childZone-img',
                    'icon-allow-overlap': false,
                },
                'filter': ['==', 'content', 'childZone']
            });

            map.addSource('bus-real-time-source', {
                type: 'geojson',
                data: busRealTimeFeatures
            });

            map.addLayer({
                'id' : 'bus-real-time-layer',
                'type' : 'symbol',
                'source' : 'bus-real-time-source',
                'layout' : {
                    'icon-image' : 'bus-img',
                    'icon-allow-overlap' : true,
                },
                "paint" : {
                    'icon-color' : ['get', 'color'],
                    'icon-opacity' : 0.6
                },
                'filter' : ['==', 'content', 'busRealTimeDataPos']
            });

            //socket 호출
            await connectSocketServer(socket,'/davi/traffic/busrealtime/get-pos',seoulBusInfos,
                async function callback(receivedData){
                    busRealTimeFeatures = {
                        type : "FeatureCollection",
                        features : await showBusRealTimeDataOnMap(receivedData, busRouteInfos),
                    };

                    console.log("BUS REAL TIME FEATURES", busRealTimeFeatures);
                    map.getSource('bus-real-time-source').setData(busRealTimeFeatures);
                }
            );
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
        width: 80%;
        height: 800px;
    }
</style>