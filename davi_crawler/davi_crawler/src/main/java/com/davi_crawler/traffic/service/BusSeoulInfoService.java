package com.davi_crawler.traffic.service;

import com.davi_crawler.common.service.DataColListService;
import com.davi_crawler.common.service.DataListService;
import com.davi_crawler.common.service.SequenceService;
import com.davi_crawler.common.util.ConstantUtil;
import com.davi_crawler.common.util.ConvertEntityDtoListUtil;
import com.davi_crawler.traffic.dto.BusSeoulInfoDto;
import com.davi_crawler.traffic.entity.BusSeoulInfoEntity;
import com.davi_crawler.traffic.repository.BusSeoulInfoRepository;
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
public class BusSeoulInfoService {
    private final DataListService dataListService;
    private final DataColListService dataColListService;
    private final BusSeoulInfoRepository busSeoulInfoRepository;
    private final SequenceService sequenceService;

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> updateApiData() throws Exception{
        Map<String, Object> result = new HashMap<>();
        BufferedReader br = null;

        try{
            deleteAll();

            dataColListService.updateColData(ConstantUtil.DATA_LIST_BUSSEOULINFO_ID,
                    dataListService.selectById(ConstantUtil.DATA_LIST_BUSSEOULINFO_ID).getTableNm());

            String filePath = ConstantUtil.CRAWLING_FILE_PATH + "traffic";
            String fileName = "busSeoulInfo.csv";

            br = new BufferedReader(new FileReader(new File(filePath, fileName)));
            String line = "";

            List<List<String>> csvDatas = new ArrayList<>();

            while((line = br.readLine()) != null){
                String lineArr[] = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                List<String> aLine = Arrays.asList(lineArr);
                csvDatas.add(aLine);
            }

            List<BusSeoulInfoDto> busSeoulInfoDtos = new ArrayList<>();

            for(int i=1; i<csvDatas.size(); i++){
                List<String> csvData = new ArrayList<>(csvDatas.get(i));

                //칼럼 데이터가 개수를 만족하지 않는 로우일 경우
                if(csvData.size() != 8){
                    int tmpCsvDataSize = 8 - csvData.size();

                    for(int j=0; j<tmpCsvDataSize; j++){
                        csvData.add(null);
                    }
                }

                BusSeoulInfoDto busSeoulInfoDto = new BusSeoulInfoDto(null, ConstantUtil.DATA_LIST_BUSSEOULINFO_ID,
                        csvData.get(0), csvData.get(1)
                        ,csvData.get(2), csvData.get(3)
                        ,csvData.get(4), csvData.get(5).replaceAll("\"", "")
                        ,csvData.get(6), csvData.get(7));

                log.debug(busSeoulInfoDto);
                busSeoulInfoDtos.add(busSeoulInfoDto);
            }

            insertAll(busSeoulInfoDtos);

            result.put("success", true);
            result.put("message", "서울 버스 정보 업데이트 성공");
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

    public void insert(BusSeoulInfoDto busSeoulInfoDto) throws Exception{
        BusSeoulInfoEntity busSeoulInfoEntity = busSeoulInfoDto.toEntity();
        busSeoulInfoRepository.save(busSeoulInfoEntity);
    }

    public void insertAll(List<BusSeoulInfoDto> busSeoulInfoDtos) throws Exception{
        List<BusSeoulInfoEntity> busSeoulInfoEntities = ConvertEntityDtoListUtil.toListBusSeoulInfoEntity(busSeoulInfoDtos);
        busSeoulInfoRepository.saveAll(busSeoulInfoEntities);
    }

    public void deleteAll() throws Exception {
        busSeoulInfoRepository.deleteAll();
        sequenceService.initSeqNumZero(ConstantUtil.BUSSEOULINFO_SEQ_NAME);
    }
}
