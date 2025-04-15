<script>
    import mapboxgl from 'mapbox-gl';
    import { onDestroy, onMount } from "svelte";
    import proj4 from 'proj4';

    export let sqlJsResult = null;

    let backendURL = import.meta.env.VITE_DAVI_URL;
    let pointId = 1;

    let map;
    let lnglat = [126.9011956, 37.5364341];
    let geoJsonLinkTest = null;

    // const setHeaders = (url) => {
    //     const headers = new Headers();
    //     headers.append('Access-Control-Allow-Origin', '*');
    //     headers.append('Access-Control-Allow-Methods', 'GET, POST, DELETE, PATCH, OPTIONS');
    //     headers.append('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    //
    //     return new Request(url, {
    //         headers : headers
    //     });
    // };

    onMount(() => {
        mapboxgl.accessToken = 'pk.eyJ1Ijoib25jbG91ZCIsImEiOiJjbGg4djQ4Ym8wMWU2M2RwMG9vMXd1MXcyIn0.PHgNVjOnQYqXH30DnqquRw';

        map = new mapboxgl.Map({
            container: 'map-container',
            style: {
                "version": 8,
                "sources": {
                    "raster-tiles": {
                        "type": "raster",
                        "tiles": [
                            // "https://cdn.lima-labs.com/{z}/{x}/{y}.png?api=demo"
                            "http://tile.openstreetmap.org/{z}/{x}/{y}.png"
                            // "http://map0.daumcdn.net/map_2d/2303ksn/L{z}/{x}/{y}.png",
                            // "http://map1.daumcdn.net/map_2d/2303ksn/L{z}/{x}/{y}.png",
                            // "http://map2.daumcdn.net/map_2d/2303ksn/L{z}/{x}/{y}.png",
                            // "http://map3.daumcdn.net/map_2d/2303ksn/L{z}/{x}/{y}.png"
                        ],
                        "tileSize": 300,
                        "attribution": "Map tiles by png"
                    },
                    "composite": {
                        "type": "vector",
                        "url": "mapbox://mapbox.mapbox-streets-v8,mapbox.mapbox-terrain-v2"
                    }
                },
                "layers": [
                    {
                        "id": "png-tiles",
                        "type": "raster",
                        "source": "raster-tiles",
                        "minzoom": 0,
                        "maxzoom": 22
                    }
                ],
                "glyphs": "https://api.mapbox.com/fonts/v1/mapbox/{fontstack}/{range}.pbf?access_token=pk.eyJ1IjoibGVla2lqb29uIiwiYSI6ImNrN3VqNXl5YjB6ZWMzb21yeWR5djE4c2IifQ.__40FDI-3-qbAE_aMpawqA"
            },
            // style: 'mapbox://styles/mapbox/dark-v11',
            center: lnglat,
            zoom: 16,
            pitch: 60
        });

        //맵데이터가 켜지기 전까지 발생하는 이벤트
        // map.on('data', () => {
        //     console.log('A data event occurred.');
        // });

        // 마커 표시 기능
        // new mapboxgl.Marker().setLngLat(lnglat).addTo(map);

        // >>> cluster
        // cluster 테스트 임시데이터
        let fakerJam = {
            "type": "FeatureCollection",
            "features": [
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.90116496608567,
                            37.53641164762067
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.90121366862576,
                            37.53617636510636
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.90136854315818,
                            37.53632912944832
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.90196159930014,
                            37.5355772862365
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.90136476573139,
                            37.53512797392577
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.90029197627831,
                            37.534912303054796
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89914363827137,
                            37.53543350659159
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89775716773596,
                            37.53476866448925
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89837288844319,
                            37.53413961967516
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89717544387656,
                            37.53482857324225
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89586044317633,
                            37.5355071397219
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89658570928538,
                            37.53647165524663
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89683879693928,
                            37.53686704309838
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.8979115815896,
                            37.5372936193575
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89805512384169,
                            37.5377429186198
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89824973589958,
                            37.536909349276755
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89798153853633,
                            37.53651096628029
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.89923434459041,
                            37.53741386703051
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.91218491013478,
                            37.54506709684729
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.91170895425284,
                            37.54513298737146
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.91356463863053,
                            37.54416721624089
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.91445233234259,
                            37.543770849720275
                        ],
                        "type": "Point"
                    }
                },
                {
                    "type": "Feature",
                    "properties": {},
                    "geometry": {
                        "coordinates": [
                            126.91374418145921,
                            37.545140345379224
                        ],
                        "type": "Point"
                    }
                }
            ]
        };
        // <<< cluster

        map.on('load', () => {
            // >>> deck.gl
            const firstLabelLayerId = map.getStyle().layers.find(layer => layer.type === 'raster').id;
            // const firstLabelLayerId = map.getStyle().layers.find(layer => layer.type === 'symbol').id;
            const {MapboxLayer, ScatterplotLayer, ArcLayer} = deck;

           /*map.addLayer({
                'id': 'add-3d-buildings',
                'source': 'composite',
                'source-layer': 'building',
                'filter': ['==', 'extrude', 'true'],
                'type': 'fill-extrusion',
                'minzoom': 15,
                'paint': {
                    'fill-extrusion-color': '#aaa',

                    // use an 'interpolate' expression to add a smooth transition effect to the
                    // buildings as the user zooms in
                    'fill-extrusion-height': [
                        "interpolate", ["linear"], ["zoom"],
                        15, 0,
                        15.05, ["get", "height"]
                    ],
                    'fill-extrusion-base': [
                        "interpolate", ["linear"], ["zoom"],
                        15, 0,
                        15.05, ["get", "min_height"]
                    ],
                    'fill-extrusion-opacity':0.6
                }
            });

            map.addLayer(new MapboxLayer({
                id: 'deckgl-circle',
                type: ScatterplotLayer,
                data: [
                    {position: lnglat, color: [255, 0, 0], radius: 300}
                ],
                getPosition: d => d.position,
                getFillColor: d => d.color,
                getRadius: d => d.radius,
                opacity: 0.1
            }));

            map.addLayer(new MapboxLayer({
                id: 'deckgl-arc',
                type: ArcLayer,
                data: [
                    {source: lnglat, target: [lnglat[0]+0.001, lnglat[1]+0.001]}
                ],
                getSourcePosition: d => d.source,
                getTargetPosition: d => d.target,
                getSourceColor: [255, 208, 0],
                getTargetColor: [0, 128, 255],
                getWidth: 8
            }));

            map.addLayer(new MapboxLayer({
                id: 'bart-stations',
                type: ScatterplotLayer,
                data: [
                    {name: 'Colma', passengers: 4214, coordinates:  [lnglat[0]-0.001, lnglat[1]-0.001]},
                    {name: 'Civic Center', passengers: 24798, coordinates:  [lnglat[0]-0.004, lnglat[1]-0.004]},
                ],
                stroked: false,
                filled: true,
                getPosition: d => d.coordinates,
                getRadius: d => Math.sqrt(d.passengers),
                getFillColor: [255, 200, 0]
            }));*/
            // <<< deck.gl

            // >>> circular polygon
           /* let center = turf.point(lnglat);
            let radius = 0.1;
            let options = {
                steps: 100,`
                units: 'kilometers'
            }

            let circle = turf.circle(center, radius, options);
            map.addSource('kennycompany-circle', {
                type: 'geojson',
                data: circle
            });

            map.addLayer({
                id: 'circle-fill',
                type: 'fill',
                source: 'kennycompany-circle',
                paint:{
                    'fill-color': 'pink',
                    'fill-opacity': 0.5
                },
                layout:{

                }
            });*/
            // <<< circular polygon


            // >>> cluster
           /* map.addSource('kennycompany', {
                type: 'geojson',
                data: fakerJam,
                cluster: true,
                clusterMaxZoom: 40,
                clusterRadius: 50
            });

            map.addLayer({
                id: 'clusters',
                type: 'circle',
                source: 'kennycompany',
                filter: ['has', 'point_count'],
                paint: {
// Use step expressions (https://docs.mapbox.com/mapbox-gl-js/style-spec/#expressions-step)
// with three steps to implement three types of circles:
//   * Blue, 20px circles when point count is less than 100
//   * Yellow, 30px circles when point count is between 100 and 750
//   * Pink, 40px circles when point count is greater than or equal to 750
                    'circle-color': [
                        'step',
                        ['get', 'point_count'],
                        '#51bbd6',
                        5,
                        '#f1f075',
                        10,
                        '#f28cb1'
                    ],
                    'circle-radius': [
                        'step',
                        ['get', 'point_count'],
                        20,
                        5,
                        30,
                        10,
                        40
                    ]
                }
            });

            // map.addLayer({
            //     id: 'cluster-count',
            //     type: 'symbol',
            //     source: 'kennycompany',
            //     filter: ['has', 'point_count'],
            //     layout: {
            //         'text-field': ['get', 'point_count_abbreviated'],
            //         'text-font': ['DIN Offc Pro Medium', 'Arial Unicode MS Bold'],
            //         'text-size': 12
            //     }
            // });

            map.addLayer({
                id: 'unclustered-point',
                type: 'circle',
                source: 'kennycompany',
                filter: ['!', ['has', 'point_count']],
                paint: {
                    'circle-color': '#11b4da',
                    'circle-radius': 4,
                    'circle-stroke-width': 1,
                    'circle-stroke-color': '#fff'
                }
            });

// inspect a cluster on click
            map.on('click', 'clusters', (e) => {
                const features = map.queryRenderedFeatures(e.point, {
                    layers: ['clusters']
                });
                const clusterId = features[0].properties.cluster_id;
                map.getSource('kennycompany').getClusterExpansionZoom(
                    clusterId,
                    (err, zoom) => {
                        if (err) return;

                        map.easeTo({
                            center: features[0].geometry.coordinates,
                            zoom: zoom
                        });
                    }
                );
            });

// When a click event occurs on a feature in
// the unclustered-point layer, open a popup at
// the location of the feature, with
// description HTML from its properties.
            map.on('click', 'unclustered-point', (e) => {
                const coordinates = e.features[0].geometry.coordinates.slice();
// Ensure that if the map is zoomed out such that
// multiple copies of the feature are visible, the
// popup appears over the copy being pointed to.
                while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                    coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
                }

                new mapboxgl.Popup()
                    .setLngLat(coordinates)
                    .setHTML(
                        `Hello World!? ${e.features[0].properties}`
                    )
                    .addTo(map);
            });

            map.on('mouseenter', 'clusters', () => {
                map.getCanvas().style.cursor = 'pointer';
            });
            map.on('mouseleave', 'clusters', () => {
                map.getCanvas().style.cursor = '';
            });*/
            // <<< cluster
        });


        // map.on('load', () => {
        //     map.addSource('my-data', {
        //         type: 'geojson',
        //         data: {
        //             type: 'FeatureCollection',
        //             features: [
        //                 {
        //                     type: 'Feature',
        //                     geometry: {
        //                         type: 'Point',
        //                         coordinates: lnglat
        //                     },
        //                     properties: {
        //                         title: 'Marker 1'
        //                     }
        //                 },
        //             ]
        //         }
        //     });
        //
        //     // Add layer to display GeoJSON data
        //     map.addLayer({
        //         id: 'my-layer',
        //         type: 'circle',
        //         source: 'my-data',
        //         paint: {
        //             'circle-color': '#007cbf',
        //             'circle-radius': 8
        //         }
        //     });
        // });
    });

    onDestroy(() => {
        if(map)
            map.remove();
    });

    function readExcel(wsgFlag){
        let input = event.target;
        let reader = new FileReader();
        let result = null;

        let color1 = null;
        let color2 = null;
        let color3 = null;

        if(pointId === 1){
            color1 = '#ff0000';
            color2 = '#ff0000';
            color3 = '#ff0000';
        }else if(pointId === 2){
            color1 = '#0000ff';
            color2 = '#0000ff';
            color3 = '#0000ff';
        }else if(pointId === 3){
            color1 = '#00ff00';
            color2 = '#00ff00';
            color3 = '#00ff00';
        }else if(pointId === 4){
            color1 = '#ffff00';
            color2 = '#ffff00';
            color3 = '#ffff00';
        }else if(pointId === 5){
            color1 = '#ff00ff';
            color2 = '#ff00ff';
            color3 = '#ff00ff';
        }

        geoJsonLinkTest = new Object();
        geoJsonLinkTest["type"] = "FeatureCollection";
        geoJsonLinkTest["features"] = new Array();

        const src_crs = '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=600000 +ellps=GRS80 +units=m +no_defs';
        const target_crs = '+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs';

        reader.onload = function(){
            let data = reader.result;
            let workBook = XLSX.read(data, {type: 'binary'});
            workBook.SheetNames.forEach(function (sheetName){
               console.log("SheetName: " + sheetName);
               let rows = XLSX.utils.sheet_to_json(workBook.Sheets[sheetName]);
               // console.log(JSON.stringify(rows));
                result = JSON.stringify(rows);
            });

            result = JSON.parse(result);
            for(let i=0; i<result.length; i++){
                // console.log("FIRST ! : ", result[i]["X"]);
                // new mapboxgl.Marker().setLngLat([result[i]["X"],result[i]["Y"]]).addTo(map);
                let tmpObj = new Object();
                tmpObj["type"] = "Feature";
                tmpObj["geometry"] = new Object();

                let tmpArr = new Array();
                let lon = Number(result[i]["X"]);
                let lat = Number(result[i]["Y"]);

                if(wsgFlag === false){
                    // Define the EPSG:5186 coordinates
                    let x = lon;  // Replace with your x-coordinate in EPSG:5186
                    let y = lat;  // Replace with your y-coordinate in EPSG:5186

                    // Perform the coordinate transformation
                    let transformed = proj4(src_crs, target_crs, [x, y]);

                    // Extract the transformed coordinates
                    lon = transformed[0];
                    lat = transformed[1];
                }

                tmpArr.push(lon);
                tmpArr.push(lat);

                tmpObj["geometry"]["coordinates"] = tmpArr;
                tmpObj["geometry"]["type"] = "Point";

                geoJsonLinkTest["features"].push(tmpObj);
            }

            // >>> cluster
            map.addSource('kennycompany' + pointId, {
                type: 'geojson',
                data: geoJsonLinkTest,
                cluster: false,
                clusterMaxZoom: 20,     //얼마나 확대했을 때 클러스터를 없앨것인가
                clusterRadius: 30       //클러스터 반경(얼만큼 크기의 원 안에 점들 개수를 셀것인지)
            });

            map.addLayer({
                id: 'clusters' + pointId,
                type: 'circle',
                source: 'kennycompany' + pointId,
                filter: ['has', 'point_count'],
                paint: {
                    'circle-color': [
                        'step',
                        ['get', 'point_count'],
                        color1,
                        5,      //첫번째 경계 포인터 개수 기준 색표현
                        color2,
                        10,     //두번째 경계 포인터 개수 기준 색표현
                        color3,
                    ],
                    'circle-radius': [
                        'step',
                        ['get', 'point_count'],
                        20,
                        5,      //첫번째 경계 포인터 개수 기준 원크기
                        50,
                        10,     //두번째 경계 포인터 개수 기준 원크기
                        80
                    ],
                    'circle-opacity': [
                        'step',
                        ['get', 'point_count'],
                        0.25,
                        5,      //첫번째 경계 포인터 개수 기준 투명도
                        0.5,
                        10,     //두번째 경계 포인터 개수 기준 투명도
                        0.75
                    ],
                }
            });

            map.addLayer({
                id: 'cluster-count' + pointId,
                type: 'symbol',
                source: 'kennycompany' + pointId,
                filter: ['has', 'point_count'],
                layout: {
                    'text-field': ['get', 'point_count_abbreviated'],
                    'text-font': ['DIN Offc Pro Medium', 'Arial Unicode MS Bold'],
                    'text-size': 12
                }
            });

            //클러스터가 아닌 점에 대한 속성처리
            map.addLayer({
                id: 'unclustered-point' + pointId,
                type: 'circle',
                source: 'kennycompany' + pointId,
                filter: ['!', ['has', 'point_count']],
                paint: {
                    'circle-color': color1,
                    'circle-radius': 5,
                    // 'circle-opacity' : 0.3
                    // 'circle-stroke-width': 1,
                    // 'circle-stroke-color': '#fff'
                }
            });

// inspect a cluster on click
            map.on('click', 'clusters' + pointId, (e) => {
                const features = map.queryRenderedFeatures(e.point, {
                    layers: ['clusters' + pointId]
                });
                const clusterId = features[0].properties.cluster_id;
                map.getSource('kennycompany' + pointId).getClusterExpansionZoom(
                    clusterId,
                    (err, zoom) => {
                        if (err) return;

                        map.easeTo({
                            center: features[0].geometry.coordinates,
                            zoom: zoom
                        });
                    }
                );
            });

            console.log(geoJsonLinkTest);
        }
        reader.readAsBinaryString(input.files[0]);
        pointId++;
    }

    function readExcel2(){
        let input = event.target;
        let reader = new FileReader();
        let result = null;
        let sendArr = [];

        reader.onload = function() {
            let data = reader.result;
            let workBook = XLSX.read(data, {type: 'binary'});
            workBook.SheetNames.forEach(function (sheetName) {
                console.log("SheetName: " + sheetName);
                let rows = XLSX.utils.sheet_to_json(workBook.Sheets[sheetName]);
                result = JSON.stringify(rows);
            });

            result = JSON.parse(result);

            for (let i = 0; i < 5; i++) {
                let tmpObj = new Object();
                tmpObj["propLinkId"] = String(result[i]["링크ID"]);
                sendArr.push(tmpObj);
            }

            sendArrFunc(sendArr, null);
        }
        reader.readAsBinaryString(input.files[0]);
        console.log(sendArr);
    }

    function oneCordFunc(data){
        let jsonData = null;
        let color = null;

        if(data === "oneCord"){
            jsonData = JSON.parse(JSON.stringify(oneCord));
            color = "#ff0000";
        }else if(data === "oneCord2"){
            jsonData = JSON.parse(JSON.stringify(oneCord2));
            color = "#00ff00";
        }else if(data === "oneCord3"){
            jsonData = JSON.parse(JSON.stringify(oneCord3));
            color = "#0000ff";
        }
        let features = jsonData.featureCollection.features[0];

        map.addSource('route' + data, {
            'type': 'geojson',
            'data': features,
        });
        map.addLayer({
            'id': 'route' + data,
            'type': 'line',
            'source': 'route' + data,
            'layout': {
                'line-join': 'round',
                'line-cap': 'round'
            },
            'paint': {
                'line-color': color,
                'line-width': 5,
                'line-opacity' : 0.3,

            }
        });
    }

    async function sendArrFunc(sendArr, speed){
        const res = await fetch(backendURL + '/molit/link/info-id', {
            method : 'POST',
            body : JSON.stringify(sendArr),
            headers : {
                'Content-Type' : 'application/json'
            }
        });

        let resJson = await res.json();
        console.log("RESULT RES JSON MOLIT LINK : ", resJson);

        for(let i=0; i<resJson.data.length; i++){
            let featureCollection = JSON.parse(resJson.data[i].jsonData).featureCollection;
            let features = featureCollection.features;
            let coordinates = features[0].geometry.coordinates[0];

            let maxSpd = Number(resJson.data[i].maxSpd);
            let linkId = resJson.data[i].propLinkId;

            if(speed[linkId] !== undefined){
                let curSpd = Number(speed[linkId]);
                if(curSpd/maxSpd < 0.5){
                    continue;
                }
            }

            let data = {
                'type': 'Feature',
                'properties': {},
                'geometry': {
                    'type': 'LineString',
                    'coordinates': coordinates
                }
            }

            map.addSource('route' + i, {
                'type': 'geojson',
                'data': data,
            });

            map.addLayer({
                'id': 'route' + i,
                'type': 'line',
                'source': 'route' + i,
                'layout': {
                    'line-join': 'round',
                    'line-cap': 'round'
                },
                'paint': {
                    'line-color': '#0000ff',
                    'line-width': 5,
                    'line-opacity' : 0.5,

                }
            });
        }
    }

    function readJson(){
        let trafficData = JSON.parse(JSON.stringify(Traffic));
        let trafficDataSp = new Object();
        console.log(trafficData);
        let sendArr = new Array();

        // trafficData.body.items.length
        for(let i=0; i < 20; i++){
            let tmpObj = new Object();
            let linkId = trafficData.body.items[i].linkId;
            tmpObj["propLinkId"] = linkId;
            trafficDataSp[linkId] = trafficData.body.items[i].speed;

            sendArr.push(tmpObj);
        }

        sendArrFunc(sendArr, trafficDataSp);
    }
</script>
<div>
    <label>
        WGS84
        <input type="file" on:change={() => readExcel(true)}/>
    </label>
    <label>
        EPSG5186
        <input type="file" on:change={() => readExcel(false)}/>
    </label>
<!--    <input type="file" on:change={readExcel2}/>-->
    <button class="btn btn-primary" on:click={readJson}>json으로 추적</button>
    <button class="btn btn-primary" on:click={() => oneCordFunc("oneCord")}>oneCord</button>
    <button class="btn btn-primary" on:click={() => oneCordFunc("oneCord2")}>oneCord2</button>
    <button class="btn btn-primary" on:click={() => oneCordFunc("oneCord3")}>oneCord3</button>
</div>
<div id="map-container"></div>

<style>
    #map-container {
        height: 800px;
    }
</style>
