import mapboxgl from "mapbox-gl";
let webSocketServerUrl = import.meta.env.VITE_WEBSOCKET_URL;
let accessToken = "pk.eyJ1Ijoib25jbG91ZCIsImEiOiJjbGg4djQ4Ym8wMWU2M2RwMG9vMXd1MXcyIn0.PHgNVjOnQYqXH30DnqquRw";
mapboxgl.accessToken = accessToken;

export function getColorByValue(value){
    if(1600 <= value){
        return '#FF0000'
    }else if(1200 <= value){
        return '#FF6600'
    }else if(800 <= value){
        return '#FF9900'
    }else if(400 <= value){
        return '#FFCC00'
    }else{
        return '#FFFF00'
    }
}

export function getColorRandom(){
    let hex = "#";
    for (let c = 0; c < 6; c++) {
        hex += Math.round(Math.random() * 0xf).toString(16);
    }
    return hex;
}

//비동기 sleep구현
export async function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

//POST fetch
export async function postFetch(url, data){
    let res;

    try{
        data = JSON.stringify(data);
    }catch (err){
        data = null;
    }

    res = await fetch(url, {
        method : 'POST',
        headers : {
            'content-type' : 'application/json'
        },
        body : data
    });

    let resJson = await res.json();
    return resJson;
}

//text Fetch(static경우 json폴더 위치의 경우 ./json/으로 시작)
export async function textToJsonFetch(path){
    let res = await fetch(path);
    let resText = await res.text();
    return JSON.parse(resText);
}

//GET fetch
export async function getFetch(url, param){
    let res;

    if(param !== undefined && param !== null){
        url+="?";
        for(let key in param){
            url += (key + "=" + param[key] + "&");
        }
        url = url.substring(0, url.length - 1);
    }

    res = await fetch(url, {
        method : 'GET',
    });

    const resText = await res.text();

    //json형태 처리
    try{
        let resJson = JSON.parse(resText);
        return resJson;
    }
    //json 형태가 아닌 xml 형태일 경우
    catch (err){
        let resXml = new DOMParser().parseFromString(resText, 'text/xml');
        resXml = await vanillaXmlToJson(resXml);

        return resXml;
    }
}


//lat, lng 좌표 map geojson형태에 맞게 조합하기
export async function getGeoCoord(data, type){
    let geoJson = {
        type : 'Feature',
        properties : {
            color : getColorRandom(),
        },
        geometry : {
            coordinates : data,
            type : type
        }
    };

    return geoJson;
}

//맵박스 부르기
export async function getMap(container, style, initLngLat, initZoom){
    return new mapboxgl.Map({
        container: container,
        style: style,
        center: initLngLat,
        zoom: initZoom,
        dragRotate: false,
    });
}

//이미지 추가하기
export async function loadMapImage(map, imgId, width, height, svgFile, option){
    let  img = new Image(width, height);
    img.onload = () => map.addImage(imgId, img, option);
    img.src = svgFile;
}

export async function vanillaXmlToJson(xml) {
    // Create the return object
    var obj = {};

    if (xml.nodeType == 1) {
        // element
        // do attributes
        if (xml.attributes.length > 0) {
            obj["@attributes"] = {};
            for (var j = 0; j < xml.attributes.length; j++) {
                var attribute = xml.attributes.item(j);
                obj["@attributes"][attribute.nodeName] = attribute.nodeValue;
            }
        }
    } else if (xml.nodeType == 3) {
        // text
        obj = xml.nodeValue;
    }

    // do children
    // If all text nodes inside, get concatenated text from them.
    var textNodes = [].slice.call(xml.childNodes).filter(function(node) {
        return node.nodeType === 3;
    });
    if (xml.hasChildNodes() && xml.childNodes.length === textNodes.length) {
        obj = [].slice.call(xml.childNodes).reduce(function(text, node) {
            return text + node.nodeValue;
        }, "");
    } else if (xml.hasChildNodes()) {
        for (var i = 0; i < xml.childNodes.length; i++) {
            var item = xml.childNodes.item(i);
            var nodeName = item.nodeName;
            if (typeof obj[nodeName] == "undefined") {
                obj[nodeName] = await vanillaXmlToJson(item);
            } else {
                if (typeof obj[nodeName].push == "undefined") {
                    var old = obj[nodeName];
                    obj[nodeName] = [];
                    obj[nodeName].push(old);
                }
                obj[nodeName].push(await vanillaXmlToJson(item));
            }
        }
    }
    return obj;
}

//소켓 메서드
//websocket 서버 호출하기
export async function connectSocketServer(socket, contextPath, data, callback){
    let sendData = {
        seoulBusRouteIds : data.data
    };

    let resJson = await postFetch(webSocketServerUrl + contextPath, sendData);

    console.log("START WEBSOCKET SERVER", resJson);

    if(resJson.success){
        socket = new WebSocket(resJson.url);

        socket.onopen = (event) => {
            console.log('Connected to WebSocket server');
        };

        socket.onmessage = async (event) => {
            let receivedData = JSON.parse(event.data);
            console.log("Socket Received Data", receivedData);

            if(receivedData.success === true){
                await callback(receivedData);
            }
        };

        socket.onclose = (event) => {
            console.log('Disconnected from WebSocket server');
        };
    }else{
        console.log(resJson.message);
    }
}