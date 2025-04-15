package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.BusRouteLinkDto;
import com.davi_crawler.traffic.entity.BusRouteLinkEntity;
import com.davi_crawler.traffic.repository.BusRouteLinkRepository;
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
public class BusRouteLinkService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final BusRouteLinkRepository busRouteLinkRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_BUSROUTELINK_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_BUSROUTELINK_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "traffic";
            String fileName = "busRouteLink.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> csvDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String lineArr[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> aLine = Arrays.asList(lineArr);
                csvDatas.add(aLine);
            }

            List<BusRouteLinkDto> busRouteLinkDtos = new ArrayList<>();

            for(int i=1; i < csvDatas.size(); i++){
                List<String> csvData = new ArrayList<>(csvDatas.get(i));

                //칼럼 데이터가 개수를 만족하지 않는 로우일 경우
                if(csvData.size() != 8){
                    int tmpCsvDataSize = 8 - csvData.size();

                    for(int j=0; j<tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                BusRouteLinkDto busRouteLinkDto = new BusRouteLinkDto(
                        null,
                        ConstantUtil.DATA_LIST_BUSROUTELINK_ID,
                        csvData.get(0),
                        csvData.get(1),
                        csvData.get(2)
                );

                log.debug(busRouteLinkDto);

                busRouteLinkDtos.add(busRouteLinkDto);
            }

            insertAll(busRouteLinkDtos);

            result.put("success", true);
            result.put("message", "버스 라우트 링크 정보 업데이트 성공");
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

    public void insert(BusRouteLinkDto busRouteLinkDto) throws Exception{
        BusRouteLinkEntity busRouteLinkEntity = busRouteLinkDto.toEntity();
        busRouteLinkRepository.save(busRouteLinkEntity);
    }

    public void insertAll(List<BusRouteLinkDto> busRouteLinkDtos) throws Exception{
        List<BusRouteLinkEntity> busRouteLinkEntities = ConvertEntityDtoListUtil.toListBusRouteLinkEntity(busRouteLinkDtos);
        busRouteLinkRepository.saveAll(busRouteLinkEntities);
    }

    public void deleteAll() throws Exception {
        busRouteLinkRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.BUSROUTELINK_SEQ_NAME);
    }
}