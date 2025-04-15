<script>
    import mapboxgl from 'mapbox-gl';
    import { onMount } from "svelte";

    export let sqlJsResult = null;
    let backendURL = import.meta.env.VITE_DAVI_URL;
    let accessToken = "pk.eyJ1Ijoib25jbG91ZCIsImEiOiJjbGg4djQ4Ym8wMWU2M2RwMG9vMXd1MXcyIn0.PHgNVjOnQYqXH30DnqquRw";
    let map;

    //해양거리 sample list가져와보기
    async function getKhoaSeaDistData(){
        let res = await fetch(backendURL + '/khoa/seadist/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            }
        });

        let resJson = await res.json();
        console.log('해양조사 데이터', resJson);

        return resJson;
    }

    //상세주소를 좌표 코드로 변환
    async function geocodeAddress(address){
        let apiKey = 'f82863d5092efa581b2dd00b1c24a27a';
        let url = `https://dapi.kakao.com/v2/local/search/address.json?query=${encodeURIComponent(address)}`;
        let lnglat = [];

        let res = await fetch(url, {
            method: 'GET',
            headers: {
                Authorization: `KakaoAK ${apiKey}`,
            }
        });

        let resJson = await res.json();

        lnglat.push(resJson.documents[0].x);
        lnglat.push(resJson.documents[0].y);

        return lnglat;
    }

    //배열 내부에서 상세주소 위치 찾기
     function detailAddressHeaderCheck(){
        let index = -1;

        for(let i=0; i<sqlJsResult.header.length; i++){
            if(sqlJsResult.header[i].includes('dtl_adres')){
                index = i;
                break;
            }
        }

        return index;
    }

    //배열 내부에서 json데이터 위치 찾기
    function findJsonData(){
        let index = -1;

        for(let i=0; i<sqlJsResult.header.length; i++){
            if(sqlJsResult.header[i].includes("json_data")){
                index = i;
                break;
            }
        }

        return index;
    }

    function getColorRandom(){
        let color = [];

        for(let i=0; i<sqlJsResult.body.length; i++){
            let hex = "#";
            for (let c = 0; c < 6; c++) {
                hex += Math.round(Math.random() * 0xf).toString(16);
            }
            color.push(hex);
        }

        return color;
    }

    onMount(async () => {
        //json위치 찾아서 담기
        let geoJsonIdx = findJsonData();
        let geoJson = JSON.parse(sqlJsResult.body[0][geoJsonIdx]);
        let coordinates = geoJson.featureCollection.features[0].geometry.coordinates;

        //사용자가 선택한 데이터 좌표들중에서 첫번째 좌표위치로 초기 화면 표시되도록 한다.
        let lnglat = coordinates[0][0][0];

        let color = getColorRandom();

        //상세주소 위치 찾기
        let dtlIdx = detailAddressHeaderCheck();

        //해상거리 데이터 불러오기
        let seaDistJson = await getKhoaSeaDistData();

        //MapBox GL 초기설정
        mapboxgl.accessToken = accessToken;
        map = new mapboxgl.Map({
            container: 'map-container',
            style: 'mapbox://styles/mapbox/dark-v11',
            center: lnglat,
            zoom: 12,
            dragRotate: false
        });

        map.on('load', async () => {
            for(let i=0; i < sqlJsResult.body.length; i++){
                if(sqlJsResult.body[i][geoJsonIdx] === null){
                    continue;
                }

                geoJson = JSON.parse(sqlJsResult.body[i][geoJsonIdx]);
                coordinates = geoJson.featureCollection.features[0].geometry.coordinates;

                let fillColor = color[i];
                let pointColor = color[i];

                for(let j=0; j<i; j++) {
                    if (sqlJsResult.body[i][geoJsonIdx] === sqlJsResult.body[j][geoJsonIdx]) {
                        fillColor = 'transparent';
                        pointColor = color[j];
                        break;
                    }
                }

                map.addSource('molit' + i, {
                    'type' : 'geojson',
                    'data' : {
                        'type': 'Feature',
                        'geometry': {
                            'type' : 'MultiPolygon',
                            'coordinates' : coordinates,
                        }
                    }
                });

                //내부 위치
                map.addLayer({
                    'id': 'molit' + i,
                    'type': 'fill',
                    'source': 'molit' + i, // reference the data source
                    'layout': {},
                    'paint': {
                        'fill-color': fillColor, // blue color fill
                        'fill-opacity': 0.2
                    }
                });

                //윤곽선
                map.addLayer({
                    'id': 'outline' + i,
                    'type': 'line',
                    'source': 'molit' + i,
                    'layout': {},
                    'paint': {
                        'line-color': '#000',
                        'line-width': 3
                    }
                });

                //상세주소 점찍어 표시
                if(dtlIdx >= 0){
                    // Kakao api -> geocoder라이브러리 사용하여 상세주소 좌표로 변환 테스트
                    let dtlAddrToLngLat = await geocodeAddress(sqlJsResult.body[i][dtlIdx]);

                    let dtlGeoJson = {};
                    dtlGeoJson["type"] = "Feature";
                    dtlGeoJson["properties"] = {};
                    dtlGeoJson["geometry"] = {};
                    dtlGeoJson["geometry"]["coordinates"] = dtlAddrToLngLat;
                    dtlGeoJson["geometry"]["type"] = "Point";

                    map.addSource('point' + i,{
                       type: 'geojson',
                       data: dtlGeoJson,
                    });

                    map.addLayer({
                        'id' : 'point' + i,
                        'type' : 'circle',
                        'source' : 'point' + i,
                        'paint' : {
                            'circle-color': pointColor,
                            'circle-radius': 4,
                        }
                    });
                }

                if(seaDistJson.data !== null && seaDistJson.data.length > i){
                    // 해안로
                    let latArr = seaDistJson.data[i].degreeLatArr.split(",");
                    let lngArr = seaDistJson.data[i].degreeLonArr.split(",");
                    let seaArr = [];

                    for(let si=0; si < latArr.length; si++){
                        let tmpArr = [];
                        tmpArr.push(lngArr[si]);
                        tmpArr.push(latArr[si]);
                        seaArr.push(tmpArr);
                    }

                    map.addSource('seaLine' + i, {
                        'type': 'geojson',
                        'data': {
                            "id": "LT_L_MOCTLINK.271630",
                            "type": "Feature",
                            "geometry": {
                                "type": "LineString",
                                "coordinates": seaArr
                            },
                            "properties": {

                            }
                        },
                    });

                    map.addLayer({
                        'id': 'seaLine' + i,
                        'type': 'line',
                        'source': 'seaLine' + i,
                        'layout': {
                            'line-join': 'round',
                            'line-cap': 'round'
                        },
                        'paint': {
                            'line-color': color[i],
                            'line-width': 5,
                            'line-opacity' : 1,

                        }
                    });
                }
            }

            /*map.addSource('image-a', {
                'type' : 'image',
                'url' : 'http://localhost:5170/image/mapBox-img.gif',
                'coordinates' : [
                    [123.57, 38.07],
                    [128.46, 38.07],
                    [128.46, 36.12],
                    [123.57, 36.12]
                ]
            });

            map.addLayer({
                id: 'image-s',
                'type': 'raster',
                'source': 'image-a',
                'paint': {
                    'raster-fade-duration': 0
                }
            });*/
        });
    });
</script>

<div id="map-container"></div>

<style>
    #map-container {
        width: 80%;
        height: 800px;
    }
</style>
