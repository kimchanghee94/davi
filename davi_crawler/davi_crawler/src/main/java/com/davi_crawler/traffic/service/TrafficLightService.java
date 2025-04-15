package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ApiUtil;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.TrafficLightDto;
import com.davi_crawler.traffic.entity.TrafficLightEntity;
import com.davi_crawler.traffic.repository.TrafficLightRepository;
import com.davi_crawler.traffic.util.TrafficUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class TrafficLightService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final TrafficLightRepository trafficLightRepository;
    private final SequenceService sequenceService;
    private final ObjectMapper objectMapper;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();

        deleteAll();

        dataColListService.updateColData(ConstantUtil.DATA_LIST_TRAFFICLIGHT_ID,
                dataListService.selectById(ConstantUtil.DATA_LIST_TRAFFICLIGHT_ID).getTableNm());

        //신호등 url
        String url = dataListService.selectById(ConstantUtil.DATA_LIST_TRAFFICLIGHT_ID).getUrl();
        int pageNo = 1;

        Map<String, String> params = TrafficUtil.getTrafficParams();

        List<TrafficLightDto> trafficLightDtos = new ArrayList<>();

        while(true){
            Map<String, Object> apiData = ApiUtil.getApiData(url,null,params);

            if((Boolean) apiData.get("success") == false){
                throw new Exception((String) apiData.get("message"));
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject root = (JSONObject) jsonParser.parse((String) apiData.get("data"));

            JSONObject header = (JSONObject) ((JSONObject) root.get("response")).get("header");

            if(!(header.get("resultCode").equals("00"))){
                break;
            }

            JSONObject body = (JSONObject) ((JSONObject) root.get("response")).get("body");
            JSONArray items = (JSONArray) body.get("items");

            for(Object item : items){
                JSONObject jsonObject = (JSONObject) item;
                JsonNode jsonNode = objectMapper.readTree(jsonObject.toJSONString());
                TrafficLightDto trafficLightDto = objectMapper.treeToValue(jsonNode, TrafficLightDto.class);
                trafficLightDto.setDataListId(ConstantUtil.DATA_LIST_TRAFFICLIGHT_ID);

                trafficLightDtos.add(trafficLightDto);
                log.debug(trafficLightDto);
            }

            pageNo++;
            params.put("pageNo", String.valueOf(pageNo));
        }

        insertAll(trafficLightDtos);

        result.put("success", true);
        result.put("message", "신호등 정보 업데이트 성공");

        return result;
    }

    public void insert(TrafficLightDto trafficLightDto) throws Exception{
        TrafficLightEntity trafficLightEntity = trafficLightDto.toEntity();
        trafficLightRepository.save(trafficLightEntity);
    }

    public void insertAll(List<TrafficLightDto> trafficLightDtos) throws Exception{
        List<TrafficLightEntity> trafficLightEntities = ConvertEntityDtoListUtil.toListTrafficLightEntity(trafficLightDtos);
        trafficLightRepository.saveAll(trafficLightEntities);
    }

    public void deleteAll() throws Exception{
        trafficLightRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.CHILDZONE_SEQ_NAME);
    }
}
