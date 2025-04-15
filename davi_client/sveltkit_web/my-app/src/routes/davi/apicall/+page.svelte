<script>
    import {afterUpdate, beforeUpdate, onDestroy, onMount} from "svelte";
    import GoogleSpreadSheet from "../../../sheet/googlespreadsheet/GoogleSpreadSheet.svelte";
    import TreeJs from "../../../treeview/treejs/TreeJs.svelte";
    import TableJoin from "../../tablejoin/TableJoin.svelte";
    import MolitTreeJs from "../../../treeview/treejs/MolitTreeJs.svelte";

    let backendURL = import.meta.env.VITE_DAVI_URL;

    let apiList = new Array();
    let routeList = new Array();
    let reqColList = new Array();
    let resColList = new Array();

    let molitList = new Array();
    let molitData = new Array();
    let molitColList = new Array();
    let molitSelectName = null;

    let apiSelectIndex = 0;
    let routeSelectIndex = 0;
    let routeSelectName = null;

    let reqParamInput = new Array(100);
    let callApiResult = null;

    let jsonData = null;
    let childComponentFlag = true;
    let childMolitComponentFlag = true;

    let firstData = null;
    let secondData = null;

    let resultInform = null;

    function IsJsonString(str) {
        try {
            var json = JSON.parse(JSON.stringify(str));
            return (typeof json === 'object');
        } catch (e) {
            return false;
        }
    }

    const callAPI = () => {
        jsonData = null;

        let apiRouteUrl = routeList[routeSelectIndex].route + "?";
        let param = "";

        for(let i=0; i<reqColList.length; i++){
            if(reqParamInput[i] === undefined){
                reqParamInput[i] = "";
            }
            param += reqColList[i].columnNmEn + "=" + reqParamInput[i];
            if(i != reqColList.length - 1){
                param += "&";
            }
        }

        let resultUrl = apiRouteUrl + param;
        console.log("resultUrl", resultUrl);

        async function callApiUrl(){
            const res = await fetch(resultUrl, {
                method: 'GET',
            });

            const resText = await res.text();

            //json 형태처리
            try{
                let resJson = JSON.parse(resText);
                console.log("Test for resJson", resJson);

                jsonData = resJson;
            }
            //json형태가 아닌 xml일 경우
            catch (err){
                callApiResult = new DOMParser().parseFromString(resText, 'text/xml');
                callApiResult = xmlToJson(callApiResult);

                jsonData = callApiResult;
            }
            console.log("Test for callApi", callApiResult);
        };

        callApiUrl();
    };

    //Vanilla JS 오픈소스
    function xmlToJson(xml) {
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

                if(nodeName === '#text'){
                    continue;
                }

                if (typeof obj[nodeName] == "undefined") {
                    let tmpVal = xmlToJson(item);

                    //Array로 만들어야되는 바디부들에 대한 하드코딩으로 예외처리 필요...
                    //Array로 만들어야 바디부인지 추후 Treejs에서 확인이 가능
                    if(nodeName === "item" || nodeName === "row"){
                        obj[nodeName] = [];
                        obj[nodeName].push(tmpVal);
                    }
                    else{
                        obj[nodeName] = tmpVal;
                    }
                } else {
                    if (typeof obj[nodeName].push == "undefined") {

                        var old = obj[nodeName];
                        obj[nodeName] = [];
                        obj[nodeName].push(old);
                    }
                    obj[nodeName].push(xmlToJson(item));
                }
            }
        }
        return obj;
    }

    const apiServiceListClick = (index) => {
        routeSelectIndex = index;
        callApiResult = null;
        jsonData = null;

        async function getReqResList(){
            let selectApiServiceInfo = routeList[index];

            const res = await fetch(backendURL + '/public-data-portal/req/list/public-data-portal-service-id', {
                method : 'POST',
                body : JSON.stringify(selectApiServiceInfo),
                headers : {
                    'content-type' : 'application/json'
                }
            });

            let resList = await res.json();
            console.log("REQRES resList", resList);

            reqColList = resList.data;

            const res2 = await fetch(backendURL + '/public-data-portal/res/list/public-data-portal-service-id', {
                method : 'POST',
                body : JSON.stringify(selectApiServiceInfo),
                headers : {
                    'content-type' : 'application/json'
                }
            });

            let resList2 = await res2.json();
            console.log("REQRES resList", resList);

            resColList = resList2.data;
        };

        getReqResList();
    };

    const molitListClick = (index, name, dataListId) => {
        apiSelectIndex = apiList.length + index;
        routeList = "molit";
        molitColList = new Array();

        let contextPath = null;
        molitSelectName = name;

        if(name === '국토부_링크') {
            contextPath = '/molit/link/sample-list';
            dataListId = 1;
        }
        else if(name === '국토부_시도') {
            contextPath = '/molit/sido/sample-list';
            dataListId = 2;
        }
        else if(name === '국토부_시군구') {
            contextPath = '/molit/sigungu/sample-list';
            dataListId = 3;
        }
        else if(name === '국토부_읍면동') {
            contextPath = '/molit/eupmyeondong/sample-list';
            dataListId = 4;
        }
        else if(name === '국토부_리'){
            contextPath = '/molit/li/sample-list';
            dataListId = 5;
        }
        else if(name === '국토부_도로') {
            contextPath = '/molit/road/sample-list';
            dataListId = 7;
        }else if(name === '해양조사원_해상거리'){
            contextPath = '/khoa/seadist/sample-list';
            dataListId = 8;
        }

        let data = {
            dataListId : dataListId,
        }

        async function getMolitCol(){
            const res = await fetch(backendURL + '/common/data-col-list/data-list-id',{
                method : 'POST',
                body : JSON.stringify(data),
                headers : {
                    'content-type' : 'application/json'
                }
            });

            let resJson = await res.json();
            molitColList = resJson.data;
            console.log("MOLIT COL LIST : ", molitColList);
        }
        getMolitCol();

        async function getMolitData(){
            const res = await fetch(backendURL + contextPath, {
                method : 'POST',
            });

            let resJson = await res.json();
            molitData = resJson.data;
            console.log("MOLIT DATA : ", molitData)
        }

        getMolitData();
    }

    const apiListClick = (index) => {
        apiSelectIndex = index;
        callApiResult = null;
        molitColList = new Array();

        async function getServiceList(){
            let selectApiInfo = apiList[index];

            const res = await fetch(backendURL + '/public-data-portal/service/list/public-data-portal-id',{
                method : 'POST',
                body : JSON.stringify(selectApiInfo),
                headers : {
                    'content-type' : 'application/json'
                }
            });

            let resList = await res.json();
            console.log("SERVICE API resList", resList);

            routeList = resList.data;

            //이미 첫번째 데이터에서 선택한 라우트 서비스 명은 선택 불가
            if(firstData !== null && routeSelectName !== null){
                for(let i=0; i<routeList.length; i++) {
                    if (routeList[i].description === routeSelectName){
                        routeList.splice(i, 1);
                        break;
                    }
                }
            }

            routeSelectIndex = routeList.length;
        };

        getServiceList();
    };


    onMount(async () => {
        // 공공데이터 포털 리스트 가져오기
        let data = {
            listCnt : 90,
        };

        const res = await fetch(backendURL + '/public-data-portal/list/table-json-flag', {
            method : 'POST',
            body : JSON.stringify(data),
            headers: {
                'content-type' : 'application/json'
            }
        });

        let resList =  await res.json();
        console.log("API LIST resList", resList);

        apiList = resList.data;
        apiSelectIndex = -1;

        // molit 행정구역 리스트 가져오기
        data = {
            metaType : 1,
        };

        const molitRes = await fetch(backendURL + '/common/data-list/meta-type', {
            method : 'POST',
            body : JSON.stringify(data),
            headers: {
                'content-type' : 'application/json'
            }
        });

        resList = await molitRes.json();
        console.log("MOLIT LIST", resList);

        molitList = resList.data;
    });

    const onSubmit = (index) => {
        let sendData = { "selectIdx" : index, "selectName" : apiList[index].apiName};

        async function doPost(){
            const resUrl = await fetch(backendURL + '/api/v1/user-api/select-api', {
                method : 'POST',
                body : JSON.stringify(sendData),
                headers: {
                    'content-type' : 'application/json'
                }
            });

            const resJson = await resUrl.json();

            if(resJson.success === true){
                console.log("정상 동작", resJson);

                if(resJson.name !== "국토교통부_교통링크"){
                    return;
                }

                // const res = await fetch("https://api.vworld.kr/req/data", {
                //     method: 'GET',
                //     dataType: 'jsonp',
                //     data: 'key=2BBB1BBD-C698-39F3-AD60-5A029B206D64&domain=http://localhost:5170&service=data&version=2.0&request=getfeature&format=json&size=1000&page=1&geometry=false&attribute=true&crs=EPSG:3857&geomfilter=BOX(13663271.680031825,3894007.9689600193,14817776.555251127,4688953.0631258525)&data=LT_C_ADSIDO_INFO'
                // });

                const res = await fetch('/vworld-api' + resJson.data, {
                    method: 'GET',
                    dataType : 'jsonp'
                });

                const resAPIJson = await res.json();
                resultInform = resAPIJson.response.result.featureCollection.features;
                console.log("응답받은 API 데이터", resAPIJson);
            }else{
                console.log("비정상 동작");
            }
        }
        doPost();
    }

    function addKrHeader(headerData){
        let krHeader = new Array();

        for(let i=0; i<headerData.length; i++){
            let selHeader = headerData[i];
            let orgLength = krHeader.length;

            for(let j=0; j<resColList.length; j++){
                let resHeader = resColList[j].columnNmEn;
                if(selHeader === resHeader){
                    let resHeaderKr = resColList[j].columnNmKr;
                    krHeader.push(resHeaderKr);

                    break;
                }
            }

            if(orgLength === krHeader.length){
                krHeader.push("");
            }
        }

        return krHeader;
    }

    beforeUpdate(()=>{
       if(childComponentFlag === false){
           routeSelectName = routeList[routeSelectIndex].description;

           if(firstData !== null && secondData === null){
               firstData["krHeader"] = addKrHeader(firstData["header"]);
               firstData["routeName"] = routeSelectName;
               console.log("FIRST DATA!!!", firstData);
           }else if(firstData !== null && secondData !== null){
               secondData["krHeader"] = addKrHeader(secondData["header"]);
               secondData["routeName"] = routeSelectName;
               console.log("Seconde DATA!!!", secondData);

               apiList = new Array();
           }

           //=====>초기화 구간
           routeList = new Array();
           reqColList = new Array();
           resColList = new Array();

           apiSelectIndex = -1;
           reqParamInput = new Array(1000);

           callApiResult = null;
           jsonData = null;
           childComponentFlag = true;
           //<=====초기화 구간

       }

        if(childMolitComponentFlag === false){
           if(firstData !== null && secondData === null){
               let tmpMolitList = new Array();
               for(let i=0; i<molitList.length; i++) {
                   if (molitSelectName !== molitList[i].name) {
                       tmpMolitList.push(molitList[i]);
                   }
               }

               molitList = tmpMolitList;

               // for(let i=0; i<molitList.length; i++) {
               //     if (molitSelectName === molitList[i].name) {
               //         molitList.splice(i, 1);
               //         break;
               //     }
               // }

               console.log("TESTTEST", molitList);

               firstData["routeName"] = molitSelectName;
               console.log("FIRST MOLIT DATA!!!", firstData);
           }else if(firstData !== null && secondData !== null){
               secondData["routeName"] = molitSelectName;
               console.log("Second MOLIT DATA!!!", secondData);

               molitList = new Array();
           }

           // ==========> 초기화 구간
           molitData = new Array();
           molitColList = new Array();
           apiSelectIndex = -1;
           childMolitComponentFlag =  true;
           // <========== 초기화 구간
       }
    });

    function testFunc(){
        secondData = {
            "header": [
                "full_nm",
                "emd_cd",
                "json_data",
                "hdong_cd",
                "bdong_cd"
            ],
            "body": [
                [
                    "세종특별자치시 세종특별자치시 어진동",
                    "36110110",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611052300",
                    "3611011000"
                ],
                [
                    "세종특별자치시 세종특별자치시 산울동",
                    "36110115",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611052500",
                    "3611011500"
                ],
                [
                    "세종특별자치시 세종특별자치시 해밀동",
                    "36110116",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611052500",
                    "3611011600"
                ],
                [
                    "세종특별자치시 세종특별자치시 누리동",
                    "36110120",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611052500",
                    "3611012000"
                ],
                [
                    "세종특별자치시 세종특별자치시 한별동",
                    "36110121",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611052500",
                    "3611012100"
                ],
                [
                    "세종특별자치시 세종특별자치시 아름동",
                    "36110113",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611053000",
                    "3611011300"
                ],
                [
                    "세종특별자치시 세종특별자치시 종촌동",
                    "36110111",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611054000",
                    "3611011100"
                ],
                [
                    "세종특별자치시 세종특별자치시 고운동",
                    "36110112",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611055000",
                    "3611011200"
                ],
                [
                    "세종특별자치시 세종특별자치시 소담동",
                    "36110102",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611055500",
                    "3611010200"
                ],
                [
                    "세종특별자치시 세종특별자치시 반곡동",
                    "36110101",
                    "{\"featureCollection\": {\"bbox\": [124.60970878563441 ...",
                    "3611055600",
                    "3611010100"
                ]
            ],
            "krHeader": [
                "시도 + 시군구 + 읍면동 full명",
                "읍면동 코드",
                "좌표",
                "행정동 코드",
                "법정동 코드"
            ],
            "routeName": "국토부_읍면동"
        };

        firstData={
            "header": [
                "arcd",
                "ctprvn_nm",
                "sgg_nm",
                "vt_acmdfclty_nm",
                "bdong_cd",
                "hdong_cd",
                "dtl_adres"
            ],
            "body": [
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "명호초등학교 운동장",
                    "2644010400",
                    "2644054500",
                    "부산광역시 강서구 명지동   3232-6"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "명지초등학교 운동장",
                    "2644010400",
                    "2644053500",
                    "부산광역시 강서구 명지동   3385-0"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "덕두초등학교 운동장",
                    "2644010200",
                    "2644052000",
                    "부산광역시 강서구 대저2동   2489-1"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "대사초등학교 운동장",
                    "2644010300",
                    "2644053000",
                    "부산광역시 강서구 강동동   101-64"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "대저중고등학교 운동장",
                    "2644010200",
                    "2644052000",
                    "부산광역시 강서구 대저2동   2039"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "대저초등학교 운동장",
                    "2644010100",
                    "2644051000",
                    "부산광역시 강서구 대저1동   1605"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "배영초등학교 운동장",
                    "2644010200",
                    "2644052000",
                    "부산광역시 강서구 대저2동   4552-0"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "낙동중학교 운동장",
                    "2644010100",
                    "2644051000",
                    "부산광역시 강서구 대저1동   1491-7"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "대상초등학교 운동장",
                    "2644010100",
                    "2644051000",
                    "부산광역시 강서구 대저1동   2355-1"
                ],
                [
                    "2644000000",
                    "부산광역시",
                    "강서구",
                    "강서고등학교 운동장",
                    "2644010100",
                    "2644051000",
                    "부산광역시 강서구 대저1동   421-3"
                ]
            ],
            "krHeader": [
                "지역코드",
                "시도명",
                "시군구명",
                "시설명",
                "법정동코드",
                "행정동코드",
                "상세주소"
            ],
            "routeName": "지진 옥외대피장소 조회 서비스"
        };
    }
