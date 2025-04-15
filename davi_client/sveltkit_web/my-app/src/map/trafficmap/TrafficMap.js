import {getColorRandom, getFetch, postFetch} from "../../util/CommonUtil.js";

let backendURL = import.meta.env.VITE_DAVI_URL;

export let initLngLat = [127.7, 36];

//lat, lng 좌표 map geojson형태에 맞게 조합하기
export async function getTrafficGeoCoord(data, type){
    let geoJson = {
        type : 'Feature',
        properties : {
            color : getColorRandom(),
        },
        geometry : {
            coordinates : data,
            type : ''
        }
    };

    if(type === 'point'){
        geoJson.geometry.type = 'MultiPoint';
    }else if(type === 'line'){
        geoJson.geometry.type = 'MultiLineString';
    }

    return geoJson;
}

export async function emdTest(){
    let res = await fetch('./json/emd.geojson');
    let resText = await res.text();
    let resJson = JSON.parse(resText);

    for(let i=0; i<resJson.features.length; i++){
        let feature = resJson.features[i];

        feature.properties["Value"] = Math.floor(Math.random()*5001);
    }
}

async function getTdataItisColor(code){
    if(code === '1281' || code === '1546' || code === '1793' || code === '534' || code === '540'){
        return '#ff0000';
    }else{
        return '#ff7f00';
    }
}

async function getTdataFormat(url){
    let pageNo = 1;
    let features = [];

    while(true){
        let res = await getFetch(url);

        if(res.length === 0){
            return features;
        }

        for(let i=0; i<res.length; i++){
            let coordinates = [];

            if(res[i].detcLot !== undefined){
                coordinates.push(new Array(res[i].detcLot, res[i].detcLat));
            }
            else{
                coordinates.push(new Array(res[i].vhcleLot, res[i].vhcleLat));
            }

            let feature = await getTrafficGeoCoord(coordinates, 'point');
            feature.properties["color"] = await getTdataItisColor(res[i].itisCd);
            feature.properties["content"] = "itis";
            features.push(feature);
        }

        url = url.replaceAll('pageNo='+pageNo,'pageNo='+(++pageNo));
        break;
    }

    return features;
}

//터널내 돌발 정보서비스
export async function getTunnelRisk(){
    let url = 'https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xTunnelRiskInformation/1.0?apiKey=ed4e154c-4bff-49a8-b17d-bbd7139fd979&numOfRows=10';
    return await getTdataFormat(url);

}

//위험 구간 정보 서비스
export async function getRiskArea(){
    let url = 'https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xRiskAreaInformation/1.0?apiKey=ed4e154c-4bff-49a8-b17d-bbd7139fd979&numOfRows=10';
    return await getTdataFormat(url);
}

//횡단 보행자 정보 서비스
export async function getCrosswalkPedis(){
    let url = 'https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xVehiclesStatusInformation/1.0?apiKey=ed4e154c-4bff-49a8-b17d-bbd7139fd979&numOfRows=10';
    return await getTdataFormat(url);
}

//교차로 위험 정보 서비스
export async function getCrossRisk(){
    let url = 'https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xCrossroadRiskInformation/1.0?apiKey=ed4e154c-4bff-49a8-b17d-bbd7139fd979&numOfRows=10';
    return await getTdataFormat(url);
}

//보행자 충돌 주의 서비스
export async function getPedisCollision(){
    let url = 'https://t-data.seoul.go.kr/apig/apiman-gateway/tapi/v2xPedestrianCollisionWarning/1.0?apiKey=ed4e154c-4bff-49a8-b17d-bbd7139fd979&numOfRows=10';
    return await getTdataFormat(url);
}

//실시간 교통 소통 정보 받아오기
export async function getRealTimeTrafficInfo(){
    let res = await getFetch('https://openapi.its.go.kr:9443/trafficInfo?apiKey=test&type=all');
    if(res.response.header.resultCode != 0){
        return res.response.header.resultMsg;
    }

    let resDatas = res.response.body.items.item;
    let linkDtos = [];

    for(let i=0; i<resDatas.length; i++){
        let linkDto = {
            propLinkId : resDatas[i].linkId,
            paramSpeed : resDatas[i].speed,
        };
        linkDtos.push(linkDto);
    }

    res = await postFetch(backendURL + "/molit/link/one/link-id", linkDtos);

    resDatas = res.data;

    let result = [];

    for(let i=0; i<resDatas.length; i++){
        let geoJson = JSON.parse(resDatas[i].jsonData);
        let coordinates = geoJson.featureCollection.features[0].geometry.coordinates;

        for(let j=0; j<coordinates[0].length; j++){
            for(let k=0; k<coordinates[0][j].length; k++){
                coordinates[0][j][k] = String(Math.floor(coordinates[0][j][k] * 1000000) / 1000000);
            }
        }

        let feature = await getTrafficGeoCoord(coordinates, 'line');
        feature.properties["trafficCongest"] = resDatas[i].paramTrfCgt;
        feature.properties["content"] = "readTimeTrafficInfo"

        if(resDatas[i].paramTrfCgt === '3'){
            feature.properties["color"] = '#00ff00';
        }else if(resDatas[i].paramTrfCgt === '2'){
            feature.properties["color"] = '#ffff00';
        }else if(resDatas[i].paramTrfCgt === '1'){
            feature.properties["color"] = '#ff7f00';
        }else{
            feature.properties["color"] = '#ff0000';
        }
        result.push(feature);
    }

    console.log("실시간 교통 정보", result);

    return result;
}


