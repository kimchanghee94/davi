package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.FrostDto;
import com.davi_crawler.traffic.entity.FrostEntity;
import com.davi_crawler.traffic.repository.FrostRepository;
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
public class FrostService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final FrostRepository frostRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})

    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_FROST_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_FROST_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "traffic";
            String fileName = "frost.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> csvDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String lineArr[] = line.split(",");
                List<String> aLine = Arrays.asList(lineArr);
                csvDatas.add(aLine);
            }

            List<FrostDto> frostDtos = new ArrayList<>();

            for(int i=1; i<csvDatas.size(); i++){
                List<String> csvData = new ArrayList<>(csvDatas.get(i));

                //칼럼 데이터가 개수를 만족하지 않는 로우일 경우
                if(csvData.size() != 13){
                    int tmpCsvDataSize = 13 - csvData.size();

                    for(int j=0; j<tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                FrostDto frostDto = new FrostDto(null, ConstantUtil.DATA_LIST_FROST_ID,
                        csvData.get(0), csvData.get(1),csvData.get(2), csvData.get(3),
                        csvData.get(4), csvData.get(5),csvData.get(6), csvData.get(7),
                        csvData.get(8), csvData.get(9),csvData.get(10), csvData.get(11),
                        csvData.get(12));

                log.debug(frostDto);
                frostDtos.add(frostDto);
            }

            insertAll(frostDtos);

            result.put("success", true);
            result.put("message", "결빙 정보 업데이트 성공");
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


    public void insert(FrostDto frostDto) throws Exception{
        FrostEntity frostEntity = frostDto.toEntity();
        frostRepository.save(frostEntity);
    }

    public void insertAll(List<FrostDto> frostDtos) throws Exception{
        List<FrostEntity> frostEntities = ConvertEntityDtoListUtil.toListFrostEntity(frostDtos);
        frostRepository.saveAll(frostEntities);
    }

    public void deleteAll() throws Exception{
        frostRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.CCTV_SEQ_NAME);
    }
}
