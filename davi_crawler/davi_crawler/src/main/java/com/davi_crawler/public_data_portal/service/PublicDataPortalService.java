package com.davi_crawler.public_data_portal.service;

import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalReqDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalResDto;
import com.davi_crawler.public_data_portal.dto.PublicDataPortalServiceDto;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalReqEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalResEntity;
import com.davi_crawler.public_data_portal.entity.PublicDataPortalServiceEntity;
import com.davi_crawler.public_data_portal.repository.PublicDataPortalRepository;
import com.davi_crawler.public_data_portal.repository.PublicDataPortalReqRepository;
import com.davi_crawler.public_data_portal.repository.PublicDataPortalResRepository;
import com.davi_crawler.public_data_portal.repository.PublicDataPortalServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class PublicDataPortalService {
    private final DataListService dataListService;
    private final PublicDataPortalRepository publicDataPortalRepository;
    private final PublicDataPortalServiceRepository publicDataPortalServiceRepository;
    private final PublicDataPortalReqRepository publicDataPortalReqRepository;
    private final PublicDataPortalResRepository publicDataPortalResRepository;

    //공공데이터포털 상세페이지 내부 라우트 url
    private final String PDPAPI_DETAIL_ROUTE_URL = "https://www.data.go.kr/tcs/dss/selectApiDetailFunction.do";

    private String getPublicDataPortalUrl(String publicDataPortalBaseUrl){
        return publicDataPortalBaseUrl + "/tcs/dss/selectDataSetList.do" +
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
    }

    //api데이터를 뽑아온다.
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        log.debug("===============================Crawling Start===============================\n");

        //공공데이터포털 리스트 url
        String publicDataPortalBaseUrl = dataListService.selectById(ConstantUtil.DATA_LIST_PUBLICDATAPORTAL_ID).getUrl();
        String publicDataPortalListUrl = getPublicDataPortalUrl(publicDataPortalBaseUrl);

        int pageNo = 1;

        //URL에 시작 페이지 설정
        publicDataPortalListUrl = publicDataPortalListUrl.replace("modifyPageNo", String.valueOf(pageNo));

        while(true){
            Document document = Jsoup.connect(publicDataPortalListUrl).timeout(5000*8).get();
            Elements elements = document.select("#apiDataList .result-list ul li");

            //페이지의 끝에 도달
            if(elements.size() == 0){
                log.debug("\n===============================페이지 끝===============================\n");
                break;
            }

            //하위 페이지로 들어가 필요한 정보를 크롤링한다.
            for(Element elem : elements){
                log.debug("\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>SubPage Start>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                String subUrl = publicDataPortalBaseUrl + elem.select("dl dt a").attr("href");
                Document subDocument = Jsoup.connect(subUrl).get();

                String title = subDocument.select(".open-api-title").text();
                log.debug("상세페이지 크롤링 대상 title: [" + title + "], " + "subUrl: [" + subUrl + "]");

                //PublicDataPortal 리스트 중 상세페이지의 정보를 담는다.
                PublicDataPortalDto publicDataPortalDto = new PublicDataPortalDto();
                publicDataPortalDto.setApiName(title);
                publicDataPortalDto.setApiSiteUrl(subUrl);

                //PublicDataPortal 상세페이지에 들어있는 테이블의 정보들을 크롤링한다.(dv_public_data_portal table)
                Elements publicDataPortalInfoElemRows = subDocument.select(".file-meta-table-pc .dataset-table tbody tr");
                if(publicDataPortalInfoElemRows.size() == 0){
                    break;
                }
                crawlingPublicDataPortalInfo(publicDataPortalInfoElemRows, publicDataPortalDto);

                //서비스 URL 크롤링
                Elements serviceUrlRows = subDocument.select("#open-api-detail-result .box-gray .dot-list li");
                crawlingServiceUrl(serviceUrlRows, publicDataPortalDto);

                //테이블 형태, json 형태 구분 후 최종 dv_public_data_portal 데이터 삽입
                String specType = seperateTableJson(subDocument);
                publicDataPortalDto.setTableJsonFlag(specType);
                log.debug("**********INSERT PublicDataPortalDto Data**********\n" + publicDataPortalDto + "\n");
                publicDataPortalRepository.save(publicDataPortalDto.toEntity());

                Long publicDataPortalId = publicDataPortalRepository.selectPublicDataPortalId(publicDataPortalDto);

                //상세 페이지의 명세서가 테이블 형태로 존재하는 경우
                if(specType.equals("table")){
                    log.debug("**********Table Spec**********");

                    //서비스 라우터 URL 크롤링(dv_public_data_portal_service table)
                    Elements routerUrlRows = subDocument.select(".file-meta-table-pc #open_api_detail_select option");

                    if(routerUrlRows.size() > 0){
                        //목록 명세서들을 확인하기 위해 post로 데이터를 받아와야되는데 이때 필요한 값들을 뽑아온다.
                        String publicDataPk = subDocument.select("#publicDataPk").attr("value");
                        String publicDataDetailPk = subDocument.select("#publicDataDetailPk").attr("value");

                        if(publicDataPk.length() != 0 && publicDataDetailPk.length() != 0){
                            for(int j=0; j<routerUrlRows.size(); j++){
                                //dv_public_data_portal_service 테이블 insert
                                PublicDataPortalServiceDto publicDataPortalServiceDto = new PublicDataPortalServiceDto();
                                publicDataPortalServiceDto.setPublicDataPortalId(publicDataPortalId);
                                publicDataPortalServiceDto.setOrder(j + 1);

                                Document routeDoc =
                                        insertPublicDataPortalServiceTable(routerUrlRows.get(j), publicDataPk, publicDataDetailPk, publicDataPortalServiceDto);
                                if(routeDoc == null) {
                                    break;
                                }

                                Long publicDataPortalServiceId = publicDataPortalServiceRepository.selectPublicDataPortalId(publicDataPortalServiceDto);
                                Elements reqResElements = routeDoc.select("#open-api-detail-result .col-table");

                                //요청 파라미터 테이블
                                insertPublicDataPortalReqResTable(reqResElements.get(0), publicDataPortalServiceId, 0);
                                //응답 출력 테이블
                                insertPublicDataPortalReqResTable(reqResElements.get(1), publicDataPortalServiceId, 1);
                            }
                        }
                    }
                }
                //상세 페이지의 명세서가 JSON 형태로 존재하는 경우
                else if(specType.equals("json")){
                    log.debug("**********Json Spec**********");
                }
                //상세 페이지의 명세서가 존재하지 않는 경우
                else{
                    log.debug("**********None Spec**********");
                }
                log.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<SubPage END<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            }
            publicDataPortalListUrl = ApiUtil.getNextPageUrl(publicDataPortalListUrl, ConstantUtil.PDP_PAGE_PREFIX, pageNo + 1);
            pageNo++;
        }

        return result;
    }

    //상세페이지에 json 형태를 어떻게 제공하고 있는가에 따른 값 반환
    private String seperateTableJson(Document document) throws Exception {
        if (document.toString().contains("col-table") == true) {
            return "table";
        } else if (document.toString().contains("API 목록") == true) {
            return "json";
        } else {
            return "none";
        }
    }

    //상세페이지의 api 정보 테이블의 데이터를 담는다.
    private void crawlingPublicDataPortalInfo(Elements publicDataPortalInfoElemRows, PublicDataPortalDto publicDataPortalDto) throws Exception{
        for(Element publicDataPortalInfoElemRow : publicDataPortalInfoElemRows){
            Elements colKey = publicDataPortalInfoElemRow.select("th");
            Elements colData = publicDataPortalInfoElemRow.select("td");

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
                    publicDataPortalDto.setExecDeptTelno(telNo);
                }
                //전화번호 이외 데이터 삽입
                else{
                    String key = colKey.get(j).text();
                    String data = colData.get(j).text();
                    if(key.equals("분류체계")){
                        publicDataPortalDto.setTaxonomy(data);
                    }else if(key.equals("제공기관")){
                        publicDataPortalDto.setProvOrg(data);
                    }else if(key.equals("관리부서명")){
                        publicDataPortalDto.setExecDept(data);
                    }else if(key.equals("publicDataPortal 유형")){
                        publicDataPortalDto.setApiType(data);
                    }else if(key.equals("데이터포맷")){
                        publicDataPortalDto.setDataFormat(data);
                    }else if(key.equals("활용신청")){
                        publicDataPortalDto.setUsesAppl(data);
                    }else if(key.equals("키워드")){
                        publicDataPortalDto.setKeyword(data);
                    }else if(key.equals("등록")){
                        publicDataPortalDto.setRegiDate(data);
                    }else if(key.equals("수정")){
                        publicDataPortalDto.setEditDate(data);
                    }else if(key.equals("심의유형")){
                        publicDataPortalDto.setReviewType(data);
                    }else if(key.equals("URL")){
                        publicDataPortalDto.setUrl(data);
                    }else if(key.equals("비용부과유무")){
                        publicDataPortalDto.setChargeYn(data);
                    }else if(key.equals("신청가능 트래픽")){
                        publicDataPortalDto.setAvailTraffic(data);
                    }
                }
            }
        }
    }

    //상세페이지에 서비스 url을 뽑는다.
    private void crawlingServiceUrl(Elements serviceUrlRows, PublicDataPortalDto publicDataPortalDto) throws Exception{
        for(Element serviceUrlRow : serviceUrlRows){
            if(serviceUrlRow.toString().contains("서비스URL")){
                String data = serviceUrlRow.text();
                int urlStrtPos = data.indexOf("http");

                if(urlStrtPos == -1){
                    break;
                }

                data = data.substring(urlStrtPos);
                publicDataPortalDto.setApiServiceUrl(data);
            }
        }
    }

    //PublicDataPortalService 테이블의 데이터를 채운다.
    private Document insertPublicDataPortalServiceTable(Element routeUrlRow, String publicDataPk, String publicDataDetailPk, PublicDataPortalServiceDto publicDataPortalServiceDto) throws IOException {
        String oprtinSeqNo = routeUrlRow.attr("value");
        String description = routeUrlRow.text();
        publicDataPortalServiceDto.setDescription(description);

        //jsoup post로 데이터를 뽑아온다.
        Map<String, String> header = new HashMap<>();
        header.put("Accept-Encoding", "gzip, deflate, br");
        header.put("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        Map<String, String> postBody = new HashMap<>();
        postBody.put("oprtinSeqNo", oprtinSeqNo);
        postBody.put("publicDataDetailPk", publicDataDetailPk);
        postBody.put("publicDataPk", publicDataPk);

        Document routeDoc = Jsoup.connect(PDPAPI_DETAIL_ROUTE_URL).headers(header).data(postBody).post();
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

        publicDataPortalServiceDto.setRoute(route);

        log.debug("**********INSERT PublicDataPortalServiceDto Data**********\n" + publicDataPortalServiceDto + "\n");
        publicDataPortalServiceRepository.save(publicDataPortalServiceDto.toEntity());

        return routeDoc;
    }

    //PublicDataPortal Req, Res 데이터를 담는다.
    private void insertPublicDataPortalReqResTable(Element elem, Long publicDataPortalServiceId, int reqResFlag) throws Exception{
        Elements rows = elem.select("tbody tr");

        for(Element row : rows) {
            Map<String, String> publicDataPortalReqResDto = new HashMap<>();
            Elements col = row.select("td");

            for (int t = 0; t < col.size(); t++) {
                String data = col.select("td").get(t).text();
                switch (t) {
                    case 0:
                        publicDataPortalReqResDto.put("columnNmKr", data);
                        break;
                    case 1:
                        publicDataPortalReqResDto.put("columnNmEn", data);
                        break;
                    case 2:
                        publicDataPortalReqResDto.put("columnSize", data);
                        break;
                    case 3:
                        publicDataPortalReqResDto.put("columnDiv", data);
                        break;
                    case 4:
                        publicDataPortalReqResDto.put("sampleData", data);
                        break;
                    case 5:
                        publicDataPortalReqResDto.put("columnDesc", data);
                        break;
                }
            }

            if (reqResFlag == 0) {
                PublicDataPortalReqDto publicDataPortalReqDto = new PublicDataPortalReqDto();
                publicDataPortalReqDto.setColumnNmKr(publicDataPortalReqResDto.get("columnNmKr"));
                publicDataPortalReqDto.setColumnNmEn(publicDataPortalReqResDto.get("columnNmEn"));
                publicDataPortalReqDto.setColumnSize(publicDataPortalReqResDto.get("columnSize"));
                publicDataPortalReqDto.setColumnDiv(publicDataPortalReqResDto.get("columnDiv"));
                publicDataPortalReqDto.setSampleData(publicDataPortalReqResDto.get("sampleData"));
                publicDataPortalReqDto.setColumnDesc(publicDataPortalReqResDto.get("columnDesc"));

                publicDataPortalReqDto.setPublicDataPortalServiceId(publicDataPortalServiceId);

                log.debug("**********INSERT PublicDataPortalReqDto Data**********\n" + publicDataPortalReqDto + "\n");
                publicDataPortalReqRepository.save(publicDataPortalReqDto.toEntity());
            } else if (reqResFlag == 1) {
                PublicDataPortalResDto publicDataPortalResDto = new PublicDataPortalResDto();
                publicDataPortalResDto.setColumnNmKr(publicDataPortalReqResDto.get("columnNmKr"));
                publicDataPortalResDto.setColumnNmEn(publicDataPortalReqResDto.get("columnNmEn"));
                publicDataPortalResDto.setColumnSize(publicDataPortalReqResDto.get("columnSize"));
                publicDataPortalResDto.setColumnDiv(publicDataPortalReqResDto.get("columnDiv"));
                publicDataPortalResDto.setSampleData(publicDataPortalReqResDto.get("sampleData"));
                publicDataPortalResDto.setColumnDesc(publicDataPortalReqResDto.get("columnDesc"));

                publicDataPortalResDto.setPublicDataPortalServiceId(publicDataPortalServiceId);

                log.debug("**********INSERT PublicDataPortalResDto Data**********\n" + publicDataPortalResDto + "\n");
                publicDataPortalResRepository.save(publicDataPortalResDto.toEntity());
            }
        }
    }

    //PublicDataPortal의 리스트들을 담아 보낸다.
    public Map<String, Object> getPublicDataPortalList(int listCnt) {
        Map<String, Object> result = new HashMap<>();
        List<PublicDataPortalDto> publicDataPortalDtoList = new ArrayList<>();

        for (int i = 1; i <= listCnt; i++) {
            PublicDataPortalEntity publicDataPortalEntity = publicDataPortalRepository.findByPublicDataPortalIdAndTableJsonFlag(Long.valueOf(i), "table");
            if(publicDataPortalEntity == null){
                continue;
            }
            PublicDataPortalDto publicDataPortalDto = publicDataPortalEntity.toDto();
            log.debug(publicDataPortalDto);
            if(publicDataPortalDto.getApiName().contains("대구"))
                publicDataPortalDtoList.add(publicDataPortalDto);
        }

        result.put("success", true);
        result.put("message", "Get PublicDataPortal List Success");
        result.put("data", publicDataPortalDtoList);
        return result;
    }

    //사용자가 선택한 publicDataPortal서비스 list들을 넘긴다.
    public Map<String, Object> getPublicDataPortalServiceList(PublicDataPortalDto publicDataPortalDto){
        Map<String, Object> result = new HashMap<>();
        List<PublicDataPortalServiceEntity> publicDataPortalServiceEntities = publicDataPortalServiceRepository.findAllByPublicDataPortalId(publicDataPortalDto.getPublicDataPortalId());

        List<PublicDataPortalServiceDto> publicDataPortalServiceDtos = ConvertEntityDtoListUtil.toListPublicDataPortalServiceDto(publicDataPortalServiceEntities);
        log.debug(publicDataPortalServiceDtos);

        result.put("success", true);
        result.put("message", "Get PublicDataPortal Service List Success");
        result.put("data", publicDataPortalServiceDtos);

        return result;
    }

    //사용자가 선택한 publicDataPortal서비스의 req, res칼럼들을 넘긴다.
    public Map<String, Object> getPublicDataPortalReqResList(PublicDataPortalServiceDto publicDataPortalServiceDto){
        Map<String, Object> result = new HashMap<>();
        List<PublicDataPortalReqEntity> publicDataPortalReqEntities = publicDataPortalReqRepository.findAllByPublicDataPortalServiceId(publicDataPortalServiceDto.getPublicDataPortalServiceId());
        List<PublicDataPortalResEntity> publicDataPortalResEntities = publicDataPortalResRepository.findAllByPublicDataPortalServiceId(publicDataPortalServiceDto.getPublicDataPortalServiceId());

        List<PublicDataPortalReqDto> publicDataPortalReqDtos = ConvertEntityDtoListUtil.toListPublicDataPortalReqDto(publicDataPortalReqEntities);
        List<PublicDataPortalResDto> publicDataPortalResDtos = ConvertEntityDtoListUtil.toListPublicDataPortalResDto(publicDataPortalResEntities);

        log.debug(publicDataPortalReqDtos);
        log.debug(publicDataPortalResDtos);

        result.put("success", true);
        result.put("message", "Get PublicDataPortal Service List Success");
        result.put("reqData", publicDataPortalReqDtos);
        result.put("resData", publicDataPortalResDtos);
        return result;
    }
}
