package com.davi_crawler.khoa.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.khoa.dto.HangDto;
import com.davi_crawler.khoa.entity.HangEntity;
import com.davi_crawler.khoa.repository.HangRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class HangService {
    private final HangRepository hangRepository;
    private final SequenceService sequenceService;
    private final DataColListService dataColListService;
    private final DataListService dataListService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_HANG_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_HANG_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "khoa";
            String fileName = "항위치(한글).csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> hangDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String[] lineArr = line.split(",");
                List<String> aLine = Arrays.asList(lineArr);
                hangDatas.add(aLine);
            }

            List<HangDto> hangDtos = new ArrayList<>();

            for(int i=2; i < hangDatas.size(); i++){
                List<String> hangData = hangDatas.get(i);
                String hangName = hangData.get(0);
                String x = hangData.get(1);
                String y = hangData.get(2);
                String hangSubName = hangData.get(3);

                HangDto hangDto = setHangDto(hangName, x, y, hangSubName);
                hangDtos.add(hangDto);
                log.debug(hangDto);
            }

            insertAll(hangDtos);

            result.put("success", true);
            result.put("message", "항 업데이트 성공");
        }finally {
            try{
                if(br != null){
                    br.close();
                }
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        return result;
    }

    private HangDto setHangDto(String hangName, String x, String y, String hangSubName) throws Exception{
        HangDto hangDto = new HangDto();
        hangDto.setDataListId(ConstantUtil.DATA_LIST_HANG_ID);
        hangDto.setHangName(hangName);
        hangDto.setHangSubName(hangSubName);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        hangDto.setLngLat(jsonObject.toJSONString());

        return hangDto;
    }

    public void insert(HangDto hangDto) throws Exception{
        HangEntity hangEntity = hangDto.toEntity();
        hangRepository.save(hangEntity);
    }

    public void insertAll(List<HangDto> hangDtos) throws Exception{
        List<HangEntity> hangEntities = ConvertEntityDtoListUtil.toListHangEntity(hangDtos);
        hangRepository.saveAll(hangEntities);
    }

    public void deleteAll() throws Exception{
        hangRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.HANG_SEQ_NAME);
    }
}
