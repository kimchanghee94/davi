package com.davi_crawler.common.service;

import com.davi_crawler.common.dto.GridDto;
import com.davi_crawler.common.entity.GridEntity;
import com.davi_crawler.common.repository.GridRepository;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
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
public class GridService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final SequenceService sequenceService;
    private final GridRepository gridRepository;


    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        Reader reader = null;

        try{
            deleteAll();
            dataColListService.updateColData(ConstantUtil.DATA_LIST_GRID_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_GRID_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "common";
            String fileName = "grid.geojson";

            reader = new FileReader(new File(filePath, fileName));

            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(reader);

            JSONArray array = (JSONArray) root.get("features");
            List<GridDto> gridDtos = new ArrayList<>();

            for(int i=0; i<array.size(); i++){
                JSONObject tmpObj = (JSONObject) array.get(i);

                JSONObject geometery = (JSONObject) tmpObj.get("geometry");
                JSONArray coord = (JSONArray) geometery.get("coordinates");

                //3중 배열 처리
                coord = (JSONArray) coord.get(0);
                coord = (JSONArray) coord.get(0);

                JSONArray firstCoord = (JSONArray) coord.get(0);
                JSONArray secondCoord = (JSONArray) coord.get(1);
                JSONArray thirdCoord = (JSONArray) coord.get(2);

                Double firLng = (Double)firstCoord.get(0);
                Double secLng = (Double)secondCoord.get(0);
                Double firLat = (Double)firstCoord.get(1);
                Double secLat = (Double)thirdCoord.get(1);

                //6번째 자리에서 반올림
                Double centerLng = Math.round(((firLng + secLng) / 2) * 1000000) / 1000000.0;
                Double centerLat = Math.round(((firLat + secLat) / 2) * 1000000) / 1000000.0;


                String gridLevel = "1";
                String strCenterLat = String.valueOf(centerLat);
                String strCenterLng = String.valueOf(centerLng);

                JSONObject properties = new JSONObject();
                properties.put("gridLevel", gridLevel);
                properties.put("centerLat", strCenterLat);
                properties.put("centerLng", strCenterLng);

                tmpObj.put("properties", properties);

                GridDto gridDto = new GridDto(
                        null, ConstantUtil.DATA_LIST_CIVILDEFENSE_ID,
                        gridLevel, strCenterLat, strCenterLng, tmpObj.toJSONString());

                log.debug(gridDto);
                gridDtos.add(gridDto);
            }

            insertAll(gridDtos);

            result.put("success", true);
            result.put("message", "그리드 정보 업데이트 성공");
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

    public void insertAll(List<GridDto> gridDtos) throws Exception{
        List<GridEntity> gridEntities = ConvertEntityDtoListUtil.toListGridEntity(gridDtos);
        gridRepository.saveAll(gridEntities);
    }

    public void deleteAll() throws Exception{
        gridRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.GRID_SEQ_NAME);
    }
}
