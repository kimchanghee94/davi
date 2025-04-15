import mapboxgl from "mapbox-gl";
import shipSvg from "../routes/svg/sailboat-solid.svg";

let backendURL = import.meta.env.VITE_DAVI_URL;
let accessToken = "pk.eyJ1Ijoib25jbG91ZCIsImEiOiJjbGg4djQ4Ym8wMWU2M2RwMG9vMXd1MXcyIn0.PHgNVjOnQYqXH30DnqquRw";
//MapBox GL 초기설정
mapboxgl.accessToken = accessToken;
export let initLngLat = [127.7, 36];

//선박 이미지 추가하기
export async function getShipImage(map){
    let img = new Image(20, 20);
    img.onload = () => map.addImage('ship-image', img, {sdf : true});
    img.src = shipSvg;
}

//해양거리 sample list가져와보기
export async function getKhoaSeaDistData(data){
    let res = null;

    if(data !== undefined){
        res = await fetch(backendURL + '/khoa/seadist/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            },
            body : JSON.stringify(data),
        });
    }else{
        res = await fetch(backendURL + '/khoa/seadist/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            }
        });
    }

    let resJson = await res.json();
    console.log('해양거리 데이터', resJson);

    return resJson;
}

//항구 데이터 가져와보기
export async function getKhoaHangData(data){
    let res = null;
    if(data !== undefined){
        res = await fetch(backendURL + '/khoa/hang/sample-list', {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(data),
        });
    }else{
        res = await fetch(backendURL + '/khoa/hang/sample-list', {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            }
        });
    }

    let resJson = await res.json();
    console.log('항구 데이터', resJson);

    return resJson;
}

//등대표 데이터 가져와보기
export async function getKhoaBuoyData(data){
    let res = null;
    if(data !== undefined){
        res = await fetch(backendURL + '/khoa/buoy/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            },
            body : JSON.stringify(data),
        });
    }else{
        res = await fetch(backendURL + '/khoa/buoy/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            }
        });
    }

    let resJson = await res.json();
    console.log('등대표 데이터', resJson);

    return resJson;
}

//항만모형 데이터 가져와보기
export async function getKhoaLsmdContData(data){
    let res = null;

    if(data !== undefined){
        res = await fetch(backendURL + '/khoa/lsmdcont/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            },
            body : JSON.stringify(data)
        });
    }else{
        res = await fetch(backendURL + '/khoa/lsmdcont/sample-list', {
            method : 'POST',
            headers : {
                'content-type' : 'application/json'
            }
        });
    }

    let resJson = await res.json();
    console.log('항만 모형 데이터', resJson);

    return resJson;
}

