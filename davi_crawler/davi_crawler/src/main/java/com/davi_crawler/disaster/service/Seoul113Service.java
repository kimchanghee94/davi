package com.davi_crawler.disaster.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.disaster.dto.Seoul113Dto;
import com.davi_crawler.disaster.entity.Seoul113Entity;
import com.davi_crawler.disaster.repository.Seoul113Repository;
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
public class Seoul113Service {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SequenceService sequenceService;
    private final Seoul113Repository seoul113Repository;


    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        Reader reader = null;

        try{
            deleteAll();
            dataColListService.updateColData(ConstantUtil.DATA_LIST_SEOUL113_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_SEOUL113_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "disaster";
            String fileName = "seoul113.geojson";

            reader = new FileReader(new File(filePath, fileName));

            JSONParser parser = new JSONParser();
            JSONObject root =  (JSONObject) parser.parse(reader);

            JSONArray array = (JSONArray)root.get("features");
            List<Seoul113Dto> seoul113Dtos = new ArrayList<>();

            for(int i=0; i<array.size(); i++){
                JSONObject tmpObj = (JSONObject)array.get(i);
                JSONObject properties = (JSONObject) tmpObj.get("properties");

                String category = (String) properties.get("CATEGORY");
                String areaCd = (String) properties.get("AREA_CD");
                String areaNm = (String) properties.get("AREA_NM");
                String jsonData = tmpObj.toJSONString();

                Seoul113Dto seoul113Dto = new Seoul113Dto(
                        null, ConstantUtil.DATA_LIST_SEOUL113_ID,
                        category, areaCd, areaNm, jsonData);

                log.debug(seoul113Dto);
                seoul113Dtos.add(seoul113Dto);
            }

            insertAll(seoul113Dtos);

            result.put("success", true);
            result.put("message", "서울113 정보 업데이트 성공");
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

    public void insertAll(List<Seoul113Dto> seoul113Dtos) throws Exception{
        List<Seoul113Entity> seoul113Entities = ConvertEntityDtoListUtil.toListSeoul113Entity(seoul113Dtos);
        seoul113Repository.saveAll(seoul113Entities);
    }

    public void deleteAll() throws Exception{
        seoul113Repository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.SEOUL113_SEQ_NAME);
    }

}
