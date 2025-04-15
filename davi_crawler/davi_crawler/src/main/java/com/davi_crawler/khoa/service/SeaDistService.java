package com.davi_crawler.khoa.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.khoa.dto.SeaDistDto;
import com.davi_crawler.khoa.entity.SeaDistEntity;
import com.davi_crawler.khoa.repository.SeaDistRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class SeaDistService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SeaDistRepository seaDistRepository;
    private final SequenceService sequenceService;

    //해상 사이트 항로선택시 출발항 데이터 받아오는 url
    private final String SEADIST_DOM_STR_NM_URL = "https://www.khoa.go.kr/kcom/cnt/getStrHbrList.do";
    //해상 사이트 출발항선택시 도착항 데이터 받아오는 url
    private final String SEADIST_DOM_END_NM_URL = "https://www.khoa.go.kr/kcom/cnt/getAjaxEndHbrList.do";
    //해상 사이트 해상거리 최종 조회할 때 사용되는 url
    private final String SEADIST_PORT_DIST_DOM_SEARCH_URL = "https://www.khoa.go.kr/kcom/cnt/getAjaxHbrDistList.do";

    private ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_SEADIST_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_SEADIST_ID).getTableNm());

        //해상거리 url
        String seaDistBaseUrl = dataListService.selectById(ConstantUtil.DATA_LIST_SEADIST_ID).getUrl();

        log.debug("===============================Crawling Start===============================\n");
        //사이트 소스 가져오기
        Document document = Jsoup.connect(seaDistBaseUrl).get();

        //항로선택 항목 value값들 담기
        Map<String,Object> domStrType = getDomStrType(document);

        //항로선택의 타입에 따라 출발항의 목록들이 달라지므로 각각에 대한 데이터를 넣도록 한다
        Map<String, Object> domStr = getDomStr((List<String>) domStrType.get("values"));

        //출발항선택에 따라 도착항의 목록들이 달라지므로 각각에 대한 데이터를 넣도록 한다.
        Map<String, Object> domEnd = getDomEnd((List<String>) domStrType.get("values"), (List<List<String>>) domStr.get("values"));

        //최종 데이터 뽑기
        List<SeaDistDto> seaDistDtos = getDistInfo(domStrType, domStr, domEnd);

        //최종 데이터 DB에 삽입
        insertAll(seaDistDtos);

        log.debug("===============================Crawling End===============================\n");

        result.put("success", true);
        result.put("message", "해양 거리 업데이트 성공");

        return result;
    }

    //항로선택에 대한 값들 가져오기
    private Map<String, Object> getDomStrType(Document document) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<String> domStrTypeNms = new ArrayList<>();
        List<String> domStrTypeValues = new ArrayList<>();

        //option 항목들 중에서 value 속성에 대한 값 추출하기
        Elements domStrTypeElems = document.select("#domStrType > option");
        for(Element elem : domStrTypeElems){
            String tmpNm = elem.text();
            String tmpVal = elem.attr("value");
            domStrTypeNms.add(tmpNm);
            domStrTypeValues.add(tmpVal);
        }

        result.put("nms", domStrTypeNms);
        result.put("values", domStrTypeValues);

        return result;
    }

    //항로선택에 대한 각각의 값에 대해서 출발항 데이터들을 가져온다.
    private Map<String, Object> getDomStr(List<String> domStrTypeValues) throws Exception{
        Map<String, Object> result = new HashMap<>();

        List<List<String>> domStrNms = new ArrayList<>();
        List<List<String>> domStrValues = new ArrayList<>();

        for(int i = 0; i < domStrTypeValues.size(); i++){
            //post를 통해 각 항로 선택에 대한 출발항 데이터들을 받아온다.
            MultiValueMap<String, String> postBody = new LinkedMultiValueMap<>();
            postBody.add("hbr_type", domStrTypeValues.get(i));
            postBody.add("domst", "Y");

            Map<String, Object> resp = ApiUtil.postApiData(SEADIST_DOM_STR_NM_URL, null, postBody);

            if((Boolean)resp.get("success") == false){
                continue;
            }

            //json 형태로 받아온 데이터 파싱 시작
            String data = (String)resp.get("data");
            JSONParser parser = new JSONParser();
            JSONArray root = (JSONArray) parser.parse(data);

            List<String> tmpDomStrNms = new ArrayList<>();
            List<String> tmpDomStrValues = new ArrayList<>();

            for(int j = 0; j < root.size(); j++){
                JSONObject jsonObject = (JSONObject) root.get(j);
                String tmpNm = (String)jsonObject.get("hbr_nm");
                String tmpVal = (String)jsonObject.get("hbr_seq");

                tmpDomStrNms.add(tmpNm);
                tmpDomStrValues.add(tmpVal);
            }

            domStrNms.add(tmpDomStrNms);
            domStrValues.add(tmpDomStrValues);
        }

        result.put("nms", domStrNms);
        result.put("values", domStrValues);

        return result;
    }

    //출발항선택에 대한 각각의 값에 대해서 도착항 데이터들을 가져온다.
    private Map<String, Object> getDomEnd(List<String> domStrTypeValues, List<List<String>> domStrValues) throws Exception{
        Map<String, Object> result = new HashMap<>();
        List<List<List<String>>> domEndNms = new ArrayList<>();
        List<List<List<String>>> domEndValues = new ArrayList<>();

        for(int i = 0; i < domStrValues.size(); i++){
            List<String> tmpDomStrValues = domStrValues.get(i);
            List<List<String>> domEndNms2 = new ArrayList<>();
            List<List<String>> domEndValues2 = new ArrayList<>();

            for(int j=0; j < tmpDomStrValues.size(); j++){
                //post를 통해 각 출발항 선택에 대한 도착항들의 데이터들을 받아온다.
                MultiValueMap<String, String> postBody = new LinkedMultiValueMap<>();
                postBody.add("hbrseq", tmpDomStrValues.get(j));
                postBody.add("hbrtype", String.valueOf(domStrTypeValues.get(i)));
                postBody.add("domst", "Y");

                Map<String, Object> resp = ApiUtil.postApiData(SEADIST_DOM_END_NM_URL, null, postBody);

                if((Boolean)resp.get("success") == false){
                    continue;
                }

                //json 형태로 받아온 데이터 파싱 시작
                String data = (String)resp.get("data");
                JSONParser parser = new JSONParser();
                JSONArray root = (JSONArray) parser.parse(data);

                List<String> domEndNms3 = new ArrayList<>();
                List<String> domEndValues3 = new ArrayList<>();

                for(int k=0; k<root.size(); k++){
                    JSONObject jsonObject = (JSONObject) root.get(k);
                    String tmpNm = (String) jsonObject.get("hbr_nm");
                    String tmpVal = (String) jsonObject.get("hbr_seq");

                    domEndNms3.add(tmpNm);
                    domEndValues3.add(tmpVal);
                }

                domEndNms2.add(domEndNms3);
                domEndValues2.add(domEndValues3);
            }
            domEndNms.add(domEndNms2);
            domEndValues.add(domEndValues2);
        }

        result.put("nms", domEndNms);
        result.put("values", domEndValues);

        return result;
    }

    //최종 데이터를 불러온다.
    private List<SeaDistDto> getDistInfo(Map<String, Object> domStrType,
                                         Map<String, Object> domStr,
                                         Map<String, Object> domEnd) throws Exception {
        List<SeaDistDto> seaDistDtos = new ArrayList<>();

        List<String> domStrTypeNms = (List<String>) domStrType.get("nms");
        List<String> domStrTypeValues = (List<String>) domStrType.get("values");
        List<List<String>> domStrNms = (List<List<String>>) domStr.get("nms");
        List<List<String>> domStrValues = (List<List<String>>) domStr.get("values");
        List<List<List<String>>> domEndNms = (List<List<List<String>>>) domEnd.get("nms");
        List<List<List<String>>> domEndValues = (List<List<List<String>>>) domEnd.get("values");


        for(int ti=0; ti < domStrTypeValues.size(); ti++) {
            String domStrTypeNm = domStrTypeNms.get(ti);
            String domStrTypeValue = domStrTypeValues.get(ti);

            List<String> domStrNms2 = domStrNms.get(ti);
            List<String> domStrValues2 = domStrValues.get(ti);

            List<List<String>> domEndNms2 = domEndNms.get(ti);
            List<List<String>> domEndValues2 = domEndValues.get(ti);

            for(int si=0; si < domStrValues2.size(); si++){
                String domStrNm = domStrNms2.get(si);
                String domStrValue = domStrValues2.get(si);

                List<String> domEndNms3 = domEndNms2.get(si);
                List<String> domEndValues3 = domEndValues2.get(si);

                for(int ei=0; ei < domEndValues3.size(); ei++){
                    String domEndNm = domEndNms3.get(ei);
                    String domEndValue = domEndValues3.get(ei);

                    //최종 선택된 데이터들 처리
                    MultiValueMap<String, String> postBody = new LinkedMultiValueMap<>();
                    postBody.add("hbrtype", domStrTypeValue);
                    postBody.add("strhbrseq", domStrValue);
                    postBody.add("endhbrseq", domEndValue);
                    postBody.add("domst", "Y");

                    Map<String, Object> resp = ApiUtil.postApiData(SEADIST_PORT_DIST_DOM_SEARCH_URL, null, postBody);

                    if((Boolean)resp.get("success") == false){
                        continue;
                    }

                    //json 형태로 받아온 데이터 파싱 시작
                    String data = (String)resp.get("data");
                    JSONParser parser = new JSONParser();
                    JSONArray jsonArray = null;

                    //parser 및 json배열 형태가 아닌 것에 대해서는 예외처리 이 후 다음 데이터로 계속 진행
                    try{
                        JSONObject root = (JSONObject) parser.parse(data);
                        jsonArray = (JSONArray) root.get("RESULT_DATA");
                    }catch (Exception e){
                        log.error("현 데이터는 무시하고 다음 데이터로 진행 " + data);
                        continue;
                    }

                    for(int i=0; i < jsonArray.size(); i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        SeaDistDto seaDistDto = setSeaDistDto(domStrTypeNm, domStrNm, domEndNm, jsonObject, i + 1);
                        seaDistDtos.add(seaDistDto);

                        log.debug(seaDistDto);
                    }
                }

            }

        }

        return seaDistDtos;
    }

    //SeaDistDto 데이터 조합
    private SeaDistDto setSeaDistDto(String domStrTypeNm, String domStrNm, String domEndNm, JSONObject jsonObject, int order) throws Exception{
        JsonNode jsonNode = objectMapper.readTree(jsonObject.toJSONString());
        SeaDistDto seaDistDto = objectMapper.treeToValue(jsonNode, SeaDistDto.class);

        seaDistDto.setDataListId(ConstantUtil.DATA_LIST_SEADIST_ID);
        seaDistDto.setDomStrTypeNm(domStrTypeNm);
        seaDistDto.setDomStrNm(domStrNm);
        seaDistDto.setDomEndNm(domEndNm);
        seaDistDto.setOrder(order);

        return seaDistDto;
    }

    public void insert(SeaDistDto seaDistDto) throws Exception{
        SeaDistEntity seaDistEntity = seaDistDto.toEntity();
        seaDistRepository.save(seaDistEntity);
    }

    public void insertAll(List<SeaDistDto> seaDistDtos) throws Exception{
        List<SeaDistEntity> seaDistEntities = ConvertEntityDtoListUtil.toListSeaDistEntity(seaDistDtos);
        seaDistRepository.saveAll(seaDistEntities);
    }

    public void deleteAll() throws Exception{
        seaDistRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.SEADIST_SEQ_NAME);
    }
}