//실시간 공사 및 사고구간 좌표 받아오기
export async function getConAccInfo(){
    let res = await getFetch('https://openapi.its.go.kr:9443/eventInfo?apiKey=eb0867cb4b804d6882d43b04b9883d4f&type=all&eventType=all');

    if(res.response.header.resultCode !== '0'){
        return res.response.header.resultMsg;
    }

    let resDatas = res.response.body.items.item;
    let accCoordinates = [];
    let conCoordinates = [];
    let events = [];

    for(let i=0; i<resDatas.length; i++){
        let resData = resDatas[i];
        let lng = resData.coordX;
        let lat = resData.coordY;

        if(resData.eventType === '교통사고'){
            accCoordinates.push(new Array(lng, lat));
        }else if(resData.eventType === '공사'){
            conCoordinates.push(new Array(lng, lat));
        }
    }

    let result = {};
    let accFeature = await getTrafficGeoCoord(accCoordinates, 'point');
    accFeature.properties["content"] = "accident";
    let conFeature = await getTrafficGeoCoord(conCoordinates, 'point');
    conFeature.properties["content"] = "construct";

    result["accFeature"] = accFeature;
    result["conFeature"] = conFeature;
    console.log("공사 및 사고구간 : ", result);

    return result;
}


//안개 데이터 가져오기
export async function getFogData(){
    let resJson = await postFetch(backendURL + '/traffic/fog/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i<resJson.data.length; i++){
        let strLng = resJson.data[i].beginMapXCrdnt;
        let strLat = resJson.data[i].beginMapYCrdnt;
        let endLng = resJson.data[i].endMapXCrdnt;
        let endLat = resJson.data[i].endMapYCrdnt;

        let tmpCoord = [];
        tmpCoord.push(new Array(strLng, strLat));
        tmpCoord.push(new Array(endLng, endLat));
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'line');
    result.properties["content"] = "fog";
    console.log('안개 데이터', result);

    return result;
}

//결빙 데이터 가져오기
export async function getFrostData(){
    let resJson = await postFetch(backendURL + '/traffic/frost/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i<resJson.data.length; i++){
        let strLng = resJson.data[i].strLng;
        let strLat = resJson.data[i].strLat;
        let endLng = resJson.data[i].endLng;
        let endLat = resJson.data[i].endLat;

        let tmpCoord = [];
        tmpCoord.push(new Array(strLng, strLat));
        tmpCoord.push(new Array(endLng, endLat));
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'line');
    result.properties["content"] = "frost";
    console.log('결빙 데이터', result);

    return result;
}

//RSE 정보 가져오기
export async function getRseData(){
    let resJson = await postFetch(backendURL + '/traffic/rse/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i < resJson.data.length; i++) {
        let lng = resJson.data[i].longitude;
        let lat = resJson.data[i].latitude;

        let tmpCoord = [];
        tmpCoord.push(lng, lat);
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'point');
    result.properties["content"] = "rse";
    console.log('Rse 데이터', result);

    return result;
}

//과속 방지턱 가져오기
export async function getSpeedDumpData(){
    let resJson = await postFetch(backendURL + '/traffic/speeddump/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i < resJson.data.length; i++) {
        let lng = resJson.data[i].longitude;
        let lat = resJson.data[i].latitude;

        let tmpCoord = [];
        tmpCoord.push(lng, lat);
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'point');
    result.properties["content"] = "speedDump";
    console.log('Speed Dump 데이터', result);

    return result;
}

//cctv 데이터 가져오기
export async function getCctvData(){
    let resJson = await postFetch(backendURL + '/traffic/cctv/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i<resJson.data.length; i++){
        let lng = resJson.data[i].longitude;
        let lat = resJson.data[i].latitude;

        let tmpCoord = [];
        tmpCoord.push(lng, lat);
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'point');
    result.properties["content"] = "cctv";
    console.log('CCTV 데이터', result);

    return result;
}
//신호등 데이터 가져오기
export async function getTrafficLightData(){
    let resJson = await postFetch(backendURL + '/traffic/trafficlight/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i<resJson.data.length; i++){
        let lng = resJson.data[i].longitude;
        let lat = resJson.data[i].latitude;

        let tmpCoord = [];
        tmpCoord.push(lng, lat);
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'point');
    result.properties["content"] = "trafficLight";
    console.log('신호등 데이터', result);

    return result;
}

//어린이보호구역 데이터 가져오기
export async function getChildZoneData(){
    let resJson = await postFetch(backendURL + '/traffic/childzone/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i<resJson.data.length; i++){
        let lng = resJson.data[i].longitude;
        let lat = resJson.data[i].latitude;

        let tmpCoord = [];
        tmpCoord.push(lng, lat);
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'point');
    result.properties["content"] = "childZone";
    console.log('어린이 보호구역 데이터', result);

    return result;
}

//횡단보도 데이터 가져오기
export async function getCrosswalkData(){
    let resJson = await postFetch(backendURL + '/traffic/crosswalk/sample-list', null);

    if(resJson.success === false){
        return resJson.message;
    }

    let coordinates = [];

    for(let i=0; i<resJson.data.length; i++){
        let lng = resJson.data[i].longitude;
        let lat = resJson.data[i].latitude;

        let tmpCoord = [];
        tmpCoord.push(lng, lat);
        coordinates.push(tmpCoord);
    }

    let result = await getTrafficGeoCoord(coordinates, 'point');
    result.properties["content"] = "crosswalk";
    console.log('횡단보도 데이터', result);

    return result;
}