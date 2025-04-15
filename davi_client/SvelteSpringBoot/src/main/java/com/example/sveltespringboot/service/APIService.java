package com.example.sveltespringboot.service;

import com.example.sveltespringboot.dto.*;
import com.example.sveltespringboot.entity.APIEntity;
import com.example.sveltespringboot.entity.APIReqEntity;
import com.example.sveltespringboot.entity.APIResEntity;
import com.example.sveltespringboot.entity.APIServiceEntity;
import com.example.sveltespringboot.repository.APIRepository;
import com.example.sveltespringboot.repository.APIReqRepository;
import com.example.sveltespringboot.repository.APIResRepository;
import com.example.sveltespringboot.repository.APIServiceRepository;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Service
public class APIService {
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private APIRepository apiRepository;
    @Autowired
    private APIServiceRepository apiServiceRepository;
    @Autowired
    private APIReqRepository apiReqRepository;
    @Autowired
    private APIResRepository apiResRepository;

    //공공데이터포털 API 리스트
    private String publicDataPortalBaseUrl = "https://www.data.go.kr";
    private String publicDataPortalListUrl = publicDataPortalBaseUrl + "/tcs/dss/selectDataSetList.do" +
            "?dType=API" +
            "&keyword=&operator=AND" +
            "&detailKeyword=" +
            "&publicDataPk=" +
            "&recmSe=N" +
            "&detailText=" +
            "&relatedKeyword=" +
            "&commaNotInData=" +
            "&commaAndData=" +
            "&commaOrData=" +
            "&must_not=" +
            "&tabId=" +
            "&dataSetCoreTf=" +
            "&coreDataNm=" +
            "&sort=updtDt" +
            "&relRadio=" +
            "&orgFullName=" +
            "&orgFilter=" +
            "&org=" +
            "&orgSearch=" +
            "&currentPage=modifyPageNo" +
            "&perPage=10" +
            "&brm=" +
            "&instt=" +
            "&svcType=" +
            "&kwrdArray=" +
            "&extsn=" +
            "&coreDataNmArray=" +
            "&pblonsipScopeCode=";
    //공공데이터포털 상세페이지 내부 라우트 url
    private final String apiDetailFunctionUrl = "https://www.data.go.kr/tcs/dss/selectApiDetailFunction.do";

