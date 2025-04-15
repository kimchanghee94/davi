import {getColorByValue, getGeoCoord, postFetch, textToJsonFetch} from "../../util/CommonUtil.js";

let backendURL = import.meta.env.VITE_DAVI_URL;

export let initLngLat = [126.981752, 37.525950];

export async function getRouteFeatrue(path){
    let resData = await textToJsonFetch(path);
    let coordinates = resData.features[0].geometry.coordinates;
    let feature = await getGeoCoord(coordinates, 'LineString');
    feature.properties['content'] =  'carrier-route';
    return feature;
}

export async function getEMDData(sendData){
    let resData = await postFetch(backendURL + '/molit/eupmyeondong/list', sendData);
    let resultFeatures = {};

    //중심좌표 및 구역 표시
    let centerFeatures = [];
    let polygonFeatures = [];

    for(let tmpResData of resData.data){
        //중심좌표 구하기
        let centerCoord = new Array(tmpResData.centerLng, tmpResData.centerLat);
        let centerFeature = await getGeoCoord(centerCoord, 'Point');

        centerFeature.properties['description'] = tmpResData.emdKorNm;
        centerFeature.properties['content'] = 'emd-center';
        centerFeatures.push(centerFeature);

        //다격형 좌표 구하기
        let jsonData = JSON.parse(tmpResData.jsonData);
        let polygonCoord = jsonData.featureCollection.features[0].geometry.coordinates[0];
        let polygonFeature = await getGeoCoord(polygonCoord, 'Polygon');

        let ranVal = Math.floor(Math.random() * 1501);
        polygonFeature.properties['fullNm'] = tmpResData.fullNm;
        polygonFeature.properties['value'] = ranVal;
        polygonFeature.properties['color'] = getColorByValue(ranVal);
        polygonFeature.properties['content'] = 'emd-polygon';
        polygonFeatures.push(polygonFeature);
    }

    resultFeatures['center'] = centerFeatures;
    resultFeatures['polygon'] = polygonFeatures;

    return resultFeatures;
}

export async function getSGGData(sendData, eupmyeondongFeature){
    let resData = await postFetch(backendURL + '/molit/sigungu/list', sendData);
    let resultFeatures = {};

    //중심좌표 및 구역 표시
    let centerFeatures = [];
    let polygonFeatures = [];

    for(let tmpResData of resData.data){
        //읍면동 구역들의 랜덤 값들을 더해주도록 한다.
        let totalVal = 0;

        for(let emdFeature of eupmyeondongFeature.polygon){
            if(emdFeature.properties.fullNm.includes(tmpResData.sigKorNm)){
                totalVal += emdFeature.properties.value;
            }
        }

        //중심좌표 구하기
        let centerCoord = new Array(tmpResData.centerLng, tmpResData.centerLat);
        let feature = await getGeoCoord(centerCoord, 'Point');

        feature.properties['description'] = tmpResData.sigKorNm;
        feature.properties['value'] = totalVal;
        feature.properties['content'] = 'sgg-center';
        centerFeatures.push(feature);


        //다격형 좌표 구하기
        let jsonData = JSON.parse(tmpResData.jsonData);
        let polygonCoord = jsonData.featureCollection.features[0].geometry.coordinates[0];
        let polygonFeature = await getGeoCoord(polygonCoord, 'Polygon');

        polygonFeature.properties['content'] = 'sgg-polygon';
        polygonFeatures.push(polygonFeature);
    }

    resultFeatures["center"] = centerFeatures;
    resultFeatures["polygon"] = polygonFeatures;

    return resultFeatures;
}