export async function inputBasicSeaData(map){
    let seaDistJson = await getKhoaSeaDistData();
    let hangJson = await getKhoaHangData();
    let byouJson = await getKhoaBuoyData();
    let lsmdContJson = await getKhoaLsmdContData();

    // >>> 해안로 출력하기
    let coordinates = []
    for(let i=0; i<seaDistJson.data.length; i++){
        let latArr = seaDistJson.data[i].degreeLatArr.split(",");
        let lngArr = seaDistJson.data[i].degreeLonArr.split(",");
        let seaArr = [];

        for(let si=0; si < latArr.length; si++){
            let tmpArr = [];
            tmpArr.push(lngArr[si]);
            tmpArr.push(latArr[si]);
            seaArr.push(tmpArr);
        }
        coordinates.push(seaArr);
    }

    map.addSource('seaDist', {
        'type': 'geojson',
        'data': {
            "id": "LT_L_MOCTLINK.271630",
            "type": "Feature",
            "geometry": {
                "type": "MultiLineString",
                "coordinates": coordinates
            },
            "properties": {

            }
        },
    });

    map.addLayer({
        'id': 'seaDist',
        'type': 'line',
        'source': 'seaDist',
        'layout': {
            'line-join': 'round',
            'line-cap': 'round'
        },
        'paint': {
            'line-color': '#ffffff',
            'line-width': 1,
            'line-opacity' : 0.3,
            'line-dasharray' : [2 ,5]

        }
    });
    // <<< 해안로 출력하기

    // >>> 해안로 각 꺾이는 위치 찍기
    coordinates = [];
    for(let i=0; i<seaDistJson.data.length; i++){
        let latArr = seaDistJson.data[i].degreeLatArr.split(",");
        let lngArr = seaDistJson.data[i].degreeLonArr.split(",");

        for(let j=0; j<latArr.length; j++){
            let tmpCoord = [];
            tmpCoord.push(lngArr[j], latArr[j]);
            coordinates.push(tmpCoord);
        }
    }

    let geoJson = {};
    geoJson["type"] = "Feature";
    geoJson["properties"] = {};
    geoJson["geometry"] = {};
    geoJson["geometry"]["coordinates"] = coordinates;
    geoJson["geometry"]["type"] = "MultiPoint";

    map.addSource('seaDistPoint',{
        type: 'geojson',
        data: geoJson,
    });

    // map.addLayer({
    //     'id' : 'seaDistPoint',
    //     'type' : 'circle',
    //     'source' : 'seaDistPoint',
    //     'paint' : {
    //         'circle-color': '#ffff00',
    //         'circle-radius': 3,
    //         'circle-opacity' : 1
    //     }
    // });
    // <<< 해안로 각 꺾이는 위치 찍기

    // >>> 항구 출력하기
    coordinates = [];
    for(let i=0; i<hangJson.data.length; i++){
        let jsonXY = JSON.parse(hangJson.data[i].lngLat);
        let tmpCoord = [];
        tmpCoord.push(jsonXY.x, jsonXY.y);
        coordinates.push(tmpCoord);
    }

    geoJson = {};
    geoJson["type"] = "Feature";
    geoJson["properties"] = {};
    geoJson["geometry"] = {};
    geoJson["geometry"]["coordinates"] = coordinates;
    geoJson["geometry"]["type"] = "MultiPoint";

    map.addSource('hang',{
        type: 'geojson',
        data: geoJson,
    });

    // map.addLayer({
    //     'id' : 'hang',
    //     'type' : 'circle',
    //     'source' : 'hang',
    //     'paint' : {
    //         'circle-color': '#00ffff',
    //         'circle-radius': 2,
    //         'circle-opacity' : 0.6
    //     }
    // });
    // <<< 항구 출력하기

    // >>> 등대표 출력하기
    coordinates = [];
    for(let i=0; i<byouJson.data.length; i++){
        let jsonXY = JSON.parse(byouJson.data[i].lngLat);
        let tmpCoord = [];
        tmpCoord.push(jsonXY.x, jsonXY.y);
        coordinates.push(tmpCoord);
    }

    geoJson = {};
    geoJson["type"] = "Feature";
    geoJson["properties"] = {};
    geoJson["geometry"] = {};
    geoJson["geometry"]["coordinates"] = coordinates;
    geoJson["geometry"]["type"] = "MultiPoint";

    map.addSource('buoy',{
        type: 'geojson',
        data: geoJson,
    });

    // map.addLayer({
    //     'id' : 'buoy',
    //     'type' : 'circle',
    //     'source' : 'buoy',
    //     'paint' : {
    //         'circle-color': '#00ff00',
    //         'circle-radius': 2,
    //         'circle-opacity' : 0.6
    //     }
    // });
    // <<< 등대표 출력하기

    // >>> 항만모형 출력하기
    coordinates = [];
    for(let i=0; i<lsmdContJson.data.length; i++){
        let jsonData = JSON.parse(lsmdContJson.data[i].shape);

        for(let j=0; j<jsonData.coordinates.length; j++){
            coordinates.push(jsonData.coordinates[j]);
        }
    }

    geoJson = {};
    geoJson["type"] = "Feature";
    geoJson["properties"] = {};
    geoJson["geometry"] = {};
    geoJson["geometry"]["coordinates"] = coordinates;
    geoJson["geometry"]["type"] = "MultiPolygon";
    // <<< 항만모형 출력하기

    map.addSource('lsmdcont', {
        'type' : 'geojson',
        'data' : geoJson
    });

    //내부 위치
    // map.addLayer({
    //     'id': 'lsmdcont',
    //     'type': 'fill',
    //     'source': 'lsmdcont',
    //     'layout': {},
    //     'paint': {
    //         'fill-color': '#ff0000',
    //         'fill-opacity': 0.3
    //     }
    // });

    //윤곽선
    // map.addLayer({
    //     'id': 'outline',
    //     'type': 'line',
    //     'source': 'lsmdcont',
    //     'layout': {},
    //     'paint': {
    //         'line-color': '#000',
    //         'line-width': 0
    //     }
    // });
}