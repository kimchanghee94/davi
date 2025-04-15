package com.davi_crawler.disaster.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.common.util.GeoToolsUtil;
import com.davi_crawler.disaster.dto.ChimsuDto;
import com.davi_crawler.disaster.entity.ChimsuEntity;
import com.davi_crawler.disaster.repository.ChimsuRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class ChimsuService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SequenceService sequenceService;
    private final ChimsuRepository chimsuRepository;
    private ObjectMapper objectMapper = new ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        Reader reader = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_CHIMSU_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_CHIMSU_ID).getTableNm());

            //geojson 파일 만들기
            GeoToolsUtil.convertShpToGeoJson(ConstantUtil.CRAWLING_FILE_PATH + "disaster/chimsu-shp", "chimsu.shp");

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "disaster/chimsu-shp";
            String fileName = "chimsu.geojson";

            reader = new FileReader(new File(filePath, fileName));
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(reader);

            JSONArray features = (JSONArray) root.get("features");

            List<ChimsuDto> chimsuDtos = new ArrayList<>();

            for(int i=0; i<features.size(); i++){
                JSONObject feature = (JSONObject) features.get(i);

                JSONObject properties = (JSONObject) feature.get("properties");
                JsonNode jsonNode = objectMapper.readTree(properties.toJSONString());
                ChimsuDto chimsuDto = objectMapper.treeToValue(jsonNode, ChimsuDto.class);

                chimsuDto.setDataListId(ConstantUtil.DATA_LIST_CHIMSU_ID);
                chimsuDto.setJsonData(feature.toJSONString());

                log.debug(chimsuDto);
                chimsuDtos.add(chimsuDto);
            }

            insertAll(chimsuDtos);

            result.put("success", true);
            result.put("message", "침수 구간 정보 업데이트 성공");
        }finally {
            try{
                if(reader != null){
                    reader.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        return result;
    }

    public void insertAll(List<ChimsuDto> chimsuDtos) throws Exception{
        List<ChimsuEntity> chimsuEntities = ConvertEntityDtoListUtil.toListChimsuEntity(chimsuDtos);
        chimsuRepository.saveAll(chimsuEntities);
    }

    public void deleteAll() throws Exception{
        chimsuRepository.deleteAll();;
        sequenceService.initSeqNumZero(ConstantUtil.CHIMSU_SEQ_NAME);
    }
}