</script>

{#if secondData === null}
    <button class="btn btn-danger ml-5 mt-5" on:click={testFunc}>
        바로 JOIN 시작
    </button>
<div style="display: flex">
    <div class="ml-5 mt-5 mb-5" style="width: 400px; display: flow">
        <div class="list-group">
            <strong>API 목록 선택</strong>
            {#each apiList as item, index}
                <a href="#" class="list-group-item list-group-item-action {index === apiSelectIndex ? 'active' : ''}"
                   aria-current="{index === apiSelectIndex ? 'true' : null}"
                   on:click={() => apiListClick(index)}>{item.apiName}
                </a>
            {/each}
            {#each molitList as item, index}
                <a href="#" class="list-group-item list-group-item-action {(apiList.length + index) === apiSelectIndex ? 'active' : ''}"
                   aria-current="{(apiList.length + index) === apiSelectIndex ? 'true' : null}"
                   on:click={() => molitListClick(index, item.name, item.dataListId)}>{item.name}
                </a>
            {/each}
        </div>
        <div>
            <h4 style="margin-top:50px"> 선택된 데이터 </h4>
            {#if firstData}
                <label>
                    {firstData.routeName}
                </label>
            {/if}
        </div>
    </div>
    {#if routeList !== "molit"}
    <div class="ml-5 mt-5 mb-5" style="width: 400px">
        <div class="list-group">
                {#if routeList.length != 0}
                    <strong>서비스 라우트 선택</strong>
                {/if}
                {#each routeList as item, index}
                    <a href="#" class="list-group-item list-group-item-action {index === routeSelectIndex ? 'active' : ''}"
                       aria-current="{index === routeSelectIndex ? 'true' : null}"
                       on:click={() => apiServiceListClick(index)}>{item.description}
                    </a>
                {/each}
            <!--{#if routeList.length != 0}-->
            <!--    <button class="btn btn-info mt-3" type="button" on:click={()=>onSubmit(routeSelectIndex)}>생성 요청</button>-->
            <!--{/if}-->
        </div>
    </div>
    {#if routeSelectIndex < routeList.length}
    <div style="display: flow">
        <div class="ml-5 mt-5 mb-5" style="width: 400px">
            <div class="list-group">
                {#if reqColList.length != 0}
                    <strong>요청 파라미터</strong>
                {/if}
                2HrkEW0EqTLtlnpp1KWE98SReBtg0ab8hqPSlRCp38J3%2Flx58aziYFgrnt5uOfm44A%2BJwa6aA9Jcb5sdccb2Bg%3D%3D
                {#each reqColList as item, index}
                    {#if item.columnNmKr.length != 0}
                    <li class="mt-2 mb-1">{item.columnNmKr}</li>
                    <input placeholder="설명 : {item.columnDesc}, 샘플 : {item.sampleData}"
                           bind:value={reqParamInput[index]}>
                    {:else if item.columnNmEn.length != 0}
                    <li class="mt-2 mb-1">{item.columnNmEn}</li>
                    <input placeholder="설명 : {item.columnDesc}, 샘플 : {item.sampleData}"
                           bind:value={reqParamInput[index]}>
                    {/if}
                {/each}
            </div>
        </div>
        <div class="ml-5 mt-5 mb-5" style="width: 800px">
            <div class="list-group">
                {#if resColList.length != 0}
                    <strong>응답 결과</strong>
                {/if}
                {#each resColList as item, index}
                    {#if item.columnNmKr.length != 0}
                        <li class="mt-2 mb-1">{item.columnNmKr}({item.columnNmEn})<br>설명 : {item.columnDesc}<br>샘플 : {item.sampleData}</li>
                    {:else if item.columnNmEn.length != 0}
                        <li class="mt-2 mb-1">{item.columnNmEn}<br>설명 : {item.columnDesc}<br>샘플 : {item.sampleData}</li>
                    {/if}
                {/each}
            </div>
        </div>
        <button class="btn btn-info"
                style="width: 300px;height: 50px; margin-left: 100px; margin-top: 10px;  margin-bottom: 50px"
                type="button" on:click={()=>callAPI()}>생성 요청
        </button>
        {#if jsonData}
            <TreeJs jsonData={jsonData} bind:childComponentFlag bind:firstData bind:secondData/>
        {/if}
    </div>
    {/if}
    {:else}
        <div style="display: flow">
            {#if molitColList != null && molitColList.length != 0}
                <MolitTreeJs {molitColList} {molitData} bind:firstData bind:secondData bind:childMolitComponentFlag/>
            {/if}
        </div>
    {/if}
    <div class="ml-5 mt-5 mb-5" style="width: 300px">
        {#if resultInform}
            {#each resultInform as result}
                <div>{result.properties.ctp_kor_nm}</div>
            {/each}
        {/if}
    </div>
    {#if resultInform}
    <div class="ml-5 mt-5 mb-5" style="width: 100%">
        <GoogleSpreadSheet/>
        <h1>Table Viewer</h1>
        {#if resultInform}
        <table>
            <thead>
            <tr>
                <th>No.</th>
                <th>CTP_KOR_NM</th>
                <th>CTP_ENG_NM</th>
                <th>CTPRVN_CD</th>
            </tr>
            </thead>
            <tbody>
            {#each resultInform as row, index}
                <tr>
                    <td>{index + 1}</td>
                    <td>{row.properties.ctp_kor_nm}</td>
                    <td>{row.properties.ctp_kor_nm}</td>
                    <td>{row.properties.ctp_eng_nm}</td>
                    <td>{row.properties.ctprvn_cd}</td>
                </tr>
            {/each}
            </tbody>
        </table>
        {:else}
            <p>Loading</p>
        {/if}
    </div>
    {/if}
</div>
{:else}
    <TableJoin {firstData} {secondData}/>
{/if}

<style>
    table{
        margin: 10px 10px 10px 10px;
    }

    table, th, td {
        border: 1px solid black;
        text-align: center;
        -ms-user-select: none;
        -moz-user-select: -moz-none;
        -khtml-user-select: none;
        -webkit-user-select: none;
        user-select: none;
    }

    th {
        pointer-events: none;
        background-color: darkgray;
        width: auto;
    }

</style>