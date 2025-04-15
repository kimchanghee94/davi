package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.RseDto;
import com.davi_crawler.traffic.entity.RseEntity;
import com.davi_crawler.traffic.repository.RseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class RseService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final RseRepository rseRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_RSE_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_RSE_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "traffic";
            String fileName = "rse-pos.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> csvDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String lineArr[] = line.split(",");
                List<String> aLine = Arrays.asList(lineArr);
                csvDatas.add(aLine);
            }

            List<RseDto> rseDtos = new ArrayList<>();

            for(int i=1; i<csvDatas.size(); i++){
                List<String> csvData = new ArrayList<>(csvDatas.get(i));

                //칼럼 데이터가 개수를 만족하지 않는 로우일경우
                if(csvData.size() != 4){
                    int tmpCsvDataSize = 4 - csvData.size();

                    for(int j=0; j< tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                RseDto rseDto = new RseDto(null, ConstantUtil.DATA_LIST_RSE_ID
                        , csvData.get(0), csvData.get(1)
                        , csvData.get(2), csvData.get(3));

                log.debug(rseDto);
                rseDtos.add(rseDto);
            }

            insertAll(rseDtos);

            result.put("success", true);
            result.put("message", "RSE 정보 업데이트 성공");
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

    public void insert(RseDto rseDto) throws Exception{
        RseEntity speedDumpEntity = rseDto.toEntity();
        rseRepository.save(speedDumpEntity);
    }

    public void insertAll(List<RseDto> rseDtos) throws Exception{
        List<RseEntity> rseEntities = ConvertEntityDtoListUtil.toListRseEntity(rseDtos);
        rseRepository.saveAll(rseEntities);
    }

    public void deleteAll() throws Exception{
        rseRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.RSE_SEQ_NAME);
    }
}
