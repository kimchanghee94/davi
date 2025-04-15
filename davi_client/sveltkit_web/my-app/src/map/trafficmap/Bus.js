import {getFetch, postFetch} from "../../util/CommonUtil.js";
import {getTrafficGeoCoord} from "./TrafficMap.js";

let backendURL = import.meta.env.VITE_DAVI_URL;

//linkId를 가지고 링크 좌표 가져오기
export async function getLinkPos(linkArray){
    let sendDatas = [];

    for(let linkId of linkArray){
        let sendData = {
            propLinkId : linkId
        };
        sendDatas.push(sendData);
    }

    let resJson = await postFetch(backendURL + "/molit/link/one/link-id", sendDatas);

    let totalCoords = [];

    // for(let feature of resJson.data){
    //     let jsonData = JSON.parse(feature.jsonData);
    //     totalCoords.push(jsonData.featureCollection.features[0].geometry.coordinates[0]);
    // }
    //
    // return totalCoords;

    for(let feature of resJson.data){
        let jsonData = JSON.parse(feature.jsonData);

        for(let coordinate of jsonData.featureCollection.features[0].geometry.coordinates[0]){
            totalCoords.push(coordinate);
        }
    }

    return new Array(totalCoords);
}

//DB를 통해서 버스 노선 경로 그리기
export async function getBusRoute2(seoulBusInfos){
    let busDatas = seoulBusInfos.data;
    let totalFeatures = new Array();

    for(let busData of busDatas){
        let sendData = {
            routeId : busData.routeId
        };

        let resJson = await postFetch(backendURL + "/traffic/busroutepath/list", sendData);

        if(resJson.success === false){
            continue;
        }

        let totalCoords = new Array();
        let positions = JSON.parse(resJson.data[0].jsonData);

        for(let position of positions){
            totalCoords.push(new Array(position.lng, position.lat));
        }

        let feature = await getTrafficGeoCoord(new Array(totalCoords), 'line');
        feature.properties["routeId"] = busData.routeId;
        feature.properties["routeNm"] = busData.routeNm;
        feature.properties["content"] = "busRoutePath";

        totalFeatures.push(feature);
    }

    return totalFeatures;
}

//버스 노선 경로 그리기
export async function getBusRoute(seoulBusInfo){
    let busDatas = seoulBusInfo.data;
    let totalFeatures = new Array();

    for(let busData of busDatas){
        let sendData = {
            routeId : busData.routeId
        };

        let resJson = await postFetch(backendURL + "/traffic/busroutelink/list", sendData);

        if(resJson.success === false){
            continue;
        }

        // >>> 링크 단위로 그리기
        let totalCoords = new Array();
        for(let feature of resJson.data){
            let jsonData = JSON.parse(feature.jsonData);

            totalCoords.push(jsonData.featureCollection.features[0].geometry.coordinates[0]);
        }

        let features = await getTrafficGeoCoord(totalCoords, 'line');
        // <<< 링크 단위로 그리기

        // >>> 모든 좌표를 이어서 그리기
        /*
        let totalCoords = new Array();
        for(let feature of resJson.data){
            let jsonData = JSON.parse(feature.jsonData);

            for(let coordinate of jsonData.featureCollection.features[0].geometry.coordinates[0]){
                totalCoords.push(coordinate);
            }
        }

        let features = await getTrafficGeoCoord(new Array(totalCoords), 'line');*/
        // <<< 모든 좌표를 이어서 그리기

        features.properties["routeId"] = busData.routeId;
        features.properties["routeNm"] = busData.routeNm;
        features.properties["content"] = "busRoute";
        totalFeatures.push(features);
    }

    console.log("Bus Route Info", totalFeatures);
    return totalFeatures;
}

//버스 정보 가져오기
export async function getSeoulBusInfo(){
    let sendData = {
        nodeOrder : 1,
    }

    let resJson = await postFetch(backendURL + '/traffic/busseoulinfo/list', sendData);

    if(resJson.success === false) {
        return resJson.message;
    }

    console.log("서울 버스 정보", resJson);

    return resJson;
}

//해당 노선 버스의 실시간 위치
export async function showBusRealTimeDataOnMap(receivedData, busRouteInfos){
    let busDatas = receivedData.data;
    let features = [];

    for(let busData of busDatas){
        if(busData.itemList === null){
            continue;
        }
        let coordinates = [];

        for(let item of busData.itemList){
            let lng = item.gpsX;
            let lat = item.gpsY;

            coordinates.push(new Array(lng, lat));
        }


        //>>>test code
        /*let coordinates = [];

        let lat = 37.614178;
        let lng = 126.910807;
        coordinates.push(new Array(lng, lat));*/
        //<<<

        let result = await getTrafficGeoCoord(coordinates, 'point');
        result.properties["content"] = "busRealTimeDataPos";
        result.properties["routeId"] = busData.routeId;
        result.properties["routeNm"] = busData.routeNm;

        for(let busRouteInfo of busRouteInfos){
            if(busRouteInfo.properties.routeId === busData.routeId){
                result.properties["color"] = busRouteInfo.properties.color;
                break;
            }
        }

        features.push(result);
    }

    return features;
}