    public Map<String, Object> getAPIDatas() {
        Map<String, Object> result = new HashMap<>();

        try{
            System.out.println("===============================Crawling Start===============================\n");

            int pageNo = 1072;
            
            //URL에 시작 페이지 설정
            publicDataPortalListUrl = publicDataPortalListUrl.replace("modifyPageNo", String.valueOf(pageNo));

            while(true){
                Document document = Jsoup.connect(publicDataPortalListUrl).timeout(5000*8).get();
                Elements elements = document.select("#apiDataList .result-list ul li");

                //페이지의 끝에 도달
                if(elements.size() == 0){
                    System.out.println("\n===============================페이지 끝===============================\n");
                    break;
                }

                //하위 페이지로 들어가 필요한 정보를 크롤링한다.
                for(Element elem : elements){
                    System.out.println("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>SubPage Start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                    String subUrl = publicDataPortalBaseUrl + elem.select("dl dt a").attr("href");
                    Document subDocument = Jsoup.connect(subUrl).get();

                    String title = subDocument.select(".open-api-title").text();
                    System.out.println("상세페이지 크롤링 대상 title: [" + title + "], " + "subUrl: [" + subUrl + "]");

                    //API 정보를 담는다.
                    APIDto apiDto = new APIDto();
                    apiDto.setApiName(title);
                    apiDto.setApiSiteUrl(subUrl);

                    //API 상세페이지에 들어있는 테이블의 정보들을 크롤링한다.(dv_api table)
                    Elements apiInfoElemRows = subDocument.select(".file-meta-table-pc .dataset-table tbody tr");
                    if(apiInfoElemRows.size() == 0){
                        break;
                    }
                    crawlingApiInfo(apiInfoElemRows, apiDto);

                    //서비스 URL 크롤링
                    Elements serviceUrlRows = subDocument.select("#open-api-detail-result .box-gray .dot-list li");
                    crawlingServiceUrl(serviceUrlRows, apiDto);

                    //테이블 형태, json 형태 구분 후 최종 dv_api 데이터 삽입
                    String specType = seperateTableJson(subDocument);
                    apiDto.setTableJsonFlag(specType);
                    System.out.println("**********INSERT ApiDto Data**********\n" + apiDto + "\n");
                    apiRepository.save(apiDto.toEntity());

//                    Long apiId = sequenceService.getCurrentValueOfSequence("dv_api_seq");  //시퀀스번호 currval로 불러오는 방법
                    Long apiId = apiRepository.selectApiId(apiDto);

                    //상세 페이지의 명세서가 테이블 형태로 존재하는 경우
                    if(specType.equals("table")){
                        System.out.println("**********Table Spec**********");

                        //서비스 라우터 URL 크롤링(dv_api_service table)
                        Elements routerUrlRows = subDocument.select(".file-meta-table-pc #open_api_detail_select option");

                        if(routerUrlRows.size() > 0){
                            //목록 명세서들을 확인하기 위해 post로 데이터를 받아와야되는데 이때 필요한 값들을 뽑아온다.
                            String publicDataPk = subDocument.select("#publicDataPk").attr("value");
                            String publicDataDetailPk = subDocument.select("#publicDataDetailPk").attr("value");

                            if(publicDataPk.length() != 0 && publicDataDetailPk.length() != 0){
                                for(int j=0; j<routerUrlRows.size(); j++){
                                    //dv_api_service 테이블 insert
                                    APIServiceDto apiServiceDto = new APIServiceDto();
                                    apiServiceDto.setApiId(apiId);
                                    apiServiceDto.setOrder(j + 1);

                                    Document routeDoc =
                                            insertApiServiceTable(routerUrlRows.get(j), publicDataPk, publicDataDetailPk, apiServiceDto);
                                    if(routeDoc == null) {
                                        break;
                                    }

//                                    Long apiServiceId = sequenceService.getCurrentValueOfSequence("dv_api_service_api_service_id_seq"); //시퀀스번호 currval로 불러오는 방법
                                    Long apiServiceId = apiServiceRepository.selectApiId(apiServiceDto);
                                    Elements reqResElements = routeDoc.select("#open-api-detail-result .col-table");

                                    //요청 파라미터 테이블
                                    insertApiReqResTable(reqResElements.get(0), apiServiceId, 0);
                                    //응답 출력 테이블
                                    insertApiReqResTable(reqResElements.get(1), apiServiceId, 1);
                                }
                            }
                        }
                    }
                    //상세 페이지의 명세서가 JSON 형태로 존재하는 경우
                    else if(specType.equals("json")){
                        System.out.println("**********Json Spec**********");
                        insertApiReqResJson();
                    }
                    //상세 페이지의 명세서가 존재하지 않는 경우
                    else{
                        System.out.println("**********None Spec**********");
                    }
                    System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<SubPage END<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
                }
                publicDataPortalListUrl = publicDataPortalListUrl.replace("currentPage=" + pageNo, "currentPage=" + (pageNo + 1));
                pageNo++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private String seperateTableJson(Document document) {
        if (document.toString().contains("col-table") == true) {
            return "table";
        } else if (document.toString().contains("API 목록") == true) {
            return "json";
        } else {
            return "none";
        }
    }

    private void crawlingApiInfo(Elements apiInfoElemRows, APIDto apiDto){
        for(Element apiInfoElemRow : apiInfoElemRows){
            Elements colKey = apiInfoElemRow.select("th");
            Elements colData = apiInfoElemRow.select("td");

            for(int j=0; j<colKey.size(); j++){

                //전화번호 정보의 경우 스크립트의 형태로 저장되고 있댜.
                if(colData.get(j).toString().contains("telNo")
                        && !colData.get(j).toString().contains("var telNo = \"\";")){
                    int telNoStrtPos = colData.get(j).toString().indexOf("0");
                    if(telNoStrtPos == -1){
                        break;
                    }
                    String telNo = colData.get(j).toString().substring(telNoStrtPos);
                    for(int k=0; k<telNo.length(); k++){
                        if(telNo.charAt(k) == '\"'){
                            telNo = telNo.substring(0, k);
                            break;
                        }
                    }
                    telNo = telNo.replaceAll("(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})$", "$1-$2-$3");
                    apiDto.setExecDeptTelno(telNo);
                }
                //전화번호 이외 데이터 삽입
                else{
                    String key = colKey.get(j).text();
                    String data = colData.get(j).text();
                    if(key.equals("분류체계")){
                        apiDto.setTaxonomy(data);
                    }else if(key.equals("제공기관")){
                        apiDto.setProvOrg(data);
                    }else if(key.equals("관리부서명")){
                        apiDto.setExecDept(data);
                    }else if(key.equals("API 유형")){
                        apiDto.setApiType(data);
                    }else if(key.equals("데이터포맷")){
                        apiDto.setDataFormat(data);
                    }else if(key.equals("활용신청")){
                        apiDto.setUsesAppl(data);
                    }else if(key.equals("키워드")){
                        apiDto.setKeyword(data);
                    }else if(key.equals("등록")){
                        apiDto.setRegiDate(data);
                    }else if(key.equals("수정")){
                        apiDto.setEditDate(data);
                    }else if(key.equals("심의유형")){
                        apiDto.setReviewType(data);
                    }else if(key.equals("URL")){
                        apiDto.setUrl(data);
                    }else if(key.equals("비용부과유무")){
                        apiDto.setChargeYn(data);
                    }else if(key.equals("신청가능 트래픽")){
                        apiDto.setAvailTraffic(data);
                    }
                }
            }
        }
    }

    private void crawlingServiceUrl(Elements serviceUrlRows, APIDto apiDto){
        for(Element serviceUrlRow : serviceUrlRows){
            if(serviceUrlRow.toString().contains("서비스URL")){
                String data = serviceUrlRow.text();
                int urlStrtPos = data.indexOf("http");

                if(urlStrtPos == -1){
                    break;
                }

                data = data.substring(urlStrtPos);
                apiDto.setApiServiceUrl(data);
            }
        }
    }

    private Document insertApiServiceTable(Element routeUrlRow, String publicDataPk, String publicDataDetailPk, APIServiceDto apiServiceDto) throws IOException {
        String oprtinSeqNo = routeUrlRow.attr("value");
        String description = routeUrlRow.text();
        apiServiceDto.setDescription(description);

        //jsoup post로 데이터를 뽑아온다.
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Encoding", "gzip, deflate, br");
        header.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Map<String, String> postBody = new HashMap<>();
        postBody.put("oprtinSeqNo", oprtinSeqNo);
        postBody.put("publicDataDetailPk", publicDataDetailPk);
        postBody.put("publicDataPk", publicDataPk);

        Document routeDoc = Jsoup.connect(apiDetailFunctionUrl).headers(header).data(postBody).post();
        //해당 목록에 대한 route URL값을 뽑는다.
        int routeStrtPos = routeDoc.toString().indexOf("dataObj.oprtinUrl = \"http");
        if(routeStrtPos == -1){
            return null;
        }
        String route = routeDoc.toString().substring(routeStrtPos);

        routeStrtPos = route.indexOf("http");
        if(routeStrtPos == -1){
            return null;
        }
        route = route.substring(routeStrtPos);
        int routeEndPos = route.indexOf("\"");
        route = route.substring(0, routeEndPos);

        apiServiceDto.setRoute(route);

        System.out.println("**********INSERT ApiServiceDto Data**********\n" + apiServiceDto + "\n");
        apiServiceRepository.save(apiServiceDto.toEntity());

        return routeDoc;
    }

    private void insertApiReqResTable(Element elem, Long apiServiceId, int reqResFlag){
        Elements rows = elem.select("tbody tr");

        for(Element row : rows) {
            Map<String, String> apiReqResDto = new HashMap<>();
            Elements col = row.select("td");

            for (int t = 0; t < col.size(); t++) {
                String data = col.select("td").get(t).text();
                switch (t) {
                    case 0:
                        apiReqResDto.put("columnNmKr", data);
                        break;
                    case 1:
                        apiReqResDto.put("columnNmEn", data);
                        break;
                    case 2:
                        apiReqResDto.put("columnSize", data);
                        break;
                    case 3:
                        apiReqResDto.put("columnDiv", data);
                        break;
                    case 4:
                        apiReqResDto.put("sampleData", data);
                        break;
                    case 5:
                        apiReqResDto.put("columnDesc", data);
                        break;
                }
            }

            if (reqResFlag == 0) {
                APIReqDto apiReqDto = new APIReqDto();
                apiReqDto.setColumnNmKr(apiReqResDto.get("columnNmKr"));
                apiReqDto.setColumnNmEn(apiReqResDto.get("columnNmEn"));
                apiReqDto.setColumnSize(apiReqResDto.get("columnSize"));
                apiReqDto.setColumnDiv(apiReqResDto.get("columnDiv"));
                apiReqDto.setSampleData(apiReqResDto.get("sampleData"));
                apiReqDto.setColumnDesc(apiReqResDto.get("columnDesc"));

                apiReqDto.setApiServiceId(apiServiceId);

                System.out.println("**********INSERT ApiReqDto Data**********\n" + apiReqDto + "\n");
                apiReqRepository.save(apiReqDto.toEntity());
            } else if (reqResFlag == 1) {
                APIResDto apiResDto = new APIResDto();
                apiResDto.setColumnNmKr(apiReqResDto.get("columnNmKr"));
                apiResDto.setColumnNmEn(apiReqResDto.get("columnNmEn"));
                apiResDto.setColumnSize(apiReqResDto.get("columnSize"));
                apiResDto.setColumnDiv(apiReqResDto.get("columnDiv"));
                apiResDto.setSampleData(apiReqResDto.get("sampleData"));
                apiResDto.setColumnDesc(apiReqResDto.get("columnDesc"));

                apiResDto.setApiServiceId(apiServiceId);

                System.out.println("**********INSERT ApiResDto Data**********\n" + apiResDto + "\n");
                apiResRepository.save(apiResDto.toEntity());
            }
        }
    }

    private void insertApiReqResJson(){
         /*
        List<APIColumnListDto> apiColumnListDtos = new ArrayList<>();

        //공공데이터 포털 고정 값 하드 코딩
        for(int j=0; j<6; j++){
            APIColumnListDto apiColumnListDto = new APIColumnListDto();
            switch (j){
                case 0:
                    apiColumnListDto.setColumnNmKr("결과코드");
                    apiColumnListDto.setColumnNmEn("resultCode");
                    break;
                case 1:
                    apiColumnListDto.setColumnNmKr("결과메세지");
                    apiColumnListDto.setColumnNmEn("resultMsg");
                    break;
                case 2:
                    apiColumnListDto.setColumnNmKr("전체 결과 수");
                    apiColumnListDto.setColumnNmEn("totalCount");
                    break;
                case 3:
                    apiColumnListDto.setColumnNmKr("페이지 크기");
                    apiColumnListDto.setColumnNmEn("numOfRows");
                    break;
                case 4:
                    apiColumnListDto.setColumnNmKr("쿼리페이지 시작점");
                    apiColumnListDto.setColumnNmEn("pageIndex");
                    break;
                case 5:
                    apiColumnListDto.setColumnNmKr("시작 페이지");
                    apiColumnListDto.setColumnNmEn("pageNo");
                    break;
            }
            apiColumnListDtos.add(apiColumnListDto);
        }

        //나머지 data칼럼의 경우 json형태로 크롤링에 저장되어 json값을 가져와 저장한다.
        int jsonStrtPos = subDocument.toString().indexOf("{\"list");
        if(jsonStrtPos == -1) {
            break;
        }
        int jsonLastPos = subDocument.toString().indexOf("}}}}") + 4;
        if(jsonLastPos == -1) {
            break;
        }
        String jsonDataStr = subDocument.toString().substring(jsonStrtPos, jsonLastPos);

        System.out.println("Test for jsonData\n" + jsonDataStr);

        JSONParser jsonParser = new JSONParser();   //Parser
        Object obj = jsonParser.parse(jsonDataStr);    //To Obj
        JSONObject jsonObject = (JSONObject)obj;    //To JsonObject
        JSONObject jsonData = (JSONObject) ((JSONObject)jsonObject.get("list")).get("properties");

        System.out.println("Test for jsonData String \n" + jsonData.toString());

        //키 값만 뽑아오기
        Iterator keyList = jsonData.keySet().iterator();
        APIColumnListDto apiColumnListDto = new APIColumnListDto();
        while(keyList.hasNext()){
            String key = keyList.next().toString();

            apiColumnListDto.setColumnNmEn(key);
            apiColumnListDto.setColumnNmKr((String) ((JSONObject)jsonData.get(key)).get("description"));
            System.out.println("Test for apiColumn : " + apiColumnListDto);
        }
        apiColumnListDtos.add(apiColumnListDto);
        */
    }

    //API의 리스트들을 담아 보낸다.
    public Map<String, Object> getApiList(int listCnt) {
        Map<String, Object> result = new HashMap<>();
        List<APIDto> apiDtoList = new ArrayList<>();


//        APIEntity apiEntity = apiRepository.findByApiName();
//        apiDtoList.add(apiEntity.toDto());
//        System.out.println("======================================================================");

        for (int i = 1; i <= listCnt; i++) {
            APIEntity apiEntity = apiRepository.findByApiIdAndTableJsonFlag(Long.valueOf(i), "table");
            if(apiEntity == null){
                continue;
            }
            APIDto apiDto = apiRepository.findByApiIdAndTableJsonFlag(Long.valueOf(i), "table").toDto();
            System.out.println("Test for APIDto : \n" + apiDto);
            if(apiDto.getApiName().contains("대구"))
                apiDtoList.add(apiDto);
        }
        System.out.println("======================================================================");

//        System.out.println("Test for APIDto List: \n" + apiRepository.findTop10ByTableJsonFlagOrderByApiIdAsc("table"));
//        System.out.println("======================================================================");

        result.put("success", true);
        result.put("message", "Get API List Success");
        result.put("data", apiDtoList);
        return result;
    }

    //사용자가 선택한 api서비스 list들을 넘긴다.
    public Map<String, Object> getApiServiceList(APIDto apiDto){
        Map<String, Object> result = new HashMap<>();
        List<APIServiceEntity> apiServiceEntities = new ArrayList<>();
        apiServiceEntities = apiServiceRepository.findAllByApiId(apiDto.getApiId());
        System.out.println("Test for apiServiceDtos" + apiServiceEntities);

        List<APIServiceDto> apiServiceDtos = new ArrayList<>();
        for(APIServiceEntity apiServiceEntity : apiServiceEntities){
            apiServiceDtos.add(apiServiceEntity.toDto());
        }

        result.put("success", true);
        result.put("message", "Get API Service List Success");
        result.put("data", apiServiceDtos);

        System.out.println("Test for apiServiceDtos" + apiServiceDtos);

        return result;
    }

    //사용자가 선택한 api서비스의 req, res칼럼들을 넘긴다.
    public Map<String, Object> getApiReqResList(APIServiceDto apiServiceDto){
        Map<String, Object> result = new HashMap<>();
        List<APIReqEntity> apiReqEntities = apiReqRepository.findAllByApiServiceId(apiServiceDto.getApiServiceId());
        List<APIResEntity> apiResEntities = apiResRepository.findAllByApiServiceId(apiServiceDto.getApiServiceId());

        List<APIReqDto> apiReqDtos = new ArrayList<>();
        List<APIResDto> apiResDtos = new ArrayList<>();

        for(APIReqEntity apiReqEntity : apiReqEntities){
            apiReqDtos.add(apiReqEntity.toDto());
        }
        System.out.println("Test for apiReqDtos : \n" + apiReqDtos);

        for(APIResEntity apiResEntity : apiResEntities){
            apiResDtos.add(apiResEntity.toDto());
        }
        System.out.println("Test for apiResDtos : \n" + apiResDtos);

        result.put("success", true);
        result.put("message", "Get API Service List Success");
        result.put("reqData", apiReqDtos);
        result.put("resData", apiResDtos);
        return result;
    }
}
