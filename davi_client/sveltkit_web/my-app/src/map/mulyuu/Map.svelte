<script>
    import {onDestroy, onMount} from "svelte";
    import Speedometer from "svelte-speedometer";
    import {
        getEMDData,
        getRouteFeatrue, getSGGData,
        initLngLat
    } from "./Mulyuu.js";
    import {getMap} from "../../util/CommonUtil.js";
    import "./mapboxgl-custom.css";

    let resultFeatures = {
        type : "FeatureCollection",
        features : [

        ]
    };

    let map;
    let socket = null;
    let speedGaugePopups = [];

    onMount(async () => {
        map = await getMap('map-container', 'mapbox://styles/mapbox/dark-v11', initLngLat, 10);

        //택배차 라우트 경로
        let gwanjangFeatures = await getRouteFeatrue('./json/mulyuu_gwanjang_route.json');
        let jayangFeatures = await getRouteFeatrue('./json/mulyuu_jayangdong_route.json');
        let junggokFeatures = await getRouteFeatrue('./json/mulyuu_junggok_route.json');
        let guyeFeatures = await getRouteFeatrue('./json/mulyuu_guyedong_route.json');
        let otherFeatures = await getRouteFeatrue('./json/mulyuu_other_route.json');

        resultFeatures.features.push(gwanjangFeatures);
        resultFeatures.features.push(jayangFeatures);
        resultFeatures.features.push(junggokFeatures);
        resultFeatures.features.push(guyeFeatures);
        resultFeatures.features.push(otherFeatures);

        //시군구 데이터 가져오기
        let emdFeatures = await getEMDData({fullNm : '서울'});
        let sggFeatures = await getSGGData({fullNm : '서울'}, emdFeatures);

        for(let emdFeature of emdFeatures.center){
            resultFeatures.features.push(emdFeature);
        }
        for(let emdFeature of emdFeatures.polygon){
            resultFeatures.features.push(emdFeature);
        }

        for(let sggFeature of sggFeatures.center){
            resultFeatures.features.push(sggFeature);
            //속도계 차트 넣기
            let centerPos = sggFeature.geometry.coordinates;
            let totVal = sggFeature.properties['value'];

            //speedometer container 생성
            let speedometerDiv = window.document.createElement('div');
            new Speedometer({
                target : speedometerDiv,
                props:{
                    maxValue:90000,
                    value:totVal,
                    segments:10,
                    needleColor:"red",
                    textColor:"#FFFFFF",
                    needleHeightRatio:0.6,
                    startColor:"green",
                    endColor:"red",
                    width:110,
                    height:110,
                    maxSegmentLabels:0,
                    ringWidth:"10"
                }
            });

            let popup = new mapboxgl.Popup({closeOnClick : false, closeButton : false, anchor : 'top'})
                .setLngLat(centerPos)
                .setDOMContent(speedometerDiv);

            speedGaugePopups.push(popup);
        }

        for(let sggFeature of sggFeatures.polygon){
            resultFeatures.features.push(sggFeature);
        }

        console.log("FINAL RESULT FEATURE ::: ", resultFeatures);

        map.addSource('base-source', {
            type : 'geojson',
            data : resultFeatures
        });

        //택배 차량 노선 그리기
        map.addLayer({
            'id' : 'carrier-route-layer',
            'type' : 'line',
            'source' : 'base-source',
            'paint' : {
                'line-color' : ['get', 'color'],
                'line-opacity' : 0.8,
                'line-width' : 3
            },
            'filter' : ['==', 'content', 'carrier-route']
        });

        //읍면동 label명 표시하기
        map.addLayer({
            'id' : 'emd-center-label-layer',
            'type' : 'symbol',
            'source' : 'base-source',
            'layout' : {
                'text-field' : ['get', 'description'],
                "text-font": ["DIN Offc Pro Medium", "Arial Unicode MS Bold"],
                "text-size": 10
            },
            'paint' : {
                'text-color' : '#FFFFFF',
            },
            'filter' : ['==', 'content', 'emd-center']
        });

        //읍면동 내부 색상 표시하기
        map.addLayer({
            'id' : 'emd-polygon-layer',
            'type' : 'fill',
            'source' : 'base-source',
            'paint' : {
                'fill-color' : ['get', 'color'],
                'fill-opacity' : 0.1
            },
            'filter' : ['==', 'content', 'emd-polygon']
        });

        //시군구 label명 표시하기
        map.addLayer({
            'id' : 'sgg-center-label-layer',
            'type' : 'symbol',
            'source' : 'base-source',
            'layout' : {
                'text-field' : ['get', 'description'],
                "text-font": ["DIN Offc Pro Medium", "Arial Unicode MS Bold"],
                "text-size": 16
            },
            'paint' : {
                'text-color' : '#FFFFFF',
            },
            'filter' : ['==', 'content', 'sgg-center']
        });

        //시군구 윤곽선 그리리
        map.addLayer({
            'id' : 'sgg-outline-layer',
            'type' : 'line',
            'source' : 'base-source',
            'paint' : {
                'line-color' : '#99FFFF',
                'line-width' : 2
            },
            'filter' : ['==', 'content', 'sgg-polygon']
        });

        map.on('zoomend', (e) => {
            let currentZoom = map.getZoom();

            if(currentZoom < 12){
                for(let speedGaugePopup of speedGaugePopups){
                    speedGaugePopup.remove();
                }
            }else{
                for(let speedGaugePopup of speedGaugePopups){
                    speedGaugePopup.addTo(map);
                }
            }
        });
    });

    onDestroy(async () => {
        // socket.close();
    });

</script>

<div style="display: flex">
    <div id="map-container"></div>
</div>

<div class="mapboxgl-popup-content"></div>
<div class="mapboxgl-popup-tip"></div>
<style>
    #map-container{
        width: 90%;
        height: 800px;
    }
</style>