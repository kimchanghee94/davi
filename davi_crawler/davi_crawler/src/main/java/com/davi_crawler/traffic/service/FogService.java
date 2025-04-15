package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.FogDto;
import com.davi_crawler.traffic.entity.FogEntity;
import com.davi_crawler.traffic.repository.FogRepository;
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
public class FogService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final FogRepository fogRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_FOG_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_FOG_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "traffic";
            String fileName = "fog.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> csvDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String lineArr[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> aLine = Arrays.asList(lineArr);
                csvDatas.add(aLine);
            }

            List<FogDto> fogDtos = new ArrayList<>();

            for(int i=1; i<csvDatas.size(); i++){
                List<String> csvData = new ArrayList<>(csvDatas.get(i));

                //칼럼 데이터가 개수를 만족하지 않는 로우일 경우
                if(csvData.size() != 24){
                    int tmpCsvDataSize = 24 - csvData.size();

                    for(int j=0; j<tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                FogDto fogDto = new FogDto(null, ConstantUtil.DATA_LIST_FOG_ID,
                        csvData.get(0), csvData.get(1),csvData.get(2), csvData.get(3),
                        csvData.get(4).replaceAll("\"", ""), csvData.get(5),csvData.get(6), csvData.get(7),
                        csvData.get(8), csvData.get(9),csvData.get(10), csvData.get(11),
                        csvData.get(12), csvData.get(13),csvData.get(14), csvData.get(15),
                        csvData.get(16), csvData.get(17),csvData.get(18), csvData.get(19),
                        csvData.get(20), csvData.get(21),csvData.get(22), csvData.get(23));

                log.debug(fogDto);
                fogDtos.add(fogDto);
            }

            insertAll(fogDtos);

            result.put("success", true);
            result.put("message", "안개 정보 업데이트 성공");
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


    public void insert(FogDto fogDto) throws Exception{
        FogEntity fogEntity = fogDto.toEntity();
        fogRepository.save(fogEntity);
    }

    public void insertAll(List<FogDto> fogDtos) throws Exception{
        List<FogEntity> fogEntities = ConvertEntityDtoListUtil.toListFogEntity(fogDtos);
        fogRepository.saveAll(fogEntities);
    }

    public void deleteAll() throws Exception {
        fogRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.FOG_SEQ_NAME);

    